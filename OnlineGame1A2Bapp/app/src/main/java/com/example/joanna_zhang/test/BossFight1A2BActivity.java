package com.example.joanna_zhang.test;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.joanna_zhang.test.Utils.AppDialogFactory;
import com.ood.clean.waterball.a1a2bsdk.core.modules.games.a1b2.boss.Boss1A2BModule;

import java.util.ArrayList;
import java.util.List;

import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.PlayerRoomModel;
import gamecore.model.games.a1b2.GuessRecord;

import static com.example.joanna_zhang.test.R.string.confirm;
import static com.example.joanna_zhang.test.Utils.Params.Keys.GAMEROOM;
import static com.example.joanna_zhang.test.Utils.Params.Keys.PLAYER;

public class BossFight1A2BActivity extends AppCompatActivity implements Boss1A2BModule.Callback, InputNumberWindowDialog.OnClickListener{

    private Boss1A2BModule boss1A2BModule;
    private GameRoom currentGameRoom;
    private Player currentPlayer;
    private List<GuessRecord> resultList;
    private InputNumberWindowDialog inputNumberWindowDialog;
    private Button inputNumberBtn;
    private ImageButton sendGuessBtn;
    private GuessResultAdapter guessResultAdapter;
    private ProgressBar progressBar;
    private RecyclerView playerRecyclerView;
    private List<Player> players = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_fight1_a2_b);
        init();
        findViews();
        mockPlayers(); //Test
        setupLayout();
    }

    private void init() {
        currentPlayer = (Player) getIntent().getSerializableExtra(PLAYER);
        currentGameRoom = (GameRoom) getIntent().getSerializableExtra(GAMEROOM);
        guessResultAdapter = new GuessResultAdapter();
    }

    private void findViews() {
        inputNumberBtn = findViewById(R.id.inputNumberBtn);
        sendGuessBtn = findViewById(R.id.sendGuessBtn);
        progressBar = findViewById(R.id.bossHpProgressBar);
        playerRecyclerView = findViewById(R.id.boss1a2bPlayerRecyclerView);
    }

    //Test
    private void mockPlayers(){
        players.add(new Player("Lin"));
        players.add(new Player("Pan"));
        players.add(new Player("WB"));
        players.add(new Player("Joanna"));
    }

    private void setupLayout() {
        setupProgressBar();
        setupPlayerRecyclerView();
    }

    private void setupProgressBar() {
        progressBar.getProgressDrawable().setColorFilter(
                Color.GREEN, PorterDuff.Mode.DARKEN);
        progressBar.setScaleY(3f);
    }

    private void setupPlayerRecyclerView() {
        playerListAdapter playerListAdapter = new playerListAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        playerRecyclerView.setLayoutManager(layoutManager);
        playerRecyclerView.setAdapter(playerListAdapter);
    }

    public void inputNumberOnClick(View view) {
    }

    public void onSendGuessNumberBtnClick(View view) {
    }

    @Override
    public void onRoomExpired() {

    }

    @Override
    public void onServerReconnected() {

    }

    @Override
    public void onError(@NonNull Throwable err) {

    }

    @Override
    public void onNextPlayerTurn() {

    }

    @Override
    public void onAttackingSuccessfully() {

    }

    @Override
    public void onGameOver() {

    }

    @Override
    public void onGameStarted() {
        setupAnswer();
    }

    private void setupAnswer() {
        new InputNumberWindowDialog.Builder(this)
                .setOnEnterClickListener(this)
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .setTitle(getString(R.string.setAnswerFirst))
                .show();
    }

    @Override
    public void onPlayerLeft(PlayerRoomModel model) {
        createAndShowPlayerLeftNotifyingDialog(model.getPlayer());
    }

    private void createAndShowPlayerLeftNotifyingDialog(Player leftPlayeer){
        AppDialogFactory.templateBuilder(this)
                .setTitle(R.string.gameClosed)
                .setMessage(getString(R.string.playerIsAlreadyLeft,leftPlayeer.getName()))
                .setPositiveButton(confirm, (d,i) -> finish())
                .show();
    }

    @Override
    public void onEnterClick(String guessNumber) {

    }


    private class GuessResultAdapter extends BaseAdapter {

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
            view = LayoutInflater.from(BossFight1A2BActivity.this).inflate(R.layout.boss_result_list_item, viewGroup, false);

            TextView player = view.findViewById(R.id.playerNameTxt);
            TextView guess = view.findViewById(R.id.guessNumberTxt);
            TextView result = view.findViewById(R.id.bNumber);


//            player.setText();
//            guess.setText();
//            result.setText();

            return view;
        }
    }

    public class playerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(BossFight1A2BActivity.this).inflate(R.layout.boss1a2b_player_list_item, parent, false);
            return new myViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Player player = players.get(position);
            ((myViewHolder)holder).playerHpBar.getProgressDrawable().setColorFilter(
                    Color.GREEN, PorterDuff.Mode.DARKEN);
            ((myViewHolder)holder).playerHpBar.setScaleY(3f);
            ((myViewHolder)holder).playerName.setText(player.getName());
            ((myViewHolder)holder).playerHp.setText("2000");
        }

        @Override
        public int getItemCount() {
            return players.size();
        }
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ProgressBar playerHpBar;
        TextView playerName;
        TextView playerHp;
        public myViewHolder(View view) {
            super(view);
            playerHpBar = view.findViewById(R.id.boss1a2bPlayerHpProgressBar);
            playerName = view.findViewById(R.id.boss1a2bPlayerNameTxt);
            playerHp = view.findViewById(R.id.boss1a2bPlayerHPTxt);
        }
    }

}
