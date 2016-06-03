package mechanics;

import mechanics.models.Item;
import models.GameUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameMechanics {
    static final Logger LOGGER = LogManager.getLogger(GameMechanics.class);

    @NotNull
    private Map<String, GameSession> nameToGame = new HashMap<>();

    @NotNull
    private Set<GameSession> allSessions = new HashSet<>();

    @NotNull
    private WebSocketService webSocketService;

    @Nullable
    private volatile String waiter;

    @NotNull
    private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    public GameMechanics(@NotNull WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    public void addUser(@NotNull String user) {
        addUserInternal(user);
    }

    private void addUserInternal(@NotNull String user) {
        if (waiter != null) {
            //noinspection ConstantConditions
            startGame(user, waiter);
            waiter = null;
        } else {
            waiter = user;
            webSocketService.notifyStartWaiting(user);
        }
    }


    public void addNewItem(@NotNull String userName, @NotNull Item item) {
        final GameSession myGameSession = nameToGame.get(userName);
        final GameUser myUser = myGameSession.getSelf(userName);
        final GameUser enemyUser = myGameSession.getEnemy(userName);
        if (!myGameSession.isCurrent(userName)) return;
        if (!myGameSession.addNewItem(item)) return;
        webSocketService.notifyNewItem(myUser.getMyName(), item);
        webSocketService.notifyNewItem(enemyUser.getMyName(), item);
        if (myGameSession.isWinner(userName, item.position)) {
            webSocketService.notifyGameOver(myUser.getMyName(), myUser.getEnemyName());
            webSocketService.notifyGameOver(myUser.getEnemyName(), myUser.getEnemyName());
        }
    }


    private void startGame(@NotNull String first, @NotNull String second) {
        final GameSession gameSession = new GameSession(first, second);
        allSessions.add(gameSession);
        nameToGame.put(first, gameSession);
        nameToGame.put(second, gameSession);

        webSocketService.notifyStartGame(gameSession.getSelf(first));
        webSocketService.notifyStartGame(gameSession.getSelf(second));
    }
}
