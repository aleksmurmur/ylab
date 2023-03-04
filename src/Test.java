import java.util.Random;

public class Test {
    public static void main(String[] args) {
            testPell();
            testStars();
            testMultTable();
    }

    public static void testPell() {
        for (int i = 0; i < 30; i++) {
            Pell.calculate(i);
        }
    }

    public static void testStars() {
        Random random = new Random();
        int lines = random.nextInt(100);
        int columns = random.nextInt(100);
        String template = Character.toString(random.nextInt(26) + 'a');
        Stars.printShape(lines, columns, template);
    }

    public static void testMultTable() {
        MultTable.printMultTable();
    }


}