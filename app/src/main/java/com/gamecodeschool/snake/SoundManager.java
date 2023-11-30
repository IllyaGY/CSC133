package com.gamecodeschool.snake;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

public class SoundManager {
    private SoundPool mSoundPool;
    private int mEat_ID = -1;
    private int mCrashID = -1;

    public SoundManager(Context context) {
        // Initialize the SoundPool
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        loadSounds(context);
    }

    private void loadSounds(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Prepare the sounds in memory
            descriptor = assetManager.openFd("get_apple.ogg");
            mEat_ID = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("snake_death.ogg");
            mCrashID = mSoundPool.load(descriptor, 0);

        } catch (IOException e) {  Log.e("error", "failed to load sound files");
        }
    }

    public void playEatSound() {
        mSoundPool.play(mEat_ID, 1, 1, 0, 0, 1);
    }

    public void playCrashSound() {
        mSoundPool.play(mCrashID, 1, 1, 0, 0, 1);
    }
}
