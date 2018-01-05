package com.example.joanna_zhang.test.view.myview;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joanna_zhang.test.R;
import com.example.joanna_zhang.test.Utils.MaxSizeLinkedList;
import com.example.joanna_zhang.test.Utils.SoundManager;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.client.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.modules.ChatModule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ErrorMessage;


public class ChatWindowView implements View.OnClickListener, ChatModule.Callback{
    private static final String TAG = "ChatWindowView";
    private List<ChatMessage> chatMessages = new MaxSizeLinkedList<ChatMessage>(30);
    private Activity activity;
    private ChatModule chatModule;
    private GameRoom gameRoom;
    private Player poster;
    private AutoCompleteTextView inputMessageEdt;
    private ListView chatWindowLst;
    private ImageButton sendMessageImgBtn;
    private ChatMessageListener listener;
    private ChatWindowAdapter adapter = new ChatWindowAdapter();
    private SoundManager soundManager;

    private ChatWindowView(Activity activity, GameRoom gameRoom, Player poster) {
        this.activity = activity;
        this.gameRoom = gameRoom;
        this.poster = poster;
        this.soundManager = new SoundManager(activity);
        chatModule = (ChatModule) CoreGameServer.getInstance().createModule(ModuleName.CHAT);
        inputMessageEdt = activity.findViewById(R.id.inputChattingTxt);
        chatWindowLst = activity.findViewById(R.id.chatwindowLst);
        sendMessageImgBtn = activity.findViewById(R.id.sendMessageBtn);
        sendMessageImgBtn.setOnClickListener(this);
        chatWindowLst.setAdapter(adapter);
        setupAutoCompletedInputMessageEditText();
    }

    private void setupAutoCompletedInputMessageEditText(){
        String[] chatUtils = activity.getResources().getStringArray(R.array.chatUtils);
        String[] showTexts = new String[chatUtils.length];
        for (int i = 0 ; i < chatUtils.length ; i ++)
            showTexts[i] = (i+1) + ") " + chatUtils[i];  //for example: (1) OK
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, showTexts);
        inputMessageEdt.setAdapter(adapter);
        inputMessageEdt.setOnClickListener(v -> inputMessageEdt.showDropDown());
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
    public void onServerReconnected() {
        //TODO
    }

    @Override
    public void onMessageReceived(ChatMessage message) {
        update(message);
        soundManager.playSound(R.raw.bo);
    }

    @Override
    public void onMessageSent(ChatMessage message) {
        Log.d(TAG, "onMessageSent");
    }

    @Override
    public void onMessageSendingFailed(ErrorMessage errorMessage) {
        listener.onMessageSendingFailed(errorMessage);
    }

    @Override
    public void onOkMessage(ChatMessage message) {
        message.setContent(activity.getString(R.string.ok));
        soundManager.playSound(R.raw.ok);
    }

    @Override
    public void onNoMessage(ChatMessage message) {
        message.setContent(activity.getString(R.string.notWell));
        soundManager.playSound(R.raw.no);
    }

    @Override
    public void onAwesomeMessage(ChatMessage message) {
        message.setContent(activity.getString(R.string.awesome));
        soundManager.playSound(R.raw.awesome);
    }

    @Override
    public void onQuicklyMessage(ChatMessage message) {
        message.setContent(activity.getString(R.string.quickly));
        soundManager.playSound(R.raw.quickly);
    }

    @Override
    public void onDamnMessage(ChatMessage message) {
        message.setContent(activity.getString(R.string.damnit));
        soundManager.playSound(R.raw.damn);
    }

    @Override
    public void onGoodGameMessage(ChatMessage message) {
        message.setContent(activity.getString(R.string.goodGame));
        soundManager.playSound(R.raw.gg);
    }

    @Override
    public void onPleaseSetReadyMessage(ChatMessage message) {
        message.setContent(activity.getString(R.string.pleaseSetReady));
        soundManager.playSound(R.raw.please_ready);
    }

    @Override
    public void onPleaseStartMessage(ChatMessage message) {
        message.setContent(activity.getString(R.string.pleaseStartGame));
        soundManager.playSound(R.raw.please_start);
    }

    @Override
    public void onSorryMessage(ChatMessage message) {
        message.setContent(activity.getString(R.string.sorry));
        soundManager.playSound(R.raw.sorry);
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
        void onMessageSendingFailed(ErrorMessage errorMessage);
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
