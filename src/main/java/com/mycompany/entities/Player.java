package com.mycompany.entities;

import com.mycompany.main.Game;
import com.mycompany.main.GameState;
import com.mycompany.main.Sound;
import com.mycompany.world.Camera;
import com.mycompany.world.World;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Player extends Entity{
    
    public boolean right,left,up,down;
    public int right_dir = 0;
    public int left_dir = 1;
    public int dir = 0;
    
    public int speed = 2;
    
    private int frames = 0;
    public int maxFrames = 5;
    public int maxIndex = 3;
    public int index = 0;
    
    private boolean moved = false;
    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;
    private BufferedImage playerDamage;
    
    private double life = 100;
    public final int lifeMax = 100;
    
    private int ammo = 0;
    
    private boolean isDamaged = false;
    
    private int damageFrames = 0;
    
    private boolean hasGun = false;
    private boolean isShooting = false;

    public boolean isIsShooting() {
        return isShooting;
    }

    public void setIsShooting(boolean isShooting) {
        this.isShooting = isShooting;
    }

    public boolean isIsDamaged() {
        return isDamaged;
    }

    public void setIsDamaged(boolean isDamaged) {
        this.isDamaged = isDamaged;
    }

    public int getAmmo() {
        return ammo;
    }

    private void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public double getLife() {
        return life;
    }

    private void setLife(double life) {
        this.life = life;
    }
    
    public void DiminuirLife(double dano){
        Sound.hurtEffect.play(5000);
        this.setLife(this.getLife() - dano);
        this.setIsDamaged(true);
        
        if(this.getLife() <= 0){
            Game.gameState = GameState.GAME_OVER;
        }
    }
    
    public void DiminuirAmmo(int ammo){
        this.setAmmo(this.getAmmo() - ammo);
        
        if(this.getAmmo() == 0){
            this.setAmmo(0);
        }
    }
    
    public void AumentarLife(double life){
        this.setLife(this.getLife() + life);          
        
        if (this.getLife() > this.lifeMax){
            this.setLife(this.lifeMax);
        }
    }
    
    public void AumentarAmmo(int ammo){
        this.setAmmo(this.getAmmo() + ammo);     
    }

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
                
                rightPlayer = new BufferedImage[4];
                leftPlayer = new BufferedImage[4];
                playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);
                
                for(int i=0;i < 4; i++){
                   rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16); 
                }
                
                for(int i=0;i < 4; i++){
                   leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16); 
                }
                
                
	}
        
        public void tick(){
            moved = false;
            
            if(right && World.IsPlaceFree(x+speed, y)){
                moved = true;
                dir = right_dir;
                x += speed;
            }else if(left && World.IsPlaceFree(x-speed, y)){
                moved = true;
                dir = left_dir;
                x -= speed;
            }
            
            if(up && World.IsPlaceFree(x, y-speed)){
                moved = true;
                y -= speed;
            }else if(down && World.IsPlaceFree(x, y+speed)){
                moved = true;
                y += speed;
            }
            
            if(moved){
                frames++;
                if(frames == maxFrames){
                    frames = 0;
                    index++;
                    if(index > maxIndex){
                        index = 0;
                    }
                }
            }
            
            this.checkCollision();
            
            if(this.isDamaged){
                this.damageFrames++;
                if(this.damageFrames >= 10){
                   this.isDamaged = false; 
                }
            }
            
            if(isShooting && hasGun && ammo > 0){
               isShooting = false;
               ammo--;
               
               int direction_x = 0; 
               int offset_x = 0;
               int offset_y = 6;
               
               if(dir == right_dir){
                    offset_x = 18;
                    direction_x = 1;
               }else{
                    offset_x = -8;
                    direction_x = -1;
               }
               
               BulletShoot bullet = new BulletShoot(this.getX() + offset_x, 
                        this.getY() + offset_y, 3, 3,
                       null,direction_x,0);
               Game.bulletsShoot.add(bullet);
               Game.entities.add(bullet);
            }
            
            Camera.x =  Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
            Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);;
        }
        
        public void checkCollision(){
            checkCollisionLifePack();
            checkCollisionAmmo();
            checkCollisionGun();
        }
        
        public void checkCollisionLifePack(){
            for(int i = 0; i < Game.entities.size(); i++){
                Entity atual = Game.entities.get(i);
                
                if(atual instanceof LifePack){
                    if(Entity.isColidding(this,atual)){
                      this.AumentarLife(((LifePack) atual).getLife());  
                      Game.entities.remove(atual);
                    }
                }      
            }
        }
        
        public void checkCollisionAmmo(){
            for(int i = 0; i < Game.entities.size(); i++){
                Entity atual = Game.entities.get(i);
                
                if(atual instanceof Bullet){
                    if(Entity.isColidding(this,atual)){
                      this.AumentarAmmo(((Bullet) atual).getAmmo());  
                      Game.entities.remove(atual);
                    }
                }      
            }
        }
        
        public void checkCollisionGun(){
            for(int i = 0; i < Game.entities.size(); i++){
                Entity atual = Game.entities.get(i);
                
                if(atual instanceof Weapon){
                    if(Entity.isColidding(this,atual)){
                        this.hasGun = true;
                        Game.entities.remove(atual);
                    }
                }      
            }
        }
        
        public void render(Graphics g){
           // animacao de dano 
           if(this.isDamaged){
               g.drawImage(playerDamage, this.getX()-Camera.x,this.getY()-Camera.y, null);
               if(this.hasGun && dir == right_dir){
                g.drawImage(Entity.GUN_DAMAGE_RIGHT, this.getX() + 8 - Camera.x, this.getY() - Camera.y, null);
               }
               if(this.hasGun && dir == left_dir){
                g.drawImage(Entity.GUN_DAMAGE_LEFT, this.getX() + 8 - Camera.x, this.getY() - Camera.y, null);
               }
               return;
           } 
            
           // animacao de movimentos
           if(dir == right_dir){
              g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
              if(this.hasGun){
                g.drawImage(Entity.GUN_RIGHT, this.getX() + 8 - Camera.x, this.getY() - Camera.y, null);
              }
           }
           
           if(dir == left_dir){
              g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
              if(this.hasGun){
                g.drawImage(Entity.GUN_LEFT, this.getX() - 8 - Camera.x, this.getY() - Camera.y, null);
              }
           }
                    
           // criar animacoes para cima e baixo
           
        }
}