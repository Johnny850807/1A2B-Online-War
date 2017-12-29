package com.example.joanna_zhang.test.Utils;

import android.content.Context;

import com.example.joanna_zhang.test.R;

import gamecore.model.GameMode;

public class GameModeHelper {

    public static String getGameModeText(Context context, GameMode gameMode){
        String gameModeText = null;
        switch (gameMode.toString()){
            case "DUEL1A2B":
                gameModeText = context.getString(R.string.duel);
                break;
            case "GROUP1A2B":
                gameModeText = context.getString(R.string.fight);
                break;
            case "BOSS1A2B":
                gameModeText = context.getString(R.string.boss);
                break;
            case "DIXIT":
                gameModeText = context.getString(R.string.dixit);
                break;
        }
        return  gameModeText;
    }

    public static Class getGameModeActivity(GameMode gameMode){
        Class activity = null;

        return  activity;
    }

}
