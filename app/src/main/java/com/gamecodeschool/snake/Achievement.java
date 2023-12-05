package com.gamecodeschool.snake;

public class Achievement {
    private String name;
    private String description;
    private boolean achieved;
    private SnakeGame snakeGame;

    Achievement(SnakeGame snakeGame) {
        this.snakeGame = snakeGame;
    }
    public Achievement(String name, String description) {
        this.name = name;
        this.description = description;
        this.achieved = false;
    }

    public boolean checkAchievement(Level level) {
        // Check if the current level is a multiple of 5 and the achievement is not yet achieved
        if (level.getCurrentLevel() % 5 == 0 && !this.achieved) {
            this.achieved = true;
            rewardPlayer();
            return true;
        }
        return false;
    }

    public void rewardPlayer() {
        // Display message for the player
        System.out.println("New level unlocked!");
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAchieved() {
        return achieved;
    }
}
