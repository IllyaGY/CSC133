package com.gamecodeschool.snake;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameRenderer extends SurfaceView {
    private Canvas canvas;
    private Paint paint;

    private Snake snake;
    private Apple apple;
    private Level level;
    private int score;
    private boolean isPaused;
    private boolean isGameOver;
    private Context context;

    public GameRenderer(Context context, SurfaceHolder SurfaceHolder, Paint Paint, Snake Snake, Apple Apple) {
        super(context);
        this.context = context;
        paint = new Paint();
        isGameOver = false; // Initialize the flag
    }

    public Canvas draw(int Achieved, int begin, boolean isGameOver, int score, boolean isPaused, Canvas canvas, Paint paint, Snake snake, Apple apple, Level level, int backgroundColor, Resources resources) {
        this.canvas = canvas;
        this.score = score;
        this.isPaused = isPaused;
        this.paint = paint;
        this.isGameOver = isGameOver;
        // Check and lock canvas
        // Fill the screen with a background color
        canvas.drawColor(backgroundColor);

        if (this.isGameOver) {
            drawGameOverScreen();
        } else if (isPaused && begin == 0) {
            drawIntroScreen();
        } else if(score%5==0 && Achieved == 1){
            drawAchievementScreen();
        }
        else if (isPaused) {
            drawPauseScreen();
        } else {
            // Draw the score
            paint.setColor(Color.WHITE);
            paint.setTextSize(120);
            canvas.drawText("Score: " + score, 30, 140, paint);

            // Draw the apple and the snake
            apple.draw(canvas, paint);
            snake.draw(canvas, paint);
            level.draw(canvas, paint);

            // Draw some text while paused

        }
        return canvas;
    }


    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    private void drawGameOverScreen() {
        paint.setColor(Color.argb(160, 160, 24, 24)); // Semi-transparent black background
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
        paint.setColor(Color.RED);
        paint.setTextSize(250);
        canvas.drawText("Game Over", 200, 400, paint);
        canvas.drawText("Score: " + score, 200, 700, paint);
        paint.setColor(Color.WHITE);
        paint.setTextSize(120);
        canvas.drawText(context.getResources().getString(R.string.tap_to_play), 200, 900, paint);
    }

    private void drawPauseScreen() {
        paint.setColor(Color.argb(100, 0, 0, 0)); // Semi-transparent black background
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

        paint.setColor(Color.YELLOW);
        paint.setTextSize(180);
        canvas.drawText("Resume, Press R", 200, 500, paint);
        paint.setTextSize(160);
        canvas.drawText("New Game, Press N", 200, 700, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(120);
        canvas.drawText(context.getResources().getString(R.string.tap_to_play), 200, 900, paint);
    }

    private void drawIntroScreen() {
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

        paint.setColor(Color.GREEN);
        paint.setTextSize(60);
        canvas.drawText(" \uD83D\uDC0D \uD83C\uDF4E LUCKY SNAKE GAME \uD83C\uDF4E \uD83D\uDC0D", 700, 100, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(53);

        canvas.drawText("In the LUCKY Snake Game, you're a snake growing longer with each lucky apple you eat. " , 100, 200, paint);
        canvas.drawText( "Navigate skillfully through obstacles, you unlock a new achievment every 5 points", 100, 250, paint);
        canvas.drawText("But GOOD LUCK, the apple will respawn randomly after new level unlocks!", 100, 300, paint);
        canvas.drawText("How to Play: ", 1000, 400, paint);
        canvas.drawText("Use arrow keys to move the snake tap W:⬆️ A:⬅️ S:⬇️ D:➡️", 100, 500, paint);
        canvas.drawText("Eat apples to grow and gain points:  \uD83C\uDF4E \uD83C\uDF4E \uD83C\uDF4E \uD83C\uDF4E", 100, 600, paint);
        canvas.drawText("Avoid hitting the walls or yourself:  ☠️☠️☠️☠️", 100, 700, paint);
        canvas.drawText("Use P to pause and C to continue", 100, 800, paint);

        paint.setColor(Color.GREEN);
        paint.setTextSize(60);
        canvas.drawText("\uD83D\uDC0DTAP ANYWHERE TO PLAY\uD83D\uDC0D", 820, 900, paint);

    }
    private void drawAchievementScreen() {
        paint.setColor(Color.argb(160, 160, 24, 24)); // Semi-transparent black background
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
        paint.setColor(Color.RED);
        paint.setTextSize(150);
        canvas.drawText("New Level Unlocked", 200, 400, paint);
        paint.setColor(Color.GREEN);
        canvas.drawText("Bonus = +1 point", 200, 550, paint);
        paint.setColor(Color.WHITE);
        paint.setTextSize(120);
        canvas.drawText("Press C to continue the game.", 200, 700, paint);
    }
}
