package com.example.joanna_zhang.test.Utils;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.example.joanna_zhang.test.R;

public class SoundManager {

    private Context context;
    private SoundPool soundPool;
    private int dingdoig;

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

    //TODO use map registration / unregistration instead
    public void playDingdong() {
        if (dingdoig == 0)
            dingdoig = soundPool.load(context, R.raw.dingdong, 1);
        soundPool.play(dingdoig, 1, 1, 0, 0, 1);
    }

}
