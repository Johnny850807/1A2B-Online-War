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

import com.example.joanna_zhang.test.Domain.NameCreator.NameCreator;
import com.example.joanna_zhang.test.Domain.NameCreator.RandomNameCreator;
import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.ConnectionTimedOutException;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.GameIOException;
import com.ood.clean.waterball.a1a2bsdk.core.model.GameServerInformation;
import com.ood.clean.waterball.a1a2bsdk.core.model.Player;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.exceptions.UserNameFormatException;

public class MainActivity extends AppCompatActivity implements UserSigningModule.Callback, CoreGameServer.Callback {

    private CoreGameServer server = CoreGameServer.getInstance();
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
        server.getInformation(this);
    }

    private void findViews() {
        nameEd = (EditText) findViewById(R.id.inputName);
        autoSignInCheckbox = (CheckBox) findViewById(R.id.checkbox);
        serverStatusTxt = (TextView) findViewById(R.id.serverStatus);
    }

    public void loginButtonOnClick(View view) {
        name = nameEd.getText().toString();
        server.startEngine(MainActivity.this);
        UserSigningModule signingModule = (UserSigningModule) server.getModule(ModuleName.SIGNING);
        signingModule.signIn(name, this);
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


    public void createAndShowErrorMessage(String exceptionMessage) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.errorMessage)
                .setMessage(exceptionMessage)
                .setIcon(R.drawable.logo)
                .setPositiveButton(R.string.confirm, null)
                .show();
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onGetInformation(GameServerInformation gameServerInformation) {
        int roomAmount = gameServerInformation.getRoomAmount();
        int onlineAmount = gameServerInformation.getOnlineAmount();
        String statusFormat = getString(R.string.serverStatus);
        serverStatusTxt.setText(String.format(statusFormat, roomAmount, onlineAmount));
    }

    @Override
    public void onError(@NonNull Throwable err) {
        if (err instanceof ConnectionTimedOutException)
            createAndShowErrorMessage(getString(R.string.signInFailed_pleaseCheckYourNetwork));
        else if (err instanceof GameIOException)
            createAndShowErrorMessage(getString(R.string.signInFailedMessage));
        else if (err instanceof UserNameFormatException)
            createAndShowErrorMessage(getString(R.string.signInFailed_playerNameIsInvalid));
    }

}
