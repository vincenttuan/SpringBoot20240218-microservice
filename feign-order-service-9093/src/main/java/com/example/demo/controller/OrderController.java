package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.ItemDto;
import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.po.Item;
import com.example.demo.model.po.Order;
import com.example.demo.model.po.Product;
import com.example.demo.model.response.ApiResponse;
import com.example.demo.service.OrderServiceFeign;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderServiceFeign orderServiceFeign;
	
	// 查詢所有訂單
	@GetMapping
	public ResponseEntity<ApiResponse<List<OrderDto>>> findAllOrders() {
		List<OrderDto> orderDtos = orderServiceFeign.findAllOrders();
		ApiResponse<List<OrderDto>> apiResponse = null;
		if(orderDtos.isEmpty()) {
			apiResponse = new ApiResponse<>(false, "查無資料", null);
		} else {
			apiResponse = new ApiResponse<>(true, "有找到資料", orderDtos);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	
	// 查詢客戶訂單
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<ApiResponse<List<OrderDto>>> getOrdersByCustomerId(@PathVariable Integer customerId) {
		List<OrderDto> orderDtos = orderServiceFeign.getOrdersByCustomerID(customerId);
		ApiResponse<List<OrderDto>> apiResponse = null;
		if(orderDtos.isEmpty()) {
			apiResponse = new ApiResponse<>(false, "查無資料", null);
		} else {
			apiResponse = new ApiResponse<>(true, "有找到資料", orderDtos);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	// 查詢指定的訂單
	@GetMapping("/{orderId}")
	public ResponseEntity<ApiResponse<OrderDto>> getOrderById(@PathVariable Integer orderId) {
		OrderDto orderDto = orderServiceFeign.getOrderById(orderId);
		ApiResponse<OrderDto> apiResponse = null;
		if(orderDto == null) {
			apiResponse = new ApiResponse<>(false, "查無資料", null);
		} else {
			apiResponse = new ApiResponse<>(true, "有找到資料", orderDto);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	// 新增訂單
	@PostMapping
	public ResponseEntity<ApiResponse<OrderDto>> addOrder(@RequestBody Order order) {
		OrderDto orderDto = orderServiceFeign.saveOrder(order);
		ApiResponse<OrderDto> apiResponse = null;
		if(orderDto == null) {
			apiResponse = new ApiResponse<>(false, "新增失敗", null);
		} else {
			apiResponse = new ApiResponse<>(true, "新增成功", orderDto);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	// 刪除訂單
	@DeleteMapping("/{orderId}")
	public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Integer orderId) {
		Boolean status = orderServiceFeign.deleteOrderById(orderId);
		ApiResponse<Void> apiResponse = null;
		if(status) {
			apiResponse = new ApiResponse<>(status, "刪除訂單成功", null);
		} else {
			apiResponse = new ApiResponse<>(status, "刪除訂單失敗", null);
		}
		return ResponseEntity.ok(apiResponse);
	} 
	
	// 新增訂單項目
	@PostMapping("/{orderId}/item")
	public ResponseEntity<ApiResponse<ItemDto>> addOrderItem(@PathVariable Integer orderId, @RequestBody Item item) {
		ItemDto savedItemDto = orderServiceFeign.addOrderItem(orderId, item);
		ApiResponse<ItemDto> apiResponse = null;
		if(savedItemDto == null) {
			apiResponse = new ApiResponse<>(false, "新增訂單項目失敗", null);
		} else {
			apiResponse = new ApiResponse<>(true, "新增訂單項目成功", savedItemDto);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	// 移除訂單項目
	@DeleteMapping("/{orderId}/item/{itemId}")
	public ResponseEntity<ApiResponse<Void>> deleteOrderItem(@PathVariable("orderId") Integer orderId, @PathVariable("itemId")  Integer itemId) {
		Boolean status = orderServiceFeign.deleteOrderItem(orderId, itemId);
		ApiResponse<Void> apiResponse = null;
		if(status) {
			apiResponse = new ApiResponse<>(status, "刪除訂單項目成功", null);
		} else {
			apiResponse = new ApiResponse<>(status, "刪除訂單項目失敗", null);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	// 修改訂單項目數量
	@PutMapping("/{orderId}/item/{itemId}")
	public ResponseEntity<ApiResponse<ItemDto>> updateOrderItem(@PathVariable("orderId") Integer orderId, 
			@PathVariable("itemId") Integer itemId, @RequestParam Integer quantity) {
		ItemDto updatedItemDto = orderServiceFeign.updateOrderItem(orderId, itemId, quantity);
		ApiResponse<ItemDto> apiResponse = null;
		if(updatedItemDto == null) {
			apiResponse = new ApiResponse<>(false, "修改訂單項目數量失敗", null);
		} else {
			apiResponse = new ApiResponse<>(true, "修改訂單項目數量成功", updatedItemDto);
		}
		return ResponseEntity.ok(apiResponse);
	}
	
	
}
