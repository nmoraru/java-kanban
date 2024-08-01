package taskmanager.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.data.Epic;
import taskmanager.manager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    public EpicsHandler(TaskManager tm, Gson gson) {
        super(tm, gson);
    }

    @Override
    protected void processGet(HttpExchange httpExchange) throws IOException {
        if (splitPath.length == 2) {
            response = handleGetAllEpics();
        }
        if (splitPath.length == 3) {
            response = handleGetEpicToId(Integer.parseInt(splitPath[2]));
        }
        if (!response.equals("null")) {
            sendText(httpExchange, 200, response);
        } else {
            sendNotFound(httpExchange, "Invalid method.");
        }
    }

    @Override
    protected void processDelete(HttpExchange httpExchange) throws IOException {
        if (splitPath.length == 3 && tm.getEpicToId(Integer.parseInt(splitPath[2])) != null) {
            tm.removeEpicById(Integer.parseInt(splitPath[2]));
            sendText(httpExchange, 201, "Epic delete.");
        } else {
            sendNotFound(httpExchange, "Invalid method.");
        }
    }

    @Override
    protected void processPost(HttpExchange httpExchange) throws IOException {
        InputStream is = httpExchange.getRequestBody();
        String resp = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        Epic newEpic = gson.fromJson(resp, Epic.class);
        System.out.println(newEpic);

        if (splitPath.length == 2) {
            if (tm.getEpicToId(newEpic.getId()) == null) {
                tm.createEpic(newEpic);
                if (tm.getEpicToId(newEpic.getId()) != null) {
                    sendText(httpExchange, 201, "Epic create.");
                } else {
                    sendHasInteractions(httpExchange, "Epic not create. Is busy time for epic.");
                }
            } else {
                sendNotFound(httpExchange, "Epic not created.");
            }
        } else if (splitPath.length == 3) {
            int parseEpicId = Integer.parseInt(splitPath[2]);
            if (tm.getEpicToId(parseEpicId) != null
                    && parseEpicId == newEpic.getId()) {
                tm.updateEpic(newEpic);
                if (tm.getEpicToId(newEpic.getId()) != null) {
                    sendText(httpExchange, 201, "Epic update.");
                } else {
                    sendHasInteractions(httpExchange, "Epic not update. Is busy time for epic.");
                }
            } else {
                sendNotFound(httpExchange, "Epic not update.");
            }
        } else {
            sendNotFound(httpExchange, "Invalid method.");
        }
    }

    public String handleGetAllEpics() {
        return gson.toJson(tm.getAllEpics());
    }

    public String handleGetEpicToId(int epicId) {
        return gson.toJson(tm.getEpicToId(epicId));
    }
}
