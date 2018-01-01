package com.example.joanna_zhang.test;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joanna_zhang.test.Utils.AppLogoDialogBuilderFactory;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.client.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.modules.games.Duel1A2BModule;

import java.util.ArrayList;
import java.util.List;

import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ContentModel;
import gamecore.model.ErrorMessage;
import gamecore.model.PlayerRoomModel;
import gamecore.model.games.a1b2.A1B2NumberValidator;
import gamecore.model.games.a1b2.Duel1A2BPlayerBarModel;
import gamecore.model.games.a1b2.GameOverModel;
import gamecore.model.games.a1b2.GuessRecord;
import gamecore.model.games.a1b2.NumberNotValidException;

import static android.R.string.cancel;
import static com.example.joanna_zhang.test.R.string.confirm;
import static com.example.joanna_zhang.test.Utils.Params.Keys.GAMEROOM;
import static com.example.joanna_zhang.test.Utils.Params.Keys.PLAYER;

public class DuelActivity extends AppCompatActivity implements ChatWindowView.ChatMessageListener, InputNumberWindowView.OnClickListener, Duel1A2BModule.Callback {

    private Duel1A2BModule duel1A2BModule;
    private android.app.AlertDialog progressDialog;
    private List<GuessRecord> p1ResultList, p2ResultList;
    private GameRoom currentGameRoom;
    private Player currentPlayer;
    private ChatWindowView chatWindowView;
    private InputNumberWindowView inputNumberWindowView;
    private Button inputNumberBtn;
    private ImageButton sendGuessBtn;
    private TextView p1NameTxt, p2NameTxt, p1AnswerTxt, p2AnswerTxt;
    private ListView p1ResultListView, p2ResultListView;
    private GuessResultAdapter p1GuessResultAdapter, p2GuessResultAdapter;
    private Handler handler = new Handler();
    private MediaPlayer mediaPlayer;
    private boolean gameStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_duel);
        mediaPlayer = MediaPlayer.create(this, R.raw.rainsong);
        mediaPlayer.setLooping(true);
        init();
        findViews();
        setUpChatWindow();
        setUpInputNumberWindowView();
        setUpAdapters();
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatWindowView.onResume();
        duel1A2BModule.registerCallback(this, currentPlayer, currentGameRoom, this);
        if (gameStarted)
            mediaPlayer.start();
        else
        {
            waitOtherPlayersPrepare();
            duel1A2BModule.enterGame();
        }
        CoreGameServer.getInstance().resendUnhandledEvents();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showLeftGameDialog();
        }
        return false;
    }

    private void showLeftGameDialog() {
        AppLogoDialogBuilderFactory.create(this)
                .setTitle(R.string.leftGame)
                .setMessage(R.string.sureToLeftGame)
                .setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        duel1A2BModule.leaveGame();
                    }
                })
                .setNegativeButton(cancel, null)
                .show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        duel1A2BModule.unregisterCallBack(this);
        if (gameStarted)
            mediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatWindowView.onStop();
    }

    private void init() {
        CoreGameServer server = CoreGameServer.getInstance();
        duel1A2BModule = (Duel1A2BModule) server.createModule(ModuleName.GAME1A2BDUEL);
        currentPlayer = (Player) getIntent().getSerializableExtra(PLAYER);
        currentGameRoom = (GameRoom) getIntent().getSerializableExtra(GAMEROOM);
        p1GuessResultAdapter = new GuessResultAdapter();
        p2GuessResultAdapter = new GuessResultAdapter();
    }

    private void findViews() {
        inputNumberBtn = findViewById(R.id.inputNumberBtn);
        sendGuessBtn = findViewById(R.id.sendGuessBtn);
        p1NameTxt = findViewById(R.id.p1NameTxt);
        p2NameTxt = findViewById(R.id.p2NameTxt);
        p1AnswerTxt = findViewById(R.id.p1AnswerTxt);
        p2AnswerTxt = findViewById(R.id.p2AnswerTxt);
        p1ResultListView = findViewById(R.id.p1ResultLst);
        p2ResultListView = findViewById(R.id.p2ResultLst);
        p1NameTxt.setText(currentPlayer.getName());
        String p2Name = currentGameRoom.getPlayers().get(0).equals(currentPlayer)?
                currentGameRoom.getPlayers().get(1).getName() : currentGameRoom.getPlayers().get(0).getName();
        p2NameTxt.setText(p2Name);
    }

    private void setUpAdapters() {
        p1ResultListView.setAdapter(p1GuessResultAdapter);
        p2ResultListView.setAdapter(p2GuessResultAdapter);
    }

    private void setupAnswer() {
        new InputNumberWindowView.Builder(this)
                .setOnEnterClickListener(answer -> duel1A2BModule.setAnswer(answer))
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .setTitle(getString(R.string.setAnswerFrist))
                .show();
    }

    public void setUpChatWindow() {
        chatWindowView = new ChatWindowView.Builder(this, currentGameRoom, currentPlayer)
                .addOnSendMessageOnClickListener(this)
                .build();
    }

    private void setUpInputNumberWindowView() {
        inputNumberWindowView = new InputNumberWindowView.Builder(this)
                .setOnEnterClickListener(this)
                .setTitle(getString(R.string.pleaseInputGuess))
                .build();
    }

    private void waitOtherPlayersPrepare() {
        progressDialog = new ProgressDialog.Builder(DuelActivity.this)
                .setCancelable(false)
                .setTitle(getString(R.string.pleaseWait))
                .setMessage(getString(R.string.waitOtherPlayersJoin))
                .show();
    }

    public void updateResultList() {
        p1GuessResultAdapter.setResultList(p1ResultList);
        p2GuessResultAdapter.setResultList(p2ResultList);
        p1GuessResultAdapter.notifyDataSetChanged();
        p2GuessResultAdapter.notifyDataSetChanged();
        p1ResultListView.setSelection(p1ResultListView.getCount() - 1);
        p2ResultListView.setSelection(p2ResultListView.getCount() - 1);
    }

    @Override
    public void onGameStarted() {
        gameStarted = true;
        mediaPlayer.start();
        progressDialog.dismiss();
        setupAnswer();
    }

    @Override
    public void onServerReconnected() {
        //TODO
    }

    @Override
    public void onChatMessageUpdate(ChatMessage chatMessage) {}

    @Override
    public void onMessageSendingFailed(ErrorMessage errorMessage) {}

    @Override
    public void onChatMessageError(Throwable err) {}

    @Override
    public void onEnterClick(String guessNumber) {
        inputNumberBtn.setText(guessNumber);
    }

    public void onSendGuessNumberBtnClick(View view) {
        String guessNumber = inputNumberBtn.getText().toString();
        try{
            A1B2NumberValidator.validateNumber(guessNumber);
            inputNumberBtn.setEnabled(false);
            sendGuessBtn.setEnabled(false);
            duel1A2BModule.guess(guessNumber);
        }catch (NumberNotValidException err){
            Toast.makeText(this, R.string.numberShouldBeInLengthFour, Toast.LENGTH_LONG).show();
        }
    }

    public void inputNumberOnClick(View view) {
        inputNumberWindowView.show();
    }

    @Override
    public void onSetAnswerSuccessfully(ContentModel setAnswerModel) {
        Toast.makeText(this, "You set the number is" + setAnswerModel.getContent(), Toast.LENGTH_SHORT).show();
        p1AnswerTxt.setText(setAnswerModel.getContent());
    }

    @Override
    public void onSetAnswerUnsuccessfully(ErrorMessage errorMessage) {

    }

    @Override
    public void onGuessSuccessfully(ContentModel guessModel) {
        Toast.makeText(this, "Your guess number is" + guessModel.getContent(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGuessUnsuccessfully(ErrorMessage errorMessage) {

    }

    @Override
    public void onGuessingStarted() {
        inputNumberBtn.setEnabled(true);
        sendGuessBtn.setEnabled(true);
        Toast.makeText(this, "開始猜", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOneRoundOver(List<Duel1A2BPlayerBarModel> models) {
        Duel1A2BPlayerBarModel playerBarModel1 = models.get(0);
        Duel1A2BPlayerBarModel playerBarModel2 = models.get(1);
        p1ResultList = playerBarModel1.getPlayerId().equals(currentPlayer.getId()) ? playerBarModel1.getGuessRecords() :
                playerBarModel2.getGuessRecords();
        p2ResultList = playerBarModel1.getPlayerId().equals(currentPlayer.getId()) ? playerBarModel2.getGuessRecords() :
                playerBarModel1.getGuessRecords();
        updateResultList();
        inputNumberBtn.setText(null);
        inputNumberBtn.setEnabled(true);
        sendGuessBtn.setEnabled(true);
    }

    @Override
    public void onGameOver(GameOverModel gameOverModel) {
        Player winner = currentGameRoom.getHost().getId().equals(gameOverModel.getWinnerId()) ?
                currentGameRoom.getHost() : currentGameRoom.getPlayerStatus().get(0).getPlayer();
        inputNumberBtn.setEnabled(false);
        handler.postDelayed(()->createAndShowDialogForWinner(winner), 3000);
    }

    @Override
    public void onOpponentLeft(PlayerRoomModel model) {
        createAndShowPlayerLeftNotifyingDialog(model.getPlayer());
    }

    @Override
    public void onGameClosed(GameRoom gameRoom) {
        createAndShowPlayerLeftNotifyingDialog(gameRoom.getHost());
    }

    private void createAndShowPlayerLeftNotifyingDialog(Player leftPlayeer){
        AppLogoDialogBuilderFactory.create(this)
                .setTitle(R.string.gameClosed)
                .setMessage(getString(R.string.playerIsAlreadyLeft,leftPlayeer.getName()))
                .setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    private void createAndShowDialogForWinner(Player winner){
        AppLogoDialogBuilderFactory.create(this)
                .setTitle(R.string.gameOver)
                .setMessage(getString(R.string.theWinnerIs, winner.getName()))
                .setCancelable(false)
                .setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void onError(@NonNull Throwable err) {
        Log.e("error", "error", err);
        Toast.makeText(this, err.getMessage(), Toast.LENGTH_LONG).show();
    }

    private class GuessResultAdapter extends BaseAdapter {

        private List<GuessRecord> resultList = new ArrayList<>();

        public void setResultList(List<GuessRecord> resultList) {
            this.resultList = resultList;
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(DuelActivity.this).inflate(R.layout.duel_list_item, viewGroup, false);

            TextView guess = view.findViewById(R.id.guessNumber);
            TextView aNumber = view.findViewById(R.id.aNumber);
            TextView bNumber = view.findViewById(R.id.bNumber);

            guess.setText(resultList.get(i).getGuess());
            aNumber.setText(String.valueOf(resultList.get(i).getA()));
            bNumber.setText(String.valueOf(resultList.get(i).getB()));

            return view;
        }
    }

}
