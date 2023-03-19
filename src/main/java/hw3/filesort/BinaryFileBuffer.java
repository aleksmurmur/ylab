package hw3.filesort;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public final class BinaryFileBuffer {
    private BufferedReader reader;
    private String cache;

    public BinaryFileBuffer(File file) throws IOException {
        this.reader = new BufferedReader(new FileReader(file));
        reload();
    }

    public void reload() throws IOException {
        this.cache = this.reader.readLine();
    }

    public boolean empty() {
        return this.cache == null;
    }

    public void close() throws IOException {
        this.reader.close();
    }

    public long peek() {
        return Long.parseLong(this.cache);
    }

    public long pop() throws IOException {
        long answer = Long.parseLong(this.cache);
        reload();
        return answer;
    }
}

