package jsclub.codefest2024.socket.data.Equipments;

import com.google.gson.Gson;

public class Armor {
    private double damageReduce;
    private int rare;
    private int point;

    // Constructor
    public Armor(double damageReduce, int rare, int point) {
        this.damageReduce = damageReduce;
        this.rare = rare;
        this.point = point;
    }

    public void setDamageReduce(Double damageReduce) {
        this.damageReduce = damageReduce;
    }

    public void setRare(int rare) {
        this.rare = rare;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public double getDamageReduce() {
        return damageReduce;
    }

    public int getRare() {
        return rare;
    }

    public int getPoint() {
        return point;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
