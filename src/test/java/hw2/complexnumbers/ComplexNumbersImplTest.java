package hw2.complexnumbers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplexNumbersImplTest {

    private ComplexNumbers numberZeroImaginary;
    private ComplexNumbers complexNumber;

    @BeforeEach
    void setUp() {
        numberZeroImaginary = new ComplexNumbersImpl(13);
        complexNumber = new ComplexNumbersImpl(7, -6);
    }

    @Test
    void plus() {
        ComplexNumbers result = numberZeroImaginary.plus(complexNumber);
        assertEquals( 20, result.getReal());
        assertEquals(-6, result.getImaginary());

        result = complexNumber.plus(complexNumber);
        assertEquals( 14, result.getReal());
        assertEquals(-12, result.getImaginary());
    }

    @Test
    void minus() {
        ComplexNumbers result = numberZeroImaginary.minus(complexNumber);
        assertEquals( 6, result.getReal());
        assertEquals(6, result.getImaginary());

        result = complexNumber.minus(complexNumber);
        assertEquals( 0, result.getReal());
        assertEquals(0, result.getImaginary());
    }


    @Test
    void multiply() {
        ComplexNumbers result = numberZeroImaginary.multiply(complexNumber);
        assertEquals( 91, result.getReal());
        assertEquals(-78, result.getImaginary());

        result = complexNumber.multiply(complexNumber);
        assertEquals( 13, result.getReal());
        assertEquals(-84, result.getImaginary());
    }

    @Test
    void absValue() {
        assertEquals( 13, numberZeroImaginary.absValue());
        assertEquals( 9.22, (double) Math.round(complexNumber.absValue() * 100) / 100);
    }

}