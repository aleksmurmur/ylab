package io.ylab.intensive.lesson05.eventsourcing.db.consumer;

public record PairDto<K, V>(
         K key,
         V value
) {

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
