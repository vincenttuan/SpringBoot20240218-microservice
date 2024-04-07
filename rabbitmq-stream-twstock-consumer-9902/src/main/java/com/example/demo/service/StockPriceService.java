package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// 股價報價服務
@Service
public class StockPriceService {
	
	// 收集每一檔股票的個別報價紀錄, 作為繪製即時股價走勢圖使用
	private final Map<String, List<String>> stockPrices = new ConcurrentHashMap<>();
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	//  接收股價資訊
	@RabbitListener(queues = "stock_queue")
	public void receiveStockPrice(String message) {
		System.out.println("Received: " + message);
		
		// 取得 Symbol
		String symbol = getSymbol(message);
		
		// 先確認 symbol 使否存在於 stockPrices ?
		if(!stockPrices.containsKey(symbol)) {
			stockPrices.put(symbol, new ArrayList<>());
		}
		
		// 資加股票代碼對應的報價資訊
		stockPrices.get(symbol).add(message);
		
		// 透過 WebStock 發佈報價資訊
		messagingTemplate.convertAndSend("/topic/" + symbol, message);
	}
	
	private String getSymbol(String message) {
		JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
		return jsonObject.get("symbol").getAsString().trim();
	}
	
	// 取得最新價格
	public String getLastPrice(String symbol) {
		List<String> list = stockPrices.get(symbol) == null ? null : stockPrices.get(symbol);
		if(list == null) {
			return symbol + " not price available!";
		}
		return list.get(list.size() - 1); // 最末筆(最新一筆)
	}
	
}
