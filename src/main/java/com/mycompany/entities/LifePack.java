package com.mycompany.entities;

import java.awt.image.BufferedImage;

public class LifePack extends Entity{
    
    private double life = 5;

    public double getLife() {
        return life;
    }

    public void setLife(double life) {
        this.life = life;
    }
    
    public LifePack(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }
    
}
