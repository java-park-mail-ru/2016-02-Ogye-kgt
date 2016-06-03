package mechanics;

import com.google.gson.JsonObject;
import main.Main;
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
        try {
            final JsonObject jsonStart = new JsonObject();
            jsonStart.addProperty("status", "newItem");
            jsonStart.addProperty("body", data);
            if (session != null && session.isOpen())
                //noinspection ConstantConditions
                session.getRemote().sendString(jsonStart.toString());
        } catch (IOException | WebSocketException e) {
            LOGGER.error("Can't send web socket", e);
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
//        gameService.remove(this);
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

    public void sendString(String data) {
        try {
            session.getRemote().sendString(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void waitForSession() {
        try {
            final JsonObject jsonEndGame = new JsonObject();
            jsonEndGame.addProperty("status", "waiting");
            if (session != null && session.isOpen())
                //noinspection ConstantConditions
                session.getRemote().sendString(jsonEndGame.toString());
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
