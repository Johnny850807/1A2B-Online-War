package com.example.joanna_zhang.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
    private Button loginBtn;
    private EditText nameEd;
    private CheckBox autoSignInCheckbox;
    private TextView serverStatusTxt;
    private SharedPreferences sharedPreferences;
    private NameCreator nameCreator = new RandomNameCreator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        signingModule = (UserSigningModule) gameServer.getModule(ModuleName.SIGNING);
        sharedPreferences = getSharedPreferences("1A2B", MODE_PRIVATE);
        readPlayerNameFromSharedPreferences();
    }

    private void readPlayerNameFromSharedPreferences() {
        String playerName = sharedPreferences.getString("name", "");
        if (!playerName.isEmpty())
        {
            signIn(playerName);
            nameEd.setText(playerName);
            autoSignInCheckbox.setChecked(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        signingModule.registerCallback(this);
        //signingModule.getServerInformation();
    }

    private void findViews() {
        nameEd = (EditText) findViewById(R.id.inputName);
        loginBtn = (Button) findViewById(R.id.loginButton);
        autoSignInCheckbox = (CheckBox) findViewById(R.id.checkbox);
        serverStatusTxt = (TextView) findViewById(R.id.serverStatus);
    }

    public void loginButtonOnClick(View view) {
        String playerName = nameEd.getText().toString();
        signIn(playerName);
    }

    private void signIn(String playerName){
        signingModule.signIn(playerName);
        loginBtn.setEnabled(false);
    }

    public void randomNameButtonOnClick(View view) {
        nameEd.setText(nameCreator.createRandomName());
    }

    @Override
    public void onSignInSuccessfully(@NonNull Player player) {
        loginBtn.setEnabled(true);
        savePlayerNameToSharedPreferences(autoSignInCheckbox.isChecked() ? nameEd.getText().toString() : "");
        Intent intent = new Intent(this, RoomListActivity.class);
        intent.putExtra("player", player); // send the player data to the next activity
        startActivity(intent);
    }


    @Override
    public void onSignInFailed() {
        createAndShowErrorMessage(getString(R.string.signInFailedPlayerNameIsInvalid));
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
        loginBtn.setEnabled(true);
        if (err instanceof ConnectionTimedOutException)
            createAndShowErrorMessage(getString(R.string.signInFailed_pleaseCheckYourNetwork));
    }

    @Override
    protected void onStop() {
        super.onStop();
        signingModule.unregisterCallBack(this);
    }


    private void savePlayerNameToSharedPreferences(String name) {
        sharedPreferences.edit()
                .putString("name", name)
                .apply();
    }

}
