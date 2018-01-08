package com.example.joanna_zhang.test.view.activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.joanna_zhang.test.R;
import com.example.joanna_zhang.test.Utils.AppDialogFactory;
import com.example.joanna_zhang.test.Utils.SoundManager;
import com.example.joanna_zhang.test.animations.CostingProgressBarAnimation;
import com.example.joanna_zhang.test.view.dialog.InputNumberDialog;
import com.example.joanna_zhang.test.animations.FadingNumberEffectAnimation;
import com.example.joanna_zhang.test.view.myview.PlayerSpiritItemViewFactory;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.client.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.modules.games.a1b2.boss.Boss1A2BModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import container.core.Constants;
import gamecore.model.ContentModel;
import gamecore.model.ErrorMessage;
import gamecore.model.games.GameOverModel;
import gamecore.model.games.a1b2.boss.core.AbstractSpirit;
import gamecore.model.games.a1b2.boss.core.AttackActionModel;
import gamecore.model.games.a1b2.boss.core.AttackResult;
import gamecore.model.games.a1b2.boss.core.Monster;
import gamecore.model.games.a1b2.boss.core.NextTurnModel;
import gamecore.model.games.a1b2.boss.core.PlayerSpirit;
import gamecore.model.games.a1b2.boss.core.SpiritsModel;
import gamecore.model.games.a1b2.core.A1B2NumberValidator;
import gamecore.model.games.a1b2.core.GuessResult;
import gamecore.model.games.a1b2.core.NumberNotValidException;

import static android.R.string.cancel;
import static com.example.joanna_zhang.test.R.string.confirm;

public class BossFight1A2BActivity extends OnlineGameActivity implements Boss1A2BModule.Callback, SpiritsModel.OnAttackActionRender {
    private final static String TAG = "BossFight1A2BActivity";
    private Handler handler = new Handler();

    private RelativeLayout containerView;
    private Button inputNumberBtn;
    private ImageButton sendGuessBtn;
    private ImageView bossImg;
    private ListView attackResultListView;
    private GuessResultAdapter guessResultAdapter;
    private ProgressBar bossHpProgressBar;

    private HorizontalScrollView playerSpiritsHorizontalScrollView;
    private LinearLayout playerSpiritsViewGroup;
    private PlayerSpiritItemViewFactory playerSpiritItemViewFactory;
    private Map<String, PlayerSpiritItemViewFactory.ViewHolder> playerSpiritViewHoldersMap = new HashMap<>();  //<player's id, view holder>

    private AlertDialog inputNumberDialog;
    private AlertDialog waitingForPlayersEnteringDialog;

    private MediaPlayer nowPlayer;
    private MediaPlayer song1Player;
    private MediaPlayer song2Player;  //the song when the boss's hp is left half
    private SoundManager soundManager;

    private Boss1A2BModule boss1A2BModule;
    private SpiritsModel spiritsModel;
    private List<AttackResult> attackResults = new ArrayList<>();
    private AbstractSpirit whosTurn;  //the turn of the player's being, used for blocking the invalid attacking request

    private boolean gameStarted = false;
    private boolean gameover = false;
    private boolean attackingStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_fight1_a2_b);
        init();
        findViews();
        loadBossGif(R.drawable.lucid_half);
        setupLayout();
        setUpInputNumberWindowView();
    }

    private void init() {
        song1Player = MediaPlayer.create(this, R.raw.lucid_fight_1);
        song2Player = MediaPlayer.create(this, R.raw.lucid_fight_2);
        song1Player.setLooping(true);
        song2Player.setLooping(true);
        soundManager = new SoundManager(this);
        CoreGameServer server = CoreGameServer.getInstance();
        boss1A2BModule = (Boss1A2BModule) server.createModule(ModuleName.GAME1A2BBOSS);
        playerSpiritItemViewFactory = new PlayerSpiritItemViewFactory(this);
        waitingForPlayersEnteringDialog = AppDialogFactory.createWaitingForPlayersEnteringDialog(this);
    }

    private void findViews() {
        playerSpiritsHorizontalScrollView = findViewById(R.id.playerSpiritsHorizontalScrollView);
        containerView = findViewById(R.id.container);
        bossImg = findViewById(R.id.bossImg);
        inputNumberBtn = findViewById(R.id.inputNumberBtn);
        sendGuessBtn = findViewById(R.id.sendGuessBtn);
        bossHpProgressBar = findViewById(R.id.bossHpProgressBar);
        attackResultListView = findViewById(R.id.attackResultsLst);
        playerSpiritsViewGroup = findViewById(R.id.playerSpiritsViewGroup);
    }

    private void loadBossGif(@DrawableRes int gifId) {
        Glide.with(this).load(gifId).asGif().placeholder(R.drawable.lucid_half_static)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).fitCenter().into(bossImg);
    }

    private void setupLayout() {
        setupAttackResultListView();
    }

    private void setupAttackResultListView() {
        guessResultAdapter = new GuessResultAdapter();
        attackResultListView.setAdapter(guessResultAdapter);
    }

    private void setUpInputNumberWindowView() {
        inputNumberDialog = new InputNumberDialog.Builder(this)
                .setOnEnterClickListener(guessNumber -> inputNumberBtn.setText(guessNumber))
                .setTitle(getString(R.string.pleaseInputGuess))
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume");
        boss1A2BModule.registerCallback(this, currentPlayer, currentGameRoom, this);
        CoreGameServer.getInstance().resendUnhandledEvents();

        if (!gameStarted) {
            Log.d(TAG, "show waiting dialog and enter the game.");
            waitingForPlayersEnteringDialog.show();
            boss1A2BModule.enterGame();
        } else if (attackingStarted)  //only play the music while attacking started
            nowPlayer.start();
    }

    public void inputNumberOnClick(View view) {
        inputNumberDialog.show();
    }

    public void onSendGuessNumberBtnClick(View view) {
        String guessNumber = inputNumberBtn.getText().toString();
        try {
            A1B2NumberValidator.validateNumber(guessNumber);
            setInputNumberViewsEnabled(false);
            boss1A2BModule.attack(guessNumber);
        } catch (NumberNotValidException err) {
            Toast.makeText(this, R.string.numberShouldBeInLengthFour, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGameStarted(SpiritsModel spiritsModel) {
        Log.d(TAG, "OnGameStarted.");
        this.gameStarted = true;
        this.spiritsModel = spiritsModel;
        waitingForPlayersEnteringDialog.dismiss();
        spiritsModel.setAttackDrawDelayTime(790);
        spiritsModel.setOnAttackActionParsingListener(this);
        showDialogForSettingAnswer();
        bossHpProgressBar.setMax(spiritsModel.getBoss().getMaxHp());
        bossHpProgressBar.setProgress(spiritsModel.getBoss().getHp());
        bossHpProgressBar.setScaleY(3f);
        createAllPlayerSpiritViews(spiritsModel);
    }

    private void createAllPlayerSpiritViews(SpiritsModel spiritsModel) {
        playerSpiritItemViewFactory = new PlayerSpiritItemViewFactory(this);
        for (PlayerSpirit playerSpirit : spiritsModel.getPlayerSpirits()) {
            PlayerSpiritItemViewFactory.ViewHolder viewHolder = playerSpiritItemViewFactory.createAbstractSpiritItemView(playerSpirit, playerSpiritsViewGroup);
            playerSpiritsViewGroup.addView(viewHolder.view);
            playerSpiritViewHoldersMap.put(playerSpirit.getId(), viewHolder);
        }
    }

    private void showDialogForSettingAnswer() {
        Log.d(TAG, "showDialogForSettingAnswer");
        new InputNumberDialog.Builder(this)
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
        nowPlayer = song1Player;
        nowPlayer.start();
        Toast.makeText(this, R.string.bossGameStarted, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSetAnswerSuccessfully(ContentModel contentModel) {
        Log.v(TAG, contentModel.getPlayerId() + "set answer successfully -> " + contentModel.getContent());
        playerSpiritViewHoldersMap.get(contentModel.getPlayerId()).playerAnswer.setText(contentModel.getContent());
    }

    @Override
    public void onSetAnswerUnsuccessfully(ErrorMessage errorMessage) {
        Log.e(TAG, errorMessage.getMessage());
        Toast.makeText(this, R.string.setAnswerUnSuccessfully, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttackSuccessfully(ContentModel contentModel) {

    }

    @Override
    public void onAttackUnsuccessfully(ErrorMessage errorMessage) {
        Log.e(TAG, errorMessage.getMessage());
        setInputNumberViewsEnabled(true);
        Toast.makeText(this, R.string.pleaseReAttack, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNextAttackAction(AttackActionModel attackActionModel) {
        spiritsModel.updateHPMPFromTheAttackActionModelAsync(attackActionModel);
    }

    @Override
    public void onNextTurn(NextTurnModel nextTurnModel) {
        this.whosTurn = nextTurnModel.getWhosTurn();

        drawhighlightOnTheTurnPlayerSpirit();

        if (whosTurn.getId().equals(currentPlayer.getId())) {
            setInputNumberViewsEnabled(true);
            inputNumberBtn.setText(null);
            soundManager.playSound(R.raw.dong);
        } else {
            setInputNumberViewsEnabled(false);
        }
    }

    @Override
    public void onBossAnswerChanged(Monster boss) {
        Toast.makeText(this, getString(R.string.theBossChangedHisAnswer, boss.getName()), Toast.LENGTH_SHORT).show();
    }

    private void drawhighlightOnTheTurnPlayerSpirit() {
        for (PlayerSpiritItemViewFactory.ViewHolder viewHolder : playerSpiritViewHoldersMap.values())
            viewHolder.view.setBackgroundColor(Color.GRAY);

        playerSpiritViewHoldersMap.get(whosTurn.getId()).view.setBackgroundResource(R.drawable.boss1a2b_player_background);
    }

    private void setInputNumberViewsEnabled(boolean enabled) {
        inputNumberBtn.setEnabled(enabled);
        sendGuessBtn.setEnabled(enabled);
    }

    @Override
    public void onGameOver(GameOverModel gameOverModel) {
        gameover = true;
        if (gameOverModel.getWinnerId().equals(Constants.Events.Games.Boss1A2B.WinnerId.BOSS_WIN)) {
            AppDialogFactory.createGameoverResultDialogForWinner(this, spiritsModel.getBoss().getName()).show();
            soundManager.playSound(R.raw.lose);
        } else
            AppDialogFactory.createGameoverResultDialogForWinner(this, getString(R.string.players)).show();
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
            TextView result = view.findViewById(R.id.resultTxt);

            GuessResult guessResult = attackResult.getGuessRecord().getResult();
            player.setText(attackResult.getAttacker().getName());
            guess.setText(attackResult.getGuessRecord().getGuess());
            result.setText(guessResult + " -> " + attackResult.getAttacked().getName());

            return view;
        }

    }

    @Override
    public void onDrawHpCosted(AbstractSpirit spirit, int cost) {
        runOnUiThread(() -> {
            ProgressBar hpBar = spirit.getId().equals(spiritsModel.getBoss().getId()) ? bossHpProgressBar : playerSpiritViewHoldersMap.get(spirit.getId()).playerHpBar;
            Log.d(TAG, "onDrawHpCosted: name " + spirit.getName() + ", cost: " + cost);
            int nowHp = hpBar.getProgress();
            CostingProgressBarAnimation animation = new CostingProgressBarAnimation(hpBar, nowHp, nowHp - cost);
            hpBar.startAnimation(animation);

            if (spiritsModel.getBoss().getHp() < spiritsModel.getBoss().getMaxHp() / 2)
                switchToSong2Player();
        });
    }

    private void switchToSong2Player() {
        song1Player.stop();
        nowPlayer = song2Player;
        song2Player.start();
    }

    @Override
    public void onDrawMpCosted(AbstractSpirit spirit, int cost) { /*no need to draw the mp cost currently*/}

    @Override
    public void onDrawNormalAttack(AbstractSpirit attacked, AbstractSpirit attacker, AttackResult attackResult) {
        runOnUiThread(() -> {
            addAttackResultAndUpdate(attackResult);
            animateDamageText(attacked, attackResult);
        });
    }

    @Override
    public void onDrawMagicAttack(AbstractSpirit attacked, AbstractSpirit attacker, AttackResult attackResult) {
        runOnUiThread(()->{
            if (attacker.getId().equals(spiritsModel.getBoss().getId()))
                switchBossImgToMagicAttackingGifAndSwitchBackThen();
            addAttackResultAndUpdate(attackResult);
            animateDamageText(attacked, attackResult);
        });
    }

    private void switchBossImgToMagicAttackingGifAndSwitchBackThen() {
        loadBossGif(R.drawable.lucid_magic_attack3);
        //load back to the half git roughly when the attacking animation is over (1800ms)
        handler.postDelayed(() -> loadBossGif(R.drawable.lucid_half), 1800);
    }

    private void addAttackResultAndUpdate(AttackResult attackResult) {
        attackResults.add(attackResult);
        guessResultAdapter.notifyDataSetChanged();
        attackResultListView.setSelection(attackResultListView.getCount() - 1);
    }

    private void animateDamageText(AbstractSpirit attacked, AttackResult attackResult) {
        TextView effectTxt = new TextView(this);
        float x, y;
        if (attacked.getId().equals(spiritsModel.getBoss().getId()))
        {
            Log.d(TAG, "Boss damaged animating."); //the number shows above the boss' image
            x = bossImg.getX();
            y = bossImg.getY();
        } else {  //the number shows on the top of that damaged player spirit
            Log.d(TAG, "Player " + attacked.getName() + " damaged animating.");
            x = playerSpiritViewHoldersMap.get(attacked.getId()).view.getX();
            y = playerSpiritsHorizontalScrollView.getY() - 32;
        }

        Log.d(TAG, "Target view (" + x + "," + y + ")");
        effectTxt.setX(x + 35);  //add some biases
        effectTxt.setY(y);
        effectTxt.setText(String.valueOf(attackResult.getDamage()));
        FadingNumberEffectAnimation animation = new FadingNumberEffectAnimation(containerView, effectTxt);
        animation.setTextSize(45);
        effectTxt.startAnimation(animation);
    }

    @Override
    public void onServerReconnected() {}

    @Override
    public void onError(@NonNull Throwable err) {
        Log.e(TAG, err.getMessage());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!gameover)
                showLeftGameDialog();
            else
                finish();
        }
        return false;
    }

    private void showLeftGameDialog() {
        AppDialogFactory.templateBuilder(this)
                .setTitle(R.string.leftGame)
                .setMessage(R.string.sureToLeftGame)
                .setPositiveButton(confirm, (d, i) -> {
                    boss1A2BModule.leaveGame();
                    finish();
                })
                .setNegativeButton(cancel, null)
                .show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        boss1A2BModule.unregisterCallBack(this);

        if (attackingStarted)
            nowPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        song1Player.release();
        song2Player.release();
        soundManager.release();
        handler.removeCallbacks(null);
    }
}
