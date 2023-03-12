public class MultTable {

    public static void main(String[] args) {
        printMultTable();
    }
    public static void printMultTable() {
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                System.out.println(i + " x " + j + " = " + (i * j));
            }
        }
    }
}
