package jsclub.codefest.sdk.factory;

import jsclub.codefest.sdk.model.npcs.Enemy;

import java.util.Map;

public class EnemyFactory {
    /**
     * Available Armors
     */
    private static final Map<String, Enemy> enemyMap = Map.of(
        "NATIVE", new Enemy("NATIVE", 10),
        "GHOST", new Enemy("GHOST", 10),
        "LEOPARD", new Enemy("LEOPARD", 15),
        "ANACONDA", new Enemy("ANACONDA", 25),
        "RHINO", new Enemy("RHINO", 15),
        "GOLEM", new Enemy("GOLEM", 15)
    );

    /**
     * Find enemy by id.
     *
     * @param id String to find enemy.
     * @return Enemy mapped with id.
     */
    public static Enemy getEnemyById(String id) {
        return enemyMap.get(id);
    }

    /**
     * Find enemy by id.
     * Set position for enemy
     *
     * @param id String to find enemy.
     * @param x,y int to set position.
     * @return Enemy with updated position,id.
     * @throws CloneNotSupportedException If clone is not supported.
     */
    public static Enemy getEnemy(String id, int x, int y) throws CloneNotSupportedException {
        Enemy enemyBase = getEnemyById(id);

        Enemy enemy = (Enemy) enemyBase.clone();
        enemy.setPosition(x, y);
        enemy.setId(id);

        return enemy;
    }

    public static Enemy getEnemy(String id, int x, int y, boolean isCooldownActive, int cooldownStepLeft) throws CloneNotSupportedException {
        Enemy enemyBase = getEnemy(id, x, y);
        enemyBase.setCooldownActive(isCooldownActive);
        enemyBase.setCooldownStepLeft(cooldownStepLeft);

        return enemyBase;
    }
}