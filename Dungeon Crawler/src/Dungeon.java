import java.util.*;

public class Dungeon {
    private Tile[][] map;
    private int width;
    private int height;
    private Random random;
    private int floorNumber;

    public Dungeon(int width, int height, int floorNumber) {
        this.width = width;
        this.height = height;
        this.floorNumber = floorNumber;
        this.map = new Tile[width][height];
        this.random = new Random();
        generateDungeon();
    }

    private void generateDungeon() {
        do {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                        map[x][y] = new Tile(true); // Border walls
                    } else {
                        map[x][y] = new Tile(random.nextDouble() < 0.3); // Random walls
                    }
                }
            }
            placeStairs();
        } while (!isValidDungeon());
    }

    private void placeStairs() {
        placeStairsDown();
        placeStairsUp();
    }

    private void placeStairsDown() {
        int x, y;
        do {
            x = random.nextInt(width);
            y = random.nextInt(height);
        } while (!isOpenSpace(x, y));

        map[x][y].setStairsDown(true);
    }

    private void placeStairsUp() {
        int x, y;
        do {
            x = random.nextInt(width);
            y = random.nextInt(height);
        } while (!isOpenSpace(x, y) || map[x][y].isStairsDown());

        map[x][y].setStairsUp(true);
    }

    private boolean isOpenSpace(int x, int y) {
        if (map[x][y].isWall()) {
            return false;
        }
        int[][] directions = { {0, 1}, {1, 0}, {0, -1}, {-1, 0} };
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            if (newX >= 0 && newY >= 0 && newX < width && newY < height && map[newX][newY].isWall()) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidDungeon() {
        return hasPath(1, 1, true) && hasPath(1, 1, false);
    }

    private boolean hasPath(int startX, int startY, boolean toStairsDown) {
        boolean[][] visited = new boolean[width][height];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            int[] point = queue.poll();
            int x = point[0];
            int y = point[1];

            if (toStairsDown && map[x][y].isStairsDown()) {
                return true;
            } else if (!toStairsDown && map[x][y].isStairsUp()) {
                return true;
            }

            for (int[] dir : new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                if (newX >= 0 && newY >= 0 && newX < width && newY < height && !map[newX][newY].isWall() && !visited[newX][newY]) {
                    queue.add(new int[]{newX, newY});
                    visited[newX][newY] = true;
                }
            }
        }
        return false;
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return new Tile(true);
        }
        return map[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void printDungeon() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(map[x][y]);
            }
            System.out.println();
        }
    }
}