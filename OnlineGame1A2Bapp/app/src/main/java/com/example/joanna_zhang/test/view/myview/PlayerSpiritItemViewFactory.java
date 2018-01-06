package com.example.joanna_zhang.test.view.myview;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.joanna_zhang.test.R;

import gamecore.model.games.a1b2.boss.core.PlayerSpirit;

public class PlayerSpiritItemViewFactory{
    private Context context;

    public PlayerSpiritItemViewFactory(Context context) {
        this.context = context;
    }

    public ViewHolder createPlayerSpiritItemView(PlayerSpirit playerSpirit, ViewGroup parent){
        View view = LayoutInflater.from(context).inflate(R.layout.boss1a2b_player_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.playerHpBar.setProgress(playerSpirit.getHp());
        viewHolder.playerHpBar.setMax(playerSpirit.getMaxHp());
        viewHolder.playerHpBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.DARKEN);
        viewHolder.playerHpBar.setScaleY(3f);
        viewHolder.playerHp.setText(String.valueOf(playerSpirit.getHp()));
        viewHolder.playerName.setText(playerSpirit.getName());
        return viewHolder;
    }

    public class ViewHolder {
        public View view;
        public ProgressBar playerHpBar;
        public TextView playerName;
        public TextView playerHp;
        private ViewHolder(View view) {
            this.view = view;
            playerHpBar = view.findViewById(R.id.boss1a2bPlayerHpProgressBar);
            playerName = view.findViewById(R.id.boss1a2bPlayerNameTxt);
            playerHp = view.findViewById(R.id.boss1a2bPlayerHPTxt);
        }
    }
}
