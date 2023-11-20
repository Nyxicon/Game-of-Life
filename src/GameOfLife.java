public class GameOfLife {

    private Cell cells[][];

    private boolean toroidal;

    int generation;
    long revivedCells;
    long deceasedCells;
    int currentCells;

    private int cellAmountX, cellAmountY;

    public GameOfLife() {
        cells = new Cell[cellAmountX][cellAmountY];
        toroidal = true;
        init();
    }

    public GameOfLife(int x, int y) {
        this.cellAmountX = x;
        this.cellAmountY = y;
        cells = new Cell[cellAmountX][cellAmountY];
        toroidal = true;
        init();
    }

    private void init() {
        for (int x = 0; x < cellAmountX; x++) {
            for (int y = 0; y < cellAmountY; y++) {
                cells[x][y] = new Cell();
            }
        }
        resetStats();
    }

    public void nextGeneration() {
        calculateNextCellState();
        updateCells();
        generation++;
    }

    private void calculateNextCellState() {
        for (int x = 0; x < cellAmountX; x++) {
            for (int y = 0; y < cellAmountY; y++) {

                if (cells[x][y].isAlive() || cells[x][y].getNeighbors() > 0) {

                    if (cells[x][y].getNeighbors() == 3) {
                        cells[x][y].setNextState(true);
                    } else if(cells[x][y].isAlive() && cells[x][y].getNeighbors() == 2) {
                        cells[x][y].setNextState(true);
                    } else {
                        cells[x][y].setNextState(false);
                    }

                }

            }
        }
    }

    private void updateCells() {
        currentCells = 0;
        for (int x = 0; x < cellAmountX; x++) {
            for (int y = 0; y < cellAmountY; y++) {

                if (cells[x][y].isAlive() != cells[x][y].isNextState()) {
                    cells[x][y].setAlive(cells[x][y].isNextState());

                    if(cells[x][y].isAlive()) {
                        revivedCells++;

                    } else {
                        deceasedCells++;
                    }

                    if (toroidal) {
                        if (cells[x][y].isAlive()) {
                            increaseNeighborAmountToroidal(x,y);
                        } else {
                            decreaseNeighborAmountToroidal(x,y);
                        }
                    } else {
                        if (cells[x][y].isAlive()) {
                            increaseNeighborAmount(x,y);
                        } else {
                            decreaseNeighborAmount(x,y);
                        }
                    }


                }

                if (cells[x][y].isAlive()) currentCells++;

                updateFilterInformation(x,y);
            }
        }
    }

    private void updateFilterInformation(int x, int y) {
        if (cells[x][y].isAlive()) {
            cells[x][y].setLivedGeneration(cells[x][y].getLivedGeneration() + 1);
            if (cells[x][y].getLivedGeneration() > 5) cells[x][y].setLivedGeneration(5);
        } else {
            cells[x][y].setLivedGeneration(0);
        }

        if (cells[x][y].isAlive()) {
            cells[x][y].setAlpha(150);
        } else {
            cells[x][y].setAlpha(cells[x][y].getAlpha() - 2);
            if (cells[x][y].getAlpha() < 0) cells[x][y].setAlpha(0);
        }
    }

    private void increaseNeighborAmount(int cellX, int cellY) {
        if (cellY < cellAmountY - 1) cells[cellX][cellY + 1].incrementNeighbors(); //N
        if (cellX < cellAmountX - 1) cells[cellX + 1][cellY].incrementNeighbors(); //E
        if (cellY > 0) cells[cellX][cellY - 1].incrementNeighbors(); //S
        if (cellX > 0) cells[cellX - 1][cellY].incrementNeighbors(); //W
        if (cellY < cellAmountY - 1 && cellX < cellAmountX - 1) cells[cellX + 1][cellY + 1].incrementNeighbors();
        if (cellY > 0 && cellX > 0) cells[cellX - 1][cellY - 1].incrementNeighbors();
        if (cellY > 0 && cellX < cellAmountX - 1) cells[cellX + 1][cellY - 1].incrementNeighbors();
        if (cellY < cellAmountY - 1 && cellX > 0) cells[cellX - 1][cellY + 1].incrementNeighbors();
    }

    private void decreaseNeighborAmount(int cellX, int cellY) {
        if (cellY < cellAmountY - 1) cells[cellX][cellY + 1].decrementNeighbors(); //N
        if (cellX < cellAmountX - 1) cells[cellX + 1][cellY].decrementNeighbors(); //E
        if (cellY > 0) cells[cellX][cellY - 1].decrementNeighbors(); //S
        if (cellX > 0) cells[cellX - 1][cellY].decrementNeighbors(); //W
        if (cellY < cellAmountY - 1 && cellX < cellAmountX - 1) cells[cellX + 1][cellY + 1].decrementNeighbors();
        if (cellY > 0 && cellX > 0) cells[cellX - 1][cellY - 1].decrementNeighbors();
        if (cellY > 0 && cellX < cellAmountX - 1) cells[cellX + 1][cellY - 1].decrementNeighbors();
        if (cellY < cellAmountY - 1 && cellX > 0) cells[cellX - 1][cellY + 1].decrementNeighbors();
    }

    private void increaseNeighborAmountToroidal(int cellX, int cellY) {
        //N
        if (cellY > 0) {
            cells[cellX][cellY - 1].incrementNeighbors();
        } else if (cellY == 0) {
            cells[cellX][cellAmountY - 1].incrementNeighbors();
        }
        //NE
        if (cellY > 0 && cellX < cellAmountX - 1) {
            cells[cellX + 1][cellY - 1].incrementNeighbors();
        } else if (cellY == 0 && cellX < cellAmountX - 1) {
            cells[cellX + 1][cellAmountY - 1].incrementNeighbors();
        } else if (cellX == cellAmountX - 1 && cellY > 0) {
            cells[0][cellY - 1].incrementNeighbors();
        } else if (cellX == cellAmountX - 1 && cellY == 0) {
            cells[0][cellAmountY - 1].incrementNeighbors();
        }
        //E
        if (cellX < cellAmountX - 1) {
            cells[cellX + 1][cellY].incrementNeighbors();
        } else if (cellX == cellAmountX - 1) {
            cells[0][cellY].incrementNeighbors();
        }
        //SE
        if (cellY < cellAmountY - 1 && cellX < cellAmountX - 1) {
            cells[cellX + 1][cellY + 1].incrementNeighbors();
        } else if (cellY == cellAmountY - 1 && cellX < cellAmountX - 1) {
            cells[cellX + 1][0].incrementNeighbors();
        } else if (cellX == cellAmountX - 1 && cellY < cellAmountY - 1) {
            cells[0][cellY + 1].incrementNeighbors();
        } else if (cellX == cellAmountX - 1 && cellY == cellAmountY - 1) {
            cells[0][0].incrementNeighbors();
        }
        //S
        if (cellY < cellAmountY - 1) {
            cells[cellX][cellY + 1].incrementNeighbors();
        } else if (cellY == cellAmountY - 1) {
            cells[cellX][0].incrementNeighbors();
        }
        //SW
        if (cellY < cellAmountY - 1 && cellX > 0) {
            cells[cellX - 1][cellY + 1].incrementNeighbors();
        } else if (cellY == cellAmountY - 1 && cellX > 0) {
            cells[cellX - 1][0].incrementNeighbors();
        } else if (cellX == 0 && cellY < cellAmountY - 1) {
            cells[cellAmountX - 1][cellY + 1].incrementNeighbors();
        } else if (cellX == 0 && cellY == cellAmountY - 1) {
            cells[cellAmountX - 1][0].incrementNeighbors();
        }
        //W
        if (cellX > 0) {
            cells[cellX - 1][cellY].incrementNeighbors();
        } else if (cellX == 0) {
            cells[cellAmountX - 1][cellY].incrementNeighbors();
        }
        //NW
        if (cellY > 0 && cellX > 0) {
            cells[cellX - 1][cellY - 1].incrementNeighbors();
        } else if (cellY == 0 && cellX > 0) {
            cells[cellX - 1][cellAmountY - 1].incrementNeighbors();
        } else if (cellX == 0 && cellY > 0) {
            cells[cellAmountX - 1][cellY - 1].incrementNeighbors();
        } else if (cellX == 0 && cellY == 0) {
            cells[cellAmountX - 1][cellAmountY - 1].incrementNeighbors();
        }
    }

    private void decreaseNeighborAmountToroidal(int cellX, int cellY) {
        //N
        if (cellY > 0) {
            cells[cellX][cellY - 1].decrementNeighbors();
        } else if (cellY == 0) {
            cells[cellX][cellAmountY - 1].decrementNeighbors();
        }
        //NE
        if (cellY > 0 && cellX < cellAmountX - 1) {
            cells[cellX + 1][cellY - 1].decrementNeighbors();
        } else if (cellY == 0 && cellX < cellAmountX - 1) {
            cells[cellX + 1][cellAmountY - 1].decrementNeighbors();
        } else if (cellX == cellAmountX - 1 && cellY > 0) {
            cells[0][cellY - 1].decrementNeighbors();
        } else if (cellX == cellAmountX - 1 && cellY == 0) {
            cells[0][cellAmountY - 1].decrementNeighbors();
        }
        //E
        if (cellX < cellAmountX - 1) {
            cells[cellX + 1][cellY].decrementNeighbors();
        } else if (cellX == cellAmountX - 1) {
            cells[0][cellY].decrementNeighbors();
        }
        //SE
        if (cellY < cellAmountY - 1 && cellX < cellAmountX - 1) {
            cells[cellX + 1][cellY + 1].decrementNeighbors();
        } else if (cellY == cellAmountY - 1 && cellX < cellAmountX - 1) {
            cells[cellX + 1][0].decrementNeighbors();
        } else if (cellX == cellAmountX - 1 && cellY < cellAmountY - 1) {
            cells[0][cellY + 1].decrementNeighbors();
        } else if (cellX == cellAmountX - 1 && cellY == cellAmountY - 1) {
            cells[0][0].decrementNeighbors();
        }
        //S
        if (cellY < cellAmountY - 1) {
            cells[cellX][cellY + 1].decrementNeighbors();
        } else if (cellY == cellAmountY - 1) {
            cells[cellX][0].decrementNeighbors();
        }
        //SW
        if (cellY < cellAmountY - 1 && cellX > 0) {
            cells[cellX - 1][cellY + 1].decrementNeighbors();
        } else if (cellY == cellAmountY - 1 && cellX > 0) {
            cells[cellX - 1][0].decrementNeighbors();
        } else if (cellX == 0 && cellY < cellAmountY - 1) {
            cells[cellAmountX - 1][cellY + 1].decrementNeighbors();
        } else if (cellX == 0 && cellY == cellAmountY - 1) {
            cells[cellAmountX - 1][0].decrementNeighbors();
        }
        //W
        if (cellX > 0) {
            cells[cellX - 1][cellY].decrementNeighbors();
        } else if (cellX == 0) {
            cells[cellAmountX - 1][cellY].decrementNeighbors();
        }
        //NW
        if (cellY > 0 && cellX > 0) {
            cells[cellX - 1][cellY - 1].decrementNeighbors();
        } else if (cellY == 0 && cellX > 0) {
            cells[cellX - 1][cellAmountY - 1].decrementNeighbors();
        } else if (cellX == 0 && cellY > 0) {
            cells[cellAmountX - 1][cellY - 1].decrementNeighbors();
        } else if (cellX == 0 && cellY == 0) {
            cells[cellAmountX - 1][cellAmountY - 1].decrementNeighbors();
        }
    }

    public void changeCellAmount(int newAmountX, int newAmountY) {
        boolean[][] tmpCells = new boolean[cellAmountX][cellAmountY];
        for (int x = 0; x < cellAmountX; x++) {
            for (int y = 0; y < cellAmountY; y++) {
                tmpCells[x][y] = cells[x][y].isAlive();
            }
        }

        this.cellAmountX = newAmountX;
        this.cellAmountY = newAmountY;
        cells = new Cell[cellAmountX][cellAmountY];

        for (int x = 0; x < cellAmountX; x++) {
            for (int y = 0; y < cellAmountY; y++) {
                cells[x][y] = new Cell();
                if (tmpCells.length > x && tmpCells[0].length > y) {
                    if (tmpCells[x][y]) {
                        cells[x][y].setAlive(true);
                        cells[x][y].setNextState(true);
                    }
                }
            }
        }

        for (int x = 0; x < cellAmountX; x++) {
            for (int y = 0; y < cellAmountY; y++) {
                if (cells[x][y].isAlive()) {
                    if (toroidal) {
                        increaseNeighborAmountToroidal(x,y);
                    } else {
                        increaseNeighborAmount(x,y);
                    }
                }
            }
        }

    }

    public void resetStats() {
        generation = 0;
        currentCells = 0;
        revivedCells = 0;
        deceasedCells = 0;
    }

    public void changeCellState(int x, int y) {

        if (!toroidal) {
            if (!cells[x][y].isAlive()) increaseNeighborAmount(x, y);
            if (cells[x][y].isAlive()) decreaseNeighborAmount(x, y);
        } else {
            if (!cells[x][y].isAlive()) increaseNeighborAmountToroidal(x, y);
            if (cells[x][y].isAlive()) decreaseNeighborAmountToroidal(x, y);
        }

        cells[x][y].setAlive(!cells[x][y].isAlive());
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void setCell(int x, int y, boolean state) {
        if (cells[x][y].isAlive() != state) {
            cells[x][y].setAlive(state);
            cells[x][y].setNextState(state);

            if (toroidal) {
                if (cells[x][y].isAlive()) {
                    increaseNeighborAmountToroidal(x,y);
                } else {
                    decreaseNeighborAmountToroidal(x,y);
                }
            } else {
                if (cells[x][y].isAlive()) {
                    increaseNeighborAmount(x,y);
                } else {
                    decreaseNeighborAmount(x,y);
                }
            }

        }

    }

    public int getCellAmountX() {
        return cellAmountX;
    }

    public int getCellAmountY() {
        return cellAmountY;
    }

    public void resetAllCells() {
        for (int x = 0; x < cellAmountX; x++) {
            for (int y = 0; y < cellAmountY; y++) {
                cells[x][y].reset();
            }
        }
    }

    public boolean isToroidal() {
        return toroidal;
    }

    public void setToroidal(boolean toroidal) {
        this.toroidal = toroidal;
    }
}
