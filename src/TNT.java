package game;

import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class TNT extends Item {

    Image image;

    public TNT(int x,int y){
        super(x,y);

        value = 0; // TNT không cộng điểm
        hitRadius = 35;
        image = new ImageIcon("src/tnt.png").getImage();
    }

    void draw(Graphics2D g){

        int size = 100;

        if(image != null){
            g.drawImage(image,x-size/2,y-size/2,size,size,null);
        }
    }
}