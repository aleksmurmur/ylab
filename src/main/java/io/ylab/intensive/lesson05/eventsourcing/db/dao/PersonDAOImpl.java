package io.ylab.intensive.lesson05.eventsourcing.db.dao;

import io.ylab.intensive.lesson05.eventsourcing.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class PersonDAOImpl implements PersonDAO {
    private static final Logger logger = LoggerFactory.getLogger(PersonDAOImpl.class);

    private static final String CREATE = """
                INSERT INTO person(person_id, first_name, last_name, middle_name) VALUES (?,?,?,?)
                """;
    private static final String UPDATE = """
                UPDATE person SET first_name=?, last_name=?, middle_name=? WHERE person_id=?
                 """;
    private static final String DELETE = """
                DELETE  FROM person WHERE person_id=?
                """;

    private Connection connection;

    public PersonDAOImpl(Connection connection) {
            this.connection = connection;
    }

    @Override
    public void save(Person person) {
        try {
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
        try (PreparedStatement statement = connection.prepareStatement(DELETE)) {
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
        try (PreparedStatement updateStatement = connection.prepareStatement(UPDATE)) {
            updateStatement.setString(1, person.getName());
            updateStatement.setString(2, person.getLastName());
            updateStatement.setString(3, person.getMiddleName());
            updateStatement.setLong(4, person.getId());

            updateStatement.executeUpdate();
        }
    }

    private void create(Connection connection, Person person) throws SQLException {
        try (PreparedStatement createStatement = connection.prepareStatement(CREATE)) {
            createStatement.setLong(1, person.getId());
            createStatement.setString(2, person.getName());
            createStatement.setString(3, person.getLastName());
            createStatement.setString(4, person.getMiddleName());

            createStatement.executeUpdate();
        }
    }

    private boolean checkIfExists(Connection connection, Long id) throws SQLException {
        boolean exists = false;
        String getQuery = "SELECT person_id FROM person WHERE person_id=?";
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
