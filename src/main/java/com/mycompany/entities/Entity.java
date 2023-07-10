package com.mycompany.entities;

import com.mycompany.main.Game;
import com.mycompany.world.Camera;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {
    
        public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(94, 0, 16, 16);
        public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(128, 0, 16, 16);
        public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(94, 16, 16, 16);
        public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(128, 16, 16, 16);
        public static BufferedImage ENEMY_FEEDBACK_EN = Game.spritesheet.getSprite(144, 16, 16, 16);
        public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprite(128, 0, 16, 16);
        public static BufferedImage GUN_LEFT = Game.spritesheet.getSprite(144, 0, 16, 16);
        public static BufferedImage GUN_DAMAGE_RIGHT = Game.spritesheet.getSprite(8, 32, 16, 16);
        public static BufferedImage GUN_DAMAGE_LEFT = Game.spritesheet.getSprite(16, 32, 16, 16);
        
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected BufferedImage sprite;

	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void tick() {

	}
        
        public void setX(int x){
            this.x = x;
        }
        
        public void setY(int y) {
            this.y = y;
        }
   

	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
        
        public static boolean isColidding(Entity e1, Entity e2){
            Rectangle rectE1 = new Rectangle(e1.getX(), e1.getY(), e1.getWidth(), e1.getHeight());
            Rectangle rectE2 = new Rectangle(e2.getX(), e2.getY(), e2.getWidth(), e2.getHeight());        
        
            return rectE1.intersects(rectE2);
        }
        
        // retorna a posicao x no mapa corigida pela camera
        public int getXCamera(){
            return this.getX() - Camera.x;
        }
        
        // retorna a posicao y no mapa corigida pela camera
        public int getYCamera(){
            return this.getY() - Camera.y;
        }
}
