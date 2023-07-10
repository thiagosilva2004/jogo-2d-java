package com.mycompany.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    
    private List<String> options;
    
    private int currentOption = 0;
    
    private boolean up,down,enter = false;

    public Menu() {
        this.options = new ArrayList<>();
        
        options.add("novo jogo");
        options.add("carregar jogo");
        options.add("sair");
    }
    
    public void addNewMenuOptions(String newOptions){
        if (options.contains(newOptions)){
           return; 
        }
        
        options.add(newOptions);
    }
    
    public void removeOptionsMenu(String optionsMenu){
        if (!options.contains(optionsMenu)){
           return; 
        }
        
        options.remove(optionsMenu);
    }
    
    private void ChangeIndexOption(boolean isUp){
        if(isUp){
          up = false;  
          currentOption--;  
        }else{
          down = false;
          currentOption++;  
        }
        
        if(currentOption > options.size() - 1){
           currentOption = 0;
        }else if(currentOption < 0){
          currentOption = options.size() - 1; 
        }
    }
    
    public void tick(){
        if(up){
            ChangeIndexOption(true);
        }
        if(down){
            ChangeIndexOption(false);
        }
        if(enter){
           enter = false;
           
           if("novo jogo".equals(options.get(currentOption))){
                Game.IniciarGame();
           }else if("carregar jogo".equals(options.get(currentOption))){
               
           }else if("sair".equals(options.get(currentOption))){
             Game.isRunning = false;
           }else if("Continuar Jogo".equals(options.get(currentOption))){
             Game.gameState = GameState.NORMAL;  
           }    
        }
    }

    public boolean isEnter() {
        return enter;
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }
    
    public void render(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
        g.setColor(Color.red);
        g.setFont(new Font("arial", Font.BOLD, 36));
        g.drawString("Jogo 2D", (Game.WIDTH*Game.SCALE) / 2 - 100, (Game.HEIGHT*Game.SCALE) / 2 - 100);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial", Font.BOLD, 24));
        
        for (int i = 0; i < options.size(); i++) {
           g.drawString(options.get(i), ((Game.WIDTH*Game.SCALE) / 2) - 100, ((Game.HEIGHT*Game.SCALE) / 2) + i * 50); 
        }
        
        g.drawString(">", ((Game.WIDTH*Game.SCALE) / 2) - 200,((Game.HEIGHT*Game.SCALE) / 2) + currentOption * 50);
    }
    
}
