package com.example.joanna_zhang.test;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joanna_zhang.test.Domain.Factory.GameRoomListFactory;
import com.example.joanna_zhang.test.Domain.GameMode;
import com.example.joanna_zhang.test.Domain.GameRoom;
import com.example.joanna_zhang.test.Mock.Factory.MockGameRoomListFactory;
import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import java.util.ArrayList;
import java.util.List;

import static com.example.joanna_zhang.test.R.array.roomMode;

public class RoomListActivity extends AppCompatActivity {
    private GameRoomListFactory gameRoomListFactory = new MockGameRoomListFactory();
    private boolean enableLoadingRoomListAnimation = true;
    private List<GameRoom> roomList = new ArrayList<>();
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
    }

    public void updateRoomList(List<GameRoom> list) {
        MyAdapter myAdapter = new MyAdapter(list);
        roomListView.setAdapter(myAdapter);
    }

    public void createRoomBtnOnClick(View view) {
        AlertDialog.Builder createRoomDialogBuilder = new AlertDialog.Builder(RoomListActivity.this);
        View mView = LayoutInflater.from(RoomListActivity.this).inflate(R.layout.create_room_dialog, null);
        createRoomDialogBuilder.setTitle(R.string.create_room);
        Spinner gameModeSpn = mView.findViewById(R.id.createRoomModeSpn);
        ArrayAdapter<CharSequence> gameModeAdapter = new ArrayAdapter<CharSequence>(RoomListActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.roomMode));
        gameModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameModeSpn.setAdapter(gameModeAdapter);
        createRoomDialogBuilder.setPositiveButton(R.string.confirm, null);
        createRoomDialogBuilder.setNegativeButton(R.string.cancel, null);
        createRoomDialogBuilder.setView(mView);
        createRoomDialogBuilder.show();
    }

    public void joinRoomBtnOnClick(View view) {
        //todo join room
    }

    public void searchBtnOnClick(View view) {
        enableLoadingRoomListAnimation = true;
        searchAndUpdateRoomList();
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
            if (enableLoadingRoomListAnimation)
            {
                setRoomListAdapterViewUpdatedAnimation(parent);
                enableLoadingRoomListAnimation = false; // only the first time will enable the animation until the new room added.
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
            modeName += (gameroom.getGameMode() == GameMode.DUEL) ? getString(R.string.duel) : getString(R.string.fight);

            viewHolder.roomNameTxt.setText(gameroom.getRoomName());
            viewHolder.roomModeTxt.setText(modeName);
            viewHolder.roomCreatorName.setText(gameroom.getRoomCreatorName());
            viewHolder.roomPlayerAmountTxt.setText(gameroom.getPlayerAmount() + "/" + gameroom.getGameMode().getPlayerAmount());

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

        LayoutAnimationController controller =
                new LayoutAnimationController(set, 0.25f);
        parent.setLayoutAnimation(controller);
    }

    private class SearchEditTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            searchAndUpdateRoomList();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    private void searchAndUpdateRoomList(){
        String searchTxt = searchEdt.getText().toString();
        updateRoomList(getRoomsByKeyName(searchTxt));
    }

    private List<GameRoom> getRoomsByKeyName(String keyName){
        List<GameRoom> results = new ArrayList<>();
        for (GameRoom gameRoom : roomList)
            if (gameRoom.getRoomName().contains(keyName) || gameRoom.getRoomCreatorName().contains(keyName))
                results.add(gameRoom);
        return results;
    }
}
