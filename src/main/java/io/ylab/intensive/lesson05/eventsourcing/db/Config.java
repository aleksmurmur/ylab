package io.ylab.intensive.lesson05.eventsourcing.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson05.DbUtil;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

@Configuration
@ComponentScan("io.ylab.intensive.lesson05.eventsourcing.db")
public class Config {
    public static final String QUEUE_NAME = "queue";

    @Bean
    public DataSource dataSource() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");
        dataSource.setDatabaseName("postgres");
        dataSource.setPortNumber(5432);

        String ddl = ""
                + "drop table if exists person;"
                + "create table if not exists person (\n"
                + "person_id bigint primary key,\n"
                + "first_name varchar,\n"
                + "last_name varchar,\n"
                + "middle_name varchar\n"
                + ")";
        DbUtil.applyDdl(ddl, dataSource);

        return dataSource;
    }

    //Вроде создание таких бинов соединений\каналов с закрытием после не противоречит
    //здравому смыслу и целям задачи. Все таки это выглядит лучше чем открывать соединения
    //каждый раз
    @Bean(name = "sqlConnection", destroyMethod = "close")
    public java.sql.Connection connection(DataSource dataSource) {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean(name = "rabbitConnection",destroyMethod = "close")
    Connection connection(ConnectionFactory connectionFactory) {
        try {
            return connectionFactory.newConnection();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @Bean(destroyMethod = "close")
    public Channel channel(Connection connection) {
        try {
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            return channel;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


}
