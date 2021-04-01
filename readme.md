# Spring Cloud Gateway Reference

## How to Include Spring Cloud Gateway

1. 引入org.springframework.cloud:spring-cloud-starter-gateway，自动装配
2. 基于Spring Boot 2.x, Spring WebFlux 和 Project Reactor技术

## Glossary

1. 路由 Route，定义路由规则
2. 断言 Predicate，确定请求匹配那个路由
3. 过滤器 Filter，GlobalFilter为不同的请求实现不同的处理器，比如http和websocket需要不同的全局处理器，GatewayFilter在请求响应前后做一些动作。

## How It Works

重要，Spring Cloud Gateway工作流程图。

## 其他

1. 路由的predicate和gateway的配置方式
2. Route Predicate Factories，内置的Predicate
3. GatewayFilter Factories，内置的GatewayFilter
4. Global Filters，内置的GlobalFilter
5. http连接超时配置
6. 开启netty访问日志
7. Actuator API
8. 扩展Predicate，GatewayFilter，GlobalFilter

# 源码分析
