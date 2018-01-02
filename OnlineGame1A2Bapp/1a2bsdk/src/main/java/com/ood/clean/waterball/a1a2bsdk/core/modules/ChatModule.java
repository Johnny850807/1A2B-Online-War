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

        /**
         * '1'
         */
        public void onOkMessage(ChatMessage message);

        /**
         * '2'
         */
        public void onNoMessage(ChatMessage message);

        /**
         * '3'
         */
        public void onAwesomeMessage(ChatMessage message);

        /**
         * '4'
         */
        public void onQuicklyMessage(ChatMessage message);

        /**
         * '5'
         */
        public void onDamnMessage(ChatMessage message);

        /**
         * '6'
         */
        public void onGoodGameMessage(ChatMessage message);

        /**
         * '7'
         */
        public void onPleaseSetReadyMessage(ChatMessage message);

        /**
         * '8'
         */
        public void onPleaseStartMessage(ChatMessage message);

        /**
         * '9'
         */
        public void onSorryMessage(ChatMessage message);
    }
}
