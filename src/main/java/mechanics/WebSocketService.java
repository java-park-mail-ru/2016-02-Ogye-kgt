package mechanics;

import mechanics.models.Item;
import models.GameUser;

import java.util.HashMap;
import java.util.Map;

public class WebSocketService {
    private Map<String, GameWebSocket> userSockets = new HashMap<>();

    public void addUser(GameWebSocket socket) {
        userSockets.put(socket.getMyName(), socket);
    }

    public void notifyStartGame(GameUser user) {
        final GameWebSocket gameWebSocket = userSockets.get(user.getMyName());
        gameWebSocket.startGame(user);
    }

    public void notifyStartWaiting(String user) {
        final GameWebSocket gameWebSocket = userSockets.get(user);
        gameWebSocket.waitForSession();
    }

    public void notifyNewItem(String user, Item item) {
        final GameWebSocket gameWebSocket = userSockets.get(user);
        gameWebSocket.newItem(item);
    }

    public void notifyGameOver(String user, String winner) {
        userSockets.get(user).gameOver(winner);
    }
}
