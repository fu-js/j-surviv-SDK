package jsclub.codefest2024.sdk.algorithm;

import jsclub.codefest2024.sdk.base.Node;
import jsclub.codefest2024.sdk.model.GameMap;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;

import static java.lang.Math.abs;

public class ShortestPath {
    // The algorithm to find the shortest path from the current node to the target node

    /**
     *         The algorithm: A*
     *         Sources: https://www.geeksforgeeks.org/a-search-algorithm/
     * @param gameMap : class get information of map
     * @param restrictedNodes : list nodes that cannot pass through
     * @param current : The beginning node
     * @param target : The end node
     * @param skipDarkArea : skip dark area or not.
     * @return the string of instruction movement
     */
    public static String getShortestPath(GameMap gameMap, List<Node> restrictedNodes, Node current, Node target, boolean skipDarkArea) {
        /**
         * Direction:
         * (-1, 0): Left
         * (1, 0): Right
         * (0, -1): Down
         * (0, 1): Up
         */
        int[] Dx = {-1, 1, 0, 0};
        int[] Dy = {0, 0, -1, 1};

        /**
         * @param mapSize: 240 * 240 or 120 * 120
         * @param darkAreaSize The length of the restricted area. The dark area will narrow to a square radius.
         * If the player passes through any cell that belongs to the dark area, their HP will be decreased.
         */
        int mapSize = gameMap.getMapSize();
        int darkAreaSize = gameMap.getDarkAreaSize();

        /**
         * @isRestrictedNodes: True or false. Check if cell is restricted
         * @g: The minimum distance from beginning node to this node
         * @trace: the last direction on shortest path from beginning node to this node
         */
        ArrayList<ArrayList<Integer>> isRestrictedNodes = new ArrayList<>(mapSize + 1);
        ArrayList<ArrayList<Integer>> g = new ArrayList<>(mapSize + 1);
        ArrayList<ArrayList<Integer>> trace = new ArrayList<>(mapSize + 1);

        /**
         * Init data for all arrayLists
         */
        for (int i = 0; i < mapSize + 1; i++) {
            isRestrictedNodes.add(new ArrayList<>(mapSize + 1));
            g.add(new ArrayList<>(mapSize + 1));
            trace.add(new ArrayList<>(mapSize + 1));

            for (int j = 0; j < mapSize + 1; j++) {
                isRestrictedNodes.get(i).add(0);
                g.get(i).add(-1);
                trace.get(i).add(-1);
            }
        }

        /**
         * isRestrictedNodes[x][y] = 1 if (x, y) is restricted node
         */
        for (Node point : restrictedNodes) {
            if (point.x >= 1 && point.x <= mapSize && point.y >= 1 && point.y <= mapSize) {
                isRestrictedNodes.get(point.x).set(point.y, 1);
            }
        }

        /**
         * A data structure is responsible to find the node that has minimum length from beginning node
         * @compare: dependent on Manhattan distance + minimum distance from beginning node to (x, y)
         */
        PriorityQueue<Node> openSet = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                return Integer.compare(g.get(n1.x).get(n1.y) + abs(n1.x - target.x) + abs(n1.y - target.y),
                        g.get(n2.x).get(n2.y) + abs(n2.x - target.x) + abs(n2.y - target.y));
            }
        });

        openSet.add(new Node(current.x, current.y));
        g.get(current.x).set(current.y, 0);

        /**
         * @param ans: the string of instruction movement
         */
        StringBuilder ans = new StringBuilder();

        /**
         * Solution: Find path until openSet is empty.
         */
        while (!openSet.isEmpty()) {
            /**
             * Get the node with smallest f = g(x, y) + ManhattanDistance(node(x, y), node.target))
             * After this operation, Node u will be removed from openSet.
             */
            Node u = openSet.poll();

            /**
             * Trace to find the list direction
             */
            if (u.x == target.x && u.y == target.y) {
                while (target.x != current.x || target.y != current.y) {
                    int dir = trace.get(target.x).get(target.y);
                    if (dir == 0) ans.append('l');
                    else if (dir == 1) ans.append('r');
                    else if (dir == 2) ans.append('d');
                    else ans.append('u');
                    target.x -= Dx[dir];
                    target.y -= Dy[dir];
                }

                ans.reverse();
                break;
            }

            /**
             * Try to move through 4 directions
             */
            for (int dir = 0; dir < 4; dir++) {
                int x = u.x + Dx[dir];
                int y = u.y + Dy[dir];

                /**
                 * If bot wants to move outside of the map or to the dark area
                 * Bot will be directed to another direction
                 */
                if (x < 1 || y < 1 || x > mapSize || y > mapSize) continue;
                if (isRestrictedNodes.get(x).get(y) == 1) continue;
                if (!skipDarkArea &&
                        (x <= darkAreaSize || y <= darkAreaSize ||
                                x >= mapSize - darkAreaSize + 1 || y >= mapSize - darkAreaSize + 1))
                    continue;

                /**
                 * Add a new node (x, y) if it is more optimal than the old version
                 */
                int cost = g.get(u.x).get(u.y) + 1;
                if (g.get(x).get(y) == -1 || g.get(x).get(y) > cost) {
                    g.get(x).set(y, cost);
                    trace.get(x).set(y, dir);
                    openSet.add(new Node(x, y));
                }
            }
        }

        return ans.toString();
    }
}
