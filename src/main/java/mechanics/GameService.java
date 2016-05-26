package mechanics;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class GameService {
    private Set<GameWebSocket> webSockets;

    public GameService() {
        this.webSockets = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public void sendMessage(String data) {
        for (GameWebSocket client : webSockets) {
            try {
                client.sendString(data);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void add(GameWebSocket webSocket) {
        webSockets.add(webSocket);
    }

    public void remove(GameWebSocket webSocket) {
        webSockets.remove(webSocket);
    }

}
