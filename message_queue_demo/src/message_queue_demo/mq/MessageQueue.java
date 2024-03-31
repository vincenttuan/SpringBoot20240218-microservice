package message_queue_demo.mq;

import java.util.LinkedList;
import java.util.Queue;

public class MessageQueue {
	private Queue<String> queue = new LinkedList<>();
	
	// 生產消息
	public synchronized void produce(String message) {
		queue.add(message); // 將消息加入到佇列
		notifyAll(); // 通知消費者
	}
	
	// 消費消息
	public synchronized String consume() {
		while (queue.isEmpty()) {
			try {
				System.out.println("消費者等待中...");
				this.wait(); // 如果佇列是空的就等待消息
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return queue.poll(); // 從佇列中"取出"消息
	}
}
