package hw3.passwordvalidator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void validate() {

        assertTrue(PasswordValidator.validate("login", "password", "password"));
        assertTrue(PasswordValidator.validate("_login_TO_1NtErNeT_", "_PASS_WORD_", "_PASS_WORD_"));

        assertTrue(PasswordValidator.validate("", "", ""));
        assertTrue(PasswordValidator.validate("a", "", ""));

        assertFalse(PasswordValidator.validate("login", "password", "WrongConfirmation"));
        assertEquals( "Пароль и подтверждение не совпадают", outputStreamCaptor.toString().trim());
        outputStreamCaptor.reset();

        assertFalse(PasswordValidator.validate("WrongLoginLengthException", "password", "password"));
        assertEquals( "Логин слишком длинный", outputStreamCaptor.toString().trim());
        outputStreamCaptor.reset();

        assertFalse(PasswordValidator.validate("login", "WrongPasswordLengthException", "WrongPasswordLengthException"));
        assertEquals( "Пароль слишком длинный", outputStreamCaptor.toString().trim());
        outputStreamCaptor.reset();

        assertFalse(PasswordValidator.validate("@WrongLogin", "password", "other"));
        assertEquals( "Логин содержит недопустимые символы", outputStreamCaptor.toString().trim());
        outputStreamCaptor.reset();

        assertFalse(PasswordValidator.validate("яWrongLogin", "password", "other"));
        assertEquals( "Логин содержит недопустимые символы", outputStreamCaptor.toString().trim());
        outputStreamCaptor.reset();

        assertFalse(PasswordValidator.validate("login", "@WrongPassword", "other"));
        assertEquals( "Пароль содержит недопустимые символы", outputStreamCaptor.toString().trim());
        outputStreamCaptor.reset();

        assertFalse(PasswordValidator.validate("login", "увыWrongPassword", "other"));
        assertEquals( "Пароль содержит недопустимые символы", outputStreamCaptor.toString().trim());
        outputStreamCaptor.reset();

        assertFalse(PasswordValidator.validate(null, null, null));
        assertEquals( "Логин не может быть null", outputStreamCaptor.toString().trim());
        outputStreamCaptor.reset();

        assertFalse(PasswordValidator.validate("a", null, null));
        assertEquals( "Пароль не может быть null", outputStreamCaptor.toString().trim());
        outputStreamCaptor.reset();
    }
}