package com.example.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
	
	// Fanout Exchange
	@Bean
	public FanoutExchange stockExchange() {
		return new FanoutExchange("stock_fanout_exchange");
	}
	
	// Queue: stock_queue
	@Bean
	public Queue stockQueue() {
		return QueueBuilder.nonDurable("stock_queue").build();
	}
	
	/*
	 * 
	 * P ->         Exchange        ->     Queue     -> C
	 *      (stock_fanout_exchange)    (stock_queue)
	 * */
	
	@Bean
	public Binding binding() {
		return BindingBuilder.bind(stockQueue()).to(stockExchange());
	}
}
