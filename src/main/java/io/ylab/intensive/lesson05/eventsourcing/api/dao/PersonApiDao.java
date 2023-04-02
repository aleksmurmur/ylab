package io.ylab.intensive.lesson05.eventsourcing.api.dao;

import io.ylab.intensive.lesson05.eventsourcing.Person;

import java.util.List;

public interface PersonApiDao {

    Person findPerson(Long personId);

    List<Person> findAll();

}
