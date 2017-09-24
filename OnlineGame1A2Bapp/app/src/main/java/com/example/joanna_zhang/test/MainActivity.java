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
import com.example.joanna_zhang.test.NameCreator.NameCreator;
import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.model.GameServerInformation;
import com.ood.clean.waterball.a1a2bsdk.core.model.User;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.exceptions.UserNameFormatException;

public class MainActivity extends AppCompatActivity implements UserSigningModule.Callback, CoreGameServer.Callback {

    private CoreGameServer server = CoreGameServer.getInstance();
    private EditText nameEd;
    private CheckBox autoSignInCheckbox;
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
        UserSigningModule signingModule = server.getModule(ModuleName.SIGNING);
        signingModule.signIn(name, this);
        //todo if (autoSignInCheckbox.isChecked());
    }

    public void randomNameButtonOnClick(View view) {
        nameEd.setText(nameCreator.createRandomName());
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
            createAndShowErrorDialog(getString(R.string.signInFailedMessage));
    }

    public void createAndShowErrorDialog(String exceptionMessage){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.errorMessage)
                .setMessage(exceptionMessage)
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
}
