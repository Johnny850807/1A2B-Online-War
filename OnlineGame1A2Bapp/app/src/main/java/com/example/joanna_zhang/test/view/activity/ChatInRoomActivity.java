package com.example.joanna_zhang.test.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.example.joanna_zhang.test.view.myview.ChatWindowView;
import com.example.joanna_zhang.test.R;
import com.example.joanna_zhang.test.Utils.GameModeHelper;
import com.example.joanna_zhang.test.Utils.AppDialogFactory;
import com.example.joanna_zhang.test.Utils.SoundManager;
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

public class ChatInRoomActivity extends BaseAbstractActivity implements ChatWindowView.ChatMessageListener, InRoomModule.Callback, AdapterView.OnItemLongClickListener {
    private static final String TAG = "ChatInRoomActivity";
    private static final int HOST_POSITION = 0;
    private ChatWindowView chatWindowView;
    private Button gameStartBtn;
    private TextView gameModeTxt;
    private ListView chatRoomPlayerListView;
    private InRoomModule inRoomModule;
    private BaseAdapter roomPlayerListAdapter;
    private SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_in_room);
        init();
        findViews();
        setUpGameModeTxt();
        if (currentPlayer.equals(currentGameRoom.getHost()))
            gameStartBtn.setText(R.string.gameStart);
        setupChatWindow();
        setUpPlayerListView();
    }

    private void init() {
        roomPlayerListAdapter = new RoomPlayerListAdapter();
        soundManager = new SoundManager(this);
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
        AppDialogFactory.templateBuilder(this)
                .setTitle(R.string.leaveThisRoom)
                .setMessage(R.string.sureAboutComeBackToRoomList)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (currentPlayer.equals(currentGameRoom.getHost()))
                            inRoomModule.closeRoom();
                        else
                            inRoomModule.leaveRoom();
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onMessageSendingFailed(ErrorMessage errorMessage) {
        Toast.makeText(this, R.string.messageSendingFailed, Toast.LENGTH_SHORT).show();
    }

    public void gameStartButtonOnClick(View view) {
        if (currentPlayer.equals(currentGameRoom.getHost()) && validateGameStarting()) {
            inRoomModule.launchGame();
        } else if (!currentPlayer.equals(currentGameRoom.getHost())){
            PlayerStatus playerStatus = currentGameRoom.getPlayerStatusOfPlayer(currentPlayer);
            inRoomModule.changeStatus(new ChangeStatusModel(currentPlayer.getId(), currentGameRoom.getId(), !playerStatus.isReady()));
            roomPlayerListAdapter.notifyDataSetChanged();
        }
    }

    private boolean validateGameStarting(){
        if (!currentGameRoom.areAllPlayerReady())
        {
            Toast.makeText(this, R.string.someoneDidntReady, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!currentGameRoom.canStartTheGame())
        {
            Toast.makeText(this, R.string.playerAmountNotEnoughToLuanchGame, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onPlayerJoined(PlayerRoomModel model) {
        currentGameRoom.addPlayer(model.getPlayer());
        roomPlayerListAdapter.notifyDataSetChanged();
        Toast.makeText(this, model.getPlayer().getName() + getString(R.string.isJoined), Toast.LENGTH_SHORT).show();
        soundManager.playSound(R.raw.dodo);
    }

    @Override
    public void onPlayerStatusChanged(ChangeStatusModel model) {
        Player player = currentGameRoom.getPlayerById(model.getPlayerId());
        PlayerStatus playerStatus = currentGameRoom.getPlayerStatusOfPlayer(player);
        playerStatus.setReady(model.isPrepare());
        if (!currentPlayer.equals(currentGameRoom.getHost()))
        {
            int statusTextId = playerStatus.isReady() ? R.string.unReady : R.string.ready;
            gameStartBtn.setText(statusTextId);
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

        soundManager.playSound(R.raw.dong);
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
        soundManager.playSound(R.raw.punch);
        finish();
    }

    @Override
    public void onRoomClosed() {
        Toast.makeText(this, R.string.theHostLeftRoomClosed, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onRoomExpired() {
        AppDialogFactory.roomTimeExpiredDialog(this).show();
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
    public boolean onItemLongClick(AdapterView<?> parent, View view, int bootedPosition, long id) {
        if (isBootingAvailable(bootedPosition))
            createAndShowDialogForBootingOptions(bootedPosition);
        return true;
    }

    private boolean isBootingAvailable(int bootedPosition){
        return bootedPosition != HOST_POSITION && currentPlayer.equals(currentGameRoom.getHost());
    }

    private void createAndShowDialogForBootingOptions(int bootedPosition){
        String[] YESORNO = new String[]{getString(R.string.yes), getString(R.string.no)};
        Player bootedPlayer =  currentGameRoom.getPlayers().get(bootedPosition);
        AppDialogFactory.templateBuilder(this)
                .setTitle(getString(R.string.thePlayerYouWantToBoot, bootedPlayer.getName()))
                .setItems(YESORNO, (dialog, yesOrNo) -> selectingBootingOptions(dialog, bootedPosition, yesOrNo))
                .show();
    }

    private void selectingBootingOptions(DialogInterface dialog, int bootedPosition, int yesOrNo){
        final int YES = 0;
        final int NO = 1;
        switch (yesOrNo) {
            case YES:
                Log.d(TAG, "On booting item long click, position: " + bootedPosition);
                inRoomModule.bootPlayer(currentGameRoom.getPlayers().get(bootedPosition));
                break;
            case NO:
                dialog.dismiss();
                break;
        }
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
