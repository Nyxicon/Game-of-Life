public class Pattern {

    String name;
    boolean cells[][];
    int cellAmountX;
    int cellAmountY;
    boolean preset;

    public Pattern() {
        preset = false;
    }

    public Pattern(boolean cells[][]) {
        this.cells = cells;
        this.cellAmountX = cells.length;
        this.cellAmountY = cells[0].length;
        preset = false;
    }

    public void rotateClockwise() {
        int totalRowsOfRotatedMatrix = cells[0].length; //Total columns of Original Matrix
        int totalColsOfRotatedMatrix = cells.length; //Total rows of Original Matrix

        boolean[][] rotatedMatrix = new boolean[totalRowsOfRotatedMatrix][totalColsOfRotatedMatrix];

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                rotatedMatrix[(totalRowsOfRotatedMatrix-1)-j][i] = cells[i][j];
            }
        }

        cells = rotatedMatrix;
    }


    public void rotateCounterClockwise() {
        int totalRowsOfRotatedMatrix = cells[0].length;
        int totalColsOfRotatedMatrix = cells.length;

        boolean[][] rotatedMatrix = new boolean[totalRowsOfRotatedMatrix][totalColsOfRotatedMatrix];

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                rotatedMatrix[j][ (totalColsOfRotatedMatrix-1)- i] = cells[i][j];
            }
        }

        cells = rotatedMatrix;
    }

    public void setCells(boolean[][] cells) {
        this.cells = cells;
        cellAmountX = cells.length;
        cellAmountY = cells[0].length;
    }

    public boolean getCell(int x, int y) {
        return cells[x][y];
    }

    public boolean[][] getCells() {
        return cells;
    }
}
