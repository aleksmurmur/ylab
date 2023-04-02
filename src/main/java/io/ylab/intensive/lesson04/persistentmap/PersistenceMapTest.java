package io.ylab.intensive.lesson04.persistentmap;

import io.ylab.intensive.lesson04.DbUtil;

import javax.sql.DataSource;
import java.sql.SQLException;

public class PersistenceMapTest {
  public static void main(String[] args) throws SQLException {
    DataSource dataSource = initDb();
    PersistentMapImpl persistentMap = new PersistentMapImpl(dataSource);

    //Все методы бросают исключение, если не инициализировать изначально мапу
    persistentMap.init("first");
    System.out.println(persistentMap.containsKey("key")); //false
    System.out.println(persistentMap.get("key")); //null
    persistentMap.remove("key");
    persistentMap.clear();
    persistentMap.put("key", "value");
    System.out.println(persistentMap.containsKey("key")); //true
    System.out.println(persistentMap.get("key")); //value
    System.out.println(persistentMap.getKeys().size());// 1

    persistentMap.init("second");
    System.out.println(persistentMap.containsKey("key")); //false
    System.out.println(persistentMap.get("key")); //null
    System.out.println(persistentMap.getKeys().size());// 0
    persistentMap.put("map2Key", "map2value");
    persistentMap.put("map2Key2", "map2value2");
    System.out.println(persistentMap.get("map2Key")); //map2value
    System.out.println(persistentMap.getKeys().size());// 2
    persistentMap.remove("map2Key");
    System.out.println(persistentMap.get("map2Key")); //null
    System.out.println(persistentMap.getKeys().size());// 1

    persistentMap.init("first");
    System.out.println(persistentMap.get("key")); //value
    System.out.println(persistentMap.getKeys().size());// 1
    persistentMap.put("key", "newValue");
    System.out.println(persistentMap.get("key")); //newValue
    System.out.println(persistentMap.getKeys().size());// 1
    persistentMap.clear();
    System.out.println(persistentMap.containsKey("key")); //false
    System.out.println(persistentMap.get("key")); //null
    System.out.println(persistentMap.getKeys().size());// 0
  }
  
  public static DataSource initDb() throws SQLException {
    String createMapTable = "" 
                                + "drop table if exists persistent_map; " 
                                + "CREATE TABLE if not exists persistent_map (\n"
                                + "   map_name varchar,\n"
                                + "   KEY varchar,\n"
                                + "   value varchar\n"
                                + ");";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(createMapTable, dataSource);
    return dataSource;
  }
}
