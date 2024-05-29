import java.util.*;

public class Pathfinding {
    private Dungeon dungeon;

    public Pathfinding(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public List<int[]> findPath(int startX, int startY, int endX, int endY) {
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(node -> node.fCost));
        Map<String, Node> allNodes = new HashMap<>();
        Node startNode = new Node(startX, startY, null, 0, getHeuristic(startX, startY, endX, endY));
        openList.add(startNode);
        allNodes.put(startX + "," + startY, startNode);

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();

            if (currentNode.x == endX && currentNode.y == endY) {
                return buildPath(currentNode);
            }

            for (int[] dir : new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}) {
                int newX = currentNode.x + dir[0];
                int newY = currentNode.y + dir[1];

                if (newX < 0 || newY < 0 || newX >= dungeon.getWidth() || newY >= dungeon.getHeight()) {
                    continue;
                }

                if (dungeon.getTile(newX, newY).isWall()) {
                    continue;
                }

                int gCost = currentNode.gCost + 1;
                int hCost = getHeuristic(newX, newY, endX, endY);
                Node neighborNode = allNodes.getOrDefault(newX + "," + newY, new Node(newX, newY, null, Integer.MAX_VALUE, hCost));

                if (gCost < neighborNode.gCost) {
                    neighborNode.gCost = gCost;
                    neighborNode.fCost = gCost + hCost;
                    neighborNode.parent = currentNode;
                    if (!openList.contains(neighborNode)) {
                        openList.add(neighborNode);
                        allNodes.put(newX + "," + newY, neighborNode);
                    }
                }
            }
        }
        return Collections.emptyList(); // No path found
    }

    private List<int[]> buildPath(Node node) {
        List<int[]> path = new ArrayList<>();
        while (node != null) {
            path.add(new int[]{node.x, node.y});
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }

    private int getHeuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private static class Node {
        int x, y, gCost, fCost;
        Node parent;

        Node(int x, int y, Node parent, int gCost, int hCost) {
            this.x = x;
            this.y = y;
            this.parent = parent;
            this.gCost = gCost;
            this.fCost = gCost + hCost;
        }
    }
}
