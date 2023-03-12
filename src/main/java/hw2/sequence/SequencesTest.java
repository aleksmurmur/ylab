package hw2.sequence;

import java.math.BigInteger;

public class SequencesTest {

    /*
    Последовательности d, i, j выбрасывают исключения
    при вводе значений, вызывающих переполнение long
    (в чате преподаватель писал, что не обязательно
    покрывать крайние случаи)
     */
    public static void main(String[] args) {
        Sequences sequences = new SequencesImpl();

        int n = 5;

        System.out.println("Последовательность А:");
        sequences.a(n);
        System.out.println("Последовательность B:");
        sequences.b(n);
        System.out.println("Последовательность C:");
        sequences.c(n);
        System.out.println("Последовательность D:");
        sequences.d(n);
        System.out.println("Последовательность E:");
        sequences.e(n);
        System.out.println("Последовательность F:");
        sequences.f(n);
        System.out.println("Последовательность G:");
        sequences.g(n);
        System.out.println("Последовательность H:");
        sequences.h(n);
        System.out.println("Последовательность I:");
        sequences.i(n);
        System.out.println("Последовательность J:");
        sequences.j(n);

    }
}
