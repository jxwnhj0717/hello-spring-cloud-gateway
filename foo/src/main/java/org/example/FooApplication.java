package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class FooApplication {

    public static void main(String[] args) {
        SpringApplication.run(FooApplication.class);
    }

    @RequestMapping("/")
    public String index() {
        return "Hello Foo";
    }

}
