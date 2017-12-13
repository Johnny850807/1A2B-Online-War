package com.example.joanna_zhang.test;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;

import java.util.List;

import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;

public class DuelActivity extends AppCompatActivity implements ChatWindowView.OnClickListener, InputNumberWindowView.OnClickListener {


    //private Duel1A2BGameModule duel1A2BGameModule;
    //private List<GuessRecord> p1ResultList, p2ResultList;
    private GameRoom gameRoom;
    private ChatWindowView chatWindowView;
    private InputNumberWindowView inputNumberWindowView;
    private TextView p1NameTxt, p2NameTxt, p1AnswerTxt, p2AnswerTxt;
    private ListView p1ResultListView, p2ResultListView;
    private List<String> p1Results, p2Results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_duel);

        CoreGameServer server = CoreGameServer.getInstance();
        //duel1A2BGameModule = (Duel1A2BGameModule) server.getModule(ModuleName.SIGNING);

        setupChatWindow();
        findViews();
        setupAnswer();
    }

    private void findViews() {
        p1NameTxt = (TextView) findViewById(R.id.p1NameTxt);
        p2NameTxt = (TextView) findViewById(R.id.p2NameTxt);
        p1AnswerTxt = (TextView) findViewById(R.id.p1AnswerTxt);
        p2AnswerTxt = (TextView) findViewById(R.id.p2AnswerTxt);
        p1ResultListView = (ListView) findViewById(R.id.p1ResultLst);
        p2ResultListView = (ListView) findViewById(R.id.p2ResultLst);
    }

    private void setupAnswer() {
        new InputNumberWindowView.Builder(this)
                .setOnEnterClickListener((number) -> {
                    p1AnswerTxt.setText(number);
                })
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .setTitle("請設置答案")
                .build();
        new AlertDialog.Builder(this)
                .setMessage(R.string.setAnswerFrist)
                .setPositiveButton("OK", null)
                .show();
    }

    public void setupChatWindow() {
        chatWindowView = new ChatWindowView.Builder(this, gameRoom)
                .addOnSendMessageOnClickListener(this)
                .build();
    }

    private void setupInputNumberWindowView() {
        inputNumberWindowView = new InputNumberWindowView.Builder(this)
                .setOnEnterClickListener(this)
                .setTitle("請輸入答案")
                .build();
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
        // TODO
    }

    public void inputNumberOnClick(View view) {
        setupInputNumberWindowView();
    }

    private class GuessResultAdapter extends BaseAdapter {

        private List<String> resultList;

        GuessResultAdapter(List<String> resultList) {
            this.resultList = resultList;
        }

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
            view = LayoutInflater.from(DuelActivity.this).inflate(android.R.layout.simple_list_item_1, viewGroup, false);

            // TODO
            return null;
        }
    }

}
