package hw2.statsaccumulator;

public class StatsAccumulatorTest {

    public static void main(String[] args) {
        StatsAccumulator s = new StatsAccumulatorImpl();
        System.out.println(s.getAvg()); // 0
        s.add(1);
        s.add(2);
        System.out.println(s.getAvg()); // 1.5 - среднее арифметическое

        s.add(0);
        System.out.println(s.getMin()); // 0 - минимальное из переданных
        System.out.println(s.getAvg()); // 1

        s.add(8);
        s.add(3);
        s.add(90);
        System.out.println(s.getMax()); // 90 - максимальный из переданных
        System.out.println(s.getCount()); // 5 - количество переданных элементов
    }
}
