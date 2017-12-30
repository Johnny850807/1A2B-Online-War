package com.example.joanna_zhang.test;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joanna_zhang.test.Utils.GameModeHelper;
import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.modules.inRoom.InRoomModule;

import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ChangeStatusModel;
import gamecore.model.GameMode;
import gamecore.model.PlayerRoomModel;
import gamecore.model.PlayerStatus;

import static com.example.joanna_zhang.test.Utils.Params.Keys.GAMEROOM;
import static com.example.joanna_zhang.test.Utils.Params.Keys.PLAYER;


/**
 * TODO:
 * (1) the player sets ready - ok
 * (2) the host starts the game and 'make sure you have ensured the player amount is suitable to the game', block the action if not. - do
 * (3) the host can boot the player by answering yes to the dialog which contains options whether to boot the player
 * created and showed by 'long-clicking' the item contains the player status you want to boot.
 * (4) show the toast if any player left or joined.  - ok
 * (5) replace all 'if game mode == DUEL then ... else Group ...' with the 'switch-case logic helping static method'.
 * (6) clean your code and organize the methods (put them in the readable order),
 * don't let any garbage be here anymore, such as some 'gray-text' attributes, some 'few-lines' methods.
 */
public class ChatInRoomActivity extends AppCompatActivity implements ChatWindowView.ChatMessageListener, InRoomModule.Callback, AdapterView.OnItemLongClickListener {

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
    protected void onResume() {
        super.onResume();
        chatWindowView.onResume();
        inRoomModule.registerCallback(currentPlayer, currentGameRoom, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatWindowView.onStop();
        inRoomModule.unregisterCallBack(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            sureAboutComeBackRoomList();
        }
        return false;
    }

    private void sureAboutComeBackRoomList() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.comeBackToRoomList)
                .setMessage(R.string.sureAboutComeBackToRoomList)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (currentPlayer.equals(roomHost))
                            inRoomModule.closeRoom();
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }


    private void init() {
        findViews();
        setUpThisRoomInfo();
        setUpGameModeTxt();
        roomPlayerListAdapter = new RoomPlayerListAdapter();
        inRoomModule = (InRoomModule) CoreGameServer.getInstance().getModule(ModuleName.INROOM);
        currentPlayer = (Player) getIntent().getSerializableExtra(PLAYER);
        currentGameRoom = (GameRoom) getIntent().getSerializableExtra(GAMEROOM);
        if (currentPlayer.equals(roomHost))
            gameStartBtn.setText(R.string.game_start);
        setupChatWindow();
    }

    private void setUpPlayerListView() {
        chatRoomPlayerListView.setAdapter(roomPlayerListAdapter);
        chatRoomPlayerListView.setOnItemLongClickListener(this);
    }

    private void setUpThisRoomInfo() {
        gameRoom = (GameRoom) getIntent().getSerializableExtra("game room");
        gameMode = gameRoom.getGameMode();
        roomHost = gameRoom.getHost();
    }

    private void setUpGameModeTxt() {
        String gameModeName = GameModeHelper.getGameModeText(this, gameMode);
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
    public void onChatMessageUpdate(ChatMessage chatMessage) {}

    @Override
    public void onMessageSendingFailed(ChatMessage chatMessage) {
        Toast.makeText(this, R.string.messageSendingFailed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChatMessageError(Throwable err) {
        Toast.makeText(this, R.string.chatMessageError, Toast.LENGTH_SHORT).show();
    }

    public void gameStartButtonOnClick(View view) {
        if (currentPlayer.equals(roomHost) && playerAmountEnoughToLaunchGame() && allPlayersAreReady()) {
            inRoomModule.launchGame();
        }
        else {
            for (PlayerStatus playerStatus : gameRoom.getPlayerStatus())
                if (playerStatus.getPlayer().equals(currentPlayer)) {
                    inRoomModule.changeStatus(new ChangeStatusModel(currentPlayer.getId(), gameRoom.getId(), !playerStatus.isReady()));
                    int statusText = !playerStatus.isReady() ? R.string.unReady : R.string.ready;
                    gameStartBtn.setText(statusText);
                }
            roomPlayerListAdapter.notifyDataSetChanged();
        }
    }

    private boolean playerAmountEnoughToLaunchGame(){
        if (gameRoom.getPlayerAmount() >= 2)
            return true;
        else
            Toast.makeText(this, R.string.playerAmountNotEnoughToLuanchGame, Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean allPlayersAreReady() {
        int readyAmount = 0;
        for (PlayerStatus player : gameRoom.getPlayerStatus())
            if (player.isReady())
                readyAmount++;
        if (readyAmount == gameRoom.getPlayerAmount()-1)
            return true;
        else
            Toast.makeText(this, R.string.someoneDidntReady, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onPlayerJoined(PlayerRoomModel model) {
        gameRoom.addPlayer(model.getPlayer());
        roomPlayerListAdapter.notifyDataSetChanged();
        Toast.makeText(this, model.getPlayer() + getString(R.string.isJoined), Toast.LENGTH_SHORT).show();
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
        if (model.getPlayer().equals(roomHost)) {
            Toast.makeText(this, R.string.theHostLeftRoomClosed, Toast.LENGTH_SHORT).show();
            finish();
        }
        roomPlayerListAdapter.notifyDataSetChanged();
        Toast.makeText(this, model.getPlayer() + getString(R.string.isLeft), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGameLaunchedSuccessfully(GameRoom gameRoom) {
        Toast.makeText(this, R.string.gameStart, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, GameModeHelper.getGameModeActivity(gameMode));
        intent.putExtra("game room", gameRoom);
        startActivity(intent);
        finish();
    }

    @Override
    public void onGameLaunchedFailed(GameRoom gameRoom) {
        Toast.makeText(this, R.string.gameLaunchFailed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onYouAreBooted() {
        Toast.makeText(this, R.string.youAreBooted, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onRoomClosed() {
        Toast.makeText(this, R.string.theHostLeftRoomClosed, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onError(@NonNull Throwable err) {
        Toast.makeText(this, R.string.errorHappened, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatInRoomActivity.this);
        builder.setTitle(R.string.selectWhichPlayerYouWantToBoot);

        ArrayAdapter<PlayerStatus> players = new ArrayAdapter<PlayerStatus>(ChatInRoomActivity.this, R.layout.chat_room_player_list_item);
            for (PlayerStatus player : gameRoom.getPlayerStatus())
                players.add(player);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setAdapter(players, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                PlayerStatus player = players.getItem(position);
                new AlertDialog.Builder(ChatInRoomActivity.this)
                        .setMessage(player.getPlayer().getName())
                        .setTitle(R.string.thePlayerYouWantToBoot)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                inRoomModule.bootPlayer(player.getPlayer());
                            }
                        })
                        .show();
            }
        })
        .show();
        return true;
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
            View playerReadyOrNot = view.findViewById(R.id.playerReadyOrNotImg);

            if (position == 0) {
                playerName.setText(roomHost.getName());
                playerName.setTextColor(Color.BLUE);
                playerReadyOrNot.setBackgroundResource(R.drawable.green_circle);
            } else {
                playerName.setText(gameRoom.getPlayerStatus().get(position-1).getPlayer().getName());
                int imageId = gameRoom.getPlayerStatus().get(position-1).isReady() ? R.drawable.green_circle : R.drawable.red_circle;
                playerReadyOrNot.setBackgroundResource(imageId);
            }
            return view;
        }
    }

}
