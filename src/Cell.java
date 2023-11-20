public class Cell {

    private boolean alive;
    private boolean nextState;
    private byte neighbors;

    private int alpha = 0;
    private int livedGeneration = 0;


    public Cell() {
        this.alive = false;
        this.nextState = false;
    }

    public void decrementNeighbors() {
        neighbors--;
    }

    public void incrementNeighbors() {
        neighbors++;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public byte getNeighbors() {
        return neighbors;
    }

    public void reset() {
        this.alive = false;
        this.nextState = false;
        this.neighbors = 0;
    }

    public boolean isNextState() {
        return nextState;
    }

    public void setNextState(boolean nextState) {
        this.nextState = nextState;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getLivedGeneration() {
        return livedGeneration;
    }

    public void setLivedGeneration(int livedGeneration) {
        this.livedGeneration = livedGeneration;
    }
}
