package com.example.joanna_zhang.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joanna_zhang.test.Utils.AppDialogFactory;
import com.example.joanna_zhang.test.Utils.RandomNameCreator;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.ConnectionTimedOutException;
import com.ood.clean.waterball.a1a2bsdk.core.client.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import container.core.Constants;
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
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private boolean serverConntected = false;

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
        nameEd = findViewById(R.id.inputName);
        loginBtn = findViewById(R.id.loginButton);
        autoSignInCheckbox = findViewById(R.id.checkbox);
        serverStatusTxt = findViewById(R.id.serverStatus);
        progressBar = findViewById(R.id.progressbar);
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
        if (serverConntected)
            signingModule.getServerInformation();
    }

    @Override
    public void onServerReconnected() {
        Log.v(TAG, "onServerReconnected.");
        serverConntected = true;
        signingModule.getServerInformation();
        readPlayerNameFromSharedPreferences();
    }

    private void readPlayerNameFromSharedPreferences() {
        String playerName = sharedPreferences.getString(PLAYERNAME, "");
        if (!playerName.isEmpty())
        {
            nameEd.setText(playerName);
            autoSignInCheckbox.setChecked(true);
        }
    }

    public void signInButtonOnClick(View view) {
        setLoading(true);
        if (!CoreGameServer.getInstance().hasConnectedToServer())
        {
            Toast.makeText(getApplicationContext(), R.string.tryingToReconnectInternet, Toast.LENGTH_SHORT).show();
            CoreGameServer.getInstance().startEngine(this);
        }
        else
            signingModule.signIn(nameEd.getText().toString());
    }

    private void setLoading(boolean signing){
        loginBtn.setEnabled(!signing);
        progressBar.setVisibility(signing ? View.VISIBLE : View.INVISIBLE);
    }

    public void randomNameButtonOnClick(View view) {
        nameEd.setText(RandomNameCreator.createRandomName());
    }

    @Override
    public void onSignInSuccessfully(@NonNull Player player) {
        setLoading(false);
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
        AppDialogFactory.errorDialog(this, getString(R.string.signInFailedPlayerNameIsInvalid)).show();
        loginBtn.setEnabled(true);
    }

    @Override
    public void onError(@NonNull Throwable err) {
        loginBtn.setEnabled(true);
        if (err instanceof ConnectionTimedOutException)
            AppDialogFactory.internetErrorDialog(this).show();
    }

    @Override
    public void onLoadServerInformation(ServerInformation serverInformation) {
        int roomAmount = serverInformation.getOnlineRoomAmount();
        int onlineAmount = serverInformation.getOnlineUserAmount();
        serverStatusTxt.setText(getString(R.string.serverStatus, roomAmount, onlineAmount));
        if (serverInformation.getServerVersion() != Constants.VERSION)
            createAndShowDialogAskingToDownloadTheLatestVersion(serverInformation);
        setLoading(false);
    }

    private void createAndShowDialogAskingToDownloadTheLatestVersion(ServerInformation serverInformation) {
        Log.d(TAG, "the app version is not matched to the server, asking to download.");
        //TODO create and show the dialog asking to download the latest version from 'google play'
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "OnStop.");
        signingModule.unregisterCallBack(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy, shut down the connection.");
        CoreGameServer.getInstance().shutdownConnection();
    }

}
