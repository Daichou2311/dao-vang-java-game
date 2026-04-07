package game;

import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*;
import java.io.File;

public class MenuPanel extends JPanel {

    GameFrame frame;
    Image bg;

    JButton start;
    JButton guide;
    JButton exit;
    Timer shakeTimer;
    float titleAlpha = 0f;

    int startY = 650;
    int guideY = 720;
    int exitY = 790;
    int soundY = 860;

    int bgOffset = 0;
    int bounce = 0;
    boolean bounceDir = true;

    Clip menuMusic;
    boolean soundOn = true;
    JButton soundBtn;
    Timer animationTimer;
    float glow = 0;
    boolean glowDir = true;

//    int mouseX = 0;
//    int mouseY = 0;
    public MenuPanel(GameFrame frame) {

        this.frame = frame;

        setLayout(null);

        bg = new ImageIcon(getClass().getResource("/menu_bg.png")).getImage();

        start = createButton("START GAME");
        guide = createButton("HOW TO PLAY");
        soundBtn = createButton("SOUND: ON");
        add(soundBtn);

        soundBtn.addActionListener(e -> toggleSound());
        exit = createButton("EXIT");

        add(start);
        add(guide);
        add(exit);

        start.setBounds(923, 343, 260, 60);
        guide.setBounds(923, 418, 260, 60);
        soundBtn.setBounds(923, 490, 260, 60);
        exit.setBounds(923, 560, 260, 60);

        start.addActionListener(e -> frame.startGame());
        guide.addActionListener(e -> showGuide());
        exit.addActionListener(e -> System.exit(0));

        playMenuSound();
//        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
//            public void mouseMoved(java.awt.event.MouseEvent e) {
//                mouseX = e.getX();
//                mouseY = e.getY();
//                repaint();
//            }
//        });
    }

    JButton createButton(String text) {

        JButton btn = new JButton(text);

        btn.setBounds(0, 0, 260, 65);
        btn.setFont(new Font("Arial", Font.BOLD, 22));

        btn.setFocusPainted(false);

        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);

        btn.setForeground(new Color(255, 230, 120));
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {

                btn.setForeground(Color.YELLOW);
                btn.setFont(new Font("Arial", Font.BOLD, 24));

                btn.setText("<html><b><font color='yellow'>" + text + "</font></b></html>");

                Point p = btn.getLocation();

                shakeTimer = new Timer(40, e -> {
                    int dx = (int) (Math.random() * 6 - 3);
                    int dy = (int) (Math.random() * 4 - 2);
                    btn.setLocation(p.x + dx, p.y + dy);
                });

                shakeTimer.start();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {

                btn.setForeground(new Color(255, 230, 120));
                btn.setFont(new Font("Arial", Font.BOLD, 22));
                btn.setText(text);

                if (shakeTimer != null) {
                    shakeTimer.stop();
                }
            }
        });

        return btn;
    }

    void playMenuSound() {

        try {

            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(
                            getClass().getResource("/menu_music.wav"));

            menuMusic = AudioSystem.getClip();
            menuMusic.open(audio);

            if (soundOn) {
                menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void toggleSound() {

        soundOn = !soundOn;

        if (soundOn) {
            soundBtn.setText("SOUND: ON");

            if (menuMusic != null) {
                menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
            }

        } else {

            soundBtn.setText("SOUND: OFF");

            if (menuMusic != null) {
                menuMusic.stop();
            }
        }
    }

    void showGuide() {

        JOptionPane.showMessageDialog(
                this,
                "SPACE : Thả hook\nQ : Thả bom",
                "How To Play",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    protected void paintComponent(Graphics g) {


        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
        int centerX = getWidth()/2;

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
//        g2.drawString("X: " + mouseX + " Y: " + mouseY, mouseX + 10, mouseY);
// glow animation
        if (glowDir) {

            glow += 0.03f;

            if (glow > 1f) {
                glowDir = false;
            }

        } else {

            glow -= 0.03f;

            if (glow < 0.4f) {
                glowDir = true;
            }

        }
    }
}