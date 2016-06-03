package mechanics;

import mechanics.models.GameField;
import models.GameUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class GameSession {
    @NotNull
    private GameUser firstUser;
    @NotNull
    private GameUser secondUser;
    @NotNull
    private GameField field;
    @NotNull
    private String currentUser;
    @NotNull
    private Map<String, GameUser> users = new HashMap<>();

    public GameSession(@NotNull String user1, @NotNull String user2) {
        final GameUser gameUser1 = new GameUser(user1);
        gameUser1.setEnemyName(user2);
        this.firstUser = gameUser1;

        final GameUser gameUser2 = new GameUser(user2);
        gameUser2.setEnemyName(user1);
        this.secondUser = gameUser2;

        users.put(user1, gameUser1);
        users.put(user2, gameUser2);

        currentUser = user1;
    }

    public void changeCurrentUser() {
        if (currentUser.equals(firstUser.getMyName())) {
            currentUser = secondUser.getMyName();
        } else {
            currentUser = firstUser.getMyName();
        }
    }

    @Nullable
    public GameUser getSelf(String user) {
        return users.get(user);
    }

    @Nullable
    public GameUser getEnemy(@NotNull String user) {
        @SuppressWarnings("ConstantConditions")
        final String enemyName = users.containsKey(user) ? users.get(user).getEnemyName() : null;
        return enemyName == null ? null : users.get(enemyName);
    }
}
