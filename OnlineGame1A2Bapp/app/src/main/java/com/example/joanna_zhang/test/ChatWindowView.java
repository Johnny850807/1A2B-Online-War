package com.example.joanna_zhang.test;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
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
import com.ood.clean.waterball.a1a2bsdk.core.modules.ChatModule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;



public class ChatWindowView implements View.OnClickListener, ChatModule.Callback{
    private static final String TAG = "ChatWindowView";
    private Activity activity;
    private ChatModule chatModule;
    private GameRoom gameRoom;
    private Player poster;
    private EditText inputMessageEdt;
    private ListView chatWindowLst;
    private ImageButton sendMessageImgBtn;
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private ChatMessageListener listener;
    private ChatWindowAdapter adapter = new ChatWindowAdapter();

    private ChatWindowView(Activity activity, GameRoom gameRoom, Player poster) {
        this.activity = activity;
        this.gameRoom = gameRoom;
        this.poster = poster;
        chatModule = (ChatModule) CoreGameServer.getInstance().createModule(ModuleName.CHAT);
        inputMessageEdt = activity.findViewById(R.id.inputChattingTxt);
        chatWindowLst = activity.findViewById(R.id.chatwindowLst);
        sendMessageImgBtn = activity.findViewById(R.id.sendMessageBtn);
        sendMessageImgBtn.setOnClickListener(this);
        chatWindowLst.setAdapter(adapter);
    }

    public void onResume() {
        Log.i(TAG, "ChatWindowView onResume");
        chatModule.registerCallback(poster, gameRoom, this);
    }

    public void onStop() {
        Log.i(TAG, "ChatWindowView onStop");
        chatModule.unregisterCallBack(this);
    }

    private void update(ChatMessage chatMessage) {
        listener.onChatMessageUpdate(chatMessage);
        chatMessages.add(chatMessage);
        adapter.notifyDataSetChanged();
        chatWindowLst.setSelection(chatWindowLst.getCount() - 1);
    }

    private void sendMessage(Player poster, String content) {
        ChatMessage chatMessage = new ChatMessage(gameRoom, poster, content);
        chatModule.sendMessage(chatMessage);
    }

    @Override
    public void onClick(View view) {
        String content = inputMessageEdt.getText().toString();
        if (!content.isEmpty()) {
            sendMessage(poster, content);
            inputMessageEdt.setText("");
        }
    }

    @Override
    public void onMessageReceived(ChatMessage message) {
        update(message);
    }

    @Override
    public void onMessageSent(ChatMessage message) {

    }

    @Override
    public void onMessageSendingFailed(ChatMessage message) {
        listener.onMessageSendingFailed(message);
    }

    @Override
    public void onError(@NonNull Throwable err) {
        Log.d("error", "error", err);
        listener.onChatMessageError(err);
    }

    public static class Builder {

        private ChatWindowView chatWindowView;

        public Builder(Activity activity, GameRoom gameRoom, Player poster) {
            chatWindowView = new ChatWindowView(activity, gameRoom, poster);
        }

        public Builder setBackgroundColor(int id) {
            chatWindowView.chatWindowLst.setBackgroundColor(id);
            return this;
        }

        public Builder addOnSendMessageOnClickListener(ChatMessageListener chatMessageListener) {
            chatWindowView.listener = chatMessageListener;
            return this;
        }

        public ChatWindowView build() {
            return chatWindowView;
        }


    }

    public interface ChatMessageListener {
        void onChatMessageUpdate(ChatMessage chatMessage);
        void onMessageSendingFailed(ChatMessage chatMessage);
        void onChatMessageError(Throwable err);
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

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date date = chatMessages.get(i).getPostDate();
            timeTxt.setText(dateFormat.format(date));

            return view;
        }

    }

}
