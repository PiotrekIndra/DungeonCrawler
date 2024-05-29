import java.util.List;

public class Player {
    private int x, y;
    private Dungeon dungeon;
    private List<int[]> path;
    private int pathIndex;
    private boolean moving;

    public Player(int x, int y, Dungeon dungeon) {
        this.x = x;
        this.y = y;
        this.dungeon = dungeon;
        this.path = null;
        this.pathIndex = 0;
        this.moving = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(Direction direction) {
        int newX = x + direction.dx;
        int newY = y + direction.dy;

        if (!dungeon.getTile(newX, newY).isWall()) {
            x = newX;
            y = newY;
        }
    }

    public void moveTo(int targetX, int targetY) {
        Pathfinding pathfinding = new Pathfinding(dungeon);
        this.path = pathfinding.findPath(x, y, targetX, targetY);
        this.pathIndex = 0;
        this.moving = true;
    }

    public void update() {
        if (moving && path != null && pathIndex < path.size()) {
            int[] nextStep = path.get(pathIndex);
            x = nextStep[0];
            y = nextStep[1];
            pathIndex++;
            if (pathIndex >= path.size()) {
                moving = false;
            }
        }
    }

    public List<int[]> getPath() {
        return path;
    }

    public boolean isMoving() {
        return moving;
    }

    public int getPathIndex() {
        return pathIndex;
    }
}
