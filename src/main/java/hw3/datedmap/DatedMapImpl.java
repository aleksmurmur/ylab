package hw3.datedmap;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DatedMapImpl implements DatedMap {

    static class Pair {
        Pair(String value, Date insertionDate) {
            this.value = value;
            this.insertionDate = insertionDate;
        }

        String value;
        Date insertionDate;
    }
    private final Map<String, Pair> map = new HashMap<>();

    @Override
    public void put(String key, String value) {
        map.put(key, new Pair(value, new Date()));
    }

    @Override
    public String get(String key) {
        Pair currentValue = map.get(key);
        return currentValue != null ? currentValue.value : null;
    }

    @Override
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    @Override
    public void remove(String key) {
        map.remove(key);
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Date getKeyLastInsertionDate(String key) {
        Pair currentValue = map.get(key);
        return currentValue != null ? currentValue.insertionDate : null;
    }
}
