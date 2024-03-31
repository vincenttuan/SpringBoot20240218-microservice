package message_queue_demo.rabbit.coffee;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Producer {
	
	private final static String QUEUE_NAME = "coffee_queue";
	private int coffeeStock = 10;
	
	public void produce() throws Exception {
		ConnectionFactory factory = new ConnectionFactory(); // 建立一個 rabbitme 的連線工廠
		factory.setHost("localhost");
		
		try(Connection connection = factory.newConnection(); // 建立連線
			Channel channel = connection.createChannel()) { // 建立一個新通道
			
			/*
	         * channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	         * queue: 隊列的名稱，這是唯一標識隊列的字符串。
	         * durable (false): 這個參數指示隊列是否持久化。當設置為 true 時，隊列會在 RabbitMQ 重啟後仍然存在，因為它會被保存到磁盤上。當設置為 false 時，隊列僅在記憶體中存在，且在 RabbitMQ 重啟後會丟失。
	         * exclusive (false): 這個參數指示隊列是否為此連接專有。若設置為 true，該隊列將僅對當前連接可見，且隊列在連接關閉時會自動刪除。這常用於只期望由一個消費者消費的臨時隊列。
	         * autoDelete (false): 這個參數指示隊列是否在最後一個消費者斷開連接後自動刪除。如果沒有消費者與隊列連接，設置為 true 時，隊列會被自動刪除。
	         * arguments (null): 這是一個包含額外參數的 map，用於擴展隊列的聲明。例如，可以用它來設定隊列的消息壽命（TTL）、最大長度、最大優先級等。在這個例子中，這個參數被設置為 null，意味著不使用任何額外參數。
	         * 總之，這行代碼聲明了一個非持久化、非專有、不自動刪除、沒有額外參數設定的隊列。
	         * 這樣的隊列適用於簡單的應用場景，其中隊列的持久性和高級特性不是必需的。
	         * */
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			
			System.out.println("[*] Waiting for message. To exit press CTRL+C");
			
			DeliverCallback deliverCallback = (customerTag, delivery) -> { // 消息交付後的回調函數
				String message = new String(delivery.getBody(), "UTF-8");
				// 表示消費者已經收到消息
				System.out.printf("[x] 消費者已經接收到消息 received: '%s'%n", message);
			};
			channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback->{});
		}
	}
	
	public int getCoffeeStock() {
		return coffeeStock;
	}
}
