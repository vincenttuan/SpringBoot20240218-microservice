package message_queue_demo.rabbit.coffee;

import java.util.Random;

public class Main {
	public static void main(String[] args) throws Exception {
		Producer producer = new Producer();
		Consumer consumer = new Consumer();
		
		// 啟動消費者執行緒
		new Thread(() -> {
			try {
				consumer.consume();
			} catch (Exception e) {
				System.out.println(e);
			}
		}).start();
		
		// 模擬購買咖啡的動作
		while (producer.getCoffeeStock() > 0) {
			producer.produce();
			Thread.sleep(new Random().nextInt(3000));
		}
		
	}
}
