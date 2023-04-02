package io.ylab.intensive.lesson05.eventsourcing.api;

import io.ylab.intensive.lesson05.eventsourcing.Person;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApiApp {
  public static void main(String[] args) throws Exception {
    // Тут пишем создание PersonApi, запуск и демонстрацию работы
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    PersonApi personApi = applicationContext.getBean(PersonApi.class);

    personApi.savePerson(1L, "bo", "bo", "jack");
    personApi.savePerson(2L, "Jack", "Jackson", "J");
    //Ждем ради консистентности данных
    Thread.sleep(1000);
    System.out.println(personApi.findAll().size());//2
    Person person = personApi.findPerson(2L);
    if (person != null) {
      System.out.println(person.getName());//Jack
    }
    personApi.savePerson(2L, "Oliver", "Jackson", "J");//update
    Thread.sleep(1000);
    if (person != null) {
      System.out.println(personApi.findPerson(2L).getName());//Oliver
    }
    personApi.deletePerson(1L);
    Thread.sleep(1000);
    System.out.println(personApi.findPerson(1L));//null
    personApi.deletePerson(4L);//В DbApp: Данные не найдены. Попытка удаления person с id: 4
    personApi.savePerson(3L, "Ivan", "Ivanov", "Ivanovich");
    Thread.sleep(1000);
    System.out.println(personApi.findAll().size());//2
    // пишем взаимодействие с PersonApi
  }
}
