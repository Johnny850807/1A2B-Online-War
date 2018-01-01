package com.ood.clean.waterball.a1a2bsdk.core.modules;


import com.ood.clean.waterball.a1a2bsdk.core.base.GameCallBack;
import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;

import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ErrorMessage;

public interface ChatModule extends GameModule{
    void registerCallback(Player currentPlayer, GameRoom currentRoom, ChatModule.Callback callback);
    void unregisterCallBack(ChatModule.Callback callback);
    void sendMessage(ChatMessage message);

    public interface Callback extends GameCallBack{

        /**
         * Any message from this room will invoke this method.
         */
        public void onMessageReceived(ChatMessage message);

        /**
         * the message is sent.
         * @param message the sent message.
         */
        public void onMessageSent(ChatMessage message);

        /**
         * the message cannot be sent by some errors.
         */
        public void onMessageSendingFailed(ErrorMessage errorMessage);
    }
}
