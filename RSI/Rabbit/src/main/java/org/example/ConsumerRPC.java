package org.example;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class ConsumerRPC {
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

                    int i = Integer.parseInt(msg);
                    String fibo_result = Integer.toString(fibo(i));

                    System.out.println(fibo_result);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    channel.basicPublish("",
                            properties.getReplyTo(),
                            properties,
                            fibo_result.getBytes("UTF-8"));
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

    public static int fibo(int i) {
        if (i <  2)
            return i;

        return fibo(i - 1) + fibo(i - 2);
    }
}
