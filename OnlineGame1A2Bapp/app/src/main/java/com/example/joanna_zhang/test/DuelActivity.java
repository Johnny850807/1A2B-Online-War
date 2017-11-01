package com.example.joanna_zhang.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.model.ChatMessage;
import com.ood.clean.waterball.a1a2bsdk.core.model.Player;
import com.ood.clean.waterball.a1a2bsdk.core.modules.game.game1a2b.duel.Duel1A2BGameModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.game.game1a2b.duel.model.Game1A2BDuelStatus;
import com.ood.clean.waterball.a1a2bsdk.core.modules.game.model.GuessRecord;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;
import com.ood.clean.waterball.a1a2bsdk.mock.MockUserSigningModule;

import java.util.List;

public class DuelActivity extends AppCompatActivity implements ChatWindowView.OnClickListener, InputNumberWindowView.OnClickListener{


    private Duel1A2BGameModule duel1A2BGameModule;
    private ChatWindowView chatWindowView;
    private InputNumberWindowView inputNumberWindowView;
    private TextView p1NameTxt, p2NameTxt, p1AnswerTxt, p2AnswerTxt;
    private ListView p1ResultListView, p2ResultListView;
    private List<GuessRecord> p1ResultList, p2ResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_duel);

        CoreGameServer server = CoreGameServer.getInstance();
        duel1A2BGameModule = (Duel1A2BGameModule) server.getModule(ModuleName.SIGNING);

        setupChatWindow();
        setupInputNumberWindowView();
        findViews();
    }

    private void findViews() {
        p1NameTxt = (TextView) findViewById(R.id.p1NameTxt);
        p2NameTxt = (TextView) findViewById(R.id.p2NameTxt);
        p1AnswerTxt = (TextView) findViewById(R.id.p1AnswerTxt);
        p2AnswerTxt = (TextView) findViewById(R.id.p2AnswerTxt);
        p1ResultListView = (ListView) findViewById(R.id.p1ResultLst);
        p2ResultListView = (ListView) findViewById(R.id.p2ResultLst);
    }

    public void setupChatWindow() {
        chatWindowView = new ChatWindowView.Builder(this)
                .addOnSendMessageOnClickListener(this)
                .build();
    }

    private void setupInputNumberWindowView() {
        inputNumberWindowView = new InputNumberWindowView(this);
    }

    public void updateResultList(List<GuessRecord> resultList, ListView resultListView) {
        ResultListAdapter adapter = new ResultListAdapter(resultList);
        resultListView.setAdapter(adapter);
        resultListView.setSelection(resultListView.getCount() - 1);
    }

    private Player getCurrentPlayer() {
        CoreGameServer server = CoreGameServer.getInstance();
        UserSigningModule signingModule = (MockUserSigningModule) server.getModule(ModuleName.SIGNING);
        return signingModule.getCurrentPlayer();
    }

    @Override
    public void onClick(ChatMessage chatMessage) {

    }

    @Override
    public void onEnterClick(String guessName) {
        p1AnswerTxt.setText(guessName);
    }


    private class ResultListAdapter extends BaseAdapter {

        private List<GuessRecord> resultLsit;

        public ResultListAdapter(List<GuessRecord> resultLsit) {
            this.resultLsit = resultLsit;
        }

        @Override
        public int getCount() {
            return resultLsit.size();
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

            TextView resultTxt = (TextView) findViewById(android.R.id.text1);

            String result = resultLsit.get(i).getGuess() + resultLsit.get(i).getResult();

            resultTxt.setText(result);

            return view;
        }
    }
}
