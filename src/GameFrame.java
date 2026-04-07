package game;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    MenuPanel menu;
    GamePanel game;

    public GameFrame(){

        setTitle("Gold Miner");

        setSize(1280,720); // cửa sổ game
        setLocationRelativeTo(null);
        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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