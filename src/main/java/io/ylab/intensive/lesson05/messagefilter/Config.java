package io.ylab.intensive.lesson05.messagefilter;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

@Configuration
@ComponentScan("io.ylab.intensive.lesson05.messagefilter")
public class Config {

  public static final String INPUT_QUEUE = "input";
  public static final String OUTPUT_QUEUE = "output";
  public static final String EXCHANGE_NAME = "message-filter";
  public static final String ROUTING_KEY_INPUT = "direct-key-input";
  public static final String ROUTING_KEY_OUTPUT = "direct-key-output";
  
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
  public DataSource dataSource() {
    PGSimpleDataSource dataSource = new PGSimpleDataSource();
    dataSource.setServerName("localhost");
    dataSource.setUser("postgres");
    dataSource.setPassword("postgres");
    dataSource.setDatabaseName("postgres");
    dataSource.setPortNumber(5432);
    return dataSource;
  }


  @Bean(destroyMethod = "close")
  public Connection connection(DataSource dataSource) {
    try {
      return dataSource.getConnection();
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  @Bean(name = "rabbitConnection", destroyMethod = "close")
  com.rabbitmq.client.Connection connection(ConnectionFactory connectionFactory) {
    try {
      return connectionFactory.newConnection();
    } catch (IOException | TimeoutException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
  @Bean(destroyMethod = "close")
  public Channel channel(com.rabbitmq.client.Connection connection) {
    try {
      Channel channel = connection.createChannel();

      //Чтобы не было ошибок если были созданы очереди с такими именами, но другими параметрами
      channel.queueDelete(INPUT_QUEUE);
      channel.queueDelete(OUTPUT_QUEUE);
      channel.exchangeDelete(EXCHANGE_NAME);

      channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
      channel.queueDeclare(INPUT_QUEUE, true, false, false, null);
      channel.queueBind(INPUT_QUEUE, EXCHANGE_NAME, ROUTING_KEY_INPUT);

      channel.queueDeclare(OUTPUT_QUEUE, true, false, false, null);
      channel.queueBind(OUTPUT_QUEUE, EXCHANGE_NAME, ROUTING_KEY_OUTPUT);
      return channel;
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
