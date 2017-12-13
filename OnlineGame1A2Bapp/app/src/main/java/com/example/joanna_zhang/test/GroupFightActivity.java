package com.example.joanna_zhang.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;

import java.util.List;

import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;

public class GroupFightActivity extends AppCompatActivity implements ChatWindowView.OnClickListener, InputNumberWindowView.OnClickListener {

    //private Duel1A2BGameModule duel1A2BGameModule;
    //private List<GuessRecord> p1ResultList, p2ResultList;
    private GameRoom gameRoom;
    private ChatWindowView chatWindowView;
    private InputNumberWindowView inputNumberWindowView;
    private TextView p1NameTxt, p2NameTxt, p1AnswerTxt, p2AnswerTxt;
    private ListView p1ResultListView, p2ResultListView;
    private List<String> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_fight);

        CoreGameServer server = CoreGameServer.getInstance();
        setupChatWindow();
        setupInputNumberWindowView();
    }

    public void setupChatWindow() {
        chatWindowView = new ChatWindowView.Builder(this, gameRoom)
                .addOnSendMessageOnClickListener(this)
                .build();
    }

    private void setupInputNumberWindowView() {
        inputNumberWindowView = new InputNumberWindowView.Builder(this)
                .setOnEnterClickListener(this)
                .build();
    }

    @Override
    public void onClick(ChatMessage chatMessage) {

    }

    @Override
    public void onEnterClick(String guessName) {

    }
}
