import java.util.Random;
import java.util.Scanner;

public class Guess {

    public static void main(String[] args) {
        int number = new Random().nextInt(99) + 1;
        int maxAttempts = 10;
        System.out.println("Я загадал число. У тебя " + maxAttempts + " попыток угадать.");

        try (Scanner scanner = new Scanner(System.in)) {
            for (int i = 1; i <= maxAttempts; i++) {
                int x = scanner.nextInt();
                if (x == number) {
                    System.out.println("Ты угадал с " + i + " попытки!");
                    break;
                }
                int remainedAttempts = maxAttempts - i;
                System.out.printf("Мое число %s! Остал%sсь %d попыт%s!%n",
                        (x < number ? "больше" : "меньше"),
                        remainedAttempts == 1 ? "а" : "о",
                        remainedAttempts,
                        remainedAttempts == 1 ? "ка" : (remainedAttempts > 4 || remainedAttempts == 0) ? "ок" : "ки");
                if (i == maxAttempts) System.out.println("Ты не угадал");

            }
        }

    }
}
