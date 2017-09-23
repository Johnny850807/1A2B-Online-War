package com.example.joanna_zhang.test;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;
import com.ood.clean.waterball.a1a2bsdk.core.model.User;

public class RoomListActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);
        CoreGameServer server = CoreGameServer.getInstance();
        UserSigningModule signingModule = (UserSigningModule) server.getModule(ModuleName.SIGNING);
        welcomeMessage();
    }

    public void welcomeMessage(){
        new AlertDialog.Builder(RoomListActivity.this)
                .setTitle(R.string.welcome)
                .setMessage(welcomeUserMessage())
                .show();
    }

    private String welcomeUserMessage() {
        String message = "歡迎登入!!";
        return message;
    }

}
