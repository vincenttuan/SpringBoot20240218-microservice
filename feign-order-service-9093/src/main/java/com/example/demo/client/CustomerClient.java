package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.po.Customer;
import com.example.demo.model.response.ApiResponse;

/*
 * 1. 撰寫時注意要在 pom.xml 加入以下 dependency
 * 2. 要在 Application 啟動類上加上 @EnableFeignClients(basePackages = "com.example.demo.client")
 * */
@FeignClient(name = "feign-customer-service") // 或 FEIGN-CUSTOMER-SERVICE (從 Eureka 上查找)
public interface CustomerClient {

	@GetMapping("/customers/{id}")
	ApiResponse<Customer> getCustomerById(@PathVariable("id") Integer id);
	
}
