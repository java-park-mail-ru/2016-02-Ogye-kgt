package mechanics.models;


public class GameField {
    static final int SIZE = 4;
    static final int VOLUME = SIZE * SIZE * SIZE;

    private int[][][] field = new int[SIZE][SIZE][SIZE];

    public GameField() {
        // init
    }

    public boolean put(Position pos, int value) {
        if (isEmpty(pos) && isValid(pos)) {
            field[pos.x][pos.y][pos.z] = value;
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty(Position pos) {
        return field[pos.x][pos.y][pos.z] == 0;
    }

    public boolean isValid(Position pos) {
//        if (field[pos.x][pos.y][pos.z - 1] == 0) {
//            return false;
//        }
        return 0 <= pos.x && pos.x < SIZE &&
                0 <= pos.y && pos.y < SIZE &&
                0 <= pos.z && pos.z < SIZE;
    }

    public int getFreeCellsNumber() {
        int freeCellNumbers = VOLUME;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                for (int k = 0; k < SIZE; k++) {
                    if (field[i][j][k] == 0) {
                        freeCellNumbers--;
                    }
                }
            }
        }
        return freeCellNumbers;
    }


}
