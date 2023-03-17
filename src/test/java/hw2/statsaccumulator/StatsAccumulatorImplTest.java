package hw2.statsaccumulator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatsAccumulatorImplTest {

    private StatsAccumulator statsAccumulator;

    @BeforeEach
    void setUp() {
        statsAccumulator = new StatsAccumulatorImpl();
    }

    @Test
    void add() {
        fillStatsAccumulator();

        assertNotEquals(0, statsAccumulator.getMin());
        assertNotEquals(0, statsAccumulator.getMax());
        assertNotEquals(0, statsAccumulator.getCount());
        assertNotEquals(0, statsAccumulator.getAvg());

    }

    @Test
    void getMin() {
        assertEquals(0, statsAccumulator.getMin());

        fillStatsAccumulator();

        assertEquals(-1, statsAccumulator.getMin());
    }

    @Test
    void getMax() {
        assertEquals(0, statsAccumulator.getMax());

        fillStatsAccumulator();

        assertEquals(4, statsAccumulator.getMax());
    }

    @Test
    void getCount() {
        assertEquals(0, statsAccumulator.getCount());

        fillStatsAccumulator();

        assertEquals(3, statsAccumulator.getCount());
    }

    @Test
    void getAvg() {
        assertEquals(0, statsAccumulator.getAvg());

        fillStatsAccumulator();

        assertEquals(2, statsAccumulator.getAvg());
    }

    private void fillStatsAccumulator() {
        statsAccumulator.add(3);
        statsAccumulator.add(4);
        statsAccumulator.add(-1);
    }
}