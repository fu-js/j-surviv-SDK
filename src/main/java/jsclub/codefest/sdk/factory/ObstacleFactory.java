package jsclub.codefest.sdk.factory;

import jsclub.codefest.sdk.model.ElementType;
import jsclub.codefest.sdk.model.obstacles.Obstacle;
import jsclub.codefest.sdk.model.obstacles.ObstacleTag;

import java.util.List;
import java.util.Map;

public class ObstacleFactory {
    /**
     * Available Obstacles
     */
    private static final Map<String, Obstacle> obstacleMap = Map.ofEntries(
        Map.entry("CHEST", new Obstacle("CHEST", ElementType.OBSTACLE, List.of(ObstacleTag.DESTRUCTIBLE, ObstacleTag.PULLABLE_ROPE, ObstacleTag.HERO_HIT_BY_BAT_WILL_BE_STUNNED), 20)),
        Map.entry("DRAGON_EGG", new Obstacle("DRAGON_EGG", ElementType.OBSTACLE, List.of(ObstacleTag.DESTRUCTIBLE, ObstacleTag.PULLABLE_ROPE, ObstacleTag.HERO_HIT_BY_BAT_WILL_BE_STUNNED),50)),
        Map.entry("HUNT_TRAP", new Obstacle("HUNT_TRAP", ElementType.OBSTACLE, List.of(ObstacleTag.TRAP, ObstacleTag.CAN_GO_THROUGH, ObstacleTag.CAN_SHOOT_THROUGH), 0)),
        Map.entry("SPIKES", new Obstacle("SPIKES", ElementType.OBSTACLE, List.of(ObstacleTag.TRAP, ObstacleTag.CAN_GO_THROUGH, ObstacleTag.CAN_SHOOT_THROUGH), 0)),
        Map.entry("BANANA_PEEL", new Obstacle("BANANA_PEEL", ElementType.OBSTACLE, List.of(ObstacleTag.TRAP, ObstacleTag.CAN_GO_THROUGH, ObstacleTag.CAN_SHOOT_THROUGH) , 0)),
        Map.entry("INDESTRUCTIBLE", new Obstacle("INDESTRUCTIBLE", ElementType.OBSTACLE, List.of(ObstacleTag.PULLABLE_ROPE, ObstacleTag.HERO_HIT_BY_BAT_WILL_BE_STUNNED))),
        Map.entry("POND", new Obstacle("POND", ElementType.OBSTACLE, List.of(ObstacleTag.CAN_GO_THROUGH, ObstacleTag.CAN_SHOOT_THROUGH, ObstacleTag.HERO_HIT_BY_BAT_WILL_BE_STUNNED))),
        Map.entry("BUSH", new Obstacle("BUSH", ElementType.OBSTACLE, List.of(ObstacleTag.CAN_GO_THROUGH, ObstacleTag.CAN_SHOOT_THROUGH))),
        Map.entry("RIVER", new Obstacle("RIVER", ElementType.OBSTACLE, List.of(ObstacleTag.CAN_SHOOT_THROUGH, ObstacleTag.HERO_HIT_BY_BAT_WILL_BE_STUNNED))) );

    /**
     * Find obstacle by id.
     *
     * @param id String to find obstacle.
     * @return Obstacle mapped with id.
     */
    public static Obstacle getObstacleById(String id){
        return obstacleMap.get(id);
    }

    /**
     * use for setting indestructible obstacles
     * Find obstacle by id.
     * Set position for obstacle
     *
     * @param id String to find obstacle.
     * @param x,y int to set position.
     * @return Obstacle with updated position,id.
     * @throws CloneNotSupportedException If clone is not supported.
     */
    public static Obstacle getObstacle(String id, int x, int y) throws CloneNotSupportedException {
        Obstacle obstacleBase = getObstacleById(id);

        Obstacle obstacle = (Obstacle) obstacleBase.clone();
        obstacle.setId(id);
        obstacle.setPosition(x,y);

        return obstacle;
    }

    /**
     * use for setting destructible obstacles
     * Find obstacle by id.
     * Set position for obstacle
     *
     * @param id String to find obstacle.
     * @param x,y int to set position.
     * @param hp int to set healing point.
     * @return Obstacle with updated position,hp,id.
     * @throws CloneNotSupportedException If clone is not supported.
     */
    public static Obstacle getObstacle(String id, int x, int y, int hp) throws CloneNotSupportedException {
        Obstacle obstacleBase = getObstacleById(id);
        Obstacle obstacle = (Obstacle) obstacleBase.clone();

        obstacle.setHp(hp);
        obstacle.setId(id);
        obstacle.setPosition(x,y);

        return obstacle;
    }
}
