package game;
import javax.swing.ImageIcon;
import java.awt.Image;

public class Hook {

    Player player;

    double length = 40;

    boolean throwing = false;
    boolean retracting = false;

    int hookRadius = 12;
    GamePanel panel;
    double x;
    double y;

    double angle = Math.PI / 2;

    double swingRange = Math.toRadians(80);

    double minAngle = Math.PI / 2 - swingRange;
    double maxAngle = Math.PI / 2 + swingRange;

    double angleSpeed = 0.015;
    int direction = 1;

    Item attachedItem = null;
    Item returnedItem = null;
    Image hookImage = new ImageIcon("src/hook.png").getImage();

    public Hook(Player player, GamePanel panel){
        this.player = player;
        this.panel = panel;
    }

    public void update() {

        if (!throwing) {

            angle += angleSpeed * direction;

            if (angle >= maxAngle) {
                angle = maxAngle;
                direction = -1;
            }

            if (angle <= minAngle) {
                angle = minAngle;
                direction = 1;
            }

        } else {

            if (!retracting) {

                if(panel.speedBoost){
                    length += 28;   // ném dây nhanh hơn khi có potion
                }else{
                    length += 16;   // tốc độ bình thường
                }

                if (length > GamePanel.BASE_HEIGHT) {
                    retracting = true;
                }
            }
            else {

                double retractSpeed;

// nếu không dính gì → kéo siêu nhanh
                if (attachedItem == null) {
                    retractSpeed = 20;
                }

// nếu có speed potion → kéo nhanh bất kể vật gì
                else if (panel.speedBoost) {
                    retractSpeed = 15;
                }

// bình thường
                else {

                    retractSpeed = 8;

                    if (attachedItem instanceof Stone) {
                        retractSpeed = 1.5;
                    }

                    if (attachedItem instanceof Gold && attachedItem.value >= 200) {
                        retractSpeed = 2;
                    }

                    if (attachedItem instanceof Gold && attachedItem.value >= 100) {
                        retractSpeed = 4;
                    }
                }

                length -= retractSpeed;
                if (length <= 40) {
                    x = player.x;
                    y = GamePanel.groundY + 5;
                    length = 40;

                    if (attachedItem != null) {
                        returnedItem = attachedItem;
                        attachedItem = null;
                    }

                    throwing = false;
                    retracting = false;
                }
            }
        }

        // tính vị trí hook (PHẢI nằm trong update)
        // điểm neo dây (đít xe)
        int ropeAnchorX = player.x;
        int ropeAnchorY = GamePanel.groundY - 45;

// tính vị trí hook
        x = ropeAnchorX + Math.cos(angle) * length;
        y = ropeAnchorY + Math.sin(angle) * length;

        if (attachedItem != null) {

            // nếu item đã bị phá (bom) thì bỏ nó khỏi hook
            if (attachedItem.collected) {
                attachedItem = null;
                retracting = true;
            } else {
                attachedItem.x = (int) x;
                attachedItem.y = (int) y;
            }
        }
    }
    public void throwHook(){

        if(!throwing && !retracting){

            attachedItem = null;   // reset item cũ
            returnedItem = null;

            length = 40;

            x = player.x;
            y = GamePanel.groundY + 5;

            throwing = true;
            retracting = false;
        }

    }

    public boolean checkCollision(Item item){

        double dx = x - item.x;
        double dy = y - item.y;

        double dist = Math.sqrt(dx*dx + dy*dy);

        return dist < (hookRadius + item.hitRadius);
    }

    public void attachItem(Item item){

        if(attachedItem == null && throwing){

            attachedItem = item;
            retracting = true;

            if(item instanceof Gold){
                SoundManager.play("/Gold.wav");
            }

            if(item instanceof Stone){
                SoundManager.play("/keoda.wav");
            }
            else if(item instanceof Diamond){
                SoundManager.play("/Gold.wav");
            }
            else if(item instanceof LuckyBox){
                SoundManager.play("/Gold.wav");
            }

        }
    }

}