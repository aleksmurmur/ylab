package hw3.transliterator;

public class TransliteratorTest {
    public static void main(String[] args) {
        Transliterator transliterator = new TransliteratorImpl();
        String res = transliterator
                .transliterate("HELLO! ПРИВЕТ! Go, boy!");
        System.out.println(res);
        String res2 = transliterator
                .transliterate("HELLO! ПРИВЕТ! Go, boy! Ёкатеринбург. ЕКАТЕРИНБУРГ. БУМЪ. бумъ. ВЪЕДЛИВЫЙ. ЛОЖЬ. ЩЕПОТКА. щепотка. ЖИ-ШИ");
        System.out.println(res2);
    }
}



