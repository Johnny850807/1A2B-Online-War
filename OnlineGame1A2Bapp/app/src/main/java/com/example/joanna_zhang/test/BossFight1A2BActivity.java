package com.example.joanna_zhang.test;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ood.clean.waterball.a1a2bsdk.core.modules.games.a1b2.boss.Boss1A2BModule;

import java.util.ArrayList;
import java.util.List;

import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.PlayerRoomModel;
import gamecore.model.games.a1b2.GuessRecord;

import static com.example.joanna_zhang.test.Utils.Params.Keys.GAMEROOM;
import static com.example.joanna_zhang.test.Utils.Params.Keys.PLAYER;

public class BossFight1A2BActivity extends AppCompatActivity implements Boss1A2BModule.Callback{

    private Boss1A2BModule boss1A2BModule;
    private GameRoom currentGameRoom;
    private Player currentPlayer;
    private List<GuessRecord> resultList;
    private InputNumberWindowDialog inputNumberWindowDialog;
    private Button inputNumberBtn;
    private ImageButton sendGuessBtn;
    private GuessResultAdapter guessResultAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_fight1_a2_b);
        init();
        findViews();
        setupLayout();
    }

    private void init() {
//        currentPlayer = (Player) getIntent().getSerializableExtra(PLAYER);
//        currentGameRoom = (GameRoom) getIntent().getSerializableExtra(GAMEROOM);
    }

    private void findViews() {
        inputNumberBtn = findViewById(R.id.inputNumberBtn);
        sendGuessBtn = findViewById(R.id.sendGuessBtn);
        guessResultAdapter = new GuessResultAdapter();
        progressBar = findViewById(R.id.bossHpProgressBar);
    }


    private void setupLayout() {
        progressBar.getProgressDrawable().setColorFilter(
                Color.GREEN, PorterDuff.Mode.DARKEN);
        progressBar.setScaleY(3f);
    }

    public void inputNumberOnClick(View view) {
    }

    public void onSendGuessNumberBtnClick(View view) {
    }

    @Override
    public void onRoomExpired() {

    }

    @Override
    public void onServerReconnected() {

    }

    @Override
    public void onError(@NonNull Throwable err) {

    }

    @Override
    public void onNextPlayerTurn() {

    }

    @Override
    public void onAttackingSuccessfully() {

    }

    @Override
    public void onGameOver() {

    }

    @Override
    public void onGameStarted() {

    }

    @Override
    public void onPlayerLeft(PlayerRoomModel model) {

    }


    private class GuessResultAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
//            view = LayoutInflater.from(BossFight1A2BActivity.this).inflate(, viewGroup, false);



            return view;
        }
    }

}
