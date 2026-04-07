package game;

import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Stone extends Item {

    int size;
    Image image;

    public Stone(int x, int y, int size){
        super(x, y);

        this.size = size;

        if(size == 1){
            value = GamePanel.stoneBoost ? 60 : 30;
        }

        if(size == 2){
            value = GamePanel.stoneBoost ? 180 : 80;
        }

        hitRadius = 30 * size;

        image = new ImageIcon("src/stone.png").getImage();
    }

    void draw(Graphics2D g){

        int r = 15 * size;   // kích thước đá

        if(image != null){
            g.drawImage(image, x - r, y - r, r * 2, r * 2, null);
        }
    }
}