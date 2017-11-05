package com.ood.clean.waterball.a1a2bsdk.core.base;


import gamecore.entity.ChatMessage;

public interface ChatRoomModule{

    void sendChatMessage(ChatMessage message);

    public interface Callback{
        void onMessageReceived(ChatMessage message);
    }
}
