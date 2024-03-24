package com.example.demo;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
	
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		
		return builder.routes()
				.route("feign-customer-service", r -> r.path("/customers/**").uri("lb://feign-customer-service"))
				.route("feign-product-service", r -> r.path("/products/**").uri("lb://feign-product-service"))
				.route("feign-order-service", r -> r.path("/orders/**").uri("lb://feign-order-service"))
				.build();
	}
	
}
