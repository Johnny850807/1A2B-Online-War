package com.example.joanna_zhang.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joanna_zhang.test.Utils.GameModeHelper;
import com.example.joanna_zhang.test.Utils.ShowDialogHelper;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.client.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.RoomListModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import java.util.ArrayList;
import java.util.List;

import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ErrorMessage;
import gamecore.model.GameMode;
import gamecore.model.PlayerRoomModel;

import static com.example.joanna_zhang.test.R.array.roomMode;
import static com.example.joanna_zhang.test.Utils.Params.Keys.GAMEROOM;
import static com.example.joanna_zhang.test.Utils.Params.Keys.PLAYER;

public class RoomListActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener, RoomListModule.Callback, ListView.OnItemClickListener {
    private final static String TAG = "RoomListActivity";
    private Player currentPlayer;
    private boolean enableLoadingRoomListAnimation = true;
    private List<GameRoom> roomList = new ArrayList<>();
    private GameMode[] gameModes = {null, GameMode.GROUP1A2B, GameMode.DUEL1A2B};
    private List<GameRoom> roomListOfQuery = new ArrayList<>();
    private EditText searchEdt;
    private ListView roomListView;
    private Spinner roomModeSpn;
    private RoomListModule roomListModule;
    private UserSigningModule signingModule;
    private BaseAdapter adapter = new MyAdapter();
    private GameMode selectedMode = gameModes[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        init();
        setupViews();
        roomListView.setAdapter(adapter);
        selectAndUpdateRoomList();
        roomListView.setOnItemClickListener(this);
    }

    private void init() {
        CoreGameServer server = CoreGameServer.getInstance();
        currentPlayer = (Player) getIntent().getSerializableExtra(PLAYER);
        signingModule = (UserSigningModule) server.createModule(ModuleName.SIGNING);
        roomListModule = (RoomListModule) server.createModule(ModuleName.ROOMLIST);
        Log.d(TAG, "Signed In Player: " + currentPlayer);
    }

    private void setupViews() {
        findViews();
        setUpSpinner();
        roomListView.setDivider(getResources().getDrawable(R.drawable.transperent_color));
    }

    private void findViews() {
        searchEdt = findViewById(R.id.searchEdt);
        searchEdt.addTextChangedListener(new SearchEditTextWatcher());
        roomListView = findViewById(R.id.roomLst);
        roomModeSpn = findViewById(R.id.modeSpn);
    }

    public void setUpSpinner() {
        ArrayAdapter<CharSequence> adapterRoomMode = ArrayAdapter.createFromResource(this, roomMode, android.R.layout.simple_spinner_dropdown_item);
        adapterRoomMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomModeSpn.setAdapter(adapterRoomMode);
        roomModeSpn.setOnItemSelectedListener(this);
    }

    public void selectAndUpdateRoomList() {
        roomListOfQuery = getRoomsByKeyNameAndGameMode(roomList);
        enableLoadingRoomListAnimation = true;
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "OnResume, getting the game room list.");
        roomListModule.getGameRoomList();
        CoreGameServer.getInstance().resendUnhandledEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart, registering.");
        roomListModule.registerCallback(currentPlayer, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop, unregistering.");
        roomListModule.unregisterCallBack(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            sureAboutSignOutDialog();
        }
        return false;
    }

    private void sureAboutSignOutDialog() {
        ShowDialogHelper.showComeBackActivityDialog(
                  R.drawable.logo
                , R.string.signOut
                , R.string.sureToSignOut
                , R.string.confirm
                , R.string.cancel, this
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        selectedMode = gameModes[position];
        selectAndUpdateRoomList();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void createRoomBtnOnClick(View view) {
        view = LayoutInflater.from(RoomListActivity.this).inflate(R.layout.create_room_dialog, null);
        EditText roomNameEd = view.findViewById(R.id.roomNameEd);
        Spinner gameModeSpn = view.findViewById(R.id.createRoomModeSpn);
        ArrayAdapter<CharSequence> gameModeAdapter = new ArrayAdapter<CharSequence>(RoomListActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.roomMode));
        gameModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameModeSpn.setAdapter(gameModeAdapter);
        showCreateRoomDialog(view, roomNameEd, gameModeSpn);
    }

    private void showCreateRoomDialog(View view, EditText roomNameEd, Spinner gameModeSpn) {
        new AlertDialog.Builder(RoomListActivity.this)
                .setTitle(R.string.create_room)
                .setIcon(R.drawable.logo)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String modeName = gameModeSpn.getSelectedItem().toString();
                        GameMode gameModeToCreateRoom = modeName.equals(getString(R.string.duel)) ? GameMode.DUEL1A2B : GameMode.GROUP1A2B;
                        roomListModule.createRoom(roomNameEd.getText().toString(), gameModeToCreateRoom);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .setView(view)
                .show();
    }

    public void fastJoinRoomBtnOnClick(View view) {
        if (roomList.isEmpty())
            Toast.makeText(this, R.string.noRoomCanJoin, Toast.LENGTH_LONG).show();
        else {
            int randomNumber = (int) (Math.random() * roomList.size());
            roomListModule.joinRoom(roomList.get(randomNumber));
        }
    }

    private void enterGameRoom(GameRoom gameRoom) {
        Log.v(TAG, "Enter the game room.");
        Intent enterToGameRoom = new Intent(this, ChatInRoomActivity.class);
        enterToGameRoom.putExtra(PLAYER, currentPlayer);
        enterToGameRoom.putExtra(GAMEROOM, gameRoom);
        startActivity(enterToGameRoom);
    }

    public void searchBtnOnClick(View view) {
        enableLoadingRoomListAnimation = true;
        selectAndUpdateRoomList();
    }

    private List<GameRoom> getRoomsByGameMode(List<GameRoom> rooms) {
        List<GameRoom> results = new ArrayList<>();
        if (selectedMode == gameModes[0])
            return rooms;
        for (GameRoom gameRoom : rooms)
            if (gameRoom.getGameMode() == selectedMode)
                results.add(gameRoom);
        return results;
    }

    private List<GameRoom> getRoomsByKeyName(List<GameRoom> rooms) {
        List<GameRoom> results = new ArrayList<>();
        String key = searchEdt.getText().toString();
        if (key.isEmpty())
            return rooms;
        for (GameRoom gameRoom : rooms)
            if (gameRoom.getName().contains(key) || gameRoom.getHost().getName().contains(key))
                results.add(gameRoom);
        return results;
    }

    private List<GameRoom> getRoomsByKeyNameAndGameMode(List<GameRoom> rooms) {
        List<GameRoom> results;
        results = getRoomsByGameMode(rooms);
        return getRoomsByKeyName(results);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        roomListModule.joinRoom(roomListOfQuery.get(position));
    }

    @Override
    public void onError(@NonNull Throwable err) {
        Log.e(TAG, "exception is " + err.getMessage());
    }

    public class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return roomListOfQuery.size();
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
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder viewHolder;
            Log.i("adapter", "GetView: position " + position);
            /*if (enableLoadingRoomListAnimation) {
                setRoomListAdapterViewUpdatedAnimation(parent);
                enableLoadingRoomListAnimation = false; // whenever the animation enabled, the animation will be only executed once.
            }*/

            if (view == null)  // if the view has not existed in view, init and bind the viewholder
            {
                view = LayoutInflater.from(RoomListActivity.this).inflate(R.layout.room_list_item, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.roomNameTxt = view.findViewById(R.id.roomNameTxt);
                viewHolder.roomModeTxt = view.findViewById(R.id.roomModeTxt);
                viewHolder.roomCreatorName = view.findViewById(R.id.roomCreatorNameTxt);
                viewHolder.roomPlayerAmountTxt = view.findViewById(R.id.roomPlayerAmountTxt);
                view.setTag(viewHolder);
            } else  // if the view exists, get the viewholder
                viewHolder = (ViewHolder) view.getTag();

            GameRoom gameroom = roomListOfQuery.get(position);

            String modeName = getString(R.string.modeName);
            modeName += GameModeHelper.getGameModeText(RoomListActivity.this, gameroom.getGameMode());

            viewHolder.roomNameTxt.setText(gameroom.getName());
            viewHolder.roomModeTxt.setText(modeName);
            viewHolder.roomCreatorName.setText(gameroom.getHost().getName());
            viewHolder.roomPlayerAmountTxt.setText(gameroom.getPlayerAmount() + "/" + gameroom.getGameMode().getMaxPlayerAmount());

            return view;
        }

        private class ViewHolder {
            TextView roomNameTxt;
            TextView roomModeTxt;
            TextView roomCreatorName;
            TextView roomPlayerAmountTxt;
        }
    }

    private void setRoomListAdapterViewUpdatedAnimation(ViewGroup parent) {
        // the animation retrieved from : https://stackoverflow.com/questions/4349803/android-listview-refresh-animation
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(400);
        set.addAnimation(animation);

        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);
        parent.setLayoutAnimation(controller);
    }

    private class SearchEditTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            selectAndUpdateRoomList();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    @Override
    public void onGetRoomList(List<GameRoom> gameRooms) {
        roomList = gameRooms;
        selectAndUpdateRoomList();
    }

    @Override
    public void onNewRoom(GameRoom gameRoom) {
        roomList.add(gameRoom);
        selectAndUpdateRoomList();
    }

    @Override
    public void onCreateRoomSuccessfully(GameRoom gameRoom) {
        enterGameRoom(gameRoom);
    }

    @Override
    public void onCreateRoomUnsuccessfully(ErrorMessage errorMessage) {
        Toast.makeText(this, R.string.createRoomFailed, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRoomLaunched(GameRoom gameRoom) {
        roomList.remove(gameRoom);
        selectAndUpdateRoomList();
    }

    @Override
    public void onRoomClosed(GameRoom gameRoom) {
        roomList.remove(gameRoom);
        selectAndUpdateRoomList();
    }

    @Override
    public void onRoomUpdated(GameRoom gameRoom) {
        roomList.remove(gameRoom);
        roomList.add(gameRoom);
        selectAndUpdateRoomList();
    }

    @Override
    public void onJoinRoomSuccessfully(PlayerRoomModel model) {
        enterGameRoom(model.getGameRoom());
    }

    @Override
    public void onJoinRoomUnsuccessfully(ErrorMessage errorMessage) {
        Toast.makeText(this, R.string.theRoomIsFull, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlayerJoined(PlayerRoomModel model) {
        onRoomUpdated(model.getGameRoom());
    }

    @Override
    public void onPlayerLeft(PlayerRoomModel model) {
        onRoomUpdated(model.getGameRoom());
    }

    @Override
    public void onServerReconnected() {
        //TODO
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy, signing out.");
        signingModule.signOut(currentPlayer);
    }

}