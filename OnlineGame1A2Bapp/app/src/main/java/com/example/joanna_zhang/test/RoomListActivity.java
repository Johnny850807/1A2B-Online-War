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

import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.RoomListModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import java.util.ArrayList;
import java.util.List;

import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.GameMode;
import gamecore.model.PlayerRoomModel;

import static com.example.joanna_zhang.test.R.array.roomMode;

public class RoomListActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener, RoomListModule.Callback, ListView.OnItemClickListener {
    private final static String TAG = "RoomListActivity";
    private Player player;
    private boolean enableLoadingRoomListAnimation = true;
    private List<GameRoom> roomList = new ArrayList<>();
    private GameMode[] gameModes = {null, GameMode.GROUP1A2B, GameMode.DUEL1A2B};
    private List<GameRoom> roomListOfQuery = new ArrayList<>();
    private EditText searchEdt;
    private ListView roomListView;
    private Spinner roomModeSpn;
    private RoomListModule roomListModule;
    private BaseAdapter adapter = new MyAdapter();
    private GameMode selectedMode = gameModes[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        init();
        setupViews();
        roomListView.setAdapter(adapter);
        updateRoomList();
        roomListView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        roomListModule.getGameRoomList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        roomListModule.registerCallback(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        roomListModule.unregisterCallBack(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO Sign out !!! important !!!
    }

    private void init() {
        CoreGameServer server = CoreGameServer.getInstance();
        UserSigningModule signingModule = (UserSigningModule) server.getModule(ModuleName.SIGNING);
        roomListModule = (RoomListModule) server.getModule(ModuleName.ROOMLIST);
        player = signingModule.getCurrentPlayer();
        Log.d(TAG, "Signed In Player: " + player);
    }

    private void setupViews() {
        findViews();
        setUpSpinner();
        roomListView.setDivider(getResources().getDrawable(R.drawable.transperent_color));
    }

    private void findViews() {
        searchEdt = (EditText) findViewById(R.id.searchEdt);
        searchEdt.addTextChangedListener(new SearchEditTextWatcher());
        roomListView = (ListView) findViewById(R.id.roomLst);
        roomModeSpn = (Spinner) findViewById(R.id.modeSpn);
    }

    public void setUpSpinner() {
        ArrayAdapter<CharSequence> adapterRoomMode = ArrayAdapter.createFromResource(this, roomMode, android.R.layout.simple_spinner_dropdown_item);
        adapterRoomMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomModeSpn.setAdapter(adapterRoomMode);
        roomModeSpn.setOnItemSelectedListener(this);
    }

    public void updateRoomList() {
        List<GameRoom> results = getRoomsByGameMode(selectedMode);
        roomListOfQuery = selectedMode == gameModes[0]? roomList : results;
        enableLoadingRoomListAnimation = true;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        selectedMode = gameModes[position];
        updateRoomList();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public List<GameRoom> getRoomsByGameMode(GameMode gameMode) {
        List<GameRoom> results = new ArrayList<>();
        for (GameRoom gameRoom : roomList)
            if (gameRoom.getGameMode() == gameMode)
                results.add(gameRoom);
        return results;
    }

    public void createRoomBtnOnClick(View view) {
        AlertDialog.Builder createRoomDialogBuilder = new AlertDialog.Builder(RoomListActivity.this);
        view = LayoutInflater.from(RoomListActivity.this).inflate(R.layout.create_room_dialog, null);
        EditText roomNameEd = view.findViewById(R.id.roomNameEd);
        Spinner gameModeSpn = view.findViewById(R.id.createRoomModeSpn);
        ArrayAdapter<CharSequence> gameModeAdapter = new ArrayAdapter<CharSequence>(RoomListActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.roomMode));
        gameModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameModeSpn.setAdapter(gameModeAdapter);

        createRoomDialogBuilder.setTitle(R.string.create_room)
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
        else
        {
            int randomNumber = (int) (Math.random() * roomList.size());
            roomListModule.joinRoom(roomList.get(randomNumber));
        }
    }

    private void enterGameRoom(GameRoom gameRoom) {
        Intent enterToGameRoom = new Intent(this, ChatInRoomActivity.class);
        enterToGameRoom.putExtra("game room", gameRoom);
        startActivity(enterToGameRoom);
    }


    public void searchBtnOnClick(View view) {
        enableLoadingRoomListAnimation = true;
        searchAndUpdateRoomList();
}

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        roomListModule.joinRoom(roomListOfQuery.get(position));
    }

    @Override
    public void onError(@NonNull Throwable err) {
        //todo handle the error
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
            if (enableLoadingRoomListAnimation) {
                setRoomListAdapterViewUpdatedAnimation(parent);
                enableLoadingRoomListAnimation = false; // whenever the animation enabled, the animation will be only executed once.
            }

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

            String modeName = "1A2B ";
            modeName += (gameroom.getGameMode() == GameMode.DUEL1A2B) ? getString(R.string.duel) : getString(R.string.fight);  //todo not only two mode

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
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            searchAndUpdateRoomList();
        }
        @Override
        public void afterTextChanged(Editable editable) {}
    }

    private void searchAndUpdateRoomList() {
        String searchTxt = searchEdt.getText().toString();
        roomListOfQuery = (searchTxt.isEmpty())? roomList : getRoomsByKeyName(searchTxt);
        updateRoomList();
    }

    private List<GameRoom> getRoomsByKeyName(String keyName) {
        List<GameRoom> results = new ArrayList<>();
        for (GameRoom gameRoom : roomListOfQuery)
            if (gameRoom.getName().contains(keyName) || gameRoom.getHost().getName().contains(keyName))
                results.add(gameRoom);
        return results;
    }

    @Override
    public void onGetRoomList(List<GameRoom> gameRooms) {
        roomList = gameRooms;
        updateRoomList();
    }

    @Override
    public void onNewRoom(GameRoom gameRoom) {
        roomList.add(gameRoom);
        updateRoomList();
    }

    @Override
    public void onCreateRoomSuccessfully(GameRoom gameRoom) {
        enterGameRoom(gameRoom);
    }

    @Override
    public void onCreateRoomUnsuccessfully(GameRoom gameRoom) {
        Toast.makeText(this, R.string.createRoomFailed, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRoomClosed(GameRoom gameRoom) {
        roomList.remove(gameRoom);
        updateRoomList();
    }

    @Override
    public void onRoomUpdated(GameRoom gameRoom) {
        roomList.remove(gameRoom);
        roomList.add(gameRoom);
        updateRoomList();
    }

    @Override
    public void onJoinRoomSuccessfully(PlayerRoomModel model) {
        enterGameRoom(model.getGameRoom());
    }

    @Override
    public void onJoinRoomUnsuccessfully(PlayerRoomModel model) {
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

}