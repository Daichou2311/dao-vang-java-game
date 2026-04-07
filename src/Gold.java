package game;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Gold extends Item {

    int size;
    Image image;

    public Gold(int x,int y,int size){
        super(x,y);
        this.size = size;

        if(size == 1){
            value = 100;
            image = new ImageIcon("src/gold_small.gif").getImage();
            hitRadius = 25;
        }

        if(size == 2){
            value = 200;
            image = new ImageIcon("src/gold_medium.gif").getImage();
            hitRadius = 35;
        }

        if(size == 3){
            value = 500;
            image = new ImageIcon("src/gold_big.gif").getImage();
            hitRadius = 50;
        }
    }

    void draw(Graphics2D g){
        int r = 10 * size;

        if(image != null){
            g.drawImage(image, x - r, y - r, r * 2, r * 2, null);
        }
    }

}