package com.mycompany.entities;

import com.mycompany.main.Game;
import com.mycompany.world.World;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BulletShoot extends Entity {
    
    private int direction_x;
    private int direction_y;
    private final double speed = 4;
    private int dano;

    public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, int direction_x, int direction_y) {
        super(x, y, width, height, sprite);
        this.direction_x = direction_x;
        this.direction_y = direction_y;
        this.dano = Game.rand.nextInt(1,Enemy.getMaxLife() + 1);
    }
    
    @Override
    public void tick(){  
        int xnext = x + direction_x*(int)speed;
        int ynext = y + direction_y*(int)speed;

        
        if(!World.IsPlaceFree(xnext,ynext)){
            Game.entities.remove(this);
            Game.bulletsShoot.remove(this);
            return;
        }      
        
        x=xnext;
        y=ynext;
    }
    
    @Override
    public void render(Graphics g){
        g.setColor(Color.red);
        g.fillRect(this.getXCamera(), this.getYCamera(), this.getWidth(), this.getHeight());
    }
    
    public int getDano() {
        return dano;
    }
}
