package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.Collections;

public class GamePanel extends JPanel implements KeyListener {

    Timer timer;

    game.Player player;
    game.Hook hook;

    ArrayList<game.Item> items = new ArrayList<>();

    int level = 1;
    int targetScore = 500;
    Random rand;   // chỉ khai báo

    int score = 0;

    int timeLeft = 35;
    long lastTime = System.currentTimeMillis();

    game.Bomb bomb = null;
    int bombTimer = 0;
    int bombs = 1;

    int hookSize = 30;

    Image bombImage = new ImageIcon("src/bomb.png").getImage();
    Image explosionImage = new ImageIcon("src/Nobom.png").getImage();
    Image ropeImage = new ImageIcon("src/rope.png").getImage();
    Image background = new ImageIcon("src/backgroundgame.png").getImage();
    Image shopImage = new ImageIcon("src/shop.png").getImage();
    Image potionspeedImage = new ImageIcon("src/potionspeed.png").getImage();
    Image potionluckyImage = new ImageIcon("src/potionlucky.png").getImage();
    Image stoneImage = new ImageIcon("src/stoneboots.png").getImage();


    int explosionX = 0;
    int explosionY = 0;
    int explosionTimer = 0;
    static int groundY = 220;
    int SAFE_ZONE = 150;

    int hudX = 5;
    int hudY = 5;

    static int BASE_WIDTH;
    static int BASE_HEIGHT;

    boolean luckyBoost = false;
    boolean speedBoost = false;

    boolean nextLevelSpeedBoost = false;
    boolean nextLevelLuckyBoost = false;
    static boolean stoneBoost = false;

    boolean levelEnded = false;
    boolean showNextLevelButton = false;
    boolean hookReady = true;
    boolean levelSoundPlayed = false;
    boolean openingShop = false;
    Rectangle nextLevelButton = new Rectangle(0,0,200,60);
    ArrayList<game.ShopItem> generateShopItems(){

        ArrayList<game.ShopItem> shopItems = new ArrayList<>();
        Random r = new Random();

        int itemCount = r.nextInt(5); // 0-4 item (có thể shop trống)
        ArrayList<Integer> used = new ArrayList<>();

        for(int i=0;i<itemCount;i++){

            int type;

            do{
                type = r.nextInt(4);
            }while(used.contains(type));

            used.add(type);

            if(type == 0){
                shopItems.add(new game.ShopItem(
                        "Bomb",
                        80 + r.nextInt(80),
                        bombImage
                ));
            }

            if(type == 1){
                shopItems.add(new game.ShopItem(
                        "Lucky Potion",
                        150 + r.nextInt(100),
                        potionluckyImage
                ));
            }

            if(type == 2){
                shopItems.add(new game.ShopItem(
                        "Speed Potion",
                        200 + r.nextInt(120),
                        potionspeedImage
                ));
            }

            if(type == 3){
                shopItems.add(new game.ShopItem(
                        "Stone Boost",
                        120 + r.nextInt(100),
                        stoneImage
                ));
            }
        }

        return shopItems;
    }
    public GamePanel() {

        BASE_WIDTH = 1280;
        BASE_HEIGHT = 720;

        setPreferredSize(new Dimension(BASE_WIDTH, BASE_HEIGHT));

        // seed random theo level
        rand = new Random(level * 999 + 7);

        setFocusable(true);
        setDoubleBuffered(true);
        addKeyListener(this);

        player = new game.Player(BASE_WIDTH / 2, groundY - 25);
        hook = new game.Hook(player, this);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {

                SoundManager.play("/button-press.wav");

            }
            public void mouseClicked(MouseEvent e){

                if(showNextLevelButton && nextLevelButton.contains(e.getPoint()) && !openingShop){

                    openingShop = true;     // khóa click
                    showNextLevelButton = false;

                    playWinAndOpenShop();
                }
            }
        });
        spawnItems();

        timer = new Timer(16, e -> {
            updateGame();
            repaint();
        });

        timer.start();
    }
    boolean isTooClose(int x, int y, int radius){

        for(game.Item item : items){

            double dx = x - item.x;
            double dy = y - item.y;

            double dist = Math.sqrt(dx*dx + dy*dy);

            // nếu khoảng cách nhỏ hơn tổng bán kính
            if(dist < radius + item.hitRadius + 25){
                return true;
            }
        }

        return false;
    }

    void spawnItems(){

        items.clear();

        int totalGoldValue = 0;
        int totalValue = 0;
        int goldCount = 0;
        int stoneCount = 0;

        int itemTarget = level + 6;

        for(int i=0;i<itemTarget;i++){

            game.Item item;
            int size = 1;
            int spawnRadius = 40;

            int type;

            if(luckyBoost){
                type = rand.nextInt(14);   // map may mắn hơn
            }else{
                type = rand.nextInt(12);
            }

            // xác định loại item trước
            if(type < 5){
                size = 1;
                spawnRadius = 40;
            }
            else if(type < 7){
                size = 2;
                spawnRadius = 60;
            }
            else if(type == 7){
                size = 3;
                spawnRadius = 90;
            }
            else if(type < 9){
                size = 1 + rand.nextInt(2);
                spawnRadius = 70;
            }
            else{
                spawnRadius = 50;
            }

            int x;
            int y;

            do{

                x = rand.nextInt(BASE_WIDTH - 120) + 60;
                y = groundY + SAFE_ZONE + rand.nextInt(BASE_HEIGHT - groundY - SAFE_ZONE - 80);

            }while(isTooClose(x,y,spawnRadius) || !inHookRange(x,y));

            // tạo item
            if(type < 5){

                item = new game.Gold(x,y,size);
                goldCount++;
                totalGoldValue += item.value;
            }
            else if(type < 7){

                item = new game.Gold(x,y,size);
                goldCount++;
                totalGoldValue += item.value;
            }
            else if(type == 7 || (luckyBoost && type == 6)){

                if(luckyBoost){
                    size = 3 + rand.nextInt(2);   // vàng to hơn
                }

                item = new Gold(x,y,size);
                goldCount++;
                totalGoldValue += item.value;

                spawnStoneGuard(x,y);
                spawnGoldCluster(x,y);
            }
            else if(type < 9){

                item = new game.Stone(x,y,size);
                stoneCount++;
            }
            else if(type == 8||type == 9){

                int offsetX = rand.nextInt(120) - 60;
                int offsetY = rand.nextInt(80) - 40;

                item = new game.LuckyBox(x + offsetX, y + offsetY); if(isTooClose(x+offsetX,y+offsetY,40)) continue;
            }

            else if(type == 10|| (luckyBoost && type == 11)){

                item = new game.Diamond(x,y);

                // tạo đá bảo vệ kim cương
                spawnDiamondGuard(x,y);
            }

            else{

                if(isTooClose(x,y,80)) continue;  // tránh spawn chồng lên item khác

                item = new game.TNT(x,y);
            }

            items.add(item);

        }

        // đảm bảo không toàn đá
        if(goldCount < 2){

            int x = rand.nextInt(BASE_WIDTH - 120) + 60;
            int y = groundY + SAFE_ZONE + 200;

            items.add(new game.Gold(x,y,2));
            totalGoldValue += 200;
        }

        // đảm bảo có thể win
        if(totalValue < targetScore * 1.3){

            int x = rand.nextInt(BASE_WIDTH - 120) + 60;
            int y = groundY + SAFE_ZONE + 300;

            items.add(new game.Gold(x,y,3));
        }
    }

    boolean inHookRange(int x, int y){

        int anchorX = player.x;
        int anchorY = groundY + 5;

        double dx = x - anchorX;
        double dy = y - anchorY;

        double dist = Math.sqrt(dx*dx + dy*dy);

        // hook dài tối đa ~ chiều cao map
        if(dist > BASE_HEIGHT - 100) return false;

        // item phải nằm dưới miner
        if(y < groundY + SAFE_ZONE) return false;

        return true;
    }
    void trySpawnStone(int x,int y){

        if(!isTooClose(x,y,60)){
            items.add(new game.Stone(x,y,2));
        }
    }
    void spawnStoneGuard(int x,int y){

        int guardCount = 3;   // số đá bảo vệ

        items.add(new game.Stone(x - 70, y - 80, 2));
        items.add(new game.Stone(x + 70, y - 80, 2));
        items.add(new game.Stone(x, y - 130, 2));
    }
    void spawnDiamondGuard(int x,int y){

        // 2 viên đá bảo vệ kim cương
        items.add(new game.Stone(x - 80, y - 80, 2));
        items.add(new game.Stone(x + 80, y - 80, 2));
    }
    void spawnGoldCluster(int x,int y){

        int count = 3 + rand.nextInt(2);

        for(int i=0;i<count;i++){

            int offsetX = rand.nextInt(220) - 110;
            int offsetY = rand.nextInt(160) - 80;

            int gx = x + offsetX;
            int gy = y + offsetY;

            if(!isTooClose(gx,gy,40) && inHookRange(gx,gy)){
                items.add(new game.Gold(gx,gy,1));
            }
        }
    }
    void updateGame() {
        long now = System.currentTimeMillis();

        if(now - lastTime >= 1000){
            timeLeft--;
            lastTime = now;

            if(timeLeft == 5){
                SoundManager.play("/5s.wav");
            }

            if(timeLeft < 0){
                timeLeft = 0;
            }
        }
        hook.update();
        if(!hook.throwing && !hook.retracting){
            hookReady = true;
        }
        for(game.Item item : items){

            if(!item.collected && hook.checkCollision(item)){

                if(item instanceof game.TNT){

                    if(!hook.retracting){   // ⭐ chỉ nổ khi hook đang ném xuống
                        explodeTNT(item.x,item.y);
                        item.collected = true;
                        hook.retracting = true;
                    }

                }else{

                    if(hook.attachedItem == null)
                    hook.attachItem(item);
                }
                }
            }
        if(explosionTimer > 0){
            explosionTimer--;
        }

        if(hook.returnedItem != null){
            hookReady = true;
            SoundManager.stopLoop();
            game.Item item = hook.returnedItem;

            if(item instanceof game.TNT){

                explodeTNT(item.x,item.y);

                item.collected = true;
                hook.returnedItem = null;
                return;
            }
            if(item instanceof game.LuckyBox){

                openLuckyBox();

                item.collected = true;
                hook.returnedItem = null;
                return;
            }

            if(!item.collected){
                score += item.value;
                SoundManager.play("/Tien.wav");
                item.collected = true;
            }

            hook.returnedItem = null;
        }
        // đủ tiền → hiện nút next level
        if(score >= targetScore && !levelEnded){
            showNextLevelButton = true;
        }

// hết thời gian → tự quyết định thắng thua
        if(timeLeft <= 0 && !levelEnded){

            levelEnded = true;

            if(score >= targetScore){
                playWinAndOpenShop();
            }
            else{
                SoundManager.stopLoop();
                SoundManager.play("/lose.wav");

                new Thread(() -> {

                    try{
                        Thread.sleep(4500);
                    }catch(Exception e){}

                    SwingUtilities.invokeLater(() -> {

                        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                        frame.dispose();

                        new GameFrame(); // quay về menu

                    });

                }).start();

            }
        }
        if(bombTimer > 0){
            bombTimer--;
        }

        if(bombTimer == 0){
            bomb = null;
        }
    }
    void explodeTNT(int x,int y){
        SoundManager.play("/bomb.wav");
        explosionX = x;
        explosionY = y;
        explosionTimer = 30;

        for(game.Item item : items){

            double dx = x - item.x;
            double dy = y - item.y;

            double dist = Math.sqrt(dx*dx + dy*dy);

            if(dist < 120){
                item.collected = true;
            }
        }
    }
    void nextLevel(){
        // dừng âm thanh kéo hook

        level++;
        targetScore += 400 + level*150;

        rand = new Random(level * 999 + 7);

        timeLeft = 35;

        speedBoost = nextLevelSpeedBoost;   // áp dụng potion cho level mới
        nextLevelSpeedBoost = false;

        luckyBoost = nextLevelLuckyBoost;
        nextLevelLuckyBoost = false;

        levelEnded = false;

        spawnItems();
    }
    void explode(){

        if(bombs <= 0) return;

        bombs--;

        SoundManager.play("/bomb.wav");

        bomb = new game.Bomb((int)hook.x,(int)hook.y);
        bombTimer = 20;   // hiệu ứng tồn tại 20 frame

        for(game.Item item : items){

            double dx = hook.x - item.x;
            double dy = hook.y - item.y;

            double dist = Math.sqrt(dx*dx + dy*dy);

            if(dist < 80){

                item.collected = true;

                // nếu hook đang kéo item này thì bỏ nó ra
                if(hook.attachedItem == item){
                    hook.attachedItem = null;
                    hook.retracting = true;
                }
            }
        }
    }
    void playWinAndOpenShop(){

        SoundManager.stopLoop();
        SoundManager.play("/quaman.wav");

        // đợi âm thanh chạy xong
        new Thread(() -> {

            try{
                Thread.sleep(3000); // thời gian âm thanh ~2s
            }catch(Exception e){}

            SwingUtilities.invokeLater(() -> openShop());

        }).start();
    }
    void openShop(){

        timer.stop();
        showNextLevelButton = false;

        ArrayList<game.ShopItem> items = generateShopItems();

        JDialog shop = new JDialog();
        shop.setTitle("SHOP");
        shop.setSize(1280,720);

        JPanel panel = new JPanel(){
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(shopImage,0,0,getWidth(),getHeight(),null);
            }
        };

        panel.setLayout(null);
        shop.setContentPane(panel);

        JLabel mousePos = new JLabel("x:0 y:0");
        mousePos.setForeground(Color.WHITE);
        mousePos.setBounds(10,10,200,30);
        panel.add(mousePos);

        shop.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        shop.setModal(true);

        int[][] slots = {
                {280,180},
                {450,180},
                {680,180},
                {890,180},
                {700,380}
        };

        int i = 0;

        for(game.ShopItem item : items){

            Image img = item.icon.getScaledInstance(80,80,Image.SCALE_SMOOTH);
            JButton btn = new JButton(new ImageIcon(img));

            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setOpaque(false);

            btn.setBounds(slots[i][0],slots[i][1],90,90);

            btn.setToolTipText(item.name + " - " + item.price + "$");

            btn.addActionListener(e -> {

                if(score >= item.price){

                    score -= item.price;

                    if(item.name.equals("Bomb")) bombs++;

                    if(item.name.equals("Lucky Potion"))
                        nextLevelLuckyBoost = true;

                    if(item.name.equals("Speed Potion"))
                        nextLevelSpeedBoost = true;

                    if(item.name.equals("Stone Boost"))
                        stoneBoost = true;

                    btn.setEnabled(false);
                }
                else{
                    JOptionPane.showMessageDialog(shop,"Not enough money!");
                }

            });

            panel.add(btn);

            i++;
        }

        JButton skip = new JButton("SKIP");

        skip.setBounds(560,580,160,60);

        skip.addActionListener(e -> {
            shop.dispose();
            nextLevel();
            timer.start();
            openingShop = false;
        });

        panel.add(skip);

        shop.setLocationRelativeTo(null);
        shop.setVisible(true);
    }
    void openLuckyBox(){

        int rand = (int)(Math.random()*100);

        // 50% túi rác (tiền ít)
        if(rand < 50){

            int money = 20 + (int)(Math.random()*30);
            score += money;
        }

        // 30% nhận mìn
        else if(rand < 80){

            int bombGain = 1 + (int)(Math.random()*2);
            bombs += bombGain;
        }

        // 20% tiền lớn
        else{

            int money = 200 + (int)(Math.random()*200);
            score += money;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // background
        // ===== SKY BACKGROUND =====
        // ===== SINGLE BACKGROUND =====
        g2.drawImage(
                background,
                0,
                0,
                getWidth(),
                getHeight(),
                null
        );

        Graphics2D gameGraphics = (Graphics2D) g2.create();

        double scaleX = (double)getWidth() / BASE_WIDTH;
        double scaleY = (double)getHeight() / BASE_HEIGHT;

        double scale = Math.min(scaleX, scaleY);

        double offsetX = (getWidth() - BASE_WIDTH * scale) / 2;
        double offsetY = (getHeight() - BASE_HEIGHT * scale) / 2;

        gameGraphics.translate(offsetX / scale, offsetY / scale);
        gameGraphics.scale(scale, scale);

        // ===== PLAYER =====
        int minerX = player.x - 70;
        int minerY = groundY - 140;

        gameGraphics.drawImage(
                player.minerImage,
                minerX,
                minerY,
                140,
                110,
                null
        );

        // ===== ROPE (thả từ đít xe) =====
        int ropeAnchorX = player.x;
        int ropeAnchorY = groundY - 45;

        double dx = hook.x - ropeAnchorX;
        double dy = hook.y - ropeAnchorY;

        double distance = Math.sqrt(dx*dx + dy*dy);
        double angle = hook.angle;

        Graphics2D gRope = (Graphics2D) gameGraphics.create();

        gRope.translate(ropeAnchorX, ropeAnchorY);
        gRope.rotate(angle - Math.PI/2);

// vẽ rope kéo dài theo chiều dài hook
        gRope.drawImage(
                ropeImage,
                -25, 0, 50, (int)distance,null
        );

        gRope.dispose();

        // ===== HOOK =====
        Graphics2D gHook = (Graphics2D) gameGraphics.create();

        gHook.translate(hook.x, hook.y);
        gHook.rotate(hook.angle - Math.PI / 2);

        gHook.drawImage(
                hook.hookImage,
                -20,
                -20,
                40,
                40,
                null
        );

        gHook.dispose();

        // ===== ITEMS =====
        for(game.Item item : items){
            if(!item.collected){
                item.draw(gameGraphics);
            }
        }

        // ===== HUD =====
        g2.setFont(new Font("Arial",Font.BOLD,16));

        int hudX = 20;
        int hudY = 20;

        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(hudX, hudY, 220, 140);

        g2.setColor(Color.WHITE);

        g2.drawString("Score: " + score, hudX + 10, hudY + 25);
        g2.drawString("Level: " + level, hudX + 10, hudY + 50);
        g2.drawString("Target: " + targetScore, hudX + 10, hudY + 75);
        if(score >= targetScore){

            // nhấp nháy mỗi 300ms
            if(System.currentTimeMillis() / 300 % 2 == 0){
                g2.setColor(Color.YELLOW);
            }else{
                g2.setColor(Color.ORANGE);
            }

        }else{
            g2.setColor(Color.WHITE);
        }

        g2.drawString("Time: " + timeLeft, hudX + 10, hudY + 100);

        g2.drawImage(bombImage, hudX + 10, hudY + 105, 30, 30, null);
        g2.drawString("x " + bombs, hudX + 45, hudY + 125);

        if(bomb != null){
            bomb.draw(g2);
        }

        if(explosionTimer > 0){

            int size = 120;

            int drawX = (int)explosionX - size/2;
            int drawY = (int)explosionY - size/2;

            gameGraphics.drawImage(
                    explosionImage,
                    drawX,
                    drawY,
                    size,
                    size,
                    null
            );
        }

        gameGraphics.dispose();
        if(showNextLevelButton){

            int btnW = 200;
            int btnH = 55;

            int x = getWidth() - btnW - 30;   // cách mép phải 30px
            int y = 30;                       // cách mép trên 30px

            nextLevelButton.setBounds(x,y,btnW,btnH);

            g2.setColor(new Color(0,150,0));
            g2.fillRoundRect(x,y,btnW,btnH,20,20);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial",Font.BOLD,22));

            g2.drawString("NEXT LEVEL", x+30, y+38);
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_SPACE){

            if(hookReady){

                hook.throwHook();

                hookReady = false;

            }
        }

        if(e.getKeyCode() == KeyEvent.VK_Q){
            explode();
        }

    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
