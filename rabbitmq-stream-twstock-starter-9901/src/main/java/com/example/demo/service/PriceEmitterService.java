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
	private FanoutExchange stockExchange;
	
	// 停止標記
	public boolean stop; // 預設是 false
	
	// 發送報價 (ContextRefreshedEvent 會在 Springboot 啟動後自動觸發)
	@EventListener(ContextRefreshedEvent.class)
	public void emitPrices() {
		try {
			String[] jsonStrs = getJsonArray();
			
			for(String json : jsonStrs) {
				String message = json.trim();
				// 判斷是否停止
				if(stop || message.contains("\"symbol\":\"000000\"")) {
					break;
				}
				
				if(message.length() < 10 ) {
					continue;
				}
				
				String routingKey = null;
				if(stockExchange.getType().equals("fanout")) {
					// FanoutExchange
					routingKey = "";
				} else {
					// DirectExchange
					routingKey = "tw.stock";
				}
				
				// 發送消息
				rabbitTemplate.convertAndSend(stockExchange.getName(), routingKey, message);
				
				System.out.println("發送: " + message);
				
				Thread.sleep(10);
			}
			System.out.println("發送: 報價停止 !");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		this.stop = true;
	}
	
	// 取得 price.json 並轉 String[]
	private String[] getJsonArray() throws IOException {
		String jsonFilePath = "C:/Users/vince/Downloads/price.json";
    	String jsonStr = Files.readString(Paths.get(jsonFilePath));
        String[] jsonStrs = jsonStr.split("\n");
        return jsonStrs;
	}
	
}
