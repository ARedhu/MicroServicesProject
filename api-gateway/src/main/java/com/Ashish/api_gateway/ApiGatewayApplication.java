package com.Ashish.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
		System.out.println("API GateWay service is running successfully.");
	}

}
// Remember all the applications now act as Eureka client other than the Eureka server application. So, all these have to register as a client in their own Application.yml file which will register them inside of Eureka server. Even this API Gateway as well.
// Because even the API Gateway need the help of Eureka server to communicate with other services.