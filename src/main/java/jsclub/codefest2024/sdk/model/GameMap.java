package jsclub.codefest2024.sdk.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import jsclub.codefest2024.sdk.factory.*;
import jsclub.codefest2024.sdk.model.enemies.*;
import jsclub.codefest2024.sdk.model.equipments.*;
import jsclub.codefest2024.sdk.model.obstacles.*;
import jsclub.codefest2024.sdk.model.players.Player;
import jsclub.codefest2024.sdk.model.weapon.*;
import jsclub.codefest2024.sdk.socket.data.receive_data.MapData;
import jsclub.codefest2024.sdk.util.MsgPackUtil;

public class GameMap {
    private int mapSize = 0;
    private int darkAreaSize = 0;
    private List<Obstacle> listIndestructibleObstacles = new ArrayList<>();
    private List<Enemy> listEnemies = new ArrayList<>();
    private List<Obstacle> listTraps = new ArrayList<>();
    private List<Obstacle> listChests = new ArrayList<>();
    private List<Weapon> listWeapons = new ArrayList<>();
    private List<HealingItem> listHealingItems = new ArrayList<>();
    private List<Armor> listArmors = new ArrayList<>();
    private List<Bullet> listBullets = new ArrayList<>();
    private List<Player> otherPlayerInfo = new ArrayList<>();
    private Player currentPlayer;

    public GameMap() {}

    public void updateOnInitMap(Object arg) {
        try {
            Gson gson = new Gson();
            String message = MsgPackUtil.decode(arg);
            MapData mapData = gson.fromJson(message, MapData.class);

            setMapSize(mapData.mapSize);

            List<Obstacle> newListIndestructibleObstacles = new ArrayList<>();
            for(Obstacle o : mapData.listIndestructible){
                Obstacle indestructible = ObstacleFactory.getObstacle("INDESTRUCTIBLE_OBSTACLE", o.getX(), o.getY());
                newListIndestructibleObstacles.add(indestructible);
            }
            setListIndestructibleObstacles(newListIndestructibleObstacles);
        } catch (CloneNotSupportedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateOnUpdateMap(Object arg) {
        try {
            Gson gson = new Gson();
            String message = MsgPackUtil.decode(arg);
            MapData mapData = gson.fromJson(message, MapData.class);

            List<Enemy> newListEnemies = new ArrayList<>();
            List<Obstacle> newListTraps = new ArrayList<>();
            List<Obstacle> newListChests = new ArrayList<>();
            List<Weapon> newListWeapons = new ArrayList<>();
            List<HealingItem> newListHealingItem = new ArrayList<>();
            List<Armor> newListArmor = new ArrayList<>();

            setDarkAreaSize(mapData.darkAreaSize);

            for(Enemy e : mapData.listEnemies){
                Enemy enemy = EnemyFactory.getEnemy(e.getId(), e.getX(), e.getY());
                newListEnemies.add(enemy);
            }
            setListEnemies(newListEnemies);

            for(Obstacle t : mapData.listTraps) {
                Obstacle trap = ObstacleFactory.getObstacle(t.getId(), t.getX(), t.getY(),t.getHp());
                newListTraps.add(trap);
            }
            setListTraps(newListTraps);

            for(Obstacle c : mapData.listChests) {
                Obstacle chest = ObstacleFactory.getObstacle(c.getId(), c.getX(), c.getY(),c.getHp());
                newListChests.add(chest);
            }
            setListChests(newListChests);

            for(Weapon w : mapData.listWeapons){
                Weapon weapon = WeaponFactory.getWeapon(w.getId(), w.getX(), w.getY());
                newListWeapons.add(weapon);
            }
            setListWeapons(newListWeapons);

            for(HealingItem h: mapData.listHealingItems){
                HealingItem healing = HealingItemFactory.getHealingItem(h.getId(), h.getX(),h.getY());
                newListHealingItem.add(healing);
            }
            setListHealingItems(newListHealingItem);

            for (Armor a: mapData.listArmors) {
                Armor armor = ArmorFactory.getArmor(a.getId(),a.getX(),a.getY());
                newListArmor.add(armor);
            }
            setListArmors(newListArmor);

            setListBullets(mapData.listBullet);
            setOtherPlayerInfo(mapData.otherPlayers);
            setCurrentPlayer(mapData.currentPlayer);
        } catch (CloneNotSupportedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Element getElementByIndex(int x, int y) {
        Element element = null;
        element = this.findElementInListByIndex(x, y, this.listIndestructibleObstacles);
        if(element != null) return element;

        element = this.findElementInListByIndex(x, y, this.listEnemies);
        if(element != null) return element;

        element = this.findElementInListByIndex(x, y, this.listTraps);
        if(element != null) return element;

        element = this.findElementInListByIndex(x, y, this.listChests);
        if(element != null) return element;

        element = this.findElementInListByIndex(x, y, this.listWeapons);
        if(element != null) return element;

        element = this.findElementInListByIndex(x, y, this.listHealingItems);
        if(element != null) return element;

        element = this.findElementInListByIndex(x, y, this.listArmors);
        if(element != null) return element;

        element = this.findElementInListByIndex(x, y, this.listBullets);
        if(element != null) return element;

        element = this.findElementInListByIndex(x, y, this.otherPlayerInfo);
        if(element != null) return element;

        if(this.currentPlayer.x == x && this.currentPlayer.y == y) {
            return this.currentPlayer;
        }

        return new Element(x, y, "ROAD", ElementType.ROAD);
    }

    private Element findElementInListByIndex(int x, int y, List elements){
        for(Object element : elements){
            Element e = (Element) element;
            if (e.getX() == x && e.getY() == y) {
                return e;
            }
        }
        return null;
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

    public int getMapSize() {
        return mapSize;
    }

    public int getDarkAreaSize() {
        return darkAreaSize;
    }

    public List<Obstacle> getListIndestructibleObstacles() {
        return listIndestructibleObstacles;
    }

    public List<Enemy> getListEnemies() {
        return listEnemies;
    }

    public List<Obstacle> getListTraps() {
        return listTraps;
    }

    public List<Obstacle> getListChests() {
        return listChests;
    }

    public List<Weapon> getListWeapons() {
        return listWeapons;
    }

    public List<HealingItem> getListHealingItems() {
        return listHealingItems;
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

    public void setDarkAreaSize(int darkAreaSize) {
        this.darkAreaSize = darkAreaSize;
    }

    public void setListIndestructibleObstacles(List<Obstacle> listIndestructibleObstacles) {
        this.listIndestructibleObstacles = listIndestructibleObstacles;
    }

    public void setListEnemies(List<Enemy> listEnemies) {
        this.listEnemies = listEnemies;
    }

    public void setListTraps(List<Obstacle> listTraps) {
        this.listTraps = listTraps;
    }

    public void setListChests(List<Obstacle> listChests) {
        this.listChests = listChests;
    }

    public void setListWeapons(List<Weapon> listWeapons) {
        this.listWeapons = listWeapons;
    }

    public void setListHealingItems(List<HealingItem> listHealingItems) {
        this.listHealingItems = listHealingItems;
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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
