package com.example.joanna_zhang.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joanna_zhang.test.Utils.GameModeHelper;
import com.example.joanna_zhang.test.Utils.ShowDialogHelper;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.client.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.modules.inRoom.InRoomModule;

import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ChangeStatusModel;
import gamecore.model.ErrorMessage;
import gamecore.model.PlayerRoomModel;
import gamecore.model.PlayerStatus;

import static com.example.joanna_zhang.test.Utils.Params.Keys.GAMEROOM;
import static com.example.joanna_zhang.test.Utils.Params.Keys.PLAYER;

public class ChatInRoomActivity extends AppCompatActivity implements ChatWindowView.ChatMessageListener, InRoomModule.Callback, AdapterView.OnItemLongClickListener {
    private static final String TAG = "ChatInRoomActivity";
    private static final int HOST_POSITION = 0;
    private GameRoom currentGameRoom;
    private Player currentPlayer;
    private ChatWindowView chatWindowView;
    private Button gameStartBtn;
    private TextView gameModeTxt;
    private ListView chatRoomPlayerListView;
    private InRoomModule inRoomModule;
    private BaseAdapter roomPlayerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_in_room);
        init();
        findViews();
        setUpGameModeTxt();
        if (currentPlayer.equals(currentGameRoom.getHost()))
            gameStartBtn.setText(R.string.game_start);
        setupChatWindow();
        setUpPlayerListView();
    }

    private void init() {
        currentPlayer = (Player) getIntent().getSerializableExtra(PLAYER);
        currentGameRoom = (GameRoom) getIntent().getSerializableExtra(GAMEROOM);
        roomPlayerListAdapter = new RoomPlayerListAdapter();
        inRoomModule = (InRoomModule) CoreGameServer.getInstance().createModule(ModuleName.INROOM);
    }

    private void findViews() {
        gameModeTxt = findViewById(R.id.roomModeNameTxt);
        gameStartBtn = findViewById(R.id.gameStartBtn);
        chatRoomPlayerListView = findViewById(R.id.chatRoomPlayersLst);
    }

    private void setUpGameModeTxt() {
        String gameModeName = GameModeHelper.getGameModeText(this, currentGameRoom.getGameMode());
        gameModeTxt.setText(gameModeName);
    }

    private void setupChatWindow() {
        chatWindowView = new ChatWindowView.Builder(this, currentGameRoom, currentPlayer)
                .addOnSendMessageOnClickListener(this)
                .build();
    }

    private void setUpPlayerListView() {
        chatRoomPlayerListView.setAdapter(roomPlayerListAdapter);
        chatRoomPlayerListView.setOnItemLongClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "OnResume.");
        chatWindowView.onResume();
        inRoomModule.registerCallback(currentPlayer, currentGameRoom, this);
        CoreGameServer.getInstance().resendUnhandledEvents();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop.");
        chatWindowView.onStop();
        inRoomModule.unregisterCallBack(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            sureAboutComeBackRoomListDialog();
            return true;
        }
        return false;
    }

    private void sureAboutComeBackRoomListDialog() {
        ShowDialogHelper.showComeBackActivityDialog(
                  R.drawable.logo
                , R.string.comeBackToRoomList
                , R.string.sureAboutComeBackToRoomList
                , R.string.confirm
                , R.string.cancel, this
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (currentPlayer.equals(currentGameRoom.getHost()))
                            inRoomModule.closeRoom();
                        else
                            inRoomModule.leaveRoom();
                        finish();
                    }
                });
    }

    @Override
    public void onMessageSendingFailed(ErrorMessage errorMessage) {
        Toast.makeText(this, R.string.messageSendingFailed, Toast.LENGTH_SHORT).show();
    }

    public void gameStartButtonOnClick(View view) {
        if (currentPlayer.equals(currentGameRoom.getHost()) && playerAmountEnoughToLaunchGame() && allPlayersAreReady()) {
            inRoomModule.launchGame();
        } else {
            for (PlayerStatus playerStatus : currentGameRoom.getPlayerStatus())
                if (playerStatus.getPlayer().equals(currentPlayer)) {
                    inRoomModule.changeStatus(new ChangeStatusModel(currentPlayer.getId(), currentGameRoom.getId(), !playerStatus.isReady()));
                    int statusText = !playerStatus.isReady() ? R.string.unReady : R.string.ready;
                    gameStartBtn.setText(statusText);
                }
            roomPlayerListAdapter.notifyDataSetChanged();
        }
    }

    private boolean playerAmountEnoughToLaunchGame() {
        if (currentGameRoom.getPlayerAmount() >= 2)
            return true;
        else
            Toast.makeText(this, R.string.playerAmountNotEnoughToLuanchGame, Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean allPlayersAreReady() {
        for (PlayerStatus player : currentGameRoom.getPlayerStatus())
            if (!player.isReady()) {
                Toast.makeText(this, R.string.someoneDidntReady, Toast.LENGTH_SHORT).show();
                return false;
            }
        return true;
    }

    @Override
    public void onPlayerJoined(PlayerRoomModel model) {
        currentGameRoom.addPlayer(model.getPlayer());
        roomPlayerListAdapter.notifyDataSetChanged();
        Toast.makeText(this, model.getPlayer().getName() + getString(R.string.isJoined), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlayerStatusChanged(ChangeStatusModel model) {
        for (PlayerStatus playerStatus : currentGameRoom.getPlayerStatus())
            if (playerStatus.getPlayer().getId().equals(model.getPlayerId())) {
                playerStatus.setReady(model.isPrepare());
                break;
            }
        roomPlayerListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPlayerLeft(PlayerRoomModel model) {
        currentGameRoom.removePlayer(model.getPlayer());
        if (model.getPlayer().equals(currentGameRoom.getHost())) {
            Toast.makeText(this, R.string.theHostLeftRoomClosed, Toast.LENGTH_SHORT).show();
            finish();
        } else
            roomPlayerListAdapter.notifyDataSetChanged();
        Toast.makeText(this, model.getPlayer().getName() + getString(R.string.isLeft), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGameLaunchedSuccessfully(GameRoom gameRoom) {
        Toast.makeText(this, R.string.gameStart, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, GameModeHelper.getGameModeActivity(currentGameRoom.getGameMode()));
        intent.putExtra(PLAYER, currentPlayer);
        intent.putExtra(GAMEROOM, gameRoom);
        startActivity(intent);
        finish();
    }

    @Override
    public void onGameLaunchedFailed(GameRoom gameRoom) {
        Toast.makeText(this, R.string.gameLaunchFailed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServerReconnected() {
        //TODO
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
        Log.e(TAG, "exception is " + err.getMessage());
    }

    @Override
    public void onChatMessageUpdate(ChatMessage chatMessage) {
        Log.v(TAG, "chat Message is update");
    }


    @Override
    public void onChatMessageError(Throwable err) {
        Log.v(TAG, "chat message error: " + err.getMessage());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final int YES = 0;
        final int NO = 1;
        String[] YESORNO = new String[]{getString(R.string.yes), getString(R.string.no)};
        if (position != HOST_POSITION && currentPlayer.equals(currentGameRoom.getHost()))
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.thePlayerYouWantToBoot, currentGameRoom.getPlayers().get(position).getName()))
                    .setItems(YESORNO, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int yesOrNo) {
                            switch (yesOrNo) {
                                case YES:
                                    Log.d(TAG, "On booting item long click, position: " + position);
                                    inRoomModule.bootPlayer(currentGameRoom.getPlayers().get(position));
                                    break;
                                case NO:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    })
                    .show();
        return true;
    }

    private class RoomPlayerListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return currentGameRoom.getPlayers().size();
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

            if (position == HOST_POSITION) {
                playerName.setText(currentGameRoom.getHost().getName());
                playerName.setTextColor(Color.BLUE);
                playerReadyOrNot.setBackgroundResource(R.drawable.green_circle);
            } else {
                playerName.setText(currentGameRoom.getPlayerStatus().get(position - 1).getPlayer().getName());
                int imageId = currentGameRoom.getPlayerStatus().get(position - 1).isReady() ? R.drawable.green_circle : R.drawable.red_circle;
                playerReadyOrNot.setBackgroundResource(imageId);
            }
            return view;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "OnDestroy.");
    }
}
