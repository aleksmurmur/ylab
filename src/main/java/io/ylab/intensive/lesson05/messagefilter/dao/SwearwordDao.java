package io.ylab.intensive.lesson05.messagefilter.dao;

public interface SwearwordDao {

    boolean existsIgnoreCase(String word);
}
