package com.example.joanna_zhang.test.view.activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joanna_zhang.test.R;
import com.example.joanna_zhang.test.Utils.AppDialogFactory;
import com.example.joanna_zhang.test.Utils.SoundManager;
import com.example.joanna_zhang.test.animations.ProgressBarAnimation;
import com.example.joanna_zhang.test.view.dialog.InputNumberWindowDialog;
import com.example.joanna_zhang.test.view.myview.PlayerSpiritItemViewFactory;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.client.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.modules.games.a1b2.boss.Boss1A2BModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gamecore.entity.GameRoom;
import gamecore.model.ContentModel;
import gamecore.model.ErrorMessage;
import gamecore.model.PlayerRoomModel;
import gamecore.model.games.GameOverModel;
import gamecore.model.games.a1b2.boss.core.AbstractSpirit;
import gamecore.model.games.a1b2.boss.core.AttackActionModel;
import gamecore.model.games.a1b2.boss.core.AttackResult;
import gamecore.model.games.a1b2.boss.core.NextTurnModel;
import gamecore.model.games.a1b2.boss.core.SpiritsModel;
import gamecore.model.games.a1b2.core.A1B2NumberValidator;
import gamecore.model.games.a1b2.core.GuessResult;
import gamecore.model.games.a1b2.core.NumberNotValidException;

/**
 * TODO
 * (1) dialog: sure to leave from the game? (let's see we have to do this in every game, so why not to make a online game base activity for all such these operations?)
 */
public class BossFight1A2BActivity extends BaseAbstractActivity implements Boss1A2BModule.Callback, SpiritsModel.OnAttackActionRender{
    private final static String TAG = "BossFight1A2BActivity";

    private Button inputNumberBtn;
    private ImageButton sendGuessBtn;
    private ImageView bossImg;
    private ListView attackResultListView;
    private GuessResultAdapter guessResultAdapter;
    private ProgressBar progressBar;

    private LinearLayout playerSpiritsViewGroup;
    private PlayerSpiritItemViewFactory playerSpiritItemViewFactory;
    private Map<String, PlayerSpiritItemViewFactory.ViewHolder> playerSpiritViewHoldersMap = new HashMap<>();  //<player's id, view holder>

    private AlertDialog inputNumberWindowDialog;  //TODO RENAME
    private AlertDialog waitingForPlayersEnteringDialog;

    private MediaPlayer mediaPlayer;
    private SoundManager soundManager;

    private Boss1A2BModule boss1A2BModule;
    private SpiritsModel spiritsModel;
    private List<AttackResult> attackResults = new ArrayList<>();
    private AbstractSpirit whosTurn;  //the turn of the player's being, used for blocking the invalid attacking request

    private boolean gameStarted = false;
    private boolean attackingStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_fight1_a2_b);
        init();
        findViews();
        setupLayout();
        setUpInputNumberWindowView();
    }

    private void init() {
        mediaPlayer = MediaPlayer.create(this, R.raw.king_boss_op);
        soundManager = new SoundManager(this);
        CoreGameServer server = CoreGameServer.getInstance();
        boss1A2BModule = (Boss1A2BModule) server.createModule(ModuleName.GAME1A2BBOSS);
        playerSpiritItemViewFactory = new PlayerSpiritItemViewFactory(this);
        waitingForPlayersEnteringDialog = AppDialogFactory.createWaitingForPlayersEnteringDialog(this);
    }

    private void findViews() {
        bossImg = findViewById(R.id.bossImg);
        inputNumberBtn = findViewById(R.id.inputNumberBtn);
        sendGuessBtn = findViewById(R.id.sendGuessBtn);
        progressBar = findViewById(R.id.bossHpProgressBar);
        attackResultListView = findViewById(R.id.attackResultsLst);
        playerSpiritsViewGroup = findViewById(R.id.playerSpiritsViewGroup);
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

    @Override
    protected void onResume() {
        super.onResume();
        boss1A2BModule.registerCallback(this, currentPlayer, currentGameRoom, this);
        CoreGameServer.getInstance().resendUnhandledEvents();

        if (!gameStarted)
        {
            waitingForPlayersEnteringDialog.show();
            boss1A2BModule.enterGame();
        }
        else if (attackingStarted)  //only play the music while attacking started
            mediaPlayer.start();
    }

    public void inputNumberOnClick(View view) {
        inputNumberWindowDialog.show();
    }

    public void onSendGuessNumberBtnClick(View view) {
        String guessNumber = inputNumberBtn.getText().toString();
        try {
            A1B2NumberValidator.validateNumber(guessNumber);
            setInputNumberViewsEnabled(false);
            boss1A2BModule.attack(guessNumber);
        }catch (NumberNotValidException err){
            Toast.makeText(this, R.string.numberShouldBeInLengthFour, Toast.LENGTH_LONG).show();
        }
    }


    @Override //TODO extract to the base online game activity
    public void onPlayerLeft(PlayerRoomModel model) {
        AppDialogFactory.playerLeftFromGameDialog(this, model.getPlayer()).show();
    }

    @Override //TODO extract to the base online game activity
    public void onGameClosed(GameRoom gameRoom) {
        AppDialogFactory.playerLeftFromGameDialog(this, gameRoom.getHost()).show();
    }

    @Override
    public void onGameStarted(SpiritsModel spiritsModel) {
        this.gameStarted = true;
        this.spiritsModel = spiritsModel;
        waitingForPlayersEnteringDialog.dismiss();
        //TODO create all player spirit views from the factory and bind into the viewHolderMaps
        spiritsModel.setOnAttackActionParsingListener(this);
        showDialogForSettingAnswer();
    }

    private void showDialogForSettingAnswer() {
        new InputNumberWindowDialog.Builder(this)
                .setTitle(getString(R.string.setAnswerFirst))
                .setOnEnterClickListener((answer) -> boss1A2BModule.setAnswer(answer))
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .show();
    }

    @Override
    public void onAttackingPhaseStarted() {
        Log.d(TAG, "The attacking phase started.");
        this.attackingStarted = true;
        mediaPlayer.start();
        Toast.makeText(this, R.string.bossGameStarted, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSetAnswerSuccessfully(ContentModel contentModel) {
        Log.v(TAG, contentModel.getPlayerId() + "set answer successfully -> " + contentModel.getContent());
    }

    @Override
    public void onSetAnswerUnsuccessfully(ErrorMessage errorMessage) {
        Log.e(TAG, errorMessage.getMessage());

        //TODO show message with some UX
    }

    @Override
    public void onAttackSuccessfully(ContentModel contentModel) {

    }

    @Override
    public void onAttackUnsuccessfully(ErrorMessage errorMessage) {
        Log.e(TAG, errorMessage.getMessage());
        setInputNumberViewsEnabled(true);
        //TODO show message with some UX
    }

    @Override
    public void onNextAttackAction(AttackActionModel attackActionModel) {
        attackResults = attackActionModel.getAttackResults();  //TODO don't do this! this will clear the last records!
        spiritsModel.updateHPMPFromTheAttackActionModel(attackActionModel);
        guessResultAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNextTurn(NextTurnModel nextTurnModel) {
        this.whosTurn = nextTurnModel.getWhosTurn();

        //TODO handle something that when it's the current player's turn if need, for example: to enable input window..
        if (whosTurn.getId().equals(currentPlayer.getId()))
        {
            setInputNumberViewsEnabled(true);
            soundManager.playSound(R.raw.dong);
        }
        else {
            setInputNumberViewsEnabled(false);
        }
    }

    private void setInputNumberViewsEnabled(boolean enabled){
        inputNumberBtn.setEnabled(enabled);
        sendGuessBtn.setEnabled(enabled);
    }

    @Override
    public void onGameOver(GameOverModel gameOverModel) {

    }

    //TODO use recyclerview instead
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
            view = LayoutInflater.from(BossFight1A2BActivity.this).inflate(R.layout.attack_result_list_item, viewGroup, false);

            AttackResult attackResult = attackResults.get(position);
            TextView player = view.findViewById(R.id.playerNameTxt);
            TextView guess = view.findViewById(R.id.guessNumberTxt);
            TextView result = view.findViewById(R.id.bNumber);

            GuessResult guessResult = attackResult.getGuessRecord().getResult();
            player.setText(attackResult.getAttacker().getName());
            guess.setText(attackResult.getGuessRecord().getGuess());
            //TODO AbstractSpirit.toString will not handle as your expectation, But the GuessResult.toString() will !
            result.setText(guessResult + " -> " + attackResult.getAttacked());

            return view;
        }
    }

    @Override
    public void onDrawHpCosted(AbstractSpirit attacker, int cost) {
        ProgressBar playerHpBar = playerSpiritViewHoldersMap.get(attacker.getId()).playerHpBar;
        int nowHp = playerHpBar.getProgress();
        ProgressBarAnimation animation = new ProgressBarAnimation(playerHpBar, nowHp, nowHp - cost);
        animation.setDuration(cost * 3);
        playerHpBar.startAnimation(animation);
    }

    @Override
    public void onDrawMpCosted(AbstractSpirit attacker, int cost) {}

    @Override
    public void onDrawNormalAttack(AbstractSpirit abstractSpirit, AbstractSpirit abstractSpirit1, AttackResult attackResult) {
        //TODO
    }

    @Override
    public void onDrawMagicAttack(AbstractSpirit abstractSpirit, AbstractSpirit abstractSpirit1, AttackResult attackResult) {
        //TODO
    }


    @Override //TODO extract to the base online game activity
    public void onRoomExpired() {
        AppDialogFactory.roomTimeExpiredDialog(this);
    }

    @Override
    public void onServerReconnected() {}

    @Override
    public void onError(@NonNull Throwable err) {
        Log.e(TAG, err.getMessage());
    }

    @Override
    protected void onStop() {
        super.onStop();
        boss1A2BModule.unregisterCallBack(this);

        if (attackingStarted)
            mediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        soundManager.release();
    }
}
