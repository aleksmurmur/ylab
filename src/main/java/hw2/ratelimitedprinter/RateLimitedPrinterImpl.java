package hw2.ratelimitedprinter;

public class RateLimitedPrinterImpl implements RateLimitedPrinter {
    /*
    В задании нет указаний на возможность изменения/получения
    значения интервала после создания объекта класса, поэтому
    геттеры/сеттеры не требуются, а поле может быть final
     */
    private final int interval;
    private long lastPrintTime = 0;

    public RateLimitedPrinterImpl(int interval) {
        this.interval = interval;
    }

    @Override
    public void print(String message) {
        if (System.currentTimeMillis() - lastPrintTime > interval) {
            System.out.println(message);
            lastPrintTime = System.currentTimeMillis();
        }
    }
}
