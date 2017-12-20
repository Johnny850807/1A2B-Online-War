package com.example.joanna_zhang.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import java.util.List;

import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.GameMode;


public class ChatInRoomActivity extends AppCompatActivity implements View.OnClickListener, ChatWindowView.OnClickListener {

    private ChatWindowView chatWindowView;
    private Button gameStartBtn;
    private TextView gameModeTxt;
    private ListView chatRoomPlayerLst;
    private GameRoom gameRoom;
    private GameMode gameMode;
    private Player roomHost;
    private List<Player> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_in_room);

        setupChatWindow();
        findViews();
        setUpThisRoomInfo();
        setUpGameModeTxt();
        init();
        setUpPlayerListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Todo 註冊chatWindow
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Todo 取消註冊chatWindow
    }

    private void init() {
        //playerList = new MockPlayerListFactory().createPlayerList();
    }

    private void setUpPlayerListView() {
        RoomPlayerList playerListAdapter = new RoomPlayerList();
        chatRoomPlayerLst.setAdapter(playerListAdapter);

    }

    private void setUpThisRoomInfo() {
        gameRoom = (GameRoom) getIntent().getSerializableExtra("game room");
        gameMode = gameRoom.getGameMode();
        roomHost = gameRoom.getHost();
    }

    private void setUpGameModeTxt() {
        String gameModeName = gameRoom.getGameMode().toString().contains("GROUP") ? getString(R.string.fight) : getString(R.string.duel);
        gameModeTxt.setText(gameModeName);
    }

    private void setupChatWindow() {
        chatWindowView = new ChatWindowView.Builder(this, gameRoom)
                .addOnSendMessageOnClickListener(this)
                .build();
    }

    private void findViews() {
        gameModeTxt = (TextView) findViewById(R.id.roomModeNameTxt);
        gameStartBtn = (Button) findViewById(R.id.gameStartBtn);
        chatRoomPlayerLst = (ListView) findViewById(R.id.chatRoomPlayersLst);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onClick(ChatMessage chatMessage) {

    }

    public void gameStartonClick(View view) {
        Intent intent = new Intent(this, gameMode == GameMode.GROUP1A2B ? GroupFightActivity.class : DuelActivity.class);
        intent.putExtra("GameRoom", gameRoom);
        startActivity(intent);
    }

    private class RoomPlayerList extends BaseAdapter {

        @Override
        public int getCount() {
            return gameMode.getMaxPlayerAmount();
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
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(ChatInRoomActivity.this).inflate(R.layout.chat_room_player_list_item, viewGroup, false);
            /*Player player = playerList.get(position);
            PlayerStatus.UNREADY.setPlayer(player);

            TextView playerName = view.findViewById(R.id.playerNameTxt);
            ImageView playerReadyOrNot = view.findViewById(R.id.playerReadyOrNotImg);

            playerName.setText(player.getName());
            int imageId = PlayerStatus.UNREADY.getPlayer().getName().equals(player.getName()) ? R.drawable.unready : R.drawable.ready;
            playerReadyOrNot.setImageResource(imageId);*/
            return view;
        }
    }

}
