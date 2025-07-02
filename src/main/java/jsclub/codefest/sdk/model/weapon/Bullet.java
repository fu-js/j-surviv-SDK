package jsclub.codefest.sdk.model.weapon;

import jsclub.codefest.sdk.model.Element;
import jsclub.codefest.sdk.model.ElementType;

public class Bullet extends Element {
    private float damage = 0;
    private int speed = 0;
    private int destinationX = 0;
    private int destinationY = 0;

    public Bullet(float damage, int speed, int destinationX, int destinationY) {
        this.setId("BULLET");
        this.setType(ElementType.BULLET);

        this.damage = damage;
        this.speed = speed;
        this.destinationX = destinationX;
        this.destinationY = destinationY;
    }
    
    public float getDamage() {
        return damage;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDestinationX() {
        return destinationX;
    }

    public int getDestinationY() {
        return destinationY;
    }
}
