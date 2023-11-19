package com.gamecodeschool.snake;

import android.graphics.Color;

import java.util.HashMap;

public class Level {

    private int snakeLength;
    private int levelCounter;

    //Total of 4 levels with their own movement speed
    HashMap<Integer, Integer> levelSpeed = new HashMap<Integer, Integer>();

    {
        for(int i = 1; i < 4; i++)
            levelSpeed.put(i, i*5);
    }

    HashMap<Integer, Integer> levelColor = new HashMap<Integer, Integer>();
    {
        levelColor.put(1, Color.argb(255, 26, 128, 182));
        levelColor.put(2, Color.argb(255, 26, 182, 128));
        levelColor.put(3, Color.argb(255, 0, 0, 0));
        levelColor.put(4, Color.argb(255, 26, 182, 128));
    }
    Level(int snakeLength){
        this.snakeLength = snakeLength;
        levelCounter = 1;

    }
    public void updateSnakeLength(int snakeLength) {
        this.snakeLength = snakeLength;
    }

    public int getSpeed(int level){
        return levelSpeed.get(level);
    }
    public int getOldSnakeLength(){
        return snakeLength;
    }
    public int getLevel(){
        return levelCounter;
    }

    public int updateSpeed(int snakeLength){
       if(levelCounter+1<4) {
            if (snakeLength - this.snakeLength>=5) {;
                return levelSpeed.get(levelCounter++);
            }
        }
        return 0;

    }
    public int updateBGColor(){
        return levelColor.get(levelCounter);
    }
    public void isDead(){
        levelCounter = 1;
        snakeLength = 1;
    }

}
