package com.example.joanna_zhang.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.joanna_zhang.test.Domain.Factory.GameRoomListFactory;
import com.example.joanna_zhang.test.Mock.MockGameRoomListFactory;
import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.RoomListModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.model.GameMode;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.model.GameRoom;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import java.util.ArrayList;
import java.util.List;

import static com.example.joanna_zhang.test.R.array.roomMode;

public class RoomListActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener, RoomListModule.Callback, ListView.OnItemClickListener {
    private GameRoomListFactory gameRoomListFactory = new MockGameRoomListFactory();
    private boolean enableLoadingRoomListAnimation = true;
    private List<GameRoom> roomList = new ArrayList<>();
    private GameMode[] gameModes = {null, GameMode.GROUP1A2B, GameMode.DUEL1A2B};
    private List<GameRoom> roomListOfGameMode = new ArrayList<>();
    private EditText searchEdt;
    private ListView roomListView;
    private Spinner roomModeSpn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        init();
        setupViews();
        updateRoomList(roomList);
        roomListView.setOnItemClickListener(this);
    }

    private void init() {
        CoreGameServer server = CoreGameServer.getInstance();
        UserSigningModule signingModule = (UserSigningModule) server.getModule(ModuleName.SIGNING);
        roomList = gameRoomListFactory.createRoomList();
    }

    private void setupViews() {
        findViews();
        setUpSpinner();
        updateRoomList(roomList);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        GameMode selectedMode = gameModes[position];
        List<GameRoom> results = getRoomsByGameMode(selectedMode);
        roomListOfGameMode = results.isEmpty() ? roomList : results;
        updateRoomList(roomListOfGameMode);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        updateRoomList(roomList);
    }

    public List<GameRoom> getRoomsByGameMode(GameMode gameMode) {
        List<GameRoom> results = new ArrayList<>();
        for (GameRoom gameRoom : roomList)
            if (gameRoom.getGameMode() == gameMode)
                results.add(gameRoom);
        return results;
    }

    public void updateRoomList(List<GameRoom> list) {
        MyAdapter myAdapter = new MyAdapter(list);
        roomListView.setAdapter(myAdapter);
    }

    public void createRoomBtnOnClick(View view) {
        AlertDialog.Builder createRoomDialogBuilder = new AlertDialog.Builder(RoomListActivity.this);
        view = LayoutInflater.from(RoomListActivity.this).inflate(R.layout.create_room_dialog, null);
        Spinner gameModeSpn = view.findViewById(R.id.createRoomModeSpn);
        ArrayAdapter<CharSequence> gameModeAdapter = new ArrayAdapter<CharSequence>(RoomListActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.roomMode));
        gameModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameModeSpn.setAdapter(gameModeAdapter);
        createRoomDialogBuilder.setTitle(R.string.create_room)
                .setIcon(R.drawable.logo)
                .setPositiveButton(R.string.confirm, null)
                .setNegativeButton(R.string.cancel, null)
                .setView(view)
                .show();
    }

    public void joinRoomBtnOnClick(View view) {
    }

    public void searchBtnOnClick(View view) {
        enableLoadingRoomListAnimation = true;
        searchAndUpdateRoomList();
    }

    @Override
    public void onGetRoomList(List<GameRoom> gameRooms) {
        this.roomList = gameRooms;
        MyAdapter myAdapter = new MyAdapter(roomList);
        roomListView.setAdapter(myAdapter);
    }

    @Override
    public void onNewRoom(GameRoom gameRoom) {
        //Todo
    }

    @Override
    public void onRoomClosed(GameRoom gameRoom) {
        //Todo
    }

    @Override
    public void onRoomUpdated(GameRoom gameRoom) {
        //Todo
    }

    @Override
    public void onRoomStartedPlaying(GameRoom gameRoom) {
        // Todo
    }

    @Override
    public void onCreatedRoomSuccessfully(GameRoom gameRoom) {
        //進入房間聊天室 Todo
    }

    @Override
    public void onJoinRoomSuccessfully(GameRoom gameRoom) {
        //進入房間聊天室 Todo
    }

    @Override
    public void onFailed(Exception err) {
        Toast.makeText(RoomListActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        GameMode gameMode = roomListOfGameMode.get(position).getGameMode();
        Intent enterToGameRoom = new Intent();
        enterToGameRoom.setClass(RoomListActivity.this, ChatInRoomActivity.class);
        enterToGameRoom.putExtra("roomGameMode", gameMode.toString());
        startActivity(enterToGameRoom);
        finish();
    }

    public class MyAdapter extends BaseAdapter {

        private List<GameRoom> roomlist;

        public MyAdapter(List<GameRoom> roomlist) {
            this.roomlist = roomlist;
        }

        @Override
        public int getCount() {
            return roomlist.size();
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

            GameRoom gameroom = roomlist.get(position);

            String modeName = "1A2B ";
            modeName += (gameroom.getGameMode() == GameMode.DUEL1A2B) ? getString(R.string.duel) : getString(R.string.fight);  //todo not only two mode

            viewHolder.roomNameTxt.setText(gameroom.getName());
            viewHolder.roomModeTxt.setText(modeName);
            viewHolder.roomCreatorName.setText(gameroom.getRoomHost().getName());
            viewHolder.roomPlayerAmountTxt.setText(gameroom.getPlayerList().size() + "/" + gameroom.getGameMode().getMaxPlayerAmount());

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
        animation.setDuration(400);
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
        updateRoomList(getRoomsByKeyName(searchTxt));
    }

    private List<GameRoom> getRoomsByKeyName(String keyName) {
        List<GameRoom> results = new ArrayList<>();
        for (GameRoom gameRoom : roomListOfGameMode)
            if (gameRoom.getName().contains(keyName) || gameRoom.getRoomHost().getName().contains(keyName))
                results.add(gameRoom);
        return results;
    }
}
