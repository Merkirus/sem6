package org.example;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ProducerRPC {
    private final static String RPC_QUEUE_NAME = "TEST";

    public static void main(String[] args) {
        try (var rpc = new RPCCall()) {
            rpc.call("16");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class RPCCall implements AutoCloseable {
    private final String RPC_QUEUE_NAME = "TEST";
    private Channel channel;
    private Connection connection;

    public RPCCall() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        channel.close();
        connection.close();
    }

    public String call(String msg) throws Exception {
        String correlationID = UUID.randomUUID().toString();
        String replyQName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                .correlationId(correlationID)
                .replyTo(replyQName)
                .build();
        channel.basicPublish("", RPC_QUEUE_NAME, props, msg.getBytes("UTF-8"));
        CompletableFuture<String> response = new CompletableFuture<>();
        String tag = channel.basicConsume(replyQName, true,
                (consumerTag, returnMsg) -> {
                    if (returnMsg.getProperties().getCorrelationId().equals(correlationID)) {
                        response.complete(new String(returnMsg.getBody(), "UTF-8"));
                    }
                },
                consumerTag -> { }
        );
        var result = response.get();
        channel.basicCancel(tag);
        return result;
    }
}