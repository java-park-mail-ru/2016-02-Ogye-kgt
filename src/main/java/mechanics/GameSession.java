package mechanics;

import mechanics.models.GameField;
import mechanics.models.Item;
import mechanics.models.Position;
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
    private GameUser currentUser;
    @NotNull
    private Map<String, GameUser> users = new HashMap<>();

    public GameSession(@NotNull String user1, @NotNull String user2) {
        field = new GameField();

        final GameUser gameUser1 = new GameUser(user1);
        gameUser1.setEnemyName(user2);
        this.firstUser = gameUser1;

        final GameUser gameUser2 = new GameUser(user2);
        gameUser2.setEnemyName(user1);
        this.secondUser = gameUser2;

        users.put(user1, gameUser1);
        users.put(user2, gameUser2);

        currentUser = gameUser1;
    }

    public void changeCurrentUser() {
        if (currentUser.equals(firstUser)) {
            currentUser = secondUser;
        } else {
            currentUser = firstUser;
        }
    }

    public boolean isCurrent(String user) {
        return currentUser.getMyName().equals(user);
    }

    public boolean isWinner(String user, Position pos) {
        return field.isWinner(pos, users.get(user).getId());
    }

    public boolean isFieldFull() {
        return field.isFull();
    }

    public boolean addNewItem(Item item) {
        if (field.put(item.position, currentUser.getId())) {
            changeCurrentUser();
            return true;
        }
        return false;
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
