package io.ylab.intensive.lesson05.eventsourcing.db.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import io.ylab.intensive.lesson05.eventsourcing.Action;
import io.ylab.intensive.lesson05.eventsourcing.db.PairDto;
import io.ylab.intensive.lesson05.eventsourcing.db.service.DbAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static io.ylab.intensive.lesson05.eventsourcing.db.Config.QUEUE_NAME;

@Component
@Primary
public class DbAppConsumerImpl implements DbAppConsumer {
    private static final Logger logger = LoggerFactory.getLogger(DbAppConsumerImpl.class);

    private Channel channel;
    private DbAppService service;

    public DbAppConsumerImpl(Channel channel, DbAppService dbAppService) {
        this.channel = channel;
        this.service = dbAppService;
    }

    @Override
    public void consume() {
        try {
            channel.basicConsume(QUEUE_NAME, false, "customTag",
                    new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag,
                                                   Envelope envelope,
                                                   AMQP.BasicProperties properties,
                                                   byte[] body) throws IOException {
                            String routingKey = envelope.getRoutingKey();
                            long deliveryTag = envelope.getDeliveryTag();

                            PairDto<Action, String> message = new PairDto<>(Action.valueOf(routingKey), new String(body));
                            service.processMessage(message);

                            channel.basicAck(deliveryTag, false);
                        }
                    });
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

}
