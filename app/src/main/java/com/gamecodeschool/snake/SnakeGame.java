package com.gamecodeschool.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class SnakeGame extends SurfaceView implements Runnable{

    public boolean isplaying;
    // Objects for the game loop/thread
    private Thread mThread = null;
    // Control pausing between updates
    private long mNextFrameTime;
    // Is the game currently playing and or paused?
    public volatile boolean mPlaying = false;
    private volatile boolean mPaused = true;

    // for playing sound effects
    //private SoundPool mSoundPool;
    private int mEat_ID = -1;
    private int mCrashID = -1;



    // The size in segments of the playable area
    private final int NUM_BLOCKS_WIDE = 40;

    private int color;
    private int mNumBlocksHigh;

    // How many points does the player have
    private int mScore;

    // Objects for drawing
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;

    private Level level;

    // A snake ssss
    private Snake mSnake;
    // And an apple
    private Apple mApple;
    private Control control;
    private Achievement Achievement;
    private int speed;
    private int mSnakeDirection;

    private GameRenderer gameRenderer;

    private SoundManager soundManager;

    // This is the constructor method that gets called
    // from SnakeActivity
    private Context mContext; // Add this variable to store the context
    private boolean isGameOver;
    public int begin = 0;
    public int Achieved = 0;
    // This is the constructor method that gets called
    // from SnakeActivity

    public SnakeGame(Context context, Point size) {
        super(context);
        mContext=context;
        // Work out how many pixels each block is
        int blockSize = size.x / NUM_BLOCKS_WIDE;


        gameRenderer = new GameRenderer(context, mSurfaceHolder,mPaint,mSnake,mApple);


        speed = 0;

        // How many blocks of the same size will fit into the height
        mNumBlocksHigh = size.y / blockSize;
        control = new Control(this);
        Achievement = new Achievement(this);
        color = Color.argb(255, 26, 128, 182);
        // Initialize the SoundPool

        soundManager = new SoundManager(context);


        // Initialize the drawing objects
        mSurfaceHolder = getHolder();
        mPaint = new Paint();


        // Call the constructors of our two game objects
        mApple = new Apple(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);

        mSnake = new Snake(context,
                new Point(NUM_BLOCKS_WIDE,
                        mNumBlocksHigh),
                blockSize);
        level = new Level(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        setFocusable(true);
        setFocusableInTouchMode(true);

        gameRenderer = new GameRenderer(context, mSurfaceHolder, mPaint, mSnake, mApple);

    }

    public void setPaused(boolean paused) {
        System.out.println(paused);
        this.mPaused = paused;
        //mPlaying = false;
        System.out.println(mPaused);
    }
    // Called to start a new game
    public void newGame() {
        setPaused(false);
        // reset the snake
        mSnake.reset(NUM_BLOCKS_WIDE, mNumBlocksHigh);

        // Get the apple ready for dinner
        mApple.spawn();

        level.randomObstacles();
        level.locationChecker(mSnake.getSegmentLocations(), mApple.getLocation());
        level.checkDirHit(mSnake.getHeading(level.getLevel()));
        level.isDead();
        // Reset the mScore
        mScore = 0;

        // Setup mNextFrameTime so an update can triggered
        mNextFrameTime = System.currentTimeMillis();
    }


    // Handles the game loop
    @Override
    public void run() {

        while (mPlaying) {

            if(!mPaused) {
                // Update 10 times a second
                if (updateRequired()) {
                    update();
                }
            }
            draw();
        }
    }


    // Check to see if it is time for an update
    public boolean updateRequired() {

        // Run at 10 frames per second
        long TARGET_FPS = 10+speed;
        // There are 1000 milliseconds in a second
        final long MILLIS_PER_SECOND = 1000;

        // Are we due to update the frame
        if(mNextFrameTime <= System.currentTimeMillis()){
            // Tenth of a second has passed

            // Setup when the next update will be triggered
            mNextFrameTime =System.currentTimeMillis()
                    + MILLIS_PER_SECOND / TARGET_FPS;

            // Return true so that the update and draw
            // methods are executed
            return true;
        }

        return false;
    }

    // Update all the game objects
    public void update() {


        // Move the snake
        mSnake.move();

        // Did the head of the snake eat the apple?
        if(mSnake.checkDinner(mApple.getLocation())) {
            // This reminds me of Edge of Tomorrow.
            // One day the apple will be ready!
            mApple.spawn();

            // Add to  mScore
            mScore = mScore + 1;
            if (mScore % 5 == 0 && mScore >= 1) {
                setPaused(true);
                Achieved = 1;
            }
            if(mSnake.getSnakeLength()-level.getOldSnakeLength()>=5) {

                if (level.getLevel() < 3) {
                    speed = level.updateSpeed(mSnake.getSnakeLength());
                    level.updateLevel();
                    level.updateSnakeLength(mSnake.getSnakeLength());
                } else if (level.getLevel() < 6) {
                    level.updateLevel();
                }
                level.randomObstacles();
                level.locationChecker(mSnake.getSegmentLocations(), mApple.getLocation());
                level.checkDirHit(mSnake.getHeading(level.getLevel()));
            }
            // Play a sound
            //mSP.play(mEat_ID, 1, 1, 0, 0, 1);
            soundManager.playEatSound();
        }

        // Did the snake die?
        if (mSnake.detectDeath() || mSnake.checkHit(level.getObstacleCoords())) {
            // Pause the game ready to start again
            //mSP.play(mCrashID, 1, 1, 0, 0, 1);
            soundManager.playCrashSound();
            speed = 0;
            isGameOver = level.isDead();
            mPaused =true;
            begin = 0;
        }

    }


    // Do all the drawing
    public void draw() {
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas = gameRenderer.draw(Achieved, begin, isGameOver, mScore, mPaused, mCanvas, mPaint, mSnake, mApple, level, level.updateBGColor(), mContext.getResources()); // Pass the context
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (mPaused) {
                    mPaused = false;
                    newGame();

                    // Don't want to process snake direction for this tap
                    return true;
                }

                // Let the Snake class handle the input
                mSnake.setSnakeDirection(control.touchUpdater(mSnake.getHeading(), motionEvent, mSnake.getHalfWayPoint()));
                break;

            default:
                break;

        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mPaused && begin == 0) {
            mPaused = false;
            isGameOver = false;
            newGame();
            begin = 1;
        }

        if (mPaused && Achieved == 1) {
            Achieved = 0;
            setPaused(false);
            // Handle the case where Achieved is 1 outside of the mPaused block
            switch (keyCode) {
                case KeyEvent.KEYCODE_C:
                    // Resume the game
                    setPaused(false);
                    mScore+=1;
                    mSnake.respawn(NUM_BLOCKS_WIDE, mNumBlocksHigh, mSnake.getSnakeLength());
                    return true;

            }
        }

//        if (mScore % 5 == 0 && mScore >= 1) {
//            setPaused(true);
//            Achieved = 1;
//        }

        if (mPaused) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_R:
                    // Resume the game
                    setPaused(false);
                    break;
                case KeyEvent.KEYCODE_N:
                    // Start a new game
                    newGame();
                    break;
            }

            return true;
        }

        if (!mPaused) {
            mSnake.setSnakeDirection(control.keyUpdater(mSnake, mSnake.getHeading(), keyCode));
        }

        return true;
    }


    // Stop the thread
    public void pause() {
        mPlaying = false;
        try {
            mThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }


    // Start the thread
    public void resume() {
        mPlaying = true;
        mThread = new Thread(this);
        mThread.start();
    }

    public boolean isPaused() {
        if (mPaused == false){
            return false;
        }
        return true;
    }
}