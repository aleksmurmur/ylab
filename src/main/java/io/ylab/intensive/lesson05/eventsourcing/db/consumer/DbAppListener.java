package io.ylab.intensive.lesson05.eventsourcing.db.consumer;


public interface DbAppListener {

    PairDto<Action, byte[]> consume();


}
