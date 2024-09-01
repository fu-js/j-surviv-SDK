package jsclub.codefest2024.sdk.util;

import jsclub.codefest2024.sdk.base.Node;
import jsclub.codefest2024.sdk.model.GameMap;
import jsclub.codefest2024.sdk.model.obstacles.Obstacle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import static java.lang.Math.abs;

public class PathUtils {
    // Calculates the Manhattan distance between Node x and Node y
    public static int distance(Node x, Node y) {
        return Math.abs(x.x - y.x) + Math.abs(x.y - y.y);
    }

    public static int shortestDistance(GameMap gameMap, List<Node> restrictedNodes, Node current, Node target, boolean skipDarkArea) {
        String path = getShortestPath(gameMap, restrictedNodes, current, target, skipDarkArea);
        if (path == null) return 1000000000;
        return path.length();
    }

    // Checks if Node x is within the safe area
    public static boolean checkInsideSafeArea(Node x, int darkAreaSize, int mapSize) {
        return (x.x >= darkAreaSize && x.y >= darkAreaSize &&
                x.x < mapSize - darkAreaSize && x.y < mapSize - darkAreaSize);
    }

    // The algorithm to find the shortest path from the current node to the target node
    public static String getShortestPath(GameMap gameMap, List<Node> restrictedNodes, Node current, Node target, boolean skipDarkArea) {
        int[] Dx = {-1, 1, 0, 0};
        int[] Dy = {0, 0, -1, 1};
        int mapSize = gameMap.getMapSize();
        int darkAreaSize = gameMap.getDarkAreaSize();
        List<Obstacle> listIndestructibleObstacles = gameMap.getListIndestructibleObstacles();
        List<Obstacle> listChests = gameMap.getListChests();
        ArrayList<ArrayList<Integer>> isRestrictedNodes = new ArrayList<>(mapSize + 5);
        ArrayList<ArrayList<Integer>> g = new ArrayList<>(mapSize + 5);
        ArrayList<ArrayList<Integer>> trace = new ArrayList<>(mapSize + 5);

        for (int i = 0; i < mapSize + 1; i++) {
            isRestrictedNodes.add(new ArrayList<>(mapSize + 5));
            g.add(new ArrayList<>(mapSize + 5));
            trace.add(new ArrayList<>(mapSize + 5));

            for (int j = 0; j < mapSize + 1; j++) {
                isRestrictedNodes.get(i).add(0);
                g.get(i).add(-1);
                trace.get(i).add(-1);
            }
        }

        for (Node point : restrictedNodes) {
            if (point.x >= 0 && point.x <= mapSize && point.y >= 0 && point.y <= mapSize) {
                isRestrictedNodes.get(point.x).set(point.y, 1);
            }
        }

        for (Node point : listIndestructibleObstacles) {
            if (point.x >= 0 && point.x <= mapSize && point.y >= 0 && point.y <= mapSize) {
                isRestrictedNodes.get(point.x).set(point.y, 1);
            }
        }

        for (Node point : listChests) {
            if (point.x >= 0 && point.x <= mapSize && point.y >= 0 && point.y <= mapSize) {
                isRestrictedNodes.get(point.x).set(point.y, 1);
                isRestrictedNodes.get(point.x).set(point.y + 1, 1);
                isRestrictedNodes.get(point.x + 1).set(point.y, 1);
                isRestrictedNodes.get(point.x + 1).set(point.y + 1, 1);
            }
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                return Integer.compare(g.get(n1.x).get(n1.y) + distance(n1, target), g.get(n2.x).get(n2.y) + distance(n2, target));
            }
        });

        openSet.add(new Node(current.x, current.y));
        g.get(current.x).set(current.y, 0);

        StringBuilder ans = new StringBuilder();
        boolean existPath = false;

        while (!openSet.isEmpty()) {
            Node u = openSet.poll();

            if (u.x == target.x && u.y == target.y) {
                existPath = true;
                while (u.x != current.x || u.y != current.y) {
                    int dir = trace.get(u.x).get(u.y);
                    if (dir == 0) ans.append('l');
                    else if (dir == 1) ans.append('r');
                    else if (dir == 2) ans.append('d');
                    else ans.append('u');
                    u.x -= Dx[dir];
                    u.y -= Dy[dir];
                }

                ans.reverse();
                break;
            }

            for (int dir = 0; dir < 4; dir++) {
                int x = u.x + Dx[dir];
                int y = u.y + Dy[dir];

                if (x < 0 || y < 0 || x > mapSize || y > mapSize) continue;
                if (isRestrictedNodes.get(x).get(y) == 1) continue;
                if (!skipDarkArea && !checkInsideSafeArea(new Node(x, y), darkAreaSize, mapSize))
                    continue;

                int cost = g.get(u.x).get(u.y) + 1;
                if (g.get(x).get(y) == -1 || g.get(x).get(y) > cost) {
                    g.get(x).set(y, cost);
                    trace.get(x).set(y, dir);
                    openSet.add(new Node(x, y));
                }
            }
        }

        if (!existPath) return null;
        return ans.toString();
    }
}
