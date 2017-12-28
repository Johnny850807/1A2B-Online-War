package com.example.joanna_zhang.test;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.modules.games.Duel1A2BModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.RoomListModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import java.util.List;

import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ContentModel;
import gamecore.model.games.a1b2.Duel1A2BPlayerBarModel;
import gamecore.model.games.a1b2.GameOverModel;
import gamecore.model.games.a1b2.GuessRecord;

public class DuelActivity extends AppCompatActivity implements ChatWindowView.ChatMessageListener, InputNumberWindowView.OnClickListener, Duel1A2BModule.Callback {


    private Duel1A2BModule duel1A2BModule;
    private List<GuessRecord> p1ResultList, p2ResultList;
    private GameRoom currentGameRoom;
    private Player currentPlayer;
    private ChatWindowView chatWindowView;
    private InputNumberWindowView inputNumberWindowView;
    private Button inputNumberBtn;
    private TextView p1NameTxt, p2NameTxt, p1AnswerTxt, p2AnswerTxt;
    private ListView p1ResultListView, p2ResultListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_duel);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        duel1A2BModule.registerCallback(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        duel1A2BModule.unregisterCallBack(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void init() {
        setUpRoomInformation();
        setupAnswer();
        findViews();
        setUpChatWindow();
        setUpInputNumberWindowView();
    }

    private void setUpRoomInformation() {
        CoreGameServer server = CoreGameServer.getInstance();
        duel1A2BModule = (Duel1A2BModule) server.getModule(ModuleName.GAME1A2BDUEL);
        currentPlayer = ((UserSigningModule) CoreGameServer.getInstance().getModule(ModuleName.SIGNING)).getCurrentPlayer();
        currentGameRoom = ((RoomListModule) CoreGameServer.getInstance().getModule(ModuleName.ROOMLIST)).getCurrentGameRoom();
    }

    private void findViews() {
        inputNumberBtn = (Button) findViewById(R.id.inputNumberBtn);
        p1NameTxt = (TextView) findViewById(R.id.p1NameTxt);
        p2NameTxt = (TextView) findViewById(R.id.p2NameTxt);
        p1AnswerTxt = (TextView) findViewById(R.id.p1AnswerTxt);
        p2AnswerTxt = (TextView) findViewById(R.id.p2AnswerTxt);
        p1ResultListView = (ListView) findViewById(R.id.p1ResultLst);
        p2ResultListView = (ListView) findViewById(R.id.p2ResultLst);
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
                .setTitle("請輸入答案")
                .build();
    }

    public void updateResultList(List<GuessRecord> resultList, ListView resultListView) {
        GuessResultAdapter adapter = new GuessResultAdapter(resultList);
        resultListView.setAdapter(adapter);
        resultListView.setSelection(resultListView.getCount() - 1);
    }

    @Override
    public void onChatMessageUpdate(ChatMessage chatMessage) {}

    @Override
    public void onMessageSendingFailed(ChatMessage chatMessage) {}

    @Override
    public void onChatMessageError(Throwable err) {}

    @Override
    public void onEnterClick(String guessNumber) {
        inputNumberBtn.setText(guessNumber);
        duel1A2BModule.guess(guessNumber);
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
    public void onSetAnswerUnsuccessfully(ContentModel setAnswerModel) {

    }

    @Override
    public void onGuessSuccessfully(ContentModel guessModel) {
        Toast.makeText(this, "Your guess number is" + guessModel.getContent(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGuessUnsuccessfully(ContentModel guessModel) {

    }

    @Override
    public void onGuessingStarted() {
        inputNumberBtn.setEnabled(true);
        Toast.makeText(this, "開始猜", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOneRoundOver(List<Duel1A2BPlayerBarModel> models) {
        p1ResultList = models.get(0).getGuessRecords();
        p2ResultList = models.get(1).getGuessRecords();
        updateResultList(p1ResultList, p1ResultListView);
        updateResultList(p2ResultList, p2ResultListView);
    }

    @Override
    public void onGameOver(GameOverModel gameOverModel) {
        new AlertDialog.Builder(DuelActivity.this)
                .setTitle(R.string.gameOver)
                .setMessage(gameOverModel.getWinnerId())
                .setIcon(R.drawable.logo)
                .setPositiveButton(R.string.confirm,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void onError(@NonNull Throwable err) {
        Toast.makeText(this, err.getMessage(), Toast.LENGTH_LONG).show();
    }

    private class GuessResultAdapter extends BaseAdapter {

        private List<GuessRecord> resultList;

        GuessResultAdapter(List<GuessRecord> resultList) {
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
            view = LayoutInflater.from(DuelActivity.this).inflate(android.R.layout.simple_list_item_1, viewGroup, false);

            TextView resultTxt = findViewById(android.R.id.text1);
            StringBuilder result = new StringBuilder();
            result.append(resultList.get(i).getGuess())
                    .append('-')
                    .append(resultList.get(i).getResult().toString());
            resultTxt.setText(result);

            return view;
        }
    }

}
