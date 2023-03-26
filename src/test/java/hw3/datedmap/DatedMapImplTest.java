package hw3.datedmap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class DatedMapImplTest {

    private DatedMap datedMap;

    @BeforeEach
    void setUp() {
        datedMap = new DatedMapImpl();

    }

    @Test
    void put() {
        datedMap.put("key", "value");

        assertTrue(datedMap.containsKey("key"));
    }

    @Test
    void get() {
        datedMap.put("key", "value");
        datedMap.put("key2", "value2");

        assertEquals("value", datedMap.get("key"));
        assertEquals("value2", datedMap.get("key2"));

        assertNull(datedMap.get("non-existent key"));
    }

    @Test
    void containsKey() {
        datedMap.put("key", "value");
        datedMap.put("key2", "value2");

        assertTrue(datedMap.containsKey("key"));
        assertTrue(datedMap.containsKey("key2"));

        assertFalse(datedMap.containsKey("non-existent key"));
    }

    @Test
    void remove() {
        datedMap.put("key", "value");
        datedMap.put("key2", "value2");
        datedMap.remove("key");
        datedMap.remove("key2");

        assertFalse(datedMap.containsKey("key"));
        assertFalse(datedMap.containsKey("key2"));
    }

    @Test
    void keySet() {
        datedMap.put("key", "value");
        datedMap.put("key2", "value2");

        assertEquals(2, datedMap.keySet().size());
    }

    @Test
    void getKeyLastInsertionDate() {
        datedMap.put("key", "value");
        datedMap.put("key2", "value2");

        assertNotNull(datedMap.getKeyLastInsertionDate("key"));
        assertNull(datedMap.getKeyLastInsertionDate("non-existent key"));
        datedMap.remove("key");
        assertNull(datedMap.getKeyLastInsertionDate("key"));
    }
}