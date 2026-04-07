package game;
import javax.swing.ImageIcon;
import java.awt.Image;

public class Player {

    int x;
    int y;

    Image minerImage = new ImageIcon("src/miner.png").getImage();

    public Player(int x, int y){
        this.x = x;
        this.y = y;
    }

}