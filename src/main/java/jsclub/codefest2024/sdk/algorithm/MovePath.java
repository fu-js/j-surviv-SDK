package jsclub.codefest2024.sdk.algorithm;

import jsclub.codefest2024.sdk.Hero;
import jsclub.codefest2024.sdk.base.Node;
import jsclub.codefest2024.sdk.model.GameMap;

public class MovePath {
    static Node lastPoint;
    static String path;
    static boolean test = true;

    public static String getMovePath(Hero hero, GameMap gameMap, String p) {
        Node currentPoint = new Node(gameMap.getCurrentPlayer().x, gameMap.getCurrentPlayer().y);
        if (test) {
            test = false;
            lastPoint = currentPoint;
            String temp = p.charAt(0) + "";
            path = p.substring(1, p.length());
            return temp;
        }
        if (!currentPoint.equals(lastPoint)) {
            lastPoint = currentPoint;
            if (path.length() > 0) {
                path = path.substring(1, path.length());
            } else {
                test = true;
            }
        }
        return path;
    }
}
