package hw3.passwordvalidator;

public class WrongPasswordException extends Exception{
    public WrongPasswordException(String message) {
        super(message);
    }

    public WrongPasswordException() {
    }
}
