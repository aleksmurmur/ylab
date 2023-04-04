package io.ylab.intensive.lesson05.messagefilter.rabbit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import io.ylab.intensive.lesson05.messagefilter.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static io.ylab.intensive.lesson05.messagefilter.Config.INPUT_QUEUE;

@Component
public class ConsumerImpl implements RabbitConsumer {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerImpl.class);


    private Channel channel;
    private MessageService messageService;

    public ConsumerImpl(Channel channel, MessageService messageService) {
        this.channel = channel;
        this.messageService = messageService;
    }

    @Override
    public void listen() {
        try {
            channel.basicConsume(INPUT_QUEUE, false, "input",
                    new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag,
                                                   Envelope envelope,
                                                   AMQP.BasicProperties properties,
                                                   byte[] body) throws IOException {
                            long deliveryTag = envelope.getDeliveryTag();

                            messageService.processMessage(new String(body));

                            channel.basicAck(deliveryTag, false);
                        }
                    });
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

}
