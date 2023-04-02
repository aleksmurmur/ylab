package io.ylab.intensive.lesson05.eventsourcing.db.dao;

import io.ylab.intensive.lesson05.eventsourcing.Person;

public interface PersonDAO {
    void save(Person person);

    void delete(Long id);
}
