package com.example.joanna_zhang.test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.modules.inRoom.InRoomModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.RoomListModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ChangeStatusModel;
import gamecore.model.GameMode;
import gamecore.model.PlayerRoomModel;
import gamecore.model.PlayerStatus;


/**
 * TODO:
 * (1) the player sets ready
 * (2) the host starts the game and 'make sure you have ensured the player amount is suitable to the game', block the action if not.
 * (3) the host can boot the player by answering yes to the dialog which contains options whether to boot the player
 *          created and showed by 'long-clicking' the item contains the player status you want to boot.
 * (4) show the toast if any player left or joined.
 * (5) replace all 'if game mode == DUEL then ... else Group ...' with the 'switch-case logic helping static method'.
 * (6) clean your code and organize the methods (put them in the readable order),
 *      don't let any garbage be here anymore, such as some 'gray-text' attributes, some 'few-lines' methods.
 */
public class ChatInRoomActivity extends AppCompatActivity implements ChatWindowView.ChatMessageListener, InRoomModule.Callback {

    private GameRoom currentGameRoom;
    private Player currentPlayer;
    private ChatWindowView chatWindowView;
    private Button gameStartBtn;
    private TextView gameModeTxt;
    private ListView chatRoomPlayerListView;
    private GameRoom gameRoom;
    private GameMode gameMode;
    private Player roomHost;
    private InRoomModule inRoomModule;
    private BaseAdapter roomPlayerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_in_room);
        init();
        setUpPlayerListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        chatWindowView.onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        inRoomModule.registerCallback(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatWindowView.onStop();
        inRoomModule.unregisterCallBack(this);
    }

    private void init() {
        findViews();

        setUpThisRoomInfo();
        setUpGameModeTxt();
        roomPlayerListAdapter = new RoomPlayerListAdapter();
        inRoomModule = (InRoomModule) CoreGameServer.getInstance().getModule(ModuleName.INROOM);
        currentPlayer = ((UserSigningModule) CoreGameServer.getInstance().getModule(ModuleName.SIGNING)).getCurrentPlayer();
        currentGameRoom = ((RoomListModule) CoreGameServer.getInstance().getModule(ModuleName.ROOMLIST)).getCurrentGameRoom();
        if (currentPlayer.getId().equals(roomHost.getId()))
            gameStartBtn.setText(R.string.game_start);
        setupChatWindow();
    }

    private void setUpPlayerListView() {
        chatRoomPlayerListView.setAdapter(roomPlayerListAdapter);
    }

    private void setUpThisRoomInfo() {
        gameRoom = (GameRoom) getIntent().getSerializableExtra("game room");
        gameMode = gameRoom.getGameMode();
        roomHost = gameRoom.getHost();
    }

    private void setUpGameModeTxt() {
        //TODO
        String gameModeName = gameRoom.getGameMode().toString().contains("GROUP") ? getString(R.string.fight) : getString(R.string.duel);
        gameModeTxt.setText(gameModeName);
    }

    private void setupChatWindow() {
        chatWindowView = new ChatWindowView.Builder(this, gameRoom, currentPlayer)
                .addOnSendMessageOnClickListener(this)
                .build();
    }

    private void findViews() {
        gameModeTxt = (TextView) findViewById(R.id.roomModeNameTxt);
        gameStartBtn = (Button) findViewById(R.id.gameStartBtn);
        chatRoomPlayerListView = (ListView) findViewById(R.id.chatRoomPlayersLst);
    }

    @Override
    public void onChatMessageUpdate(ChatMessage chatMessage) {

    }

    @Override
    public void onMessageSendingFailed(ChatMessage chatMessage) {
        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChatMessageError(Throwable err) {
        Toast.makeText(this, err.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void gameStartButtonOnClick(View view) {
        if (currentPlayer.equals(roomHost))
            inRoomModule.launchGame();
        else {
            for (PlayerStatus playerStatus : gameRoom.getPlayerStatus())
                if (playerStatus.getPlayer().getId().equals(currentPlayer.getId())) {
                    inRoomModule.changeStatus(new ChangeStatusModel(currentPlayer.getId(), gameRoom.getId(), !playerStatus.isReady()));
                    int statusText = playerStatus.isReady() ? R.string.unReady : R.string.ready;
                    gameStartBtn.setText(statusText);
                }
        }
    }

    @Override
    public void onPlayerJoined(PlayerRoomModel model) {
        gameRoom.addPlayer(model.getPlayer());
        roomPlayerListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPlayerStatusChanged(ChangeStatusModel model) {
        for (PlayerStatus playerStatus : gameRoom.getPlayerStatus())
            if (playerStatus.getPlayer().getId().equals(model.getPlayerId()))
                playerStatus.setReady(model.isPrepare());
        roomPlayerListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPlayerLeft(PlayerRoomModel model) {
        gameRoom.removePlayer(model.getPlayer());
        roomPlayerListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGameLaunchedSuccessfully(GameRoom gameRoom) {
        Toast.makeText(this, R.string.gameStart, Toast.LENGTH_SHORT).show();

        //TODO remove this such things
        Class inclass = gameRoom.getGameMode() == GameMode.DUEL1A2B ? DuelActivity.class : GroupFightActivity.class;
        Intent intent = new Intent(this, inclass);
        intent.putExtra("game room", gameRoom);
        startActivity(intent);
    }

    @Override
    public void onGameLaunchedFailed(GameRoom gameRoom) {
        Toast.makeText(this, R.string.gameLaunchFailed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onYouAreBooted() {
        Toast.makeText(this, R.string.youAreBooted, Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    public void onRoomClosed() {
        Toast.makeText(this, R.string.theHostLeftRoomClosed, Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    public void onError(@NonNull Throwable err) {

    }

    private class RoomPlayerListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return gameRoom.getPlayers().size();
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

            TextView playerName = view.findViewById(R.id.playerNameTxt);
            ImageView playerReadyOrNot = view.findViewById(R.id.playerReadyOrNotImg);

            if (position == 0) {
                playerName.setText(roomHost.getName());
                playerName.setTextColor(Color.BLUE);
                playerReadyOrNot.setImageResource(R.drawable.ready);
            } else {
                playerName.setText(gameRoom.getPlayerStatus().get(position-1).getPlayer().getName());
                int imageId = gameRoom.getPlayerStatus().get(position-1).isReady() ? R.drawable.ready : R.drawable.unready;
                playerReadyOrNot.setImageResource(imageId);
            }
            return view;
        }
    }

}
