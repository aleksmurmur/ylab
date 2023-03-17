package hw2.sequence;

public class SequencesImpl implements Sequences {
    @Override
    public void a(int n) {
        long current = 0;
        for (int i = 0; i < n; i++) {
            System.out.println(current += 2);
        }
    }

    @Override
    public void b(int n) {
        long current = 1;
        for (int i = 0; i < n; i++) {
            System.out.println(current);
            current += 2;
        }
    }

    @Override
    public void c(int n) {
        for (long i = 1; i <= n; i++) {
            System.out.println(i * i);
        }
    }

    @Override
    public void d(int n) {
        //В чате преподаватель писал, что задача на ООП,
        // не обязательно покрывать крайние случаи
        // (последовательности d, i, j)
        // (тогда надо bigint использовать)
        if (n > 2097151) {
            throw new RuntimeException("Введено слишком большое число");
        }
        for (long i = 1; i <= n; i++) {
            System.out.println(i * i * i);
        }
    }

    @Override
    public void e(int n) {
        int positive = 1;
        for (int i = 1; i <= n; i++) {
            System.out.println(i % 2 != 0 ? positive : -positive);
        }
    }

    @Override
    public void f(int n) {
        for (int i = 1; i <= n; i++) {
            System.out.println(i % 2 != 0 ? i : -i);
        }
    }

    @Override
    public void g(int n) {
        long absoluteValue;
        for (long i = 1; i <= n; i++) {
            absoluteValue = i * i;
            System.out.println(i % 2 != 0 ? absoluteValue : -absoluteValue);
        }
    }

    @Override
    public void h(int n) {
        int odd = 1;
        int even = 0;
        for (int i = 1; i <= n; i++) {
            System.out.println(i % 2 != 0 ? odd++ : even);
        }
    }

    @Override
    public void i(int n) {
        if (n > 20) {
            throw new RuntimeException("Введено слишком большое число");
        }
        long result = 1;
        for (int i = 1; i <= n; i++) {
            System.out.println(result = result * i);
        }
    }

    @Override
    public void j(int n) {
        if (n > 92) {
            throw new RuntimeException("Введено слишком большое число");
        }
        long beforePrev = 1;
        long prev = 0;
        long current;
        for (int i = 1; i <= n; i++) {
            System.out.println(current = prev + beforePrev);
            beforePrev = prev;
            prev = current;
        }
    }
}
