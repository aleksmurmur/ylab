package hw3.filesort;

import java.io.File;
import java.io.IOException;

public class SorterTest {
    public static void main(String[] args) throws IOException {
        //На 370_000_000 проверял, работает
        File dataFile = new Generator().generate("data.txt", 10_000);//375_000_000);
        System.out.println(new Validator(dataFile).isSorted()); // false
        File sortedFile = new Sorter().sortFile(dataFile);
        System.out.println(new Validator(sortedFile).isSorted()); // true
    }

}
