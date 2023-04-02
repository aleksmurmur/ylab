package io.ylab.intensive.lesson04.persistentmap;

public class MapNotInitializedException extends RuntimeException {
    public MapNotInitializedException() {
    }

    public MapNotInitializedException(String message) {
        super(message);
    }
}
