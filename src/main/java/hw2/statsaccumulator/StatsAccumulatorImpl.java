package hw2.statsaccumulator;

public class StatsAccumulatorImpl implements StatsAccumulator {
    private int min = Integer.MAX_VALUE;
    private int max = Integer.MIN_VALUE;
    private int count = 0;
    private long sum = 0;
    private Double avg = 0d;

    @Override
    public void add(int value) {
        if (value > max) max = value;
        if (value < min) min = value;
        count++;
        sum += value;
        avg = (double) sum / count;
    }

    @Override
    public int getMin() {
        if (count == 0) return 0;
        else return min;
    }

    @Override
    public int getMax() {
        if (count == 0) return 0;
        else return max;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Double getAvg() {
        return avg;
    }
}
