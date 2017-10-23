package com.example.joanna_zhang.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.UpdateLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ood.clean.waterball.a1a2bsdk.core.model.ChatMessage;

import java.util.List;

public class DuelActivity extends AppCompatActivity implements ChatWindowView.OnClickListener{

    private ChatWindowView chatWindowView;
    private TextView p1NameTxt, p2NameTxt, p1AnswerTxt, p2AnswerTxt;
    private ListView p1ResultLst, p2ResultLst;
    private List<> p1ResultList, p2ResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_duel);

        setupChatWindow();
        findViews();
    }

    private void findViews() {
        p1NameTxt = (TextView) findViewById(R.id.p1NameTxt);
        p2NameTxt = (TextView) findViewById(R.id.p2NameTxt);
        p1AnswerTxt = (TextView) findViewById(R.id.p1AnswerTxt);
        p2AnswerTxt = (TextView) findViewById(R.id.p2AnswerTxt);
        p1ResultLst = (ListView) findViewById(R.id.p1ResultLst);
        p2ResultLst = (ListView) findViewById(R.id.p2ResultLst);
    }

    public void setupChatWindow() {
        chatWindowView = new ChatWindowView.Builder(this)
                .addOnSendMessageOnClickListener(this)
                .build();
    }

    public void updateResultList(List<> resultList, ListView resultListView) {
        ResultListAdapter adapter = new ResultListAdapter(resultList);
        resultListView.setAdapter(adapter);
        resultListView.setSelection(resultListView.getCount() - 1);
    }

    @Override
    public void onClick(ChatMessage chatMessage) {

    }

    private class ResultListAdapter extends BaseAdapter {

        private List<> resultLsit;

        public ResultListAdapter(List<> resultLsit) {
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

            TextView result = (TextView) findViewById(android.R.id.text1);

            String guessNumber = resultLsit

            result.setText();

            return null;
        }
    }
}
