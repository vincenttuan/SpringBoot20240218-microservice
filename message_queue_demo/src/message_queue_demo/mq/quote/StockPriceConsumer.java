package message_queue_demo.mq.quote;

import message_queue_demo.mq.MessageQueue;

public class StockPriceConsumer implements Runnable {
	private final MessageQueue queue;

	public StockPriceConsumer(MessageQueue queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		while (true) {
			String message = queue.consume();
			System.out.printf("Consumer received '%s'%n", message);
		}
	}
	
}
