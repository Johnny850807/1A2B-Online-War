package com.example.joanna_zhang.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ood.clean.waterball.a1a2bsdk.core.client.CoreGameServer;

import java.util.List;

import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ErrorMessage;

import static com.example.joanna_zhang.test.Utils.Params.Keys.PLAYER;

public class GroupFightActivity extends AppCompatActivity implements ChatWindowView.ChatMessageListener, InputNumberWindowView.OnClickListener {

    //private Duel1A2BGameModule duel1A2BGameModule;
    //private List<GuessRecord> p1ResultList, p2ResultList;
    private GameRoom gameRoom;
    private Player currentPlayer;
    private ChatWindowView chatWindowView;
    private InputNumberWindowView inputNumberWindowView;
    private Button inputNumberBtn;
    private TextView p1NameTxt, p2NameTxt, p3NameTxt, p4NameTxt, p5NameTxt, p6NameTxt;
    private ListView p1ResultListView, p2ResultListView;
    private List<String> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_fight);

        CoreGameServer server = CoreGameServer.getInstance();
        currentPlayer = (Player) getIntent().getSerializableExtra(PLAYER);
        setupChatWindow();
        findViews();
    }

    private void findViews() {
        inputNumberBtn = findViewById(R.id.inputNumberBtn);
        p1NameTxt = findViewById(R.id.p1NameTxt);
    }

    public void setupChatWindow() {
        chatWindowView = new ChatWindowView.Builder(this, gameRoom, currentPlayer)
                .addOnSendMessageOnClickListener(this)
                .build();
    }

    private void setupInputNumberWindowView() {
        inputNumberWindowView = new InputNumberWindowView.Builder(this)
                .setOnEnterClickListener(this)
                .build();
    }

    @Override
    public void onChatMessageUpdate(ChatMessage chatMessage) {

    }

    @Override
    public void onMessageSendingFailed(ErrorMessage errorMessage) {

    }

    @Override
    public void onChatMessageError(Throwable err) {

    }

    @Override
    public void onEnterClick(String guessNumber) {
        inputNumberBtn.setText(guessNumber);
    }

    private class GuessResultAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return results.size();
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
            view = LayoutInflater.from(GroupFightActivity.this).inflate(android.R.layout.simple_list_item_1, viewGroup, false);

            // TODO
            return null;
        }
    }
}
