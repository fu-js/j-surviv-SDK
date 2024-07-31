/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jsclub.codefest2024.sdk.socket.data.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import jsclub.codefest2024.sdk.socket.data.enemies.*;
import jsclub.codefest2024.sdk.socket.data.equipments.*;
import jsclub.codefest2024.sdk.socket.data.obstacles.*;
import jsclub.codefest2024.sdk.socket.data.weapon.*;
import jsclub.codefest2024.sdk.util.element.*;

/**
 *
 * @author AD
 */
public class MapInit {
    private int gameId;
    private int mapHeight;
    private int mapWidth;
    private int darkAreaWidth;
    private int darkAreaHeight;
    private String[] indestructibles;
    private List<Chaser> chasers;
    private List<Crow> crows;
    private List<Armor> armors;
    private List<DarkArea> darkAreas;
    private List<HealingItem> healingItems;
    private List<Obstacle> obstacles;
    private List<Weapon> weapons;

    
    public MapInit() {
        this.mapHeight=0;
        this.mapWidth=0;
        this.darkAreaWidth=0;
        this.darkAreaHeight=0;
        this.indestructibles = null;
    }
    public MapInit(int mapHeight, int mapWidth, int darkAreaWidth, int darkAreaHeight, String[] walls) {
        this.mapHeight=mapHeight;
        this.mapWidth=mapWidth;
        this.darkAreaWidth=darkAreaWidth;
        this.darkAreaHeight=darkAreaHeight;
        this.indestructibles = walls;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getDarkAreaWidth() {
        return darkAreaWidth;
    }

    public int getDarkAreaHeight() {
        return darkAreaHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public void setDarkAreaWidth(int darkAreaWidth) {
        this.darkAreaWidth = darkAreaWidth;
    }

    public void setDarkAreaHeight(int darkAreaHeight) {
        this.darkAreaHeight = darkAreaHeight;
    }



    public void MapUpdate(
            List<Weapon> weapons,
            List<Obstacle> obstacles,
            List<HealingItem> healingItems,
            List<DarkArea> darkAreas,
            List<Armor> armors,
            List<Crow> crows,
            List<Chaser> chasers) {

        this.weapons = weapons;
        this.obstacles = obstacles;
        this.healingItems = healingItems;
        this.darkAreas = darkAreas;
        this.armors = armors;
        this.crows = crows;
        this.chasers = chasers;
    }

    public List<Chaser> getChasers() {
        return chasers;
    }

    public List<Crow> getCrows() {
        return crows;
    }

    public List<Armor> getArmors() {
        return armors;
    }

    public List<HealingItem> getHealingItems() {
        return healingItems;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public List<DarkArea> getDarkAreas() {
        return darkAreas;
    }

    public void setWeapons(List<Weapon> weapons) {
        this.weapons = weapons;
    }

    public void setObstacles(List<Obstacle> obstacles) {
        this.obstacles = obstacles;
    }

    public void setHealingItems(List<HealingItem> healingItems) {
        this.healingItems = healingItems;
    }

    public void setDarkAreas(List<DarkArea> darkAreas) {
        this.darkAreas = darkAreas;
    }

    public void setArmors(List<Armor> armors) {
        this.armors = armors;
    }

    public void setCrows(List<Crow> crows) {
        this.crows = crows;
    }

    public void setChasers(List<Chaser> chasers) {
        this.chasers = chasers;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
