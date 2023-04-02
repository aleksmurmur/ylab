package io.ylab.intensive.lesson04.movie;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;

public class MovieLoaderImpl implements MovieLoader {
    private DataSource dataSource;

    public MovieLoaderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
     * Оказывается батчи в filesort только по дз обязательны, но думаю и тут хуже не будет.
     * рекомендации Sun и Oracle по размеру батча - 50-100.
     * На лекции предлагали реализовать в т.ч. без батчей, по сути
     * выставление размера == 1 это и делает, т.к. внутри executeUpdate()
     * и executeBatch() одинаковая реализация
     */
    private static final int BATCH_SIZE = 100;

    @Override
    public void loadData(File file) {
        String query = """
                INSERT INTO movie(year, length, title, 
                subject, actors, actress, 
                director, popularity, awards) 
                VALUES (?,?,?,?,?,?,?,?,?)
                """;
        /*
         * Насколько я понял выставление auto-commit в false является good practice
         * при использовании батчей поэтому код раздулся блоками try-catch,
         * Без этого можно было всего autocloseable в try-with-resources делать
         */
        Connection connection = null;
        PreparedStatement movieStatement = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            movieStatement = connection.prepareStatement(query);

            validateFirstLines(reader);

            while (reader.ready()) {
                int currentBatchSize = 0;
                while (reader.ready() && currentBatchSize < BATCH_SIZE) {
                    Movie movie = convertLineToEntity(reader.readLine());
                    setParameters(movieStatement, movie);
                    movieStatement.addBatch();
                    currentBatchSize++;
                }
                movieStatement.executeBatch();
                connection.commit();
            }

            movieStatement.close();
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
                if (movieStatement != null) {
                    movieStatement.close();
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

    private void validateFirstLines(BufferedReader reader) throws IOException {
        String titleLine = reader.readLine();
        String columnTitles = reader.readLine();
        if (!titleLine.equals("Year;Length;Title;Subject;Actor;Actress;Director;Popularity;Awards;*Image")
                || !columnTitles.equals("INT;INT;STRING;CAT;CAT;CAT;CAT;INT;BOOL;STRING")) {
            throw new FileStructureNotValidException("File has not valid structure");
        }
    }

    private Movie convertLineToEntity(String line) {
        String[] movieArr = line.split(";");

        Movie movie = new Movie();
        movie.setYear(parseIntOrNull(movieArr[0]));
        movie.setLength(parseIntOrNull(movieArr[1]));
        movie.setTitle(movieArr[2]);
        movie.setSubject(movieArr[3]);
        movie.setActors(movieArr[4]);
        movie.setActress(movieArr[5]);
        movie.setDirector(movieArr[6]);
        movie.setPopularity(parseIntOrNull(movieArr[7]));
        movie.setAwards(movieArr[8].equalsIgnoreCase("Yes"));
        return movie;
    }

    private Integer parseIntOrNull(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void setParameters(PreparedStatement movieStatement, Movie movie) throws SQLException {
        setIntOrNull(movieStatement, 1, movie.getYear());
        setIntOrNull(movieStatement, 2, movie.getLength());
        movieStatement.setString(3, movie.getTitle());
        movieStatement.setString(4, movie.getSubject());
        movieStatement.setString(5, movie.getActors());
        movieStatement.setString(6, movie.getActress());
        movieStatement.setString(7, movie.getDirector());
        setIntOrNull(movieStatement, 8, movie.getPopularity());
        movieStatement.setBoolean(9, movie.getAwards());
    }

    private void setIntOrNull(PreparedStatement preparedStatement, int parameterIndex, Integer nullableInteger) throws SQLException {
        if (nullableInteger != null) {
            preparedStatement.setInt(parameterIndex, nullableInteger);
        } else {
            preparedStatement.setNull(parameterIndex, Types.INTEGER);
        }
    }
}
