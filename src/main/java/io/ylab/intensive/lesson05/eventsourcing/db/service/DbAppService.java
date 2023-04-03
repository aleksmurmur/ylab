package io.ylab.intensive.lesson05.eventsourcing.db.service;

import io.ylab.intensive.lesson05.eventsourcing.db.Action;
import io.ylab.intensive.lesson05.eventsourcing.db.PairDto;

public interface DbAppService {

//    void listen();

    void processMessage(PairDto<Action, String > dto);
}

