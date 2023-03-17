package hw2.snilsvalidator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SnilsValidatorImplTest {

    private SnilsValidator snilsValidator;

    @BeforeEach
    void setUp() {
        snilsValidator = new SnilsValidatorImpl();
    }

    @Test
    void validate() {
        String valid = "29865692969";
        String invalid = "12835692960";

        assertTrue(snilsValidator.validate(valid));
        assertFalse(snilsValidator.validate(invalid));
        assertFalse(snilsValidator.validate("Snils"));
        assertFalse(snilsValidator.validate("Snils123456"));
        assertFalse(snilsValidator.validate(null));
    }
}