package io.ylab.intensive.lesson05.eventsourcing.api.producer;


import io.ylab.intensive.lesson05.eventsourcing.Action;

public interface PersonApiProducer {

    void sendMessage(Action action, String message);
}
