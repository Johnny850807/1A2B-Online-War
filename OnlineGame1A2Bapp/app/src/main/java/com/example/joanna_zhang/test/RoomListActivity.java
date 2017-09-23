package com.example.joanna_zhang.test;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RoomListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);
    }

    public void welcomeMessage(){
        new AlertDialog.Builder(RoomListActivity.this)
                .setTitle(R.string.welcome)
                .setMessage(R.string.welcomeMessage)
                .show();
    }

}
