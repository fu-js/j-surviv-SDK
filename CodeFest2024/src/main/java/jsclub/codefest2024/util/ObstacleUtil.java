package jsclub.codefest2024.util;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

import jsclub.codefest2024.socket.data.Obstacles.Obstacle;

public class ObstacleUtil {

    public static Map<String, Obstacle> createObstaclesMap() {
        Map<String, Obstacle> obstacleList = new HashMap<>();
        obstacleList.put("CHEST", new Obstacle(40));
        obstacleList.put("SPECIAL_CHEST", new Obstacle(40));
        obstacleList.put("FLOWER_VASE", new Obstacle(10));
        obstacleList.put("GAS_TANK", new Obstacle(100));
        return obstacleList;
    }

    public Map<String, Obstacle> obstacleList;

    public ObstacleUtil() {
        this.obstacleList = createObstaclesMap();
    }

    @Override
    public String toString() {
        return new Gson().toJson(obstacleList);
    }

    public Obstacle getObstacle(String name) {
        return obstacleList.get(name);
    }
}
