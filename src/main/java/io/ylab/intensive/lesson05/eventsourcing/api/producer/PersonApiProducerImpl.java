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

import static io.ylab.intensive.lesson05.eventsourcing.api.Config.EXCHANGE_NAME;
import static io.ylab.intensive.lesson05.eventsourcing.api.Config.QUEUE_NAME;

@Component
public class PersonApiProducerImpl implements PersonApiProducer{

    private final Logger logger = LoggerFactory.getLogger(PersonApiProducer.class);

    private Channel channel;

    public PersonApiProducerImpl(Channel channel) {
        this.channel = channel;
    }


    @Override
    public void sendMessage(Action action, String message) {
      try {
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, action.name());
            channel.basicPublish(EXCHANGE_NAME, action.name(), null, message.getBytes());
        } catch (IOException e) {
            logger.debug("Connection exception", e);
        }
    }
}
