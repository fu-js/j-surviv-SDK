package jsclub.codefest2024.sdk.algorithm;

import jsclub.codefest2024.sdk.base.Node;
import jsclub.codefest2024.sdk.model.obstacles.Obstacle;

import java.util.ArrayList;
import java.util.List;

public class Distance {
    public static double distance(Node currentNode, Node targetNode) {
        return Math.sqrt((currentNode.getX() - targetNode.getX()) * (currentNode.getX() - targetNode.getX())
                + (currentNode.getY() - targetNode.getY()) * (currentNode.getY() - targetNode.getY()));
    }

    public static Node nearestNode(Node currentNode, List<? extends Node> listNodes) {
        if (listNodes == null || listNodes.isEmpty()) {
            return null;
        }

        Node nearest = listNodes.get(0);
        double shortestDistance = distance(currentNode, nearest);

        for (Node n : listNodes) {
            double newDistance = distance(currentNode, n);
            if (newDistance < shortestDistance) {
                nearest = n;
                shortestDistance = newDistance;
            }
        }
        return nearest;
    }


}
