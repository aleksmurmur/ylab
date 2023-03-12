package hw2.complexnumbers;

public class ComplexNumbersImpl implements ComplexNumbers {

    private final double real;
    private final double imaginary;

    public ComplexNumbersImpl(double a) {
        this.real = a;
        this.imaginary = 0;
    }

    public ComplexNumbersImpl(double a, double b) {
        this.real = a;
        this.imaginary = b;
    }

    @Override
    public ComplexNumbers plus(ComplexNumbers other) {
        return new ComplexNumbersImpl(
                this.real + other.getReal(),
                this.imaginary + other.getImaginary()
        );
    }

    @Override
    public ComplexNumbers minus(ComplexNumbers other) {
        return new ComplexNumbersImpl(
                this.real - other.getReal(),
                this.imaginary - other.getImaginary()
        );
    }

    @Override
    public ComplexNumbers multiply(ComplexNumbers other) {
        double newReal = this.real * other.getReal() - this.imaginary * other.getImaginary();
        double newImaginary = this.imaginary * other.getReal() + this.getReal() * other.getImaginary();
        return new ComplexNumbersImpl(
                newReal,
                newImaginary
        );
    }

    @Override
    public double absValue() {
//        Если Math использовать нельзя:
//        return squareRoot(real * real + imaginary * imaginary);
        return Math.sqrt(real * real + imaginary * imaginary);
    }

    private double customSqrt(double number) {
        double temp;
        double sr = number / 2;
        do {
            temp = sr;
            sr = (temp + (number / temp)) / 2;
        } while ((temp - sr) != 0);
        return sr;
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    @Override
    public String toString() {
        if (imaginary > 0) {
            return real + " + " + imaginary + "i";
        } else if (imaginary < 0) {
            return real + "" + imaginary + "i";
        } else {
            return real + "";
        }
    }
}
