package com.example.joanna_zhang.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private CheckBox checkBox;
    private TextView serverStatus;
    private String name;
    public static String[] randomNames = {"嗨你好", "丁丁", "拉拉", "小波"};

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

    public void randomNameButtonOnClick(View view) {
        Random random = new Random();
        String name = randomNames[random.nextInt(randomNames.length)];
        editText.setText(name);
    }

    public void loginButtonOnClick(View view) {
        name = editText.getText().toString();
        //if (checkBox.isChecked());
    }
}
