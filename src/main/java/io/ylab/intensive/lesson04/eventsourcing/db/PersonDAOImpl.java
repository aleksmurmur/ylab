package io.ylab.intensive.lesson04.eventsourcing.db;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import io.ylab.intensive.lesson04.eventsourcing.JsonUtil;
import io.ylab.intensive.lesson04.eventsourcing.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonDAOImpl implements PersonDAO {

    private final static Logger logger = LoggerFactory.getLogger(PersonDAOImpl.class);
    private DataSource dataSource;

    public PersonDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Person person) {
        try (Connection connection = dataSource.getConnection()) {
            if (checkIfExists(connection, person.getId())) {
                update(connection, person);
            } else {
                create(connection, person);
            }
        } catch (SQLException e) {
            logger.debug(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        String query = """
                DELETE  FROM person WHERE person_id=?
                """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            int deleted = statement.executeUpdate();
            if (deleted == 0) {
                logger.info("Данные не найдены. Попытка удаления person с id: " + id);
            }

        } catch (SQLException e) {
            logger.debug(e.getMessage());
        }
    }

    private void update(Connection connection, Person person) throws SQLException {
        String updateQuery = """
                UPDATE person SET first_name=?, last_name=?, middle_name=? WHERE person_id=?
                 """;
        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setString(1, person.getName());
            updateStatement.setString(2, person.getLastName());
            updateStatement.setString(3, person.getMiddleName());
            updateStatement.setLong(4, person.getId());

            updateStatement.executeUpdate();
        }
    }

    private void create(Connection connection, Person person) throws SQLException {
        String createQuery = """
                INSERT INTO person(person_id, first_name, last_name, middle_name) VALUES (?,?,?,?)
                """;
        try (PreparedStatement createStatement = connection.prepareStatement(createQuery)) {
            createStatement.setLong(1, person.getId());
            createStatement.setString(2, person.getName());
            createStatement.setString(3, person.getLastName());
            createStatement.setString(4, person.getMiddleName());

            createStatement.executeUpdate();
        }
    }

    private boolean checkIfExists(Connection connection, Long id) throws SQLException {
        boolean exists = false;
        String getQuery = "SELECT * FROM person WHERE person_id=?";
        try (PreparedStatement getStatement = connection.prepareStatement(getQuery)) {
            getStatement.setLong(1, id);
            ResultSet rs = getStatement.executeQuery();
            if (rs.next()) {
                exists = true;
            }
            rs.close();
            return exists;
        }
    }

}
