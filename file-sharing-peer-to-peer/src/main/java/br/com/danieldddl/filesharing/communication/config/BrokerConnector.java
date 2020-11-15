package br.com.danieldddl.filesharing.communication.config;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class BrokerConnector {

    private Connection connection;
    protected Channel channel;

    public BrokerConnector() {
        initializeConnection();
        addShutdownHook();
    }

    private void initializeConnection() {

        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(BrokerConfiguration.HOST);

        try {
            this.connection = factory.newConnection();
            this.channel = connection.createChannel();
        } catch (IOException | TimeoutException e) {
            final String message = "Error: could not connection to broker on host: %s";
            throw new IllegalStateException(String.format(message, BrokerConfiguration.HOST), e);
        }
    }

    public void declareQueue (final String queueName) {
        try {
            channel.queueDeclare(queueName, false, false, false, null);
        } catch (IOException e) {
            String message = "Error while declaring queue %s";
            throw new IllegalStateException(String.format(message, queueName), e);
        }
    }

    public void startConsuming (final String queue, DeliverCallback deliverCallback) {
        try {
            channel.basicConsume(queue, true, deliverCallback, consumerTag -> { });
        } catch (IOException e) {
            throw new IllegalStateException("Error while attempting to start message consumption");
        }
    }

    public void sendMessage (final String queue, byte[] bytes) {
        try {
            channel.basicPublish("", queue, null, bytes);
        } catch (IOException e) {
            String message = "Error while sending message to queue: %s";
            throw new IllegalStateException(String.format(message, queue));
        }
    }

    private void addShutdownHook() {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                connection.close();
                channel.close();
            } catch (final IOException | TimeoutException ignored) { }

        }, "shutdown-thread"));
    }

}
