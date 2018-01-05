package com.example.joanna_zhang.test;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joanna_zhang.test.Utils.AppDialogFactory;
import com.example.joanna_zhang.test.mocks.MockClient;
import com.example.joanna_zhang.test.mocks.MockProtocolFactory;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.client.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.modules.games.a1b2.boss.Boss1A2BModule;

import java.util.ArrayList;
import java.util.List;

import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ClientPlayer;
import gamecore.model.ContentModel;
import gamecore.model.ErrorMessage;
import gamecore.model.MockLogger;
import gamecore.model.PlayerRoomModel;
import gamecore.model.games.GameOverModel;
import gamecore.model.games.a1b2.boss.core.AbstractSpirit;
import gamecore.model.games.a1b2.boss.core.AttackActionModel;
import gamecore.model.games.a1b2.boss.core.AttackResult;
import gamecore.model.games.a1b2.boss.core.NextTurnModel;
import gamecore.model.games.a1b2.boss.core.PlayerSpirit;
import gamecore.model.games.a1b2.boss.core.SpiritsModel;
import gamecore.model.games.a1b2.core.A1B2NumberValidator;
import gamecore.model.games.a1b2.core.GuessRecord;
import gamecore.model.games.a1b2.core.NumberNotValidException;


public class BossFight1A2BActivity extends BaseAbstractActivity implements Boss1A2BModule.Callback, SpiritsModel.OnAttackActionRender{

    private final static String TAG = "BossFight1A2BActivity";
    private Boss1A2BModule boss1A2BModule;
    private List<GuessRecord> resultList;
    private List<AttackResult> attackResults = new ArrayList<>();
    private InputNumberWindowDialog inputNumberWindowDialog;
    private Button inputNumberBtn;
    private ImageButton sendGuessBtn;
    private ListView attackResultListView;
    private GuessResultAdapter guessResultAdapter;
    private ProgressBar progressBar;
    private LinearLayout playerSpiritsViewGroup;
    private List<PlayerSpirit> players = new ArrayList<>();
    private PlayerSpiritItemViewFactory playerSpiritItemViewFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_fight1_a2_b);
        init();
        findViews();
        setupLayout();
        setUpInputNumberWindowView();
        testPlayerSpiritViews();
    }

    private void init() {
        CoreGameServer server = CoreGameServer.getInstance();
        boss1A2BModule = (Boss1A2BModule) server.createModule(ModuleName.GAME1A2BBOSS);
        playerSpiritItemViewFactory = new PlayerSpiritItemViewFactory(this);
    }

    private void findViews() {
        inputNumberBtn = findViewById(R.id.inputNumberBtn);
        sendGuessBtn = findViewById(R.id.sendGuessBtn);
        progressBar = findViewById(R.id.bossHpProgressBar);
        playerSpiritsViewGroup = findViewById(R.id.playerSpiritsViewGroup);
        attackResultListView = findViewById(R.id.bossResultsLst);
    }

    private void setupLayout() {
        setupProgressBar();
        setupAttackResultListView();
    }

    private void setupAttackResultListView() {
        guessResultAdapter = new GuessResultAdapter();
        attackResultListView.setAdapter(guessResultAdapter);
    }

    private void setupProgressBar() {
        progressBar.getProgressDrawable().setColorFilter(
                Color.GREEN, PorterDuff.Mode.DARKEN);
        progressBar.setScaleY(3f);
    }


    private void setUpInputNumberWindowView() {
        inputNumberWindowDialog = new InputNumberWindowDialog.Builder(this)
                .setOnEnterClickListener(guessNumber -> inputNumberBtn.setText(guessNumber))
                .setTitle(getString(R.string.pleaseInputGuess))
                .build();
    }

    //TODO please remove this if we don't need it anymore
    private void testPlayerSpiritViews() {
        MockLogger mockLogger = new MockLogger();
        MockProtocolFactory mockProtocolFactory = new MockProtocolFactory();
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");
        Player p3 = new Player("p3");
        Player p4 = new Player("p4");
        Player p5 = new Player("p5");
        players.add(new PlayerSpirit(new ClientPlayer(new MockClient(p1), p1), mockLogger, mockProtocolFactory));
        players.add(new PlayerSpirit(new ClientPlayer(new MockClient(p2), p2), mockLogger, mockProtocolFactory));
        players.add(new PlayerSpirit(new ClientPlayer(new MockClient(p3), p3), mockLogger, mockProtocolFactory));
        players.add(new PlayerSpirit(new ClientPlayer(new MockClient(p4), p4), mockLogger, mockProtocolFactory));
        players.add(new PlayerSpirit(new ClientPlayer(new MockClient(p5), p5), mockLogger, mockProtocolFactory));
        for (PlayerSpirit playerSpirit : players)
        {
            PlayerSpiritItemViewFactory.PlayerSpiritViewHolder  viewHolder = playerSpiritItemViewFactory.createPlayerSpiritItemView(playerSpirit, playerSpiritsViewGroup);
            this.playerSpiritsViewGroup.addView(viewHolder.view);
        }
    }

    public void inputNumberOnClick(View view) {
        inputNumberWindowDialog.show();
    }

    public void onSendGuessNumberBtnClick(View view) {
        String guessNumber = inputNumberBtn.getText().toString();
        try {
            A1B2NumberValidator.validateNumber(guessNumber);
            inputNumberBtn.setEnabled(false);
            sendGuessBtn.setEnabled(false);
            boss1A2BModule.attack(guessNumber);
        }catch (NumberNotValidException err){
            Toast.makeText(this, R.string.numberShouldBeInLengthFour, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRoomExpired() {

    }

    @Override
    public void onServerReconnected() {

    }

    @Override
    public void onError(@NonNull Throwable err) {
        Log.e(TAG, err.getMessage());
    }

    private void setupAnswer() {
        new InputNumberWindowDialog.Builder(this)
                .setOnEnterClickListener(new SettingAnswerOnEnterClickListener())
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .setTitle(getString(R.string.setAnswerFirst))
                .show();
    }

    @Override
    public void onDrawHpCosted(AbstractSpirit abstractSpirit, int i) {

    }

    @Override
    public void onDrawMpCosted(AbstractSpirit abstractSpirit, int i) {

    }

    @Override
    public void onDrawNormalAttack(AbstractSpirit abstractSpirit, AbstractSpirit abstractSpirit1, AttackResult attackResult) {

    }

    @Override
    public void onDrawMagicAttack(AbstractSpirit abstractSpirit, AbstractSpirit abstractSpirit1, AttackResult attackResult) {

    }

    private class SettingAnswerOnEnterClickListener implements InputNumberWindowDialog.OnClickListener {
        @Override
        public void onEnterClick(String guessNumber) {
            boss1A2BModule.setAnswer(guessNumber);
        }
    }

    @Override
    public void onPlayerLeft(PlayerRoomModel model) {
        AppDialogFactory.playerLeftFromGameDialog(this, model.getPlayer());
    }

    @Override
    public void onGameClosed(GameRoom gameRoom) {
        AppDialogFactory.playerLeftFromGameDialog(this, gameRoom.getHost());
    }

    @Override
    public void onGameStarted(SpiritsModel spiritsModel) {
        spiritsModel.setOnAttackActionParsingListener(this);
        players = spiritsModel.getPlayerSpirits();
        setupAnswer();
    }

    @Override
    public void onSetAnswerSuccessfully(ContentModel contentModel) {
        Log.v(TAG, contentModel.getPlayerId() + "set answer successfully");
    }

    @Override
    public void onSetAnswerUnsuccessfully(ErrorMessage errorMessage) {
        Log.e(TAG, errorMessage.getMessage());
    }

    @Override
    public void onAttackSuccessfully(ContentModel contentModel) {

    }

    @Override
    public void onAttackUnsuccessfully(ErrorMessage errorMessage) {
        Log.e(TAG, errorMessage.getMessage());
    }

    @Override
    public void onNextAttackAction(AttackActionModel attackActionModel) {
        for (AttackResult attackResult : attackActionModel) {
            drawAttackResult(attackResult);
            attackResults.add(attackResult);
        }

        guessResultAdapter.notifyDataSetChanged();
    }

    private void drawAttackResult(AttackResult attackResult){

    }

    @Override
    public void onNextTurn(NextTurnModel nextTurnModel) {

    }

    @Override
    public void onGameOver(GameOverModel gameOverModel) {

    }


    private class GuessResultAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return attackResults.size();
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
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(BossFight1A2BActivity.this).inflate(R.layout.boss_result_list_item, viewGroup, false);

            AttackResult attackResult = attackResults.get(position);
            TextView player = view.findViewById(R.id.playerNameTxt);
            TextView guess = view.findViewById(R.id.guessNumberTxt);
            TextView result = view.findViewById(R.id.bNumber);

            player.setText(attackResult.getAttacker().getName());
            guess.setText(attackResult.getGuessRecord().getGuess());
            result.setText(attackResult.getA() + "A" + attackResult.getB() + "B -> " + attackResult.getAttacked());

//            player.setText(resultList.get(i).getAttacker());
//            guess.setText(resultList.get(i).getGuessRecord().getGuess());
//            result.setText();

            return view;
        }
    }

}
