package mechanics.models;

@SuppressWarnings("PublicField")
public class Item {
    public String player;
    public Position position;

    public Item(Position position, String player) {
        this.position = position;
        this.player = player;
    }

    public Item(int x,int y,int z) {
        this.position = new Position(x, y, z);
    }
}
