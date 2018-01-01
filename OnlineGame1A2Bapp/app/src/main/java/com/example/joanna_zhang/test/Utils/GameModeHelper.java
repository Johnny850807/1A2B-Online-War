package com.example.joanna_zhang.test.Utils;

import android.content.Context;

import com.example.joanna_zhang.test.Duel1A2BActivity;
import com.example.joanna_zhang.test.GroupFightActivity;
import com.example.joanna_zhang.test.R;

import gamecore.model.GameMode;

public class GameModeHelper {

    public static String getGameModeText(Context context, GameMode gameMode){
        switch (gameMode){
            case DUEL1A2B:
                return context.getString(R.string.duel);
            case GROUP1A2B:
                return context.getString(R.string.fight);
            case BOSS1A2B:
                return context.getString(R.string.boss);
            case DIXIT:
                return context.getString(R.string.dixit);
        }

        throw new IllegalArgumentException("The gamemode " + gameMode + " is not found.");
    }

    public static Class getGameModeActivity(GameMode gameMode){
        switch (gameMode){
            case DUEL1A2B:
                return Duel1A2BActivity.class;
            case GROUP1A2B:
                return GroupFightActivity.class;
        }

        throw new IllegalArgumentException("The gamemode " + gameMode + " is not found.");
    }

}
