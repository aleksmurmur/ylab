package io.ylab.intensive.lesson05.eventsourcing.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.intensive.lesson05.eventsourcing.Person;
import io.ylab.intensive.lesson05.eventsourcing.api.dao.PersonApiDao;
import io.ylab.intensive.lesson05.eventsourcing.api.producer.Action;
import io.ylab.intensive.lesson05.eventsourcing.api.producer.PersonApiProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonApiImpl implements PersonApi {

    private static final Logger logger = LoggerFactory.getLogger(PersonApiImpl.class);

    private final PersonApiProducer producer;
    private final PersonApiDao dao;
    private final ObjectMapper objectMapper;

    public PersonApiImpl(PersonApiProducer producer, PersonApiDao dao, ObjectMapper objectMapper) {
        this.producer = producer;
        this.dao = dao;
        this.objectMapper = objectMapper;
    }

    @Override
    public void deletePerson(Long personId) {
        String message = "{\"id\":" + personId + "}";
        producer.sendMessage(Action.DELETE, message);
    }

    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName) {
        Person person = new Person(personId, firstName, lastName, middleName);
        String message = readValue(person);
        producer.sendMessage(Action.SAVE, message);
    }

    @Override
    public Person findPerson(Long personId) {
        return dao.findPerson(personId);
    }

    @Override
    public List<Person> findAll() {
        return dao.findAll();
    }

    private String readValue(Person person) {
        String message = "";
        try {
            message = objectMapper.writeValueAsString(person);
        } catch (JsonProcessingException e) {
            logger.debug(e.getMessage(), e);
        }
        return message;
    }

}
