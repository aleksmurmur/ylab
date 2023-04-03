package io.ylab.intensive.lesson05.sqlquerybuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SQLQueryBuilderImpl implements SQLQueryBuilder {

    Logger logger = LoggerFactory.getLogger(SQLQueryBuilderImpl.class);
    private final Connection connection;

    public SQLQueryBuilderImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public String queryForTable(String tableName) throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();

        if (!tableExists(databaseMetaData, tableName)) {
            logger.debug(tableName + " - таблица не существует");
            return null;
        }
        List<String> columns = getColumnNames(databaseMetaData, tableName);
        return concateSelectAllQuery(columns, tableName);
    }

    @Override
    public List<String> getTables() throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();

        // В чате писали выводить все, включая системные, таблицы.
        // А формально в бд и вьхи и индексы и пр. тоже таблицы.
        // В целом алгоритм одинаковый
        // Тут хотя бы отфильтруем таблицы без типа.
        ResultSet rs = databaseMetaData.getTableTypes();
        List<String> tableType = fillFromResultSet(rs, "TABLE_TYPE");

        rs = databaseMetaData.getTables(null, null, null, tableType.toArray(new String[0]));
        List<String> tables = fillFromResultSet(rs, "TABLE_NAME");

        rs.close();
        return tables;
    }

    private boolean tableExists(DatabaseMetaData databaseMetaData, String tableName) throws SQLException {
        try (ResultSet rs = databaseMetaData.getTables(null, null, tableName, null)) {
            return rs.next();
        }
    }

    private List<String> getColumnNames(DatabaseMetaData databaseMetaData,
                                        String tableName) throws SQLException {
        try (ResultSet rs = databaseMetaData.getColumns(null, null,
                tableName, null)) {
            return fillFromResultSet(rs, "COLUMN_NAME");
        }
    }

    private List<String> fillFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
        List<String> result = new ArrayList<>();
        while (rs.next()) {
            result.add(rs.getString(columnLabel));
        }
        return result;
    }

    private String concateSelectAllQuery(List<String> columns, String tableName) {
        String query = null;
        if (!columns.isEmpty()) {
            query = columns.stream().collect(Collectors.joining(", ", "SELECT ", " FROM " + tableName));
        } else {
            logger.debug(tableName + ": таблица без столбцов");
        }
        return query;
    }
}
