package game;

import javax.swing.*;

public class GameFrame extends JFrame {

    MenuPanel menu;
    GamePanel game;

    public GameFrame(){

        setTitle("Gold Miner");

        setSize(1280,720);
        setLocationRelativeTo(null);
        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        showMenu();

        setVisible(true);
    }

    public void showMenu(){

        SoundManager.stopLoop();   // ⭐ tránh nhạc game chồng

        menu = new MenuPanel(this);

        setContentPane(menu);

        revalidate();
        repaint();

        menu.playMenuSound();
    }

    public void startGame(){

        game = new GamePanel();

        setContentPane(game);

        revalidate();
        repaint();

        game.requestFocus();
    }

    public void showGameOver(int score, int level){

        if(menu != null){
            menu.stopMenuSound();   // ⭐ tắt nhạc menu
        }

        SoundManager.stopLoop();   // ⭐ tắt nhạc game

        setContentPane(new GameOverPanel(this, score, level));

        revalidate();
        repaint();
    }
}