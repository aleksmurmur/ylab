package io.ylab.intensive.lesson05.eventsourcing.db.service;

import io.ylab.intensive.lesson05.eventsourcing.Action;
import io.ylab.intensive.lesson05.eventsourcing.db.PairDto;

public interface DbAppService {

    void processMessage(PairDto<Action, String > dto);
}

