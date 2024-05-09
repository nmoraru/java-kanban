package taskmanager.exception;

import java.io.IOException;

public class ManagerLoadException extends IOException {
    public ManagerLoadException() {
        super("Произошла ошибка во время чтения файла.");
    }
}
