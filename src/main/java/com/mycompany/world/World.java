package com.mycompany.world;

import com.mycompany.entities.Bullet;
import com.mycompany.entities.Enemy;
import com.mycompany.entities.Entity;
import com.mycompany.entities.LifePack;
import com.mycompany.entities.Weapon;
import com.mycompany.main.Game;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class World {
    
    public static Tile[] tiles; 
    public static int WIDTH,HEIGHT;
    public static final int TILE_SIZE = 16;
   
    public World(String path){
        try{          
            BufferedImage map = ImageIO.read(new File(path));
            int[] pixels = new int[map.getWidth() * map.getHeight()];
            
            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();
            
            tiles = new Tile[WIDTH * HEIGHT];
                    
            map.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
            
            for(int xx = 0; xx < WIDTH; xx++){
                for(int yy = 0; yy < HEIGHT; yy++){
                   int pixelAtual = pixels[xx + (yy*WIDTH)];
                   
                   tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
                   
                    switch (pixelAtual) {
                        case 0xFF000000:
                            // chao
                            tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
                            break;
                        case 0xFFFFFFFF:
                            // parede
                            tiles[xx + (yy * WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_WALL);
                            break;
                        case 0xFF0026FF:
                            // player
                            Game.player.setX(xx*16);
                            Game.player.setY(yy*16);
                            break;
                        case 0xFFFF0000:
                            // enemy
                            Enemy enemy = new Enemy(xx*16, yy*16, 16,16, Entity.ENEMY_EN);
                            Game.entities.add(enemy);
                            Game.enemies.add(enemy);
                            break;   
                        case 0xFFFF6A00:
                            // weapon
                            Game.entities.add(new Weapon(xx*16, yy*16, 16,16, Entity.WEAPON_EN));
                            break;   
                        case 0xFFFF7F7F:
                            // life pack
                            Game.entities.add(new LifePack(xx*16, yy*16, 16,16, Entity.LIFEPACK_EN));
                            break;   
                        case 0xFFFFD800:
                            // bullet
                            Game.entities.add(new Bullet(xx*16, yy*16, 16,16, Entity.BULLET_EN));
                            break;   
                        default:
                            // chao
                            tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
                            break;
                    }
                   
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void render(Graphics g){
        int xStart = Camera.x / Game.player.getWidth();
        int yStart = Camera.y / Game.player.getHeight();
        
        int xFinal = xStart + Game.WIDTH / Game.player.getWidth();
        int yFinal = yStart + Game.HEIGHT / Game.player.getHeight();
        
        for(int xx = xStart; xx <= xFinal; xx++){
            for(int yy = yStart; yy <= yFinal; yy++){
                if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT){
                    continue;
                }
                
                Tile tile = tiles[xx + (yy*WIDTH)];
                tile.render(g);
            }
        }
    }
    
    public static boolean IsPlaceFree(int xNext, int yNext){
        int x1 = xNext / TILE_SIZE;
        int y1 = yNext / TILE_SIZE;
        
        int x2 = (xNext+TILE_SIZE-1) / TILE_SIZE;
        int y2 = yNext / TILE_SIZE;
        
        int x3 = xNext / TILE_SIZE;
        int y3 = (yNext+TILE_SIZE-1) / TILE_SIZE;
        
        int x4 = (xNext+TILE_SIZE-1) / TILE_SIZE;
        int y4 = (yNext+TILE_SIZE-1) / TILE_SIZE;
        
        return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
                (tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) || 
                (tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) || 
                (tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
    }
}
