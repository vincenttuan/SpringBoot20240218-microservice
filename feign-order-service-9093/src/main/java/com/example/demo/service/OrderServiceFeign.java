package com.example.demo.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.client.CustomerClient;
import com.example.demo.client.ProductClient;
import com.example.demo.dao.OrderRepository;
import com.example.demo.model.dto.ItemDto;
import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.po.Customer;
import com.example.demo.model.po.Item;
import com.example.demo.model.po.Order;
import com.example.demo.model.po.Product;
import com.example.demo.model.response.ApiResponse;

@Service
public class OrderServiceFeign {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CustomerClient customerClient;
	
	@Autowired
	private ProductClient productClient;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	// Item 單筆訂單的資料轉換 PO->Dto
	private ItemDto convertToDto(Item item) {
		ItemDto itemDto = new ItemDto();
		itemDto.setId(item.getId());
		itemDto.setQuantity(item.getQuantity());
		itemDto.setSubtotal(item.getSubtotal());
		
		// 透過 Feign 取得 Product 實體
		Integer productId = item.getProductId();
		// 相當於 Product product = http://192.168.30.240:9091/products/1;
		Product product = productClient.getProductById(productId).getData();
		itemDto.setProduct(product);
		return itemDto;
	}
	
	// Order 單筆訂單的資料轉換 PO->Dto
	private OrderDto convertToDto(Order order) {
		OrderDto orderDto = new OrderDto();
		orderDto.setId(order.getId());
		orderDto.setOrderDate(order.getOrderDate());
		
		// 透過 Feign 取得 Customer 實體
		Integer customerId = order.getCustomerId();
		// 相當於 Customer customer = http://192.168.30.240:9092/customers/1;
		Customer customer = customerClient.getCustomerById(customerId).getData();	
		orderDto.setCustomer(customer);
		
		for(Item item : order.getItems()) {
			ItemDto itemDto = convertToDto(item);
			orderDto.getItemDtos().add(itemDto); // 加入到 itemDtos
		}
		
		return orderDto;
	}
	
	// Orders 多筆訂單的資料轉換
	private List<OrderDto> convertToDto(List<Order> orders) {
		List<OrderDto> orderDtos = new ArrayList<>();
		orders.forEach(order -> {
			OrderDto orderDto = convertToDto(order);
			orderDtos.add(orderDto);
		});
		return orderDtos;
	}
	
	// 查詢所有訂單
	public List<OrderDto> findAllOrders() {
		List<Order> orders = orderRepository.getAllOrders();
		return convertToDto(orders);
	}
	
	// 查詢客戶訂單
	public List<OrderDto> getOrdersByCustomerID(Integer customerId) {
		List<Order> orders = orderRepository.getAllOrders().stream()
				.filter(order -> order.getCustomerId().equals(customerId))
				//.peek(System.out::println)
				.toList();
		
		// List<Order> 轉 List<OrderDto>
		return convertToDto(orders);
	}
	
	// 查詢指定的訂單
	public OrderDto getOrderById(Integer orderId) {
		Order order = orderRepository.getOrderById(orderId);
		return convertToDto(order);
	}
	
	// 新增訂單
	public OrderDto saveOrder(Order order) {
		order.setOrderDate(sdf.format(new Date()));
		Order savedOrder = orderRepository.addOrder(order);
		
		Integer orderId = savedOrder.getId();
		OrderDto savedOrderDto = getOrderById(orderId);
		return savedOrderDto;
	}
	
	// 刪除訂單
	public Boolean deleteOrderById(Integer orderId) {
	    Order order = orderRepository.getOrderById(orderId);
	    if (order == null) {
	        return false;
	    }
	    
	    // 使用另一個集合來收集需要刪除的項目ID
	    List<Integer> itemIdsToDelete = order.getItems().stream()
	                                          .map(Item::getId)
	                                          .collect(Collectors.toList());
	    
	    // 遍歷收集到的項目ID集合來進行刪除操作
	    for (Integer itemId : itemIdsToDelete) {
	        deleteOrderItem(orderId, itemId);
	    }
	    
	    return orderRepository.deleteOrder(orderId);
	}

	
	// 新增訂單項目
	public ItemDto addOrderItem(Integer orderId, Item item) {
		Item savedItem = orderRepository.addOrderItem(orderId, item);
		// 調整商品庫存(減量)
		ResponseEntity<ApiResponse<Product>> responseEntity = 
				productClient.adjustProductStock(savedItem.getProductId(), savedItem.getQuantity() * -1);
		return convertToDto(savedItem);
	} 
	
	// 移除訂單項目
	public Boolean deleteOrderItem(Integer orderId, Integer itemId) {
		Item item = orderRepository.getOrderItemById(orderId, itemId);
		if(item == null) {
			return false;
		}
		
		// 刪除訂單項目
		Boolean status = orderRepository.deleteOrderItem(orderId, itemId);
		
		if(status) {
			// 調整商品庫存(增量)
			ResponseEntity<ApiResponse<Product>> responseEntity = 
					productClient.adjustProductStock(item.getProductId(), item.getQuantity());
		}
		
		return status;
	}
	
	// 修改訂單項目數量
	public ItemDto updateOrderItem(Integer orderId, Integer itemId, Integer afterQuantity) {
		// 調整商品庫存(增量或減量)
		Item item = orderRepository.getOrderItemById(orderId, itemId);
		// 取得 item 尚未修改前的數量
		Integer beforeQuantity = item.getQuantity();
		// 修改訂單項目數量
		Item updatedItem = orderRepository.updateOrderItemQuantity(orderId, itemId, afterQuantity);
		if(updatedItem == null) {
			return null;
		}
		Integer adjectQuantity = beforeQuantity - afterQuantity;
		ResponseEntity<ApiResponse<Product>> responseEntity = 
				productClient.adjustProductStock(item.getProductId(), adjectQuantity);
		
		return convertToDto(updatedItem);
	} 
	
	
}
