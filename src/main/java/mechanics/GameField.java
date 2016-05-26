package mechanics;


public class GameField {
    static final int SIZE = 4;
    static final int VOLUME = SIZE * SIZE * SIZE;

    private int[][][] field = new int[SIZE][SIZE][SIZE];

    public GameField() {
        // init
    }

    public boolean put(int x, int y, int z, int value) {
        if (!isEmpty(x, y, z) && isValid(x, y, z)) {
            field[x][y][z] = value;
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty(int x, int y, int z) {
        return field[x][y][z] == 0;
    }

    public boolean isValid(int x, int y, int z) {
        return 0 <= x && x < SIZE &&
                0 <= y && y < SIZE &&
                0 <= z && z < SIZE;
    }

    public int countFreeCells() {
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
