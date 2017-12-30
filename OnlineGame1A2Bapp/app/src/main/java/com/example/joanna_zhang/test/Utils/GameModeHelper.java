package com.example.joanna_zhang.test.Utils;

import android.content.Context;

import com.example.joanna_zhang.test.DuelActivity;
import com.example.joanna_zhang.test.GroupFightActivity;
import com.example.joanna_zhang.test.R;

import gamecore.model.GameMode;
import gamecore.model.games.a1b2.boss.Boss1A2BGame;

public class GameModeHelper {

    public static String getGameModeText(Context context, GameMode gameMode){
        String gameModeText = null;
        switch (gameMode){
            case DUEL1A2B:
                gameModeText = context.getString(R.string.duel);
                break;
            case GROUP1A2B:
                gameModeText = context.getString(R.string.fight);
                break;
            case BOSS1A2B:
                gameModeText = context.getString(R.string.boss);
                break;
            case DIXIT:
                gameModeText = context.getString(R.string.dixit);
                break;
        }
        return  gameModeText;
    }

    public static Class getGameModeActivity(GameMode gameMode){
        Class activity = null;
        switch (gameMode){
            case DUEL1A2B:
                activity = DuelActivity.class;
                break;
            case GROUP1A2B:
                activity = GroupFightActivity.class;
                break;
            case BOSS1A2B:
                activity = null;
                break;
            case DIXIT:
                activity = null;
                break;
        }

        return  activity;
    }

}
