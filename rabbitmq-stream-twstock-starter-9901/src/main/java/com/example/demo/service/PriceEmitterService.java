package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

// 報價發送服務
@Service
public class PriceEmitterService {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private FanoutExchange fanoutExchange;
	
	// 停止標記
	public boolean stop; // 預設是 false
	
	// 發送報價 (ContextRefreshedEvent 會在 Springboot 啟動後自動觸發)
	@EventListener(ContextRefreshedEvent.class)
	public void emitPrices() {
		try {
			
		} catch(Exception e) {
			
		}
	}
	
	// 取得 price.json 並轉 String[]
	private String[] getJsonArray() throws IOException {
		String jsonFilePath = "C:/Users/vince/Downloads/price.json";
    	String jsonStr = Files.readString(Paths.get(jsonFilePath));
        String[] jsonStrs = jsonStr.split("\n");
        return jsonStrs;
	}
	
}
