package org.example;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ProducerR {
    private final static String EXCHANGE_NAME = "TEST2";
    private final static String KEY = "KEY";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
//            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            for (int i = 0; i < 10; i++) {

                String msg_alert = "Producer alert " + i;
                String msg_info = "Producer info " + i;
                String msg_news = "Producer news " + i;

                System.out.println(i);

                channel.basicPublish(EXCHANGE_NAME, "info", null, msg_info.getBytes());
                channel.basicPublish(EXCHANGE_NAME, "alert", null, msg_alert.getBytes());
                channel.basicPublish(EXCHANGE_NAME, "news", null, msg_news.getBytes());

//                Thread.sleep(1000);
            }

            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
