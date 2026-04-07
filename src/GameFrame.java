package game;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    MenuPanel menu;
    GamePanel game;

    public GameFrame(){

        // lấy kích thước màn hình
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int width = (int)(screenSize.width * 0.8);
        int height = (int)(screenSize.height * 0.8);

        setTitle("Gold Miner");

        setExtendedState(JFrame.MAXIMIZED_BOTH); // mở full màn hình
        setUndecorated(false); // vẫn giữ thanh tiêu đề

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        menu = new MenuPanel(this);
        add(menu);

        setVisible(true);
    }

    public void startGame(){

        remove(menu);

        game = new GamePanel();
        add(game);

        revalidate();
        repaint();

        game.requestFocus();
    }
}