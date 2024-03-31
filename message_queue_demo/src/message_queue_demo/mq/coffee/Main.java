package message_queue_demo.mq.coffee;

import java.util.Random;

import message_queue_demo.mq.MessageQueue;

public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		MessageQueue queue = new MessageQueue();
		
		// 啟動消費者執行緒
		Consumer consumer = new Consumer(queue);
		new Thread(consumer).start();
		
		
		// 啟動生產者執行緒
		Producer producer = new Producer(queue, 10);
		// 當咖啡庫存大於 0 時, 持續購買咖啡
		while (true) {
			// 模擬購買咖啡的動作
			if(producer.getCoffeeStock() > 0) {
				new Thread(producer).start();
			}
			Thread.sleep(new Random().nextInt(5000));
		}
		
	}
	
}
