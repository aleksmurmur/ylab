import java.util.Scanner;

public class Stars {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите количество строк:");
            int n = scanner.nextInt();
            System.out.println("Введите количество столбцов:");
            int m = scanner.nextInt();
            System.out.println("Введите символ:");
            String template = scanner.next();
            printShape(n, m, template);
        }
    }

    public static void printShape(int lines, int columns, String template) {
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(template + " ");
            }
            System.out.println();
        }
    }
}
