package jsclub.codefest.sdk.model;

import com.google.gson.Gson;
import jsclub.codefest.sdk.factory.*;
import jsclub.codefest.sdk.model.armors.Armor;
import jsclub.codefest.sdk.model.effects.Effect;
import jsclub.codefest.sdk.model.npcs.Ally;
import jsclub.codefest.sdk.model.npcs.Enemy;
import jsclub.codefest.sdk.model.obstacles.Obstacle;
import jsclub.codefest.sdk.model.obstacles.ObstacleTag;
import jsclub.codefest.sdk.model.players.Player;
import jsclub.codefest.sdk.model.support_items.SupportItem;
import jsclub.codefest.sdk.model.weapon.Bullet;
import jsclub.codefest.sdk.model.weapon.Weapon;
import jsclub.codefest.sdk.socket.data.receive_data.Entity;
import jsclub.codefest.sdk.socket.data.receive_data.EntityAttribute;
import jsclub.codefest.sdk.socket.data.receive_data.MapData;
import jsclub.codefest.sdk.util.MsgPackUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private static final Logger log = LogManager.getLogger(GameMap.class);
    private int mapSize = 0;
    private int safeZone = 0;
    private int stepNumber = 0;
    private List<Obstacle> listObstacles = new ArrayList<>();
    private List<Obstacle> listIndestructibles = new ArrayList<>();
    private List<Enemy> listEnemies = new ArrayList<>();
    private List<Ally> listAllies = new ArrayList<>();
    private List<Weapon> listWeapons = new ArrayList<>();
    private List<SupportItem> listSupportItems = new ArrayList<>();
    private List<Armor> listArmors = new ArrayList<>();
    private List<Bullet> listBullets = new ArrayList<>();
    private List<Player> otherPlayerInfo = new ArrayList<>();
    private Player currentPlayer;
    private Inventory heroInventory;
    private List<Effect> heroEffect;

    private Inventory getHeroInventory() {
        return heroInventory;
    }

    public void setHeroInventory(Inventory heroInventory) {
        this.heroInventory = heroInventory;
    }

    public List<Effect> getHeroEffect() {
        return heroEffect;
    }

    public void setHeroEffect(List<Effect> heroEffect) {
        this.heroEffect = heroEffect;
    }

    public GameMap(Inventory heroInventory, List<Effect> heroEffect) {
        this.heroInventory = heroInventory;
        this.heroEffect = heroEffect;
    }

    /**
     * Decode message from server when initializing map.
     * update map size and indestructible obstacles attributes
     *
     * @param arg The message parsed from server.
     */
    public void updateOnInitMap(Object arg) {
        try {
            // Reset step number when initializing map
            this.stepNumber = 0;
            
            Gson gson = new Gson();
            String message = MsgPackUtil.decode(arg);
            MapData mapData = gson.fromJson(message, MapData.class);
            setMapSize(mapData.mapSize);

            // Because on map init, game will send indestructible obstacles so we add them to listIndestructibles.
            List<Obstacle> newListIndestructibles = new ArrayList<>();

            for (Obstacle o : mapData.listObstacles){
                Obstacle obstacle = ObstacleFactory.getObstacle(o.getId(), o.x, o.y);
                newListIndestructibles.add(obstacle);
            }

            setListIndestructibles(newListIndestructibles);
            // Initialize the lists with indestructible obstacles
            setListObstacles(newListIndestructibles);
        } catch (CloneNotSupportedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decode message from server when updating map.
     * update dark area size and items information
     *
     * @param arg The message parsed from server.
     */
    public void updateOnUpdateMap(Object arg) {
        try {
            // Increment step number on each update
            this.stepNumber++;
            
            Gson gson = new Gson();
            String message = MsgPackUtil.decode(arg);
            MapData mapData = gson.fromJson(message, MapData.class);

            // Extend listObstacles with listIndestructibles (get from init map)
            List<Obstacle> newListObstacles = new ArrayList<>(listIndestructibles);

            // Initialize new lists for each type of entity
            List<Enemy> newListEnemies = new ArrayList<>();
            List<Ally> newListAllies = new ArrayList<>();
            List<Weapon> newListWeapons = new ArrayList<>();
            List<SupportItem> newListSupportItem = new ArrayList<>();
            List<Armor> newListArmor = new ArrayList<>();
            List<Bullet> newListBullets = new ArrayList<>();

            setSafeZone(mapData.safeZone);

            for (Entity entity : mapData.listEntities) {
                // On game update will send all changeable obstacles, for 2025 version, we only have chests and traps.
                if (entity.type == ElementType.CHEST) {
                    Obstacle obstacle = ObstacleFactory.getObstacle(entity.id, entity.x, entity.y);
                    // Set hp for CHEST obstacle
                    obstacle.setCurrentHp(entity.attributes.currentHp);
                    newListObstacles.add(obstacle);
                }

                if (entity.type == ElementType.TRAP) {
                    Obstacle obstacle = ObstacleFactory.getObstacle(entity.id, entity.x, entity.y);
                    newListObstacles.add(obstacle);
                }

                if (entity.type == ElementType.ENEMY) {
                    Enemy enemy = EnemyFactory.getEnemy(entity.id, entity.x, entity.y,
                            entity.attributes.isCooldownActive, entity.attributes.cooldownStepLeft);
                    newListEnemies.add(enemy);
                }

                if (entity.type == ElementType.ALLY) {
                    Ally ally = AllyFactory.getAlly(entity.id, entity.x, entity.y,
                            entity.attributes.isCooldownActive, entity.attributes.cooldownStepLeft);
                    newListAllies.add(ally);
                }

                if (entity.type == ElementType.MELEE
                 || entity.type == ElementType.THROWABLE
                 || entity.type == ElementType.GUN
                 || entity.type == ElementType.SPECIAL) {
                    Weapon weapon = WeaponFactory.getWeapon(entity.id, entity.x, entity.y);
                    newListWeapons.add(weapon);
                }

                if (entity.type == ElementType.SUPPORT_ITEM) {
                    SupportItem support = SupportItemFactory.getSupportItem(entity.id, entity.x, entity.y);
                    newListSupportItem.add(support);
                }

                if (entity.type == ElementType.ARMOR
                 || entity.type == ElementType.HELMET) {
                    Armor armor = ArmorFactory.getArmor(entity.id, entity.x, entity.y);
                    newListArmor.add(armor);
                }

                 if (entity.type == ElementType.BULLET) {
                     EntityAttribute entityAttributes = entity.attributes;
                     Bullet b = new Bullet(
                             entityAttributes.damage,
                             entityAttributes.speed,
                             entityAttributes.destinationX,
                             entityAttributes.destinationY);
                     b.setPosition(entity.x, entity.y);

                     newListBullets.add(b);
                 }
            }

            // Update the lists in GameMap
            setListObstacles(newListObstacles);
            setListEnemies(newListEnemies);
            setListAllies(newListAllies);
            setListWeapons(newListWeapons);
            setListSupportItems(newListSupportItem);
            setListArmors(newListArmor);
            setListBullets(newListBullets);

            setCurrentPlayer(mapData.currentPlayer);
            setOtherPlayerInfo(mapData.otherPlayers);

            if (currentPlayer.getHealth() <= 0) {
                this.heroInventory.reset();
                this.heroEffect.clear();
            }
        } catch (CloneNotSupportedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * find element by position
     *
     * @param x,y int Position of element.
     * @return The Element mapped with position.
     */

    public Element getElementByIndex(int x, int y) {
        Element element = null;
        // element = this.findElementInListByIndex(x, y, this.listIndestructibleObstacles);
        // if (element != null) return element;

        element = this.findElementInListByIndex(x, y, this.listObstacles);
        if (element != null) return element;

        element = this.findElementInListByIndex(x, y, this.listEnemies);
        if (element != null) return element;

        element = this.findElementInListByIndex(x, y, this.listAllies);
        if (element != null) return element;

        element = this.findElementInListByIndex(x, y, this.listWeapons);
        if (element != null) return element;

        element = this.findElementInListByIndex(x, y, this.listSupportItems);
        if (element != null) return element;

        element = this.findElementInListByIndex(x, y, this.listArmors);
        if (element != null) return element;

        element = this.findElementInListByIndex(x, y, this.listBullets);
        if (element != null) return element;

        element = this.findElementInListByIndex(x, y, this.otherPlayerInfo);
        if (element != null) return element;

        if (this.currentPlayer.x == x && this.currentPlayer.y == y) {
            return this.currentPlayer;
        }

        return new Element(x, y, "ROAD", ElementType.ROAD);
    }

    private Element findElementInListByIndex(int x, int y, List elements) {
        for (Object element : elements) {
            Element e = (Element) element;
            if (e.getX() == x && e.getY() == y) {
                return e;
            }
        }
        return null;
    }

    /**
     * get,set functions
     */

    public List<Obstacle> getObstaclesByTag(String tag) {
        List<Obstacle> obstacles = new ArrayList<>();
        try {
            ObstacleTag t = ObstacleTag.valueOf(tag);
            for (Obstacle o : listObstacles) {
                if (o.getTags().contains(t)) {
                    obstacles.add(o);
                }
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new RuntimeException(e);
        }
        return obstacles;
    }

    public List<Weapon> getAllGun() {
        List<Weapon> guns = new ArrayList<>();
        for (Weapon weapon : listWeapons) {
            if (weapon.getType() == ElementType.GUN) {
                guns.add(weapon);
            }
        }
        return guns;
    }

    public List<Weapon> getAllMelee() {
        List<Weapon> melees = new ArrayList<>();
        for (Weapon weapon : listWeapons) {
            if (weapon.getType() == ElementType.MELEE) {
                melees.add(weapon);
            }
        }
        return melees;
    }

    public List<Weapon> getAllThrowable() {
        List<Weapon> throwables = new ArrayList<>();
        for (Weapon weapon : listWeapons) {
            if (weapon.getType() == ElementType.THROWABLE) {
                throwables.add(weapon);
            }
        }
        return throwables;
    }

    public List<Weapon> getAllSpecial() {
        List<Weapon> specials = new ArrayList<>();
        for (Weapon weapon : listWeapons) {
            if (weapon.getType() == ElementType.SPECIAL) {
                specials.add(weapon);
            }
        }
        return specials;
    }

    public int getMapSize() {
        return mapSize;
    }

    public int getSafeZone() {
        return safeZone;
    }

    public List<Obstacle> getListObstacles() {
        return listObstacles;
    }

    public List<Enemy> getListEnemies() {
        return listEnemies;
    }

    public List<Ally> getListAllies() {
        return listAllies;
    }

    public List<Weapon> getListWeapons() {
        return listWeapons;
    }

    public List<SupportItem> getListSupportItems() {
        return listSupportItems;
    }

    public List<Armor> getListArmors() {
        return listArmors;
    }

    public List<Bullet> getListBullets() {
        return listBullets;
    }

    public List<Player> getOtherPlayerInfo() {
        return otherPlayerInfo;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    public void setSafeZone(int safeZone) {
        this.safeZone = safeZone;
    }

    public void setListIndestructibles(List<Obstacle> listIndestructibles) {
        this.listIndestructibles = listIndestructibles;
    }

    public List<Obstacle> getListIndestructibles() {
        return listIndestructibles;
    }

    // public void setListIndestructibleObstacles(List<Obstacle> listIndestructibleObstacles) {
    //     this.listIndestructibleObstacles = listIndestructibleObstacles;
    // }

    public void setListObstacles(List<Obstacle> listObstacles) {
        this.listObstacles = listObstacles;
    }

    public void setListEnemies(List<Enemy> listEnemies) {
        this.listEnemies = listEnemies;
    }

    public void setListAllies(List<Ally> listAllies) {
        this.listAllies = listAllies;
    }

    public void setListWeapons(List<Weapon> listWeapons) {
        this.listWeapons = listWeapons;
    }

    public void setListSupportItems(List<SupportItem> listSupportItems) {
        this.listSupportItems = listSupportItems;
    }

    public void setListArmors(List<Armor> listArmors) {
        this.listArmors = listArmors;
    }

    public void setListBullets(List<Bullet> listBullets) {
        this.listBullets = listBullets;
    }

    public void setOtherPlayerInfo(List<Player> otherPlayerInfo) {
        this.otherPlayerInfo = otherPlayerInfo;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
