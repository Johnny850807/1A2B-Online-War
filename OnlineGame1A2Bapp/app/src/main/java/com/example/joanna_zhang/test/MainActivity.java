package com.example.joanna_zhang.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.joanna_zhang.test.Domain.NameCreator.NameCreator;
import com.example.joanna_zhang.test.Domain.NameCreator.RandomNameCreator;
import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.ConnectionTimedOutException;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import gamecore.entity.Player;
import gamecore.model.ServerInformation;

public class MainActivity extends AppCompatActivity implements UserSigningModule.Callback {
    private CoreGameServer gameServer = CoreGameServer.getInstance();
    private UserSigningModule signingModule;
    private EditText nameEd;
    private CheckBox autoSignInCheckbox;  // TODO
    private TextView serverStatusTxt;
    private String name;
    private NameCreator nameCreator = new RandomNameCreator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        signingModule = (UserSigningModule) gameServer.getModule(ModuleName.SIGNING);
    }

    @Override
    protected void onStart() {
        super.onStart();
        signingModule.registerCallback(this);
        //signingModule.getServerInformation();
    }

    private void findViews() {
        nameEd = (EditText) findViewById(R.id.inputName);
        autoSignInCheckbox = (CheckBox) findViewById(R.id.checkbox);
        serverStatusTxt = (TextView) findViewById(R.id.serverStatus);
    }

    public void loginButtonOnClick(View view) {
        name = nameEd.getText().toString();
        signingModule.signIn(name);
        //todo if (autoSignInCheckbox.isChecked());
    }

    public void randomNameButtonOnClick(View view) {
        nameEd.setText(nameCreator.createRandomName());
    }

    @Override
    public void onSignInSuccessfully(@NonNull Player player) {
        Intent intent = new Intent(this, RoomListActivity.class);
        intent.putExtra("player", player); // send the player data to the next activity
        startActivity(intent);
    }


    @Override
    public void onSignInFailed() {
        createAndShowErrorMessage(getString(R.string.signInFailed_playerNameIsInvalid));
    }

    @Override
    public void onLoadServerInformation(ServerInformation serverInformation) {
        int roomAmount = serverInformation.getOnlineRoomAmount();
        int onlineAmount = serverInformation.getOnlineUserAmount();
        serverStatusTxt.setText(getString(R.string.serverStatus, roomAmount, onlineAmount));
    }


    public void createAndShowErrorMessage(String exceptionMessage) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.errorMessage)
                .setMessage(exceptionMessage)
                .setIcon(R.drawable.logo)
                .setPositiveButton(R.string.confirm, null)
                .show();
    }


    @Override
    public void onError(@NonNull Throwable err) {
        if (err instanceof ConnectionTimedOutException)
            createAndShowErrorMessage(getString(R.string.signInFailed_pleaseCheckYourNetwork));
    }

    @Override
    protected void onStop() {
        super.onStop();
        signingModule.unregisterCallBack(this);
    }


}
