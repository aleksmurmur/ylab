package io.ylab.intensive.lesson05.messagefilter.rabbit;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;


import static io.ylab.intensive.lesson05.messagefilter.Config.*;

@Component
public class ProducerImpl implements RabbitProducer {
    private static final Logger logger = LoggerFactory.getLogger(ProducerImpl.class);

    private Channel channel;

    public ProducerImpl(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void sendMessage(String message) {
        try {
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_OUTPUT, null, message.getBytes());
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }
}
