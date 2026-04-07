package game;

import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Diamond extends Item {

    Image image;

    public Diamond(int x,int y){
        super(x,y);

        value = 600;
        hitRadius = 25;
        image = new ImageIcon("src/diamond.gif").getImage();
    }

    void draw(Graphics2D g){

        int size = 30;   // kim cương nhỏ

        if(image != null){
            g.drawImage(image,x-size/2,y-size/2,size,size,null);
        }
    }
}