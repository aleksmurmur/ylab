package io.ylab.intensive.lesson05.eventsourcing.api.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
@Component
public class PersonApiProducerImpl implements PersonApiProducer{

    private final Logger logger = LoggerFactory.getLogger(PersonApiProducer.class);

    private static final String exchangeName = "exc";
    private static final String queueName = "queue";

    private final ConnectionFactory connectionFactory;

    public PersonApiProducerImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }


    @Override
    public void sendMessage(Action action, String message) {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, action.name());

            channel.basicPublish(exchangeName, action.name(), null, message.getBytes());

        } catch (IOException | TimeoutException e) {
            logger.debug("Connection exception", e);
        }
    }
}
