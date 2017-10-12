package com.example.joanna_zhang.test;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.joanna_zhang.test.Abstract.Mode;
import com.example.joanna_zhang.test.Abstract.RoomListItemData;
import com.example.joanna_zhang.test.Mock.MockRoomListItemData;
import com.example.joanna_zhang.test.Mock.MockUser;
import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.model.Room;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import java.util.ArrayList;
import java.util.List;

public class RoomListActivity extends AppCompatActivity {

    private List<RoomListItemData> roomListItemDatas = new ArrayList<>();
    private ListView roomLst;
    private Spinner roomModeSpn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);
        CoreGameServer server = CoreGameServer.getInstance();
        UserSigningModule signingModule = (UserSigningModule) server.getModule(ModuleName.SIGNING);
        createAndShowWelcomeMessage();

        roomLst = (ListView) findViewById(R.id.roomLst);
        roomModeSpn = (Spinner) findViewById(R.id.modeSpn);



        roomListItemDatas.add(new MockRoomListItemData("來玩啊", Mode.DUEL, new MockUser()));
        roomListItemDatas.add(new MockRoomListItemData("來玩啊啊啊啊", Mode.FIGHT, new MockUser()));
        updateRoomList();
        setUpSpinner();


    }

    public void setUpSpinner() {
        ArrayAdapter<CharSequence> adapterRoomMode = ArrayAdapter.createFromResource(this, R.array.roomMode, android.R.layout.simple_spinner_dropdown_item);
        adapterRoomMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomModeSpn.setAdapter(adapterRoomMode);
    }

    public void updateRoomList() {
        MyAdapter myAdapter = new MyAdapter();
        roomLst.setAdapter(myAdapter);
    }

    public void createAndShowWelcomeMessage() {
        new AlertDialog.Builder(RoomListActivity.this)
                .setTitle(R.string.signInMessage)
                .setMessage(welcomeUserMessage())
                .show();
    }

    private String welcomeUserMessage() {
        String message = getString(R.string.signInWelcomeMessage);
        return message;
    }

    public void createRoomBtnOnClick(View view) {
        AlertDialog.Builder createRoomDialog = new AlertDialog.Builder(RoomListActivity.this);



        new AlertDialog.Builder(RoomListActivity.this)
                .setView(R.layout.create_room_dialog)
                .setTitle(R.string.create_room)
                .setIcon(R.drawable.logo)
                .setPositiveButton(R.string.confirm, null)
                .setNegativeButton(R.string.cancel, null)
                .show();

    }

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return roomListItemDatas.size();
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
            view = LayoutInflater.from(RoomListActivity.this).inflate(R.layout.room_list_item, viewGroup, false);


            TextView roomName = view.findViewById(R.id.roomListItemNameTxt);
            TextView roomMode = view.findViewById(R.id.roomListItemModeTxt);
            TextView roomCreator = view.findViewById(R.id.roomListItemCreatorNameTxt);
            TextView roomPeopleAmount = view.findViewById(R.id.roomListItemPeopleAmountTxt);

            String mode, totalPeopleAmount;
            if (roomListItemDatas.get(i).getMode() == Mode.DUEL) {
                mode = "1A2B 對決戰";
                totalPeopleAmount = "2";
            } else {
                mode = "1A2B 爭奪戰";
                totalPeopleAmount = "6";
            }

            roomName.setText(roomListItemDatas.get(i).getRoomName());
            roomMode.setText(mode);
            roomCreator.setText(roomListItemDatas.get(i).getRoomCreatorName());
            roomPeopleAmount.setText(String.valueOf(roomListItemDatas.get(i).getPeopleAmount() + "/" + totalPeopleAmount));

            return view;
        }
    }
}
