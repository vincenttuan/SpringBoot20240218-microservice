package message_queue_demo.rabbit.coffee;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Consumer { // 定義消費者類別
    private final static String QUEUE_NAME = "coffee_queue"; // 定義要從中接收消息的隊列名稱

    public void consume() throws Exception { // 定義消費消息的方法，可能拋出異常
    	System.out.println("消費者開始進行消費...");
        ConnectionFactory factory = new ConnectionFactory(); // 創建連接工廠
        factory.setHost("localhost"); // 設置連接到本地 RabbitMQ 服務器，根據您的服務器設置可能需要修改
        Connection connection = factory.newConnection(); // 使用工廠創建新的連接
        Channel channel = connection.createChannel(); // 從連接中創建一個新的通道
        
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
        channel.queueDeclare(QUEUE_NAME, false, false, false, null); // 聲明一個隊列，如果隊列不存在則創建
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C"); // 打印等待消息的提示信息

        DeliverCallback deliverCallback = (consumerTag, delivery) -> { // 定義消息交付時的回調函數
            String message = new String(delivery.getBody(), "UTF-8"); // 從消息體中解析出字符串內容
            // 表示消費者已經接收到消息
            System.out.println(" [x] 消費者已經接收到消息 Received '" + message + "'"); // 打印接收到的消息
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { }); // 開始消費隊列中的消息，並使用回調函數處理接收到的每條消息
    }
}
