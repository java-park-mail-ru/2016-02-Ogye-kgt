package mechanics;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.Main;
import mechanics.models.Item;
import mechanics.models.Position;
import models.GameUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.json.Json;
import java.io.IOException;


@WebSocket
public class GameWebSocket {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    @NotNull
    private String myName;
    @Nullable
    private Session session;
    @NotNull
    private GameMechanics gameMechanics;
    @NotNull
    private WebSocketService webSocketService;


    public GameWebSocket(@NotNull String myName, @NotNull GameMechanics gameMechanics, @NotNull WebSocketService webSocketService) {
        this.myName = myName;
        this.gameMechanics = gameMechanics;
        this.webSocketService = webSocketService;
    }

    @SuppressWarnings({"ParameterHidesMemberVariable", "unused"})
    @OnWebSocketConnect
    public void onOpen(@NotNull Session session) {
        this.session = session;
        webSocketService.addUser(this);
        gameMechanics.addUser(myName);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        final Gson gson = new Gson();
        final Position position = gson.fromJson(data, Position.class);
        final Item item = new Item(position, myName);
        gameMechanics.addNewItem(myName, item);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        // ...
    }

    public void startGame(@NotNull GameUser user) {
        try {
            final JsonObject jsonStart = new JsonObject();
            jsonStart.addProperty("status", "start");
            jsonStart.addProperty("enemyName", user.getEnemyName() == null ? "" : user.getEnemyName());
            if (session != null && session.isOpen())
                //noinspection ConstantConditions
                session.getRemote().sendString(jsonStart.toString());
        } catch (IOException | WebSocketException e) {
            LOGGER.error("Can't send web socket", e);
        }
    }


    public void newItem(Item item) {
        try {
            final JsonObject jsonNewItem = new JsonObject();
            final JsonObject jsonItem = new JsonObject();
            jsonItem.addProperty("x", item.position.x);
            jsonItem.addProperty("y", item.position.y);
            jsonItem.addProperty("z", item.position.z);

            jsonNewItem.addProperty("status", "newItem");
            jsonNewItem.add("body", jsonItem);
            if (session != null && session.isOpen())
                //noinspection ConstantConditions
                session.getRemote().sendString(jsonNewItem.toString());
        } catch (IOException | WebSocketException e) {
            LOGGER.error("Can't send web socket", e);
        }
    }

    public void waitForSession() {
        try {
            final JsonObject jsonWaiting = new JsonObject();
            jsonWaiting.addProperty("status", "waiting");
            if (session != null && session.isOpen())
                //noinspection ConstantConditions
                session.getRemote().sendString(jsonWaiting.toString());
        } catch (IOException | WebSocketException e) {
            LOGGER.error("Can't send web socket", e);
        }
    }

    public void gameOver(boolean win) {
        try {
            final JsonObject jsonEndGame = new JsonObject();
            jsonEndGame.addProperty("status", "finish");
            jsonEndGame.addProperty("win", win);
            if (session != null && session.isOpen())
                //noinspection ConstantConditions
                session.getRemote().sendString(jsonEndGame.toString());
        } catch (IOException | WebSocketException e) {
            LOGGER.error("Can't send web socket", e);
        }
    }

    @NotNull
    public String getMyName() {
        return myName;
    }

}
