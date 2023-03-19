package hw3.passwordvalidator;

import java.util.regex.Pattern;

public class PasswordValidator {
    //Примечание - null. В задании не указано, но это кажется логичным.

    public static boolean validate(String login, String password, String confirmPassword) {
        String regex = "\\w*";//^a-zA-Z0-9_]";

        try {
            validateLogin(login, regex);
            validatePassword(password, regex);
            if (!password.equals(confirmPassword)) {
                throw new WrongPasswordException("Пароль и подтверждение не совпадают");
            }
        } catch (WrongLoginException | WrongPasswordException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    private static void validateLogin(String login, String regex) throws WrongLoginException {
        if (login == null) {
            throw new WrongLoginException("Логин не может быть null");
        }
        if (!login.matches(regex)) {
            throw new WrongLoginException("Логин содержит недопустимые символы");
        }
        if (login.length() >= 20) {
            throw new WrongLoginException("Логин слишком длинный");
        }
    }

    private static void validatePassword(String password, String regex) throws WrongPasswordException {
        if (password == null) {
            throw new WrongPasswordException("Пароль не может быть null");
        }
        if (!password.matches(regex)) {
            throw new WrongPasswordException("Пароль содержит недопустимые символы");
        }
        if (password.length() >= 20) {
            throw new WrongPasswordException("Пароль слишком длинный");
        }
    }
}
