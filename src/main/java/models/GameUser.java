package models;

public class GameUser {
    private String name;
    private String enemyName;

    public GameUser(String name) {
        this.name = name;
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
}
