package game;

import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Bomb {

    Image image = new ImageIcon("src/Nobom.png").getImage();

    int x;
    int y;

    public Bomb(int x,int y){
        this.x = x;
        this.y = y;
    }

    void draw(Graphics2D g){

        int size = 120;   // tăng kích thước

        int drawX = x - size/2;
        int drawY = y - size/2;

        g.drawImage(image, drawX, drawY, size, size, null);
    }
}