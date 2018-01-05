package com.example.joanna_zhang.test.Utils;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.RawRes;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private Context context;
    private SoundPool soundPool;
    private Map<Integer, Integer> soundIdMaps = new HashMap<>();  //<resource id, sound id in the pool>


    public SoundManager(Context context) {
        this.context = context;
        soundPool = setUpSoundPool();
    }

    private SoundPool setUpSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return new SoundPool.Builder().setMaxStreams(10).build();
        else
            return new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
    }

    public void playSound(@RawRes int resid){
        int soundId;
        if (!soundIdMaps.containsKey(resid))
            soundId = register(resid);
        else
            soundId = soundIdMaps.get(resid);
        soundPool.play(soundId, 1, 1, 60, 0, 1);
    }

    private int register(@RawRes int resId){
        int soundId = soundPool.load(context, resId, 1);
        soundIdMaps.put(resId, soundId);
        return soundId;
    }

}
