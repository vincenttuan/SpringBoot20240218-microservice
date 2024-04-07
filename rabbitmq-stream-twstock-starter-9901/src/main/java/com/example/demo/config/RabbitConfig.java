package com.example.demo.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
	
	// Fanout Exchange
	@Bean
	public FanoutExchange stockExchange() {
		return new FanoutExchange("stock_fanout_exchange");
	}
}
