package com.example.joanna_zhang.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;

import gamecore.entity.ChatMessage;

public class DuelActivity extends AppCompatActivity implements ChatWindowView.OnClickListener, InputNumberWindowView.OnClickListener{


    //private Duel1A2BGameModule duel1A2BGameModule;
    //private List<GuessRecord> p1ResultList, p2ResultList;
    private ChatWindowView chatWindowView;
    private InputNumberWindowView inputNumberWindowView;
    private TextView p1NameTxt, p2NameTxt, p1AnswerTxt, p2AnswerTxt;
    private ListView p1ResultListView, p2ResultListView;
    private ListView p1ResultLst, p2ResultLst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_duel);

        CoreGameServer server = CoreGameServer.getInstance();
        //duel1A2BGameModule = (Duel1A2BGameModule) server.getModule(ModuleName.SIGNING);

        setupChatWindow();
        setupInputNumberWindowView();
        findViews();
    }

    private void findViews() {
        p1NameTxt = (TextView) findViewById(R.id.p1NameTxt);
        p2NameTxt = (TextView) findViewById(R.id.p2NameTxt);
        p1AnswerTxt = (TextView) findViewById(R.id.p1AnswerTxt);
        p2AnswerTxt = (TextView) findViewById(R.id.p2AnswerTxt);
        p1ResultListView = (ListView) findViewById(R.id.p1ResultLst);
        p2ResultListView = (ListView) findViewById(R.id.p2ResultLst);
    }

    public void setupChatWindow() {
        chatWindowView = new ChatWindowView.Builder(this)
                .addOnSendMessageOnClickListener(this)
                .build();
    }

    private void setupInputNumberWindowView() {
        inputNumberWindowView = new InputNumberWindowView(this);
    }
/*
    public void updateResultList(List<GuessRecord> resultList, ListView resultListView) {
        ResultListAdapter adapter = new ResultListAdapter(resultList);
        resultListView.setAdapter(adapter);
        resultListView.setSelection(resultListView.getCount() - 1);
    }*/
/*
    private Player getCurrentPlayer() {
        CoreGameServer server = CoreGameServer.getInstance();
        UserSigningModule signingModule = (MockUserSigningModule) server.getModule(ModuleName.SIGNING);
        return signingModule.getCurrentPlayer();
    }*/

    @Override
    public void onClick(ChatMessage chatMessage) {

    }

    @Override
    public void onEnterClick(String guessName) {
        p1AnswerTxt.setText(guessName);
    }


}
