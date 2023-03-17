package hw2.snilsvalidator;

public class SnilsValidatorImpl implements SnilsValidator {

    @Override
    public boolean validate(String snils) {
        int[] snilsArr = new int[11];
        int numbersSum;
        int controlNumber;

        if (snils == null) return false;

        snils = snils.trim();

        if (snils.length() != 11) return false;

        for (int i = 0; i < snils.length(); i++) {
            char current = snils.charAt(i);
            if (!Character.isDigit(current)) return false;
            else snilsArr[i] = Character.digit(current, 10);
        }

        numbersSum = countNumbersSum(snilsArr);

        controlNumber = countControlNumber(numbersSum);

        int snilsLastDigits = snilsArr[9] * 10 + snilsArr[10];
        return controlNumber == snilsLastDigits;
    }

    private int countNumbersSum(int[] snilsArr) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum = sum + snilsArr[i] * (9 - i);
        }
        return sum;
    }

    private int countControlNumber(int sum) {
        if (sum < 100) {
            return sum;
        } else if (sum == 100 || sum % 101 == 100) {
            return 0;
        } else {
            return sum % 101;
        }
    }
}
