package message_queue_demo.mq.coffee;

public class Consumer implements Runnable {
	private final MessageQueue queue;
	
	public Consumer(MessageQueue queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		System.out.println("Consumer.run()");
		while (true) {
			String message = queue.consume();
			processMessage(message);
		}
		
	}
	
	// 處理消息
	private void processMessage(String message) {
		System.out.printf("處理消息: %s%n", message);
	}
	
}
