package hw2.complexnumbers;

public interface ComplexNumbers {

    ComplexNumbers plus(ComplexNumbers other);
    ComplexNumbers minus(ComplexNumbers other);
    ComplexNumbers multiply(ComplexNumbers other);
    double absValue();

    double getReal();
    double getImaginary();
}
