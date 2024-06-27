package org.example;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class ConsumerR {
    private final static String EXCHANGE_NAME = "TEST2";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
//            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String queueName = channel.queueDeclare().getQueue();
            for (String rKey : args) {
                channel.queueBind(queueName, EXCHANGE_NAME, rKey);
            }
//            channel.queueBind(queueName, EXCHANGE_NAME, KEY);

            DefaultConsumer consumer = new DefaultConsumer(channel) {
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body, "UTF-8");
                    System.out.println(msg);
                    System.out.println(envelope.getRoutingKey());

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            };

            channel.basicConsume(queueName, false, consumer);

            Scanner sc = new Scanner(System.in);

            do {
                System.out.println("Press q to close");
            } while (!Objects.equals(sc.nextLine(), "q"));

            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
