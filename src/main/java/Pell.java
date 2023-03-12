import java.util.Scanner;

public class Pell {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите число от 0 до 30");
            int n = scanner.nextInt();
            calculate(n);
        }
    }

    public static void calculate(int n) {
        if (n > 30) {
            System.out.println("Необходимо ввести число от 0 до 30");
            return;
        }
        long beforePrev = 0;
        long prev = 1;
        long current = n;
        for (int i = 2; i <= n; i++) {
            current = 2*prev + beforePrev;
            beforePrev = prev;
            prev = current;
        }
        System.out.println(current);
    }
}
