package com.ood.clean.waterball.a1a2bsdk.core.base;


import com.ood.clean.waterball.a1a2bsdk.core.model.ChatMessage;

public interface ChatRoomModule{

    void sendChatMessage(ChatMessage message);

    public interface Callback{
        void onMessageReceived(ChatMessage message);
    }
}
