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
        if (pos.y > 0 && field[pos.x][pos.y - 1][pos.z] == 0) {
            return false;
        }
        return 0 <= pos.x && pos.x < SIZE &&
                0 <= pos.y && pos.y < SIZE &&
                0 <= pos.z && pos.z < SIZE;
    }

    public boolean isWinner(Position pos, int value) {
        if (isRowInColumns(pos, value)) return true;
        if (isRowInMainDiagonals(pos, value)) return true;
//        if (isRowInMinorDiagonals(pos, value)) return true;
        return false;
    }

    private boolean isRowInColumns(Position pos, int value) {
        int rowLength = 0;
        for (int x = 0; x < SIZE; x++) {
            if (field[x][pos.y][pos.z] == value) {
                rowLength++;
            } else {
                rowLength = 0;
                break;
            }
        }
        if (rowLength == SIZE) return true;
        for (int y = 0; y < SIZE; y++) {
            if (field[pos.x][y][pos.z] == value) {
                rowLength++;
            } else {
                rowLength = 0;
                break;
            }
        }
        if (rowLength == SIZE) return true;
        for (int z = 0; z < SIZE; z++) {
            if (field[pos.x][pos.y][z] == value) {
                rowLength++;
            } else {
                rowLength = 0;
                break;
            }
        }
        return rowLength == SIZE;
    }

    private boolean isRowInMainDiagonals(Position pos, int value) {
        int rowLength = 0;
        if (pos.x == pos.y) {
            for (int i = 0; i < SIZE; i++) {
                if (field[i][i][pos.z] == value) {
                    rowLength++;
                } else {
                    rowLength = 0;
                    break;
                }
            }
        }
        if (rowLength == SIZE) return true;

        if (pos.x == pos.z) {
            for (int i = 0; i < SIZE; i++) {
                if (field[i][pos.y][i] == value) {
                    rowLength++;
                } else {
                    rowLength = 0;
                    break;
                }
            }
        }
        if (rowLength == SIZE) return true;

        if (pos.y == pos.z) {
            for (int i = 0; i < SIZE; i++) {
                if (field[pos.x][i][i] == value) {
                    rowLength++;
                } else {
                    rowLength = 0;
                    break;
                }
            }
        }
        if (rowLength == SIZE) return true;

        if (pos.x == pos.y && pos.y == pos.z) {
            for (int i = 0; i < SIZE; i++) {
                if (field[i][i][i] == value) {
                    rowLength++;
                } else {
                    rowLength = 0;
                    break;
                }
            }
        }
        return rowLength == SIZE;
    }

    private boolean isRowInMinorDiagonals(Position pos, int value) {
        if (pos.x == pos.y) {

        }
        if (pos.x == pos.z) {

        }
        if (pos.y == pos.z) {

        }
        if (pos.x == pos.y && pos.y == pos.z) {

        }
        return false;
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
