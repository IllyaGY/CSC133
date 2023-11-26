package com.gamecodeschool.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Level {

    private int snakeLength;
    private int levelCounter;

    private int blockSize;
    private Bitmap mBitmapObject;

    private HashMap<Integer, Integer> obstacleCoords;

    private Point screen;

    private HashMap<Integer, Integer> levelSpeed = new HashMap<>();

    {
        for (int i = 1; i < 4; i++)
            levelSpeed.put(i, i * 5);
    }

    private int currentLevel;

    private boolean gameOver; // New flag to indicate game over

    public Level() {
        this.currentLevel = 1;
        this.gameOver = false; // Initialize the game over flag
    }

    private HashMap<Integer, Integer> levelColor = new HashMap<>();

    {
        levelColor.put(1, Color.argb(255, 26, 128, 182));
        levelColor.put(2, Color.argb(255, 26, 182, 128));
        levelColor.put(3, Color.argb(255, 0, 0, 0));
    }

    private HashMap<Integer, Integer> levelObstacles = new HashMap<>();

    {
        for (int i = 1; i < 4; i++) {
            levelObstacles.put(i, 5 - i);
        }
    }

    Level(Context context, Point screen, int blockSize) {
        this.blockSize = blockSize;
        this.screen = screen;
        this.snakeLength = 0;
        this.levelCounter = 1;
        this.mBitmapObject = BitmapFactory.decodeResource(context.getResources(), R.drawable.body);
        this.mBitmapObject = Bitmap.createScaledBitmap(mBitmapObject, blockSize, blockSize, false);
    }

    public void updateSnakeLength(int snakeLength) {
        this.snakeLength = snakeLength;
    }

    public int getSpeed(int level) {
        return levelSpeed.get(level);
    }

    public int getOldSnakeLength() {
        return snakeLength;
    }

    public int getLevel() {
        return levelCounter;
    }

    public void updateLevel() {
        levelCounter++;
    }

    public int updateSpeed(int snakeLength) {
        if (levelCounter < 4) {
            if (snakeLength - this.snakeLength >= 5) {
                return levelSpeed.get(levelCounter);
            }
        }
        return 0;
    }

    public int updateBGColor() {
        if (levelCounter < 4)
            return levelColor.get(levelCounter);
        else if (levelCounter < 7)
            return levelColor.get(levelCounter - 3);
        return 1;
    }

    public boolean isDead() {
        levelCounter = 1;
        snakeLength = 1;
        obstacleCoords.clear();
        setGameOver(true); // Set the game over flag
        if (gameOver=true){
            return true;
        }return false;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void locationChecker(ArrayList<Point> segmentLocation, Point appleLocation) {
        for (int i = 0; i < segmentLocation.size(); i++) {
            if (obstacleCoords.containsKey(segmentLocation.get(i).x)) {
                if (obstacleCoords.get(segmentLocation.get(i).x) == segmentLocation.get(i).y) {
                    obstacleCoords.remove(segmentLocation.get(i).x);
                    continue;
                }
            }
            if (obstacleCoords.containsKey(appleLocation.x)) {
                if (obstacleCoords.get(appleLocation.x) == appleLocation.y) obstacleCoords.remove(appleLocation.x);
            }
        }
    }

    public void checkDirHit(Point objectCoords) {
        for (int key : obstacleCoords.keySet()) {
            if (key == objectCoords.x && obstacleCoords.get(key) == objectCoords.y)
                obstacleCoords.remove(key);
        }
    }

    public void randomObstacles() {
        gameOver = false;
        if (!gameOver) { // Only generate obstacles if the game is not over
            obstacleCoords = new HashMap<>();
            Random random = new Random();
            int level = levelCounter;
            if (levelCounter > 3) level = levelCounter - 3;
            int newX = (screen.x / levelObstacles.get(level));
            int newY = (screen.y / levelObstacles.get(level));
            for (int i = 1; i < level * 6; i++) {
                obstacleCoords.put((random.nextInt(newX - 1) + 1) * levelObstacles.get(level),
                        (random.nextInt(newY - 1) + 1) * levelObstacles.get(level));
            }
        }
    }

    public HashMap<Integer, Integer> getObstacleCoords() {
        return obstacleCoords;
    }

    public void draw(Canvas canvas, Paint paint) {
        gameOver = false;
        if (!gameOver) { // Draw obstacles only if the game is not over
            if (obstacleCoords != null && !obstacleCoords.isEmpty()) {
                for (int key : obstacleCoords.keySet()) {
                    canvas.drawBitmap(mBitmapObject, key * blockSize, obstacleCoords.get(key) * blockSize, paint);
                }
            }
        }
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}
