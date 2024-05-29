import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel {
    private ArrayList<Dungeon> dungeons;
    private Player player;
    private final int TILE_SIZE = 32;
    private int currentFloor;
    private Timer timer;

    public GamePanel() {
        this.dungeons = new ArrayList<>();
        this.currentFloor = 0;

        // Generate multiple floors
        for (int i = 0; i < 5; i++) {
            dungeons.add(new Dungeon(20, 20, i));
        }

        this.player = new Player(1, 1, dungeons.get(currentFloor));

        setPreferredSize(new Dimension(dungeons.get(currentFloor).getWidth() * TILE_SIZE, dungeons.get(currentFloor).getHeight() * TILE_SIZE));
        setBackground(Color.BLACK);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP: player.move(Direction.UP); break;
                    case KeyEvent.VK_DOWN: player.move(Direction.DOWN); break;
                    case KeyEvent.VK_LEFT: player.move(Direction.LEFT); break;
                    case KeyEvent.VK_RIGHT: player.move(Direction.RIGHT); break;
                }
                checkForStairs();
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int targetX = e.getX() / TILE_SIZE;
                int targetY = e.getY() / TILE_SIZE;
                player.moveTo(targetX, targetY);
                repaint();
            }
        });

        setFocusable(true);
        requestFocusInWindow();

        // Timer to update the game state
        timer = new Timer(100, e -> {
            player.update();
            repaint();
        });
        timer.start();
    }

    private void checkForStairs() {
        Dungeon currentDungeon = dungeons.get(currentFloor);
        Tile currentTile = currentDungeon.getTile(player.getX(), player.getY());

        if (currentTile.isStairsDown() && currentFloor < dungeons.size() - 1) {
            changeFloor(currentFloor + 1);
        } else if (currentTile.isStairsUp() && currentFloor > 0) {
            changeFloor(currentFloor - 1);
        }
    }

    private void changeFloor(int floor) {
        currentFloor = floor;
        Dungeon currentDungeon = dungeons.get(currentFloor);
        player.setPosition(1, 1);
        player = new Player(player.getX(), player.getY(), currentDungeon);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dungeon currentDungeon = dungeons.get(currentFloor);

        for (int y = 0; y < currentDungeon.getHeight(); y++) {
            for (int x = 0; x < currentDungeon.getWidth(); x++) {
                Tile tile = currentDungeon.getTile(x, y);

                if (tile.isWall()) {
                    g.setColor(Color.DARK_GRAY);
                } else if (tile.isStairsUp()) {
                    g.setColor(Color.BLUE);
                } else if (tile.isStairsDown()) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                }

                g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        List<int[]> path = player.getPath();
        if (path != null && player.isMoving()) {
            g.setColor(Color.YELLOW);
            for (int i = 0; i < player.getPathIndex(); i++) {
                int[] step = path.get(i);
                g.fillRect(step[0] * TILE_SIZE, step[1] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        g.setColor(Color.RED);
        g.fillRect(player.getX() * TILE_SIZE, player.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
}
