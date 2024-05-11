package taskmanager.exception;

public class ManagerLoadException extends RuntimeException {
    public ManagerLoadException() {
        super("Произошла ошибка во время чтения файла.");
    }
}
