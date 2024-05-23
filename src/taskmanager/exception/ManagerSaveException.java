package taskmanager.exception;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException() {
        super("Произошла ошибка во время записи файла.");
    }
}
