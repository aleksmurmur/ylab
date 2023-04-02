package io.ylab.intensive.lesson04.eventsourcing.db;

import io.ylab.intensive.lesson04.eventsourcing.Person;

public interface PersonDAO {
    void save(Person person);

    void delete(Long id);
}
