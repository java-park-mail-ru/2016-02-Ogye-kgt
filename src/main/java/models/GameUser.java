package models;

@SuppressWarnings("InstanceVariableNamingConvention")
public class GameUser {
    private String name;
    private String enemyName;
    private int id;

    public GameUser(String name) {
        this.name = name;
        this.id = hashCode();
    }

    public String getMyName() {
        return name;
    }

    public void setEnemyName(String name) {
        this.enemyName = name;
    }

    public String getEnemyName() {
        return enemyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameUser gameUser = (GameUser) o;

        if (!name.equals(gameUser.name)) return false;
        return enemyName != null ? enemyName.equals(gameUser.enemyName) : gameUser.enemyName == null;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (enemyName != null ? enemyName.hashCode() : 0);
        return result;
    }

    public int getId() {
        return id;
    }
}
