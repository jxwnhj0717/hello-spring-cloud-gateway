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

## 拦截http请求

``` java
class HttpServer.HttpServerHandle {
  public void onStateChange(Connection connection, State newState) {
    ...
    HttpServerOperations ops = (HttpServerOperations) connection;
    Mono<Void> mono = Mono.fromDirect(handler.apply(ops, ops));
    ...
  }
}
```

1. handler代理给HttpWebHandlerAdapter，创建DefaultServerWebExchange
2. HttpWebHandlerAdapter又代理给DispatcherHandler，调用SimpleHandlerAdapter处理请求
3. SimpleHandlerAdapter调用FilteringWebHandler.handle()处理ServerWebExchange
4. FilteringWebHandler把GatewayFilter和GlobalFilter合并，处理ServerWebExchange
5. GlobalFilter-NettyRoutingFilter匹配http请求，在请求-响应的前后进行处理

## 转发http请求

```java
class NettyRoutingFilter {
  ...
  Flux<HttpClientResponse> responseFlux = getHttpClient(route, exchange)
    .headers()
    .request()
    .uri()
    .send()
    .responseConnection();
  ...
}
```
1. getHttpClient()返回的HttpClient负责转发请求