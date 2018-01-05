package com.example.joanna_zhang.test;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import gamecore.model.games.a1b2.boss.core.PlayerSpirit;

public class PlayerSpiritItemViewFactory{
    private Context context;

    public PlayerSpiritItemViewFactory(Context context) {
        this.context = context;
    }

    public PlayerSpiritViewHolder createPlayerSpiritItemView(PlayerSpirit playerSpirit, ViewGroup parent){
        View view = LayoutInflater.from(context).inflate(R.layout.boss1a2b_player_list_item, parent, false);
        PlayerSpiritViewHolder playerSpiritViewHolder = new PlayerSpiritViewHolder(view);
        playerSpiritViewHolder.playerHpBar.setProgress(playerSpirit.getHp());
        playerSpiritViewHolder.playerHpBar.setMax(playerSpirit.getMaxHp());
        playerSpiritViewHolder.playerHpBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.DARKEN);
        playerSpiritViewHolder.playerHpBar.setScaleY(3f);
        playerSpiritViewHolder.playerHp.setText(String.valueOf(playerSpirit.getHp()));
        playerSpiritViewHolder.playerName.setText(playerSpirit.getName());
        return playerSpiritViewHolder;
    }

    public class PlayerSpiritViewHolder{
        View view;
        ProgressBar playerHpBar;
        TextView playerName;
        TextView playerHp;
        private PlayerSpiritViewHolder(View view) {
            this.view = view;
            playerHpBar = view.findViewById(R.id.boss1a2bPlayerHpProgressBar);
            playerName = view.findViewById(R.id.boss1a2bPlayerNameTxt);
            playerHp = view.findViewById(R.id.boss1a2bPlayerHPTxt);
        }
    }
}
