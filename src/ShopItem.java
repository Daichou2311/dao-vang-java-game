package game;

import java.awt.Image;

public class ShopItem {

    String name;
    int price;
    Image icon;

    public ShopItem(String name,int price,Image icon){
        this.name = name;
        this.price = price;
        this.icon = icon;
    }
}