package io.ylab.intensive.lesson05.messagefilter;

import io.ylab.intensive.lesson05.messagefilter.rabbit.RabbitConsumer;
import org.springframework.stereotype.Component;

@Component
public class MessageLauncherImpl implements MessageLauncher {
    private RabbitConsumer consumer;

    public MessageLauncherImpl(RabbitConsumer consumer) {
        this.consumer = consumer;
    }

    // В данном случае не самый обязательный класс/метод, но если переключиться с push api
    // на pull api, то можно отсюда в цикле опрашивать consumer и отдавать в сервис
    @Override
    public void start() {
        consumer.listen();
    }
}
