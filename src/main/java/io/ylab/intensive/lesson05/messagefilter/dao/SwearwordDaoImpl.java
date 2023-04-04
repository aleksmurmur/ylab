package io.ylab.intensive.lesson05.messagefilter.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

@Repository
public class SwearwordDaoImpl implements SwearwordDao {

    private static final Logger logger = LoggerFactory.getLogger(SwearwordDaoImpl.class);
    private static final String TABLE_NAME = "swearword";
    private static final String FILE = "swearwordDictionary";
    private static final int BATCH_SIZE = 100;
    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS swearword (
            word VARCHAR
            )
            """;
    private static final String DELETE_ALL = """
            DELETE FROM swearword
            """;
    private static final String INSERT = "INSERT INTO swearword(word) VALUES (?)";
    private static final String EXISTS = "SELECT * FROM swearword WHERE word ILIKE ?";

    private Connection connection;

    public SwearwordDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean existsIgnoreCase(String word) {
        boolean exists = false;
        try (PreparedStatement statement = connection.prepareStatement(EXISTS)) {
            statement.setString(1, word);
            ResultSet rs = statement.executeQuery();
            exists = rs.next();
            rs.close();
        } catch (SQLException e) {
            logger.warn(e.getMessage(), e);
        }
        return exists;
    }


    @PostConstruct
    void init() throws SQLException {
        if (!tableExists()) {
            createTable();
        }
        deleteAllData();
        fillDb();
    }

    private boolean tableExists() throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        try (ResultSet rs = databaseMetaData.getTables(null, null, TABLE_NAME, null)) {
            return rs.next();
        }
    }

    private void createTable() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_TABLE)) {
            statement.executeUpdate();
        }
    }

    private void deleteAllData() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_ALL)) {
            statement.executeUpdate();
        }
    }

    private void fillDb() throws SQLException {

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE));
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            int currentBatchSize = 0;
            while (reader.ready()) {
                String line = reader.readLine();
                statement.setString(1, line);
                statement.addBatch();
                currentBatchSize++;
                if (currentBatchSize > BATCH_SIZE || !reader.ready()) {
                    statement.executeBatch();
                    currentBatchSize = 0;
                }
            }
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

}
