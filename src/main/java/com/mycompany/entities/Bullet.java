package com.mycompany.entities;

import java.awt.image.BufferedImage;

public class Bullet extends Entity{
    
    private int ammo = 10;

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }
    
    public Bullet(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }
    
}
