package com.example.joanna_zhang.test;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.MockUserSigningModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gamecore.entity.ChatMessage;
import gamecore.entity.Player;

public class ChatWindowView implements View.OnClickListener{

    private Activity activity;
    private EditText inputMessageEdt;
    private ListView chatWindowLst;
    private ImageButton sendMessageImgBtn;
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private List<OnClickListener> onClickListeners = new ArrayList<>();

    private ChatWindowView(Activity activity) {
        this.activity = activity;
        inputMessageEdt = activity.findViewById(R.id.inputChattingTxt);
        chatWindowLst = activity.findViewById(R.id.chatwindowLst);
        sendMessageImgBtn = activity.findViewById(R.id.sendMessageBtn);
        sendMessageImgBtn.setOnClickListener(this);
    }

    private void update(ChatMessage chatMessage) {
        for (OnClickListener onClickListener : onClickListeners)
            onClickListener.onClick(chatMessage);

        chatMessages.add(chatMessage);
        ChatWindowAdapter adapter = new ChatWindowAdapter();
        chatWindowLst.setAdapter(adapter);
        chatWindowLst.setSelection(chatWindowLst.getCount() - 1);
    }

    private void sendMessage(Player poster, String content) {
        //todo 請修正 chattmessage的建構子多了 gameroom 參數
        ChatMessage chatMessage = new ChatMessage(null, poster, content);
        update(chatMessage);
    }

    @Override
    public void onClick(View view) {
        String content = inputMessageEdt.getText().toString();
        if (!content.isEmpty()) {
            sendMessage(getCurrentPlayer(), content);
            inputMessageEdt.setText("");
        }
    }

    private Player getCurrentPlayer() {
        CoreGameServer server = CoreGameServer.getInstance();
        UserSigningModule signingModule = (MockUserSigningModule) server.getModule(ModuleName.SIGNING);
        return signingModule.getCurrentPlayer();
    }

    public static class Builder {

        private ChatWindowView chatWindowView;

        public Builder(Activity activity) {
            chatWindowView = new ChatWindowView(activity);
        }

        public Builder setBackgroundColor(int id) {
            chatWindowView.activity.findViewById(R.id.chatwindowLst).setBackgroundColor(id);
            return this;
        }

        public Builder addOnSendMessageOnClickListener(OnClickListener onClickListener) {
            chatWindowView.onClickListeners.add(onClickListener);
            return this;
        }

        public ChatWindowView build() {
            return chatWindowView;
        }


    }

    public interface OnClickListener {
        void onClick(ChatMessage chatMessage);
    }

    private class ChatWindowAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return chatMessages.size();
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
            view = LayoutInflater.from(activity).inflate(R.layout.chatwindow_list_item, viewGroup, false);

            TextView contentTxt = view.findViewById(R.id.contentTxt);
            TextView timeTxt = view.findViewById(R.id.timeTxt);

            String name = chatMessages.get(i).getPoster().getName();
            String content = chatMessages.get(i).getContent();
            contentTxt.setText(name + " : " + content);

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Date date = chatMessages.get(i).getPostDate();
            timeTxt.setText(dateFormat.format(date));

            return view;
        }

    }

}
