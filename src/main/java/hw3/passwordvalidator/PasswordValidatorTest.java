package hw3.passwordvalidator;

public class PasswordValidatorTest {

    public static void main(String[] args) {
        System.out.println(PasswordValidator.validate("login", "password", "password"));
        System.out.println(PasswordValidator.validate("login", "password", "WrongConfirmation"));
        System.out.println(PasswordValidator.validate("WrongLoginLengthException", "password", "password"));
        System.out.println(PasswordValidator.validate("login", "WrongPasswordLengthException", "WrongPasswordLengthException"));
        System.out.println(PasswordValidator.validate("login", "password", "other"));
        System.out.println(PasswordValidator.validate("@WrongLogin", "password", "other"));
        System.out.println(PasswordValidator.validate("яWrongLogin", "password", "other"));
        System.out.println(PasswordValidator.validate("login", "@WrongPassword", "other"));
        System.out.println(PasswordValidator.validate("login", "увыWrongPassword", "other"));

    }
}
