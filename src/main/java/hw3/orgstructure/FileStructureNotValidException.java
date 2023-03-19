package hw3.orgstructure;

public class FileStructureNotValidException extends RuntimeException{
        public FileStructureNotValidException(String message) {
                super("File structure not valid: " + message);
        }
}
