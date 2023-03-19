package hw3.transliterator;

import java.util.HashMap;
import java.util.Map;

public class TransliteratorImpl implements Transliterator {
    private final Map<Character, String> translitCharsMap = new HashMap<>();

    //letter Ё in the end
    String[] translitLetters = {"A", "B", "V", "G", "D", "E", "ZH", "Z", "I", "I", "K", "L",
            "M", "N", "O", "P", "R", "S", "T", "U", "F", "KH", "TS", "CH", "SH", "SHCH", "IE", "Y", "", "E", "IU", "IA", "E"};

    public TransliteratorImpl() {
        init();
    }

    private void init() {
        char a = '\u0410';
        char yo = '\u0401';
        for (int i = 0; i < 32; i++) {
            translitCharsMap.put(a++, translitLetters[i]);
        }
        translitCharsMap.put(yo, translitLetters[32]);
    }

    @Override
    public String transliterate(String source) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            Character currentChar = source.charAt(i);
            if (translitCharsMap.containsKey(currentChar)) {
                sb.append(translitCharsMap.get(currentChar));
            } else {
                sb.append(currentChar);
            }
        }
        return sb.toString();
    }
}
