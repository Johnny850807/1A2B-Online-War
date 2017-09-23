package com.example.joanna_zhang.test;

import android.annotation.SuppressLint;
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
import com.ood.clean.waterball.a1a2bsdk.core.model.GameServerInformation;
import com.ood.clean.waterball.a1a2bsdk.core.model.User;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.exceptions.UserNameFormatException;

public class MainActivity extends AppCompatActivity implements UserSigningModule.Callback, CoreGameServer.Callback {

    private CoreGameServer server = CoreGameServer.getInstance();
    private EditText editText;
    private CheckBox checkBox;
    private TextView serverStatusTxt;
    private String name;
    private RandomNameCreator randomNameCreator = new RandomNameCreator();  // 這是她媽的依賴具體嗎?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        server.getInformation(this);
    }

    private void findViews() {
        editText = (EditText) findViewById(R.id.inputName);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        serverStatusTxt = (TextView) findViewById(R.id.serverStatus);
    }

    public void loginButtonOnClick(View view) {
        name = editText.getText().toString();
        server.startEngine(MainActivity.this);
        UserSigningModule signingModule = (UserSigningModule) server.getModule(ModuleName.SIGNING);
        signingModule.signIn(name, this);
        //todo if (checkBox.isChecked());
    }

    public void errorMessage(String exceptionMessage){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.errorMessage)
                .setMessage(exceptionMessage)
                .setPositiveButton(R.string.confirm, null)
                .show();
    }

    public void randomNameButtonOnClick(View view) {
        editText.setText(randomNameCreator.createRandomName());
    }

    @Override
    public void onSignInSuccessfully(@NonNull User user) {
        Intent intent = new Intent(this, RoomListActivity.class);
        intent.putExtra("user", user); // send the user data to the next activity
        startActivity(intent);
    }

    @Override
    public void onSignInFailed(@NonNull Exception err) {
        if (err instanceof UserNameFormatException)
            errorMessage(getString(R.string.signInFailedMessage));
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onGetInformation(GameServerInformation gameServerInformation) {
        int roomAmount = gameServerInformation.getRoomAmount();
        int onlineAmount = gameServerInformation.getOnlineAmount();
        String statusFormat = getString(R.string.serverStatus);
        serverStatusTxt.setText(String.format(statusFormat, roomAmount, onlineAmount));
    }
}
