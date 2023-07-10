package com.mycompany.main;

public enum GameState {
    
    MENU("menu"),
    NORMAL("normal"),
    GAME_OVER("game_over");
    
    private String state;
    
    GameState(String state){
        this.state = state;
    }
    
    public String getState(){
        return this.state;
    }
    
}
