package com.example.joanna_zhang.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DuelActivity extends AppCompatActivity {

    private ChatWindowView chatWindowView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_duel);

        createchatWindow();
    }

    public void createchatWindow() {
        chatWindowView = new ChatWindowView.Builder(this).build();
    }
}
