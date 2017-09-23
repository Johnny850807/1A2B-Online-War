package com.example.joanna_zhang.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.ood.clean.waterball.a1a2bsdk.core.model.Room;
import com.ood.clean.waterball.a1a2bsdk.core.model.User;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private CheckBox checkBox;
    private TextView serverStatus;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

    }

    private void findViews() {
        editText = (EditText) findViewById(R.id.inputName);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        serverStatus = (TextView) findViewById(R.id.serverStatus);
    }

    public void loginButtonOnClick(View view) {
        name = editText.getText().toString();
        Intent activity = new Intent(MainActivity.this, RoomList.class);
        activity.putExtra("user", new User(name));
        startActivity(activity);
        //if (checkBox.isChecked());
    }

}
