package io.ylab.intensive.lesson05.eventsourcing.db;

import io.ylab.intensive.lesson05.eventsourcing.db.consumer.DbAppConsumer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DbApp {
  public static void main(String[] args) throws Exception {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    //без этого не отрабатывали destroy callback'и
    applicationContext.registerShutdownHook();

    DbAppConsumer consumer = applicationContext.getBean(DbAppConsumer.class);
    consumer.consume();

  }
}
