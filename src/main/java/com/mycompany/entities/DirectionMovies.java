package com.mycompany.entities;

public enum DirectionMovies {
    
    RIGHT("right"),
    LEFT("left"),
    UP("up"),
    DOWN("down");
    
    private String direction;
    
    DirectionMovies(String state){
        this.direction = state;
    }
    
    public String getDirection(){
        return this.direction;
    }
    
}
