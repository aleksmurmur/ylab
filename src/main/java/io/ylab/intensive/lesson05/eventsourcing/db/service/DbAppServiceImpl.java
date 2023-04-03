package io.ylab.intensive.lesson05.eventsourcing.db.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.intensive.lesson05.eventsourcing.Person;
import io.ylab.intensive.lesson05.eventsourcing.db.Action;
import io.ylab.intensive.lesson05.eventsourcing.db.PairDto;
import io.ylab.intensive.lesson05.eventsourcing.db.dao.PersonDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static io.ylab.intensive.lesson05.eventsourcing.db.Action.DELETE;
import static io.ylab.intensive.lesson05.eventsourcing.db.Action.SAVE;

@Service
public class DbAppServiceImpl implements DbAppService {

    private static final Logger logger = LoggerFactory.getLogger(DbAppServiceImpl.class);
    private final PersonDAO dao;
    private final ObjectMapper objectMapper;

    public DbAppServiceImpl(PersonDAO dao, ObjectMapper objectMapper) {
        this.dao = dao;
        this.objectMapper = objectMapper;
    }

    @Override
    public void processMessage(PairDto<Action, String> message) {
        if (message.getKey().equals(SAVE)) {
            jsonToEntity(message.getValue()).ifPresent(dao::save);
        } else if (message.getKey().equals(DELETE)) {
            jsonToId(message.getValue()).ifPresent(dao::delete);
        }
    }


    private Optional<Long> jsonToId(String json) {
        Optional<Long> id = Optional.empty();
        try {
            id = Optional.of(objectMapper
                    .readValue(json, new TypeReference<Map<String, Long>>() {
                    })
                    .get("id"));
        } catch (Exception e) {
            logger.warn("Json processing exception", e);
        }
        return id;
    }

    private Optional<Person> jsonToEntity(String json) {
        Optional<Person> person = Optional.empty();
        try {
            person = Optional.of(objectMapper.readValue(json, Person.class));
        } catch (Exception e) {
            logger.warn("Json processing exception", e);
        }
        return person;
    }
}
