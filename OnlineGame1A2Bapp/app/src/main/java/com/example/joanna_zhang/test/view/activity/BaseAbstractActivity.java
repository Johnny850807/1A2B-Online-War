package com.example.joanna_zhang.test.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import gamecore.entity.GameRoom;
import gamecore.entity.Player;

import static com.example.joanna_zhang.test.Utils.Params.Keys.GAMEROOM;
import static com.example.joanna_zhang.test.Utils.Params.Keys.PLAYER;

public class BaseAbstractActivity extends AppCompatActivity {

    protected GameRoom currentGameRoom;
    protected Player currentPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPlayer = (Player) getIntent().getSerializableExtra(PLAYER);
        currentGameRoom = (GameRoom) getIntent().getSerializableExtra(GAMEROOM);
    }



}
