package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
	
	// Fanout Exchange
	@Bean
	public FanoutExchange stockExchange() {
		
	}
}
