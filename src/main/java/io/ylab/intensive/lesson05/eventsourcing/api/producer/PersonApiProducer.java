package io.ylab.intensive.lesson05.eventsourcing.api.producer;


public interface PersonApiProducer {

    void sendMessage(Action action, String message);
}
