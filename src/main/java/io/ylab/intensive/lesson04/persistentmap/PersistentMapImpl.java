package io.ylab.intensive.lesson04.persistentmap;

import javax.sql.DataSource;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, методы которого надо реализовать
 */
public class PersistentMapImpl implements PersistentMap {

    private DataSource dataSource;

    public PersistentMapImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private String name;

    @Override
    public void init(String name) {
        this.name = name;
    }

    @Override
    public boolean containsKey(String key) throws SQLException {
        validateInitialized();
        String query = """
                SELECT count(*) FROM persistent_map WHERE map_name=? AND key =?
                """;
        int resultColumns;
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, key);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            resultColumns = rs.getInt(1);

            rs.close();
        }
        return resultColumns != 0;
    }

    @Override
    public List<String> getKeys() throws SQLException {
        validateInitialized();
        String query = """
                SELECT key FROM persistent_map WHERE map_name=?
                """;
        List<String> result = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                result.add(rs.getString("key"));
            }
            rs.close();
        }
        return result;
    }

    @Override
    public String get(String key) throws SQLException {
        validateInitialized();
        String query = """
                SELECT value FROM persistent_map WHERE map_name=? AND key =?
                """;
        String value = null;
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, key);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                value = rs.getString("value");
            }
            rs.close();
        }
        return value;
    }

    @Override
    public void remove(String key) throws SQLException {
        validateInitialized();
        String query = """
                DELETE FROM persistent_map WHERE map_name=? AND key =?
                """;
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, key);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void put(String key, String value) throws SQLException {
        validateInitialized();
        remove(key);

        String query = """
                INSERT INTO persistent_map(map_name, key, value) VALUES (?,?,?)
                """;
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, key);
            preparedStatement.setString(3, value);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void clear() throws SQLException {
        validateInitialized();
        String query = """
                DELETE FROM persistent_map WHERE map_name=?
                """;
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        }
    }

    private void validateInitialized() {
        if (this.name == null) {
            throw new MapNotInitializedException("Map should be initialized before calling any methods");
        }
    }
}
