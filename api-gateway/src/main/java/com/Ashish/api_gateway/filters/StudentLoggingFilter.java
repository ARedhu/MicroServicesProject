package com.Ashish.api_gateway.filters;

import com.netflix.spectator.impl.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class StudentLoggingFilter extends AbstractGatewayFilterFactory<StudentLoggingFilter.Config> {

    // We can skip this Config class. But it is highly recommended to use it.
    public static class Config{
    }

    public StudentLoggingFilter(){
        super(Config.class); // And, we are passing it to the parent AbstractGatewayFilterFactory. Which tells the parent that hey, my configurations would be of this type.
    }

    // apply() is the method where you define what your custom filter actually does.
    @Override
    public GatewayFilter apply(Config config){
//        return new GatewayFilter() {
//            @Override
//            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//                return null;
//            }
//        }

        // We can use the lamda as well.

        return (exchange, chain) -> {

            log.info("Student filter pre: {}", exchange.getRequest().getURI());
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                log.info("Student filter post: {}", exchange.getResponse().getStatusCode());
            }));
        };
    }
}

// Some rules related to static.
/*
same static variable will be shared with all the other objects of the class.
static nested class - It just belongs to the outer class, not the objects of the outer class.
We create static nested class so that we can create the objects of this nested class without creating the objects of the outer class. StudentLoggingFilter.Config c1 = new StudentLoggingFilter.Config(); Remember we can create the objects of the static class, even if it is nested.
 */

/*
Q: Why we created a nested static Config class?
A: First of all this Config class will contain configurations for the filter like: "private boolean isEnabled", "private String message" etc.
But then why we haven't created simple variables inside of this outer class.
A:  the object/bean of the StudentLoggingFilter would be created when the application starts. And, as we can use this same filter for multiple services like later may be 2-more services will have this same filter, that's why we created a nested static class so that those two service can have their seperate values for the configuration variables. If we directly write the normal variables or even the static variables those variables values would have been fixed for all the services.
 */