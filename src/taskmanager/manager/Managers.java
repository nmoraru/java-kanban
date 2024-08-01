package taskmanager.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import taskmanager.jsonadapters.DurationAdapter;
import taskmanager.jsonadapters.LocalDateTimeAdapter;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {

    static GsonBuilder gsonBuilder = new GsonBuilder();
    static Gson gson = gsonBuilder.setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public static Gson getGson() {
        return gson;
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTaskManager getFileBackedTaskManager(File file) {
        return new FileBackedTaskManager(file.getPath());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


}
