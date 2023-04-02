package io.ylab.intensive.lesson04.movie;

public class FileStructureNotValidException extends RuntimeException{
    public FileStructureNotValidException() {
    }

    public FileStructureNotValidException(String message) {
        super(message);
    }
}
