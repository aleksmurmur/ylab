package io.ylab.intensive.lesson05.eventsourcing.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

@Configuration
@ComponentScan("io.ylab.intensive.lesson05.eventsourcing.api")
public class Config {
  //по хорошему конфиги разнести на условные AppConfig, DbConfig, RabbitConfig
  public static final String QUEUE_NAME = "queue";
  public static final String EXCHANGE_NAME = "exc";
  
  @Bean
  public DataSource dataSource() {
    PGSimpleDataSource dataSource = new PGSimpleDataSource();
    dataSource.setServerName("localhost");
    dataSource.setUser("postgres");
    dataSource.setPassword("postgres");
    dataSource.setDatabaseName("postgres");
    dataSource.setPortNumber(5432);
    return dataSource;
  }

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

  //чистый spring (не wvc/boot...) вроде не создает бина ObjectMapper'a поэтому можем через new
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
      channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
      channel.queueDeclare(QUEUE_NAME, true, false, false, null);
      return channel;
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

}
