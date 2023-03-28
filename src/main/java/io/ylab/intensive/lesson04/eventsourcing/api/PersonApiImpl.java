package io.ylab.intensive.lesson04.eventsourcing.api;

import com.rabbitmq.client.*;
import io.ylab.intensive.lesson04.eventsourcing.JsonUtil;
import io.ylab.intensive.lesson04.eventsourcing.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * Тут пишем реализацию
 */
public class PersonApiImpl implements PersonApi {

    private final Logger logger = LoggerFactory.getLogger(PersonApiImpl.class);

    private ConnectionFactory connectionFactory;
    private DataSource dataSource;

    public PersonApiImpl(ConnectionFactory connectionFactory, DataSource dataSource) {
        this.connectionFactory = connectionFactory;
        this.dataSource = dataSource;
    }

    @Override
    public void deletePerson(Long personId) {
        String exchangeName = "exc";
        String queueName = "queue";
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, "delete");

            String json = "{\"id\":" + personId + "}";
            channel.basicPublish(exchangeName, "delete", null, json.getBytes());

        } catch (IOException | TimeoutException e) {
            logger.debug("Connection exception", e);
        }

    }

    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName) {
        String exchangeName = "exc";
        String queueName = "queue";
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, "save");

            Person person = new Person(personId, firstName, lastName, middleName);
            String json = JsonUtil.getInstance().writeValueAsString(person);
            channel.basicPublish(exchangeName, "save", null, json.getBytes());

        } catch (IOException | TimeoutException e) {
            logger.debug("Connection exception", e);
        }
    }

    @Override
    public Person findPerson(Long personId) {
        String query = """
                SELECT * FROM person WHERE person_id=?
                """;

        Person person = null;
        try (java.sql.Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, personId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                person = getPerson(rs);
            }

            rs.close();
        } catch (SQLException e) {
            logger.warn(e.getMessage(), e);
        }
        return person;
    }

    @Override
    public List<Person> findAll() {
        String query = """
                SELECT * FROM person
                """;

        List<Person> people = new ArrayList<>();
        try (java.sql.Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                people.add(getPerson(rs));
            }

        } catch (SQLException e) {
            logger.warn(e.getMessage(), e);
        }
        return people;
    }

    private Person getPerson(ResultSet rs) throws SQLException {
        Person person = new Person();
        person.setId(rs.getLong("person_id"));
        person.setName(rs.getString("first_name"));
        person.setLastName(rs.getString("last_name"));
        person.setMiddleName(rs.getString("middle_name"));
        return person;
    }

}
