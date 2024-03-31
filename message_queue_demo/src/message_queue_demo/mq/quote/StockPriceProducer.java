package message_queue_demo.mq.quote;

import java.util.Random;

import message_queue_demo.mq.MessageQueue;

public class StockPriceProducer implements Runnable {
	private final MessageQueue queue;
	private final String stockSymbol;
	private final int minPrice;
	private final int maxPrice;
	
	public StockPriceProducer(MessageQueue queue, String stockSymbol, int minPrice, int maxPrice) {
		this.queue = queue;
		this.stockSymbol = stockSymbol;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
	}

	@Override
	public void run() {
		try {
			Random random = new Random();
			while (true) {
				int price = random.nextInt(maxPrice - minPrice + 1) + minPrice;
				String message = stockSymbol + " Price: " + price;
				queue.produce(message);
				System.out.printf("Producer send '%s'%n", message);
				Thread.sleep(new Random().nextInt(1000));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
