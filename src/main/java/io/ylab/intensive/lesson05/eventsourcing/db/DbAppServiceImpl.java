package io.ylab.intensive.lesson05.eventsourcing.db;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.intensive.lesson05.eventsourcing.Person;
import io.ylab.intensive.lesson05.eventsourcing.db.consumer.Action;
import io.ylab.intensive.lesson05.eventsourcing.db.consumer.DbAppListener;
import io.ylab.intensive.lesson05.eventsourcing.db.consumer.PairDto;
import io.ylab.intensive.lesson05.eventsourcing.db.dao.PersonDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static io.ylab.intensive.lesson05.eventsourcing.db.consumer.Action.DELETE;
import static io.ylab.intensive.lesson05.eventsourcing.db.consumer.Action.SAVE;

@Service
public class DbAppServiceImpl implements DbAppService {

    private static final Logger logger = LoggerFactory.getLogger(DbAppServiceImpl.class);
    private final DbAppListener listener;
    private final PersonDAO dao;
    private final ObjectMapper objectMapper;

    public DbAppServiceImpl(DbAppListener listener, PersonDAO dao, ObjectMapper objectMapper) {
        this.listener = listener;
        this.dao = dao;
        this.objectMapper = objectMapper;
    }

    @Override
    public void listen() {
        while (!Thread.currentThread().isInterrupted()) {
             PairDto<Action, byte[]> message = listener.consume();
            if (message != null) {
                if (message.getKey().equals(SAVE)) {
                    jsonToEntity(message.getValue()).ifPresent(dao::save);
                } else if (message.getKey().equals(DELETE)) {
                    jsonToId(message.getValue()).ifPresent(dao::delete);
                }
            }
        }
    }


    private Optional<Long> jsonToId(byte[] s) {
        Optional<Long> id = Optional.empty();
        try {
            id = Optional.of(objectMapper
                    .readValue(s, new TypeReference<Map<String, Long>>() {
                    })
                    .get("id"));
        } catch (Exception e) {
            logger.warn("Json processing exception", e);
        }
        return id;
    }

    private Optional<Person> jsonToEntity(byte[] json) {
        Optional<Person> person = Optional.empty();
        try {
            person = Optional.of(objectMapper.readValue(json, Person.class));
        } catch (Exception e) {
            logger.warn("Json processing exception", e);
        }
        return person;
    }
}
