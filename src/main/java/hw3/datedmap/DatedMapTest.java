package hw3.datedmap;

public class DatedMapTest {

    public static void main(String[] args) {
        DatedMap datedMap = new DatedMapImpl();
        datedMap.put("First key", "First value");
        datedMap.put("Second key", "Second value");
        datedMap.put("Third key", "Third value");

        System.out.println(datedMap.containsKey("First key")); //true
        System.out.println(datedMap.get("Second key")); //Second value
        System.out.println(datedMap.getKeyLastInsertionDate("Second key")); //today date
        datedMap.remove("Second key");
        System.out.println(datedMap.containsKey("Second key")); //false
        System.out.println(datedMap.keySet());//First key, Third key
    }
}
