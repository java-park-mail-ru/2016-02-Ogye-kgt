package mechanics;

import models.GameUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameMechanics {
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
//        tasks.add(()->addUserInternal(user));
        addUserInternal(user);
    }

    private void addUserInternal(@NotNull String user) {
        if (waiter != null) {
            //noinspection ConstantConditions
            startGame(user, waiter);
            waiter = null;
        } else {
            waiter = user;
            System.out.println("Waiter: " + user);
            webSocketService.notifyStartWaiting(user);
        }
    }

    public void incrementScore(@NotNull String userName) {
        tasks.add(() -> incrementScoreInternal(userName));
    }

    private void incrementScoreInternal(String userName) {
        /*GameSession myGameSession = nameToGame.get(userName);
        GameUser myUser = myGameSession.getSelf(userName);
        myUser.incrementMyScore();
        GameUser enemyUser = myGameSession.getEnemy(userName);
        enemyUser.incrementEnemyScore();
        webSocketService.notifyMyNewScore(myUser);
        webSocketService.notifyEnemyNewScore(enemyUser);*/
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
