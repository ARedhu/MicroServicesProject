package com.Ashish.api_gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalLoggingFilter implements GlobalFilter, Ordered {

//    Mono<Void> means this method will return nothing because of void. But it is asynchronous method because of mono.
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // exchange - contains the current HTTP request + response information.
        // chain.filter(exchange): This means Continue this request to the next filter in the Gateway filter chain. If we don't do this then the request will stop at our current filter and won't proceed further.

        // This is pre-filter.
        log.info("Logging from Global Pre filter: {}", exchange.getRequest().getURI());

        // .then is the post-filter.
//        return chain.filter(exchange).then(Mono.fromRunnable(new Runnable() {
//            @Override
//            public void run() {
//                log.info("Logginf from Global Post filter: {}", exchange.getResponse().getStatusCode());
//            }
//        }));

        // We can replace the upper anonymous runnable class with lambda as well.
        return chain.filter(exchange).then(Mono.fromRunnable(()->{
            log.info("Logging from Global Post filter: {}", exchange.getResponse().getStatusCode());
        }));
    }

    @Override
    public int getOrder(){
        return 5;
    }
}

/*
There are two types of filters.
Global filters, which will apply to requests going to all services/routes.
Service/route specific filters.

Order concept in filters:
Order can range from -infinity to +infinity. And the execution of filters always goes from lower order to higher order.

Also, each filter has two things: pre and post.
Pre - means the things that should be executed before the execution of other filters in the chain and service.
Post - means the things that should be executed after the execution of service.

		        ORDERING
                     │
        1 → 2 → 3 → 4 → pre-execution of filter.
                     │
        4 → 3 → 2 → 1 -> post execution of filter.
It is kind of recursion.
 */

/*
A functional interface is an interface that has exactly one abstract method.
Runnable       → run()
Comparator     → compare()
Functional Interface → can be represented using a Lambda expression.
An anonymous class is a class that you create without giving it a name, usually when you need a one-time implementation.
 */