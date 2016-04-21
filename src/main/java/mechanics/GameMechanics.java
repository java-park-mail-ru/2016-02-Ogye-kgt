package mechanics;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Clock;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by gantz on 21.04.16.
 */
public class GameMechanics {

    @NotNull
    private WebSocketService webSocketService;

    @NotNull
    private Map<String, GameSession> nameToGame = new HashMap<>();

    @NotNull
    private Set<GameSession> allSessions = new HashSet<>();

    @Nullable
    private volatile String waiter;

    private long lastPingUpdate = 0;

    @NotNull
    private Clock clock = Clock.systemDefaultZone();

    @NotNull
    private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    public GameMechanics() {}

    public void addUser(@NotNull String user) {
        tasks.add(()->addUserInternal(user));
    }

    private void addUserInternal(@NotNull String user) {
        if (waiter != null) {
            //noinspection ConstantConditions
            starGame(user, waiter);
            waiter = null;
        } else {
            waiter = user;
        }
    }

    public void run() {
        //noinspection InfiniteLoopStatement
        long lastFrameMillis = STEP_TIME;
        while (true) {
            final long before = clock.millis();
            gmStep(lastFrameMillis);
            updatePings(before);
            final long after = clock.millis();
            TimeHelper.sleep(STEP_TIME - (after - before));

            final long afterSleep = clock.millis();
            lastFrameMillis = afterSleep - before;
        }
    }

    private void gmStep() {
        while (!tasks.isEmpty()) {
            final Runnable nextTask = tasks.poll();
            if (nextTask != null) {
                try {
                    nextTask.run();
                } catch(RuntimeException ex) {
                    // ...
                }
            }

        }
        for (GameSession session : allSessions) {
                final boolean firstWin = session.isFirstWin();
                webSocketService.notifyGameOver(session.getFirst(), firstWin);
                webSocketService.notifyGameOver(session.getSecond(), !firstWin);
        }
    }

    private void starGame(@NotNull String first, @NotNull String second) {
        GameSession gameSession = new GameSession(first, second);
        allSessions.add(gameSession);
        nameToGame.put(first, gameSession);
        nameToGame.put(second, gameSession);

        webSocketService.notifyStartGame(gameSession.getSelf(first));
        webSocketService.notifyStartGame(gameSession.getSelf(second));
    }
}
