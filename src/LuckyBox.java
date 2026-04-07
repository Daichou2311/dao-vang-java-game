package game;

import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class LuckyBox extends Item {

    Image image;

    public LuckyBox(int x,int y){
        super(x,y);

        value = 70;
        hitRadius = 50;
        image = new ImageIcon("src/luckybox.png").getImage();
    }

    void draw(Graphics2D g){

        int size = hitRadius * 2;

        g.drawImage(image,
                x - size/2,
                y - size/2,
                size,
                size,
                null);
    }
}