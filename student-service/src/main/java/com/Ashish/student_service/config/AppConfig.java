package com.Ashish.student_service.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper getModeMapper(){
        return new ModelMapper();
    }

    // Wrong way:
//    @Bean
//    @LoadBalanced
//    public RestClient getRestClient(){
//        return RestClient.builder().build();
//    }


    // Remember we have to create two seperate beans of restclient one for our application (eureka client) to make a rest api request to connect with eureka server. As we have provided the url of eureka server there rather than the service name inside of application.yml of student-service.
    @Bean
    @Primary
    public RestClient.Builder getRestClientBuilder(){
        return RestClient.builder();
    }

    // // Right way:
    //    // Because Spring Cloud LoadBalancer hooks into RestClient by attaching a LoadBalancerInterceptor during the building phase (RestClient.Builder).
    @Bean
    @LoadBalanced
    @Qualifier("loadBalancedRestClientBuilder")
    public RestClient.Builder getLoadBalancedRestClientBuilder(){
        return RestClient.builder();
    }
}
