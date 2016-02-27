package com.example.jimmyle.pacmanandroid;

public class Globals{
    private static Globals instance;

    // Global variable
    private int highScore;

    // Restrict the constructor from being instantiated
    private Globals(){}

    public void setHighScore(int newScore){
        this.highScore =newScore;
    }
    public int getHighScore(){
        return this.highScore;
    }

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }
}