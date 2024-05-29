public class Tile {
    private boolean isWall;
    private boolean isStairsUp;
    private boolean isStairsDown;

    public Tile(boolean isWall) {
        this.isWall = isWall;
        this.isStairsUp = false;
        this.isStairsDown = false;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean isWall) {
        this.isWall = isWall;
    }

    public boolean isStairsUp() {
        return isStairsUp;
    }

    public void setStairsUp(boolean stairsUp) {
        isStairsUp = stairsUp;
    }

    public boolean isStairsDown() {
        return isStairsDown;
    }

    public void setStairsDown(boolean stairsDown) {
        isStairsDown = stairsDown;
    }

    @Override
    public String toString() {
        if (isWall) {
            return "#";
        } else if (isStairsUp) {
            return "U";
        } else if (isStairsDown) {
            return "D";
        } else {
            return ".";
        }
    }
}
