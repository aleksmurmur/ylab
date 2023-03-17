package hw2.ratelimitedprinter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class RateLimitedPrinterImplTest {

    private RateLimitedPrinter printer;
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
    void print() {
        int interval = 100;
        printer = new RateLimitedPrinterImpl(interval);
        long beforeTest = System.currentTimeMillis();
        for (int i = 0; i < 1000000000; i++) {
            printer.print("0");
        }
        long afterTest = System.currentTimeMillis();
        long maxPrinted = ((afterTest - beforeTest) / interval) + 1; //adding first sout
        long soutCount = outputStreamCaptor.toString().replace(System.lineSeparator(), "").length();
        assertTrue(soutCount <= maxPrinted);


    }
}