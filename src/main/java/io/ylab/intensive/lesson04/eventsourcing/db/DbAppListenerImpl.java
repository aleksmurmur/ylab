package io.ylab.intensive.lesson04.eventsourcing.db;

import com.fasterxml.jackson.core.type.TypeReference;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import io.ylab.intensive.lesson04.eventsourcing.JsonUtil;
import io.ylab.intensive.lesson04.eventsourcing.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

public class DbAppListenerImpl implements DbAppListener {

    private final static Logger logger = LoggerFactory.getLogger(DbAppListenerImpl.class);
    private PersonDAO personDAO;
    private ConnectionFactory connectionFactory;

    public DbAppListenerImpl(DataSource dataSource, ConnectionFactory connectionFactory) {
        this.personDAO = new PersonDAOImpl(dataSource);
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void listen() {
        String queueName = "queue";
        try (com.rabbitmq.client.Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            //Чтобы не вылететь с эксепшном если DbApp запущено первым
            channel.queueDeclare(queueName, true, false, false, null);

            while (!Thread.currentThread().isInterrupted()) {
                GetResponse message = channel.basicGet(queueName, true);
                if (message != null) {
                    byte[] msg = message.getBody();
                    var type = message.getEnvelope().getRoutingKey();
                    if (type.equals("save")) {
                        jsonToEntity(msg).ifPresent(person -> personDAO.save(person));
                    } else if (type.equals("delete")) {
                        jsonToId(msg).ifPresent(id -> personDAO.delete(id));
                    }
                }
            }
        } catch (IOException | TimeoutException e) {
            logger.warn("Connection exception", e);
        }
    }

    private Optional<Long> jsonToId(byte[] s) {
        Optional<Long> id = Optional.empty();
        try {
            id = Optional.of(JsonUtil.getInstance()
                    .readValue(s, new TypeReference<Map<String, Long>>() {
                    })
                    .get("id"));
        } catch (Exception e) {
            logger.warn("Json processing exception", e);
        }
        return id;
    }

    private Optional<Person> jsonToEntity(byte[] s) {
        Optional<Person> person = Optional.empty();
        try {
            person = Optional.of(JsonUtil.getInstance().readValue(s, Person.class));
        } catch (Exception e) {
            logger.warn("Json processing exception", e);
        }
        return person;
    }
}
