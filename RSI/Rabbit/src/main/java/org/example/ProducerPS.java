package org.example;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ProducerPS {
    private final static String EXCHANGE_NAME = "TEST";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
//            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            for (int i = 0; i < 10; i++) {

                String msg = "Producer " + i;

                System.out.println(i);

                channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());

//                Thread.sleep(1000);
            }

            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
