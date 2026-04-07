package game;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Graphics2D;

public class Player {

    int x;
    int y;

    Image minerImage = new ImageIcon("src/miner.png").getImage();

    public Player(int x, int y){
        this.x = x;
        this.y = y;
    }

    void draw(Graphics2D g){

        int width = 80;
        int height = 80;

        g.drawImage(minerImage,
                x - width/2,
                y - height/2,
                width,
                height,
                null);
    }
}