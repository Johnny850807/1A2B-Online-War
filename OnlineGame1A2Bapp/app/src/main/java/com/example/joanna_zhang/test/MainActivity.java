package com.example.joanna_zhang.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.joanna_zhang.test.Domain.NameCreator.NameCreator;
import com.example.joanna_zhang.test.Domain.NameCreator.RandomNameCreator;
import com.ood.clean.waterball.a1a2bsdk.core.client.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.ConnectionTimedOutException;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import java.util.concurrent.TimeUnit;

import gamecore.entity.Player;
import gamecore.model.ServerInformation;

import static com.example.joanna_zhang.test.Utils.Params.Keys.PLAYER;
import static com.example.joanna_zhang.test.Utils.Params.Keys.PLAYERNAME;
import static com.example.joanna_zhang.test.Utils.Params.Keys.SP_NAME;


public class MainActivity extends AppCompatActivity implements UserSigningModule.Callback{
    private static final String TAG = "MainActivity";
    private CoreGameServer gameServer = CoreGameServer.getInstance();
    private UserSigningModule signingModule;
    private Button loginBtn;
    private EditText nameEd;
    private CheckBox autoSignInCheckbox;
    private TextView serverStatusTxt;
    private SharedPreferences sharedPreferences;
    private NameCreator nameCreator = new RandomNameCreator();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        findViews();
    }

    private void init(){
        signingModule = (UserSigningModule) gameServer.createModule(ModuleName.SIGNING);
        sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
    }

    private void findViews() {
        nameEd = (EditText) findViewById(R.id.inputName);
        loginBtn = (Button) findViewById(R.id.loginButton);
        autoSignInCheckbox = (CheckBox) findViewById(R.id.checkbox);
        serverStatusTxt = (TextView) findViewById(R.id.serverStatus);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "OnStart.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume, registering.");
        signingModule.registerCallback(this);
        CoreGameServer.getInstance().startEngine(this);
    }

    @Override
    public void onServerReconnected() {
        Log.v(TAG, "onServerReconnected.");
        signingModule.getServerInformation();
        readPlayerNameFromSharedPreferences();
        //loadServerInformationIn10Secs();
        loginBtn.setEnabled(true);
    }

    private void readPlayerNameFromSharedPreferences() {
        String playerName = sharedPreferences.getString(PLAYERNAME, "");
        if (!playerName.isEmpty())
        {
            nameEd.setText(playerName);
            autoSignInCheckbox.setChecked(true);
        }
    }

    private void loadServerInformationIn10Secs(){
        handler.postDelayed(()->{
            signingModule.getServerInformation();
            loadServerInformationIn10Secs();
        }, TimeUnit.SECONDS.toMillis(10));
    }

    public void loginButtonOnClick(View view) {
        String playerName = nameEd.getText().toString();
        signingModule.signIn(playerName);
    }

    public void randomNameButtonOnClick(View view) {
        nameEd.setText(nameCreator.createRandomName());
    }

    @Override
    public void onSignInSuccessfully(@NonNull Player player) {
        loginBtn.setEnabled(true);
        savePlayerNameToSharedPreferences(autoSignInCheckbox.isChecked() ? nameEd.getText().toString() : "");
        Intent intent = new Intent(this, RoomListActivity.class);
        intent.putExtra(PLAYER, player);
        startActivity(intent);
    }

    private void savePlayerNameToSharedPreferences(String name) {
        sharedPreferences.edit()
                .putString(PLAYERNAME, name)
                .apply();
    }

    @Override
    public void onSignInFailed() {
        createAndShowErrorMessage(getString(R.string.signInFailedPlayerNameIsInvalid));
    }

    @Override
    public void onError(@NonNull Throwable err) {
        loginBtn.setEnabled(true);
        if (err instanceof ConnectionTimedOutException)
            createAndShowErrorMessage(getString(R.string.signInFailed_pleaseCheckYourNetwork));
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
    public void onLoadServerInformation(ServerInformation serverInformation) {
        int roomAmount = serverInformation.getOnlineRoomAmount();
        int onlineAmount = serverInformation.getOnlineUserAmount();
        serverStatusTxt.setText(getString(R.string.serverStatus, roomAmount, onlineAmount));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "OnStop.");
        handler.removeCallbacks(null);
        signingModule.unregisterCallBack(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy, shut down the connection.");
        CoreGameServer.getInstance().shutdownConnection();
    }

}
