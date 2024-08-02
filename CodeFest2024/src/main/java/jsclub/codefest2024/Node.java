package jsclub.codefest2024;

public class Node {
    public int x;
    public int y;

    public Node() {}
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() { return y; }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
