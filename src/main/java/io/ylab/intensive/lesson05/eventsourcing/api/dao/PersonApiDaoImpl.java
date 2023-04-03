package io.ylab.intensive.lesson05.eventsourcing.api.dao;

import io.ylab.intensive.lesson05.eventsourcing.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PersonApiDaoImpl implements PersonApiDao {

    private final Logger logger = LoggerFactory.getLogger(PersonApiDao.class);

    private static final String getByIdQuery = """
            SELECT * FROM person WHERE person_id=?
            """;
    private static final String getAllQuery = """
            SELECT * FROM person
            """;

    private final Connection connection;

    public PersonApiDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Person findPerson(Long personId) {
        Person person = null;
        try (PreparedStatement statement = connection.prepareStatement(getByIdQuery)) {
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
        List<Person> people = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(getAllQuery);
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
