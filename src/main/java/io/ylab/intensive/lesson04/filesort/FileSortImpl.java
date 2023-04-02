package io.ylab.intensive.lesson04.filesort;


import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileSortImpl implements FileSorter {
    private DataSource dataSource;

    public FileSortImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
     * Комментарий тот же, что и в MovieLoaderImpl
     * рекомендации Sun и Oracle по размеру батча - 50-100.
     * На лекции предлагали реализовать в т.ч. без батчей, по сути
     * выставление размера == 1 это и делает, т.к. внутри executeUpdate()
     * и executeBatch() одинаковая реализация
     */
    private static final int BATCH_SIZE = 100;

    @Override
    public File sort(File data) {

        writeFileToDb(data);
        File output = new File("result.txt");
        writeSortedToFile(output);

        return output;
    }

    private void writeFileToDb(File data) {
        String query = "INSERT INTO numbers(val) VALUES (?)";
        /*
         * Комментарий тот же, что и в MovieLoaderImpl
         * Насколько я понял выставление auto-commit в false является good practice
         * при использовании батчей поэтому код раздулся блоками try-catch,
         * Без этого можно было всего autocloseable в try-with-resources делать
         */
        Connection connection = null;
        PreparedStatement numbersStatement = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(data))) {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            numbersStatement = connection.prepareStatement(query);

            while (reader.ready()) {
                int currentBatchSize = 0;
                while (reader.ready() && currentBatchSize < BATCH_SIZE) {
                    String line = reader.readLine();
                    long number = Long.parseLong(line);
                    numbersStatement.setLong(1, number);
                    numbersStatement.addBatch();
                    currentBatchSize++;
                }
                numbersStatement.executeBatch();
                connection.commit();
            }

            numbersStatement.close();
            connection.setAutoCommit(true);
            connection.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (connection != null)
                    connection.rollback();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        } finally {
            try {
                if (numbersStatement != null) {
                    numbersStatement.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    private void writeSortedToFile(File output) {
        String query = """
                SELECT val FROM numbers ORDER BY val DESC --LIMIT ? OFFSET ?
                """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                writer.write(rs.getLong(1) + System.lineSeparator());
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
