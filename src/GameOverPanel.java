package game;

import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel {

    GameFrame frame;
    int score;
    int level;


    public GameOverPanel(GameFrame frame, int score, int level){

        this.frame = frame;
        this.score = score;
        this.level = level;

        setLayout(null);

        JButton menu = new JButton("MENU");
        JButton exit = new JButton("EXIT");

        menu.setBounds(540,420,200,60);
        exit.setBounds(540,500,200,60);

        menu.setFont(new Font("Arial",Font.BOLD,24));
        exit.setFont(new Font("Arial",Font.BOLD,24));

        add(menu);
        add(exit);

        menu.addActionListener(e -> {
            frame.showMenu();
        });

        exit.addActionListener(e -> {
            System.exit(0);
        });
    }

    protected void paintComponent(Graphics g){

        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0,0,getWidth(),getHeight());

        g.setFont(new Font("Arial",Font.BOLD,70));
        g.setColor(Color.RED);
        g.drawString("GAME OVER",420,280);

        g.setFont(new Font("Arial",Font.BOLD,30));
        g.setColor(Color.WHITE);

        g.drawString("Score: " + score,520,360);
        g.drawString("Level: " + level,520,400);
    }
}