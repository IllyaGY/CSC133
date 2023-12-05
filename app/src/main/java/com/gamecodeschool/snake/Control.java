package com.gamecodeschool.snake;

import android.view.KeyEvent;
import android.view.MotionEvent;

public class Control {
    private SnakeGame snakeGame;

    Control(SnakeGame snakeGame) {
        this.snakeGame = snakeGame;
    }

    public Snake.Heading keyUpdater(Snake mSnake, Snake.Heading heading, int keyCode) {
        boolean paused = snakeGame.isPaused();
        if (snakeGame.isPaused()){
            switch (keyCode) {
                case KeyEvent.KEYCODE_R:
                    // Resume the game
                    snakeGame.setPaused(false);
                    break;
                case KeyEvent.KEYCODE_N:
                    // Start a new game
                    snakeGame.newGame();
                    break;
            }

        } else{
            switch (keyCode) {
                case KeyEvent.KEYCODE_W:
                    if (mSnake.getHeading() != Snake.Heading.DOWN)
                        return Snake.Heading.UP;
                    else
                        return heading;
                case KeyEvent.KEYCODE_D:
                    if (mSnake.getHeading() != Snake.Heading.LEFT)
                        return Snake.Heading.RIGHT;
                    else
                        return heading;
                case KeyEvent.KEYCODE_S:
                    if (mSnake.getHeading() != Snake.Heading.UP)
                        return Snake.Heading.DOWN;
                    else
                        return heading;
                case KeyEvent.KEYCODE_A:
                    if (mSnake.getHeading() != Snake.Heading.RIGHT)
                        return Snake.Heading.LEFT;
                    else
                        return heading;
                case KeyEvent.KEYCODE_ENTER:
                    snakeGame.setPaused(true);
                    break;
            }
        }
        return heading;
    }
        public Snake.Heading touchUpdater(Snake.Heading heading, MotionEvent motionEvent, int halfWayPoint){
        if (motionEvent.getX() >= halfWayPoint) {
            switch (heading) {
                // Rotate right
                case UP:
                    return Snake.Heading.RIGHT;

                case RIGHT:
                    return Snake.Heading.DOWN;

                case DOWN:
                    return Snake.Heading.LEFT;

                case LEFT:
                    return Snake.Heading.UP;

            }
        } else {
            // Rotate left
            switch (heading) {
                case UP:
                    return Snake.Heading.LEFT;

                case LEFT:
                    return Snake.Heading.DOWN;

                case DOWN:
                    return Snake.Heading.RIGHT;

                case RIGHT:
                    return Snake.Heading.UP;

            }
        }
        return heading;
    }
}
