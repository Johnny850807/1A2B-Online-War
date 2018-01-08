package com.example.joanna_zhang.test.Utils;


import android.content.Context;

import com.example.joanna_zhang.test.R;

public class DefaultTtsVoiceManager {
    private static DefaultTtsVoiceManager instance;
    private Context context;
    private SoundManager soundManager;

    private DefaultTtsVoiceManager(Context context, SoundManager soundManager) {
        this.context = context;
        this.soundManager = soundManager;
    }

    public static void init(Context context, SoundManager soundManager) {
        instance = new DefaultTtsVoiceManager(context, soundManager);
    }

    public static DefaultTtsVoiceManager getInstance() {
        return instance;
    }

    /**
     * play the tts voice from the message if it's in the default message
     */
    public void playDefaultVoice(String content) {
        char num = content.trim().charAt(0);
        if (isDefaultVoiceContent(content))
            switch (num) {
                case '1':
                    soundManager.playSound(R.raw.ok);
                    break;
                case '2':
                    soundManager.playSound(R.raw.no);
                    break;
                case '3':
                    soundManager.playSound(R.raw.awesome);
                    break;
                case '4':
                    soundManager.playSound(R.raw.quickly);
                    break;
                case '5':
                    soundManager.playSound(R.raw.damn);
                    break;
                case '6':
                    soundManager.playSound(R.raw.gg);
                    break;
                case '7':
                    soundManager.playSound(R.raw.please_ready);
                    break;
                case '8':
                    soundManager.playSound(R.raw.please_start);
                    break;
                case '9':
                    soundManager.playSound(R.raw.sorry);
                    break;
            }
    }

    public String parseDefaultContent(String content){
        char num = content.trim().charAt(0);
        if (isDefaultVoiceContent(content))
        {
            switch (num) {
                case '1':
                    return context.getString(R.string.ok);
                case '2':
                    return context.getString(R.string.no);
                case '3':
                    return context.getString(R.string.awesome);
                case '4':
                    return context.getString(R.string.quickly);
                case '5':
                    return context.getString(R.string.damnit);
                case '6':
                    return context.getString(R.string.goodGame);
                case '7':
                    return context.getString(R.string.pleaseSetReady);
                case '8':
                    return context.getString(R.string.pleaseStartGame);
                case '9':
                    return context.getString(R.string.sorry);
            }
        }
        return content;
    }

    public boolean isDefaultVoiceContent(String content) {
        content = content.trim();
        char num = content.charAt(0);  //for example: 1) ok
        //if the content is only the message id or with the ')' back from the default message
        //such as the content '1'  or  '1) Ok'
        return Character.isDigit(num) && (content.length() == 1 || content.charAt(1) == ')');
    }

    public void release() {
        soundManager.release();
    }
}
