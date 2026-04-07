package game;

import java.awt.*;

public abstract class Item {

    int x;
    int y;
    int hitRadius = 30;
    int value;

    boolean collected = false;

    public Item(int x, int y){
        this.x = x;
        this.y = y;
    }

    abstract void draw(Graphics2D g);

}