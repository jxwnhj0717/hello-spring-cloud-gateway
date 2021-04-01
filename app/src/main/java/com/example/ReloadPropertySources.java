package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@Component
public class ReloadPropertySources implements ApplicationContextAware, ApplicationListener<EnvironmentChangeEvent> {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private ApplicationContext applicationContext;

    private Map<String, Long> lastModifiedMap = new HashMap<>();

    @Scheduled(fixedDelay = 1000)
    public void monitor() {
        List<String> changedPropertiesList = new ArrayList<>();
        Environment environment = applicationContext.getEnvironment();
        Stream<String> stream = Stream.concat(Arrays.stream(environment.getDefaultProfiles()), Arrays.stream(environment.getActiveProfiles()));
        stream.map(e -> "classpath:application" + (e.equals("default") ? "" : e) + ".yml")
                .forEach(e -> {
                    Resource resource = applicationContext.getResource(e);
                    long lastModifiedTime = resource.isFile() ? getLastModifiedTime(resource) : 0;
                    if(lastModifiedTime != 0) {
                        Long oldTime = lastModifiedMap.putIfAbsent(e, lastModifiedTime);
                        if(oldTime != null && oldTime != lastModifiedTime) {
                            changedPropertiesList.add(e);
                            lastModifiedMap.put(e, lastModifiedTime);
                        }
                    }
                });

        if(!changedPropertiesList.isEmpty()) {
            eventPublisher.publishEvent(new RefreshEvent(this, changedPropertiesList, "refresh " + changedPropertiesList));
        }

    }

    private long getLastModifiedTime(Resource resource) {
        try {
            return resource.getFile().lastModified();
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        event.getKeys().forEach(e -> {
            log.info("配置热更[{}={}]", e, applicationContext.getEnvironment().getProperty(e));
        });
    }
}
