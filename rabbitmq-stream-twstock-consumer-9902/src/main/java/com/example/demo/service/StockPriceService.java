package com.example.demo.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 股價報價服務
@Service
public class StockPriceService {
	
	// 收集每一檔股票的個別報價紀錄, 作為繪製即時股價走勢圖使用
	private final Map<String, List<String>> stockPrices = new ConcurrentHashMap<>();
	
	
	//  接收股價資訊
	@RabbitListener(queues = "stock_queue")
	public void receiveStockPrice(String message) {
		System.out.println("Received: " + message);
	}
	
}
