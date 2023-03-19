package hw3.transliterator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransliteratorImplTest {

    private Transliterator transliterator;

    @BeforeEach
    void setUp() {
        transliterator = new TransliteratorImpl();
    }

    @Test
    void transliterate() {
        String testString = "ЕВГЕНИЯ - евгения, СЪЕДЕННЫЙ - съеденный, МЕЛОЧЬ - мелочь, ЁЖ - ёж, ПОДЪЯРЁМНЫЙ - подъяремный, ALPHABET";
        String expected = "EVGENIIA - евгения, SIEEDENNYI - съеденный, MELOCH - мелочь, EZH - ёж, PODIEIAREMNYI - подъяремный, ALPHABET";

        String result = transliterator.transliterate(testString);

        assertEquals(expected, result);
    }
}