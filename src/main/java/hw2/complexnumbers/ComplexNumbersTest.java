package hw2.complexnumbers;

public class ComplexNumbersTest {

    public static void main(String[] args) {
        ComplexNumbers a = new ComplexNumbersImpl(9);
        ComplexNumbers b = new ComplexNumbersImpl(5);
        System.out.println(a.minus(b)); //4
        System.out.println(a.plus(b)); //14
        System.out.println(a.multiply(b)); //45
        System.out.println(a.absValue()); //9

        ComplexNumbers complexA = new ComplexNumbersImpl(5, 9);
        ComplexNumbers complexB = new ComplexNumbersImpl(2, 3);
        System.out.println(complexA.minus(complexB)); //3 + 6i
        System.out.println(complexB.minus(complexA)); //-3 - 6i
        System.out.println(complexA.plus(complexB)); //7 + 12i
        System.out.println(complexA.multiply(complexB)); // -17 + 33i
        System.out.println(complexA.absValue()); //~10.3
     }
}
