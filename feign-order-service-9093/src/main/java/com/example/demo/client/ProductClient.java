package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.po.Product;
import com.example.demo.model.response.ApiResponse;

/*
 * 1. 撰寫時注意要在 pom.xml 加入以下 dependency
 * 2. 要在 Application 啟動類上加上 @EnableFeignClients(basePackages = "com.example.demo.client")
 * */
@FeignClient(name = "feign-product-service") // 或 FEIGN-PRODUCT-SERVICE (從 Eureka 上查找)
public interface ProductClient {

	@GetMapping("/products/{productId}")
	ApiResponse<Product> getProductById(@PathVariable("productId") Integer productId);
	
	// 調整(增加或減少)商品庫存
	@GetMapping("/products/{productId}/stock/{amount}")
	ResponseEntity<ApiResponse<Product>> adjustProductStock(@PathVariable("productId") Integer productId, @PathVariable("amount") Integer amount);
	
}
