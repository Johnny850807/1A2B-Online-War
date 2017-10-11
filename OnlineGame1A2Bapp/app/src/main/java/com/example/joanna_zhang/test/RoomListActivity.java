package com.example.joanna_zhang.test;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.joanna_zhang.test.Abstract.RoomListItemData;
import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import java.util.List;

public class RoomListActivity extends AppCompatActivity {

    private List<RoomListItemData> roomListItemDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);
        CoreGameServer server = CoreGameServer.getInstance();
        UserSigningModule signingModule = (UserSigningModule) server.getModule(ModuleName.SIGNING);
        createAndShowWelcomeMessage();
    }

    public void createAndShowWelcomeMessage(){
        new AlertDialog.Builder(RoomListActivity.this)
                .setTitle(R.string.SignInMessage)
                .setMessage(welcomeUserMessage())
                .show();
    }

    private String welcomeUserMessage() {
        String message = getString(R.string.signInWelcomeMessage);
        return message;
    }

    public void createRoomBtnOnClick(View view) {

        new AlertDialog.Builder(this)
                .setView(R.layout.create_room_dialog)
                .create()
                .show();
    }

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
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

            roomName.setText(roomListItemDatas.get(i).getRoomName());
            roomMode.setText(roomListItemDatas.get(i).getMode().toString());
            roomCreator.setText(roomListItemDatas.get(i).getRoomCreatorName());
            roomPeopleAmount.setText(roomListItemDatas.get(i).getPeopleAmount());

            return view;
        }
    }
}
