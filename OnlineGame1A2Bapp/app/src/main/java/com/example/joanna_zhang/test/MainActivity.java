package com.example.joanna_zhang.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.joanna_zhang.test.Game.RandomNameCreator;
import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.model.User;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.exceptions.UserNameFormatException;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private CheckBox checkBox;
    private TextView serverStatus;
    private String name;
    private RandomNameCreator randomNameCreator; // 這是她媽的依賴具體嗎?
    private Exception e;

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
        CoreGameServer server = CoreGameServer.getInstance();
        server.startEngine(MainActivity.this);
        UserSigningModule signingModule = (UserSigningModule) server.getModule(ModuleName.SIGNING);
        signingModule.signIn(name , new UserSigningModule.Callback() {
            @Override
            public void onSignInSuccessfully(@NonNull User user) {
                Intent intent = new Intent(MainActivity.this, RoomListActivity.class);
                intent.putExtra("user", user); // send the user data to the next activity
                startActivity(intent);
            }

            @Override
            public void onSignInFailed(@NonNull Exception err) throws UserNameFormatException {
                if (err instanceof UserNameFormatException)
                    errorMessage();
            }
        });

        //todo if (checkBox.isChecked());
    }

    public void errorMessage(){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.errorMessage)
                .setMessage(exceptionMessage(e.getMessage()))
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }


    public String exceptionMessage(String message){
        return message;
    }

    public void randomNameButtonOnClick(View view) {
        editText.setText(randomNameCreator.createRandomName());
    }
}
