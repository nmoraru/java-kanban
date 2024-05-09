package taskmanager.exception;

import java.io.IOException;

public class ManagerSaveException extends IOException {
    public ManagerSaveException() {
        super("Произошла ошибка во время записи файла.");
    }
}
