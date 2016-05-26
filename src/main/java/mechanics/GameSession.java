package mechanics;

import models.GameUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Clock;
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
    private Map<String, GameUser> users = new HashMap<>();

    public GameSession(@NotNull String user1, @NotNull String user2) {
        final GameUser gameUser1 = new GameUser(user1);
        firstUser.setEnemyName(user2);

        final GameUser gameUser2 = new GameUser(user2);
        gameUser2.setEnemyName(user1);

        users.put(user1, gameUser1);
        users.put(user2, gameUser2);

        this.firstUser = gameUser1;
        this.secondUser = gameUser2;
    }

    @Nullable
    public GameUser getSelf(String user) {
        return users.get(user);
    }


}
