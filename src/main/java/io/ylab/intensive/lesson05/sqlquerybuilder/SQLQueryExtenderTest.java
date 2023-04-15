package io.ylab.intensive.lesson05.sqlquerybuilder;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;
import java.util.List;

public class SQLQueryExtenderTest {
    public static void main(String[] args) throws SQLException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();
        applicationContext.registerShutdownHook();
        SQLQueryBuilder queryBuilder = applicationContext.getBean(SQLQueryBuilder.class);
        List<String> tables = queryBuilder.getTables();
        System.out.println(tables.size());
        // вот так сгенерируем запросы для всех таблиц что есть в БД
        for (String tableName : tables) {
            //В чате писали возрващать null если нет столбцов и выводить в консоль.
            //В консоль логгер выводит, а null не будем печатать
            String query = queryBuilder.queryForTable(tableName);
            if (query != null) {
                System.out.println(query);
            }
        }
    }
}
