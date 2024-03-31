package message_queue_demo.rabbit.coffee;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer { // 定義生產者類別
    private final static String QUEUE_NAME = "coffee_queue"; // 定義要發送消息的隊列名稱
    private int coffeeStock = 10; // 初始化咖啡庫存為 10

    public void produce() throws Exception { // 定義發送消息的方法，可能拋出異常
        ConnectionFactory factory = new ConnectionFactory(); // 創建連接工廠
        factory.setHost("localhost"); // 設置連接到本地 RabbitMQ 服務器，根據您的服務器設置可能需要修改
        try (Connection connection = factory.newConnection(); // 使用工廠創建新的連接
             Channel channel = connection.createChannel()) { // 從連接中創建一個新的通道
             
            channel.queueDeclare(QUEUE_NAME, false, false, false, null); // 聲明一個隊列，如果隊列不存在則創建
            String message = "一杯咖啡做好了"; // 定義發送的消息內容
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8")); // 向指定的隊列發布消息
            // 表示生產者已經發送了消息
            System.out.println(" [x] 生產者已經發送了消息 Sent '" + message + "'"); // 打印消息發送的確認信息
            
            coffeeStock--; // 發送消息後，庫存減少
        }
    }

    public int getCoffeeStock() { // 定義方法獲取當前咖啡庫存
        return coffeeStock;
    }
}
