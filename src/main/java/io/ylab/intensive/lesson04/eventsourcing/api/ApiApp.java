package io.ylab.intensive.lesson04.eventsourcing.api;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.RabbitMQUtil;

import javax.sql.DataSource;

public class ApiApp {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = initMQ();
        DataSource dataSource = DbUtil.buildDataSource();

        PersonApi personApi = new PersonApiImpl(connectionFactory, dataSource);
        personApi.savePerson(1L, "bo", "bo", "jack");
        personApi.savePerson(2L, "Jack", "Jackson", "J");
        //Ждем ради консистентности данных
        Thread.sleep(1000);
        System.out.println(personApi.findAll().size());//2
        System.out.println(personApi.findPerson(2l).getName());//Jack
        personApi.savePerson(2L, "Oliver", "Jackson", "J");//update
        Thread.sleep(1000);
        System.out.println(personApi.findPerson(2l).getName());//Oliver
        personApi.deletePerson(1L);
        Thread.sleep(1000);
        System.out.println(personApi.findPerson(1l));//null
        personApi.deletePerson(4L);//В DbApp: Данные не найдены. Попытка удаления person с id: 4
        personApi.savePerson(3L, "Ivan", "Ivanov", "Ivanovich");
        Thread.sleep(1000);
        System.out.println(personApi.findAll().size());//2


        // Тут пишем создание PersonApi, запуск и демонстрацию работы
    }

    private static ConnectionFactory initMQ() throws Exception {
        return RabbitMQUtil.buildConnectionFactory();
    }
}
