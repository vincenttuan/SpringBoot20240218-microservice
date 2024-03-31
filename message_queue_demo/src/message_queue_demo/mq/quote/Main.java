package message_queue_demo.mq.quote;

import message_queue_demo.mq.MessageQueue;

public class Main {
	public static void main(String[] args) {
		MessageQueue queue = new MessageQueue();
		
		StockPriceProducer producer1 = new StockPriceProducer(queue, "2330", 600, 700);
		
		StockPriceConsumer consumer = new StockPriceConsumer(queue);
		
		new Thread(producer1).start();
		new Thread(consumer).start();
		
	}
}
