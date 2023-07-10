package com.mycompany.entities;

import com.mycompany.main.Game;
import com.mycompany.world.Camera;
import com.mycompany.world.World;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Enemy extends Entity{
    
    private final int speed = 1;
    
    private int frames = 0;
    public int maxFrames = 20;
    public int maxIndex = 1;
    public int index = 0;
    private static final int maxLife = 10;

    private int life;
    private boolean isDamage = false;
    private int damageFrames = 0;
        
    private BufferedImage[] sprites;
    
    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, null);
        
        sprites = new BufferedImage[2];
        sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
        sprites[1] = Game.spritesheet.getSprite(128, 16, 16, 16);
        
        this.life = Game.rand.nextInt(1,maxLife + 1);
    }
    
    public void render(Graphics g){
        // animacao de dano 
        if(this.GetIsDamage()){
            g.drawImage(Entity.ENEMY_FEEDBACK_EN, this.getXCamera(),this.getYCamera(), null);
            return;
        } 
        
        g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
    }
    
    public void tick(){
        
        if(this.GetIsDamage()){
            this.damageFrames++;
            if(this.damageFrames >= 10){
               this.setIsDamage(false); 
            }
        }
        
        if (!checkColliding()){
            return;
        }
        
        if(Game.rand.nextInt(100) < 50){
            return;
        }
        
        
        if(x < Game.player.getX() && World.IsPlaceFree(x+speed, y)
               && !isColidding(x+speed, y)){
            x+=speed;
        }else if(x > Game.player.getX() && World.IsPlaceFree(x-speed, y)
                && !isColidding(x-speed, y)){
           x-=speed; 
        }
        
        if(y < Game.player.getY() && World.IsPlaceFree(x, y+speed) 
                && !isColidding(x, y+speed)){
            y+=speed;
        }else if(y > Game.player.getY() && World.IsPlaceFree(x, y-speed)
                && !isColidding(x, y-speed)){
           y-=speed; 
        }
        
        frames++;
        if(frames == maxFrames){
            frames = 0;
            index++;
            if(index > maxIndex){
                index = 0;
            }
        }
    }
    
    public void diminuirVida(int dano){
        this.setLife(this.getLife() - dano);
        this.setIsDamage(true);
        
        if(this.getLife() <= 0){
            Game.entities.remove(this);
            Game.enemies.remove(this);
        } 
    }
    
    // checha todas as colisoes e retorna se continua o movimento
    public boolean checkColliding(){ 
       checkCollidingWithBullet(); 
       
        if(isColiddingWithPlayer()){
            Game.player.DiminuirLife(Game.rand.nextInt(3));
            return false;
        }
        
        return true;
    }
    
    public void checkCollidingWithBullet(){
        for(int i = 0; i < Game.entities.size(); i++){
            Entity atual = Game.entities.get(i);
            if(atual instanceof BulletShoot){
              if(Entity.isColidding(this,atual)){              
                Game.bulletsShoot.remove(atual);
                Game.entities.remove(atual);
                this.diminuirVida(((BulletShoot) atual).getDano());
              }    
            }           
        }
    }
    
    public boolean isColiddingWithPlayer(){
        Rectangle enemyCurrent = new Rectangle(this.getX(), this.getY(),this.getWidth(), this.getHeight());
        Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(),Game.player.getWidth(),Game.player.getHeight());        
        
        return enemyCurrent.intersects(player);
    }
    
    public boolean isColidding(int xnext, int ynext){
        Rectangle enemyCurrent = new Rectangle(xnext, ynext, World.TILE_SIZE, World.TILE_SIZE);
        for(int i = 0; i < Game.enemies.size(); i++){
            Enemy enemy = Game.enemies.get(i);
            
            if(enemy == this){
                continue;
            }
            
            Rectangle targetEnemy = new Rectangle(enemy.getX(), enemy.getY(), World.TILE_SIZE, World.TILE_SIZE);
            if(enemyCurrent.intersects(targetEnemy)){
                return true;
            }
        }
        
        return false;
    }
    
    public static int getMaxLife() {
        return maxLife;
    }
    
    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }
    
    public boolean GetIsDamage() {
        return isDamage;
    }

    public void setIsDamage(boolean isDamage) {
        this.isDamage = isDamage;
    }
}
