package message_queue_demo.mq.coffee;

public class Producer implements Runnable {
	
	private final MessageQueue queue;
	
	// 咖啡庫存
	private int coffeeStock = 0;
	
	public Producer(MessageQueue queue) {
		this.queue = queue;
	}
	
	@Override
	public void run() {
		System.out.println("Producer.run()");
		System.out.printf("咖啡庫存: %d%n", coffeeStock);
		// 模擬購買咖啡
		queue.produce("一杯咖啡被購買");
		// 更新庫存
		coffeeStock--;
	}
	
	public int getCoffeeStock() {
		return coffeeStock;
	}
	
}
