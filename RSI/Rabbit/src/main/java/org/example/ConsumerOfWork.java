package org.example;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class ConsumerOfWork {
    private final static String QUEUE_NAME = "TEST";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.basicQos(1);

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            DefaultConsumer consumer = new DefaultConsumer(channel) {
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body, "UTF-8");
                    System.out.println(msg);

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            };

            channel.basicConsume(QUEUE_NAME, false, consumer);

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
