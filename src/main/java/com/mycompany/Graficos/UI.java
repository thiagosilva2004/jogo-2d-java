package com.mycompany.Graficos;

import com.mycompany.main.Game;
import java.awt.Color;
import java.awt.Graphics;

public class UI {
    
    public void render(Graphics g){
       g.setColor(Color.RED);
       g.fillRect(8, 4, 50, 8);
        
       g.setColor(Color.GREEN);
       g.fillRect(8, 4, (int)((Game.player.getLife()/Game.player.lifeMax)*50), 8);
    }
    
}
