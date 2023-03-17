package hw2.sequence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;


class SequenceGeneratorImplTest {

    private final Sequences sequences = new SequencesImpl();
    private int n;
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
    void a() {
        n = 5;
        String expectedA = "2,4,6,8,10,";

        sequences.a(n);

        assertEquals(expectedA, replaceLineSeparatorsToString());
    }

    @Test
    void b() {
        n = 5;
        String expectedB = "1,3,5,7,9,";

        sequences.b(n);
        assertEquals(expectedB, replaceLineSeparatorsToString());
    }

    private String replaceLineSeparatorsToString() {
        return outputStreamCaptor.toString().replace(System.lineSeparator(), ",");
    }

    @Test
    void c() {
        n = 5;
        String expectedC = "1,4,9,16,25,";

        sequences.c(n);

        assertEquals(expectedC, replaceLineSeparatorsToString());
    }

    @Test
    void d() {
        n = 5;
        String expectedD = "1,8,27,64,125,";

        sequences.d(n);

        assertEquals(expectedD, replaceLineSeparatorsToString());

        n = 2097152;
        assertThrows(RuntimeException.class, () -> sequences.d(n));
    }

    @Test
    void e() {
        n = 5;
        String expectedE = "1,-1,1,-1,1,";

        sequences.e(n);

        assertEquals(expectedE, replaceLineSeparatorsToString());
    }

    @Test
    void f() {
        n = 5;
        String expectedF = "1,-2,3,-4,5,";

        sequences.f(n);

        assertEquals(expectedF, replaceLineSeparatorsToString());
    }

    @Test
    void g() {
        n = 5;
        String expectedG = "1,-4,9,-16,25,";

        sequences.g(n);

        assertEquals(expectedG, replaceLineSeparatorsToString());
    }

    @Test
    void h() {
        n = 5;
        String expectedH = "1,0,2,0,3,";

        sequences.h(n);

        assertEquals(expectedH, replaceLineSeparatorsToString());
    }

    @Test
    void i() {
        n = 6;
        String expectedI = "1,2,6,24,120,720,";

        sequences.i(n);

        assertEquals(expectedI, replaceLineSeparatorsToString());

        n = 21;
        assertThrows(RuntimeException.class, () -> sequences.i(n));
    }

    @Test
    void j() {
        n = 8;
        String expectedJ = "1,1,2,3,5,8,13,21,";

        sequences.j(n);

        assertEquals(expectedJ, replaceLineSeparatorsToString());

        n = 93;
        assertThrows(RuntimeException.class, () -> sequences.j(n));
    }
}