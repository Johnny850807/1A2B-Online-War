package com.example.joanna_zhang.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.ood.clean.waterball.a1a2bsdk.core.model.User;

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
        Intent changeActivity = new Intent(MainActivity.this, RoomListActivity.class);
        changeActivity.putExtra("user", new User(name));
        startActivity(changeActivity);
        //if (checkBox.isChecked());
    }

    public void errorMessage(){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.errorMessage)
                .setMessage("登入錯誤")
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }

}
