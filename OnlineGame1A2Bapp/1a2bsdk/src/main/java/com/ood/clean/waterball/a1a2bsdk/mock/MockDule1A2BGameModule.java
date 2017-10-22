package com.ood.clean.waterball.a1a2bsdk.mock;

import com.ood.clean.waterball.a1a2bsdk.core.model.ChatMessage;
import com.ood.clean.waterball.a1a2bsdk.core.model.Player;
import com.ood.clean.waterball.a1a2bsdk.core.modules.game.game1a2b.duel.Duel1A2BGameModule;


public class MockDule1A2BGameModule implements Duel1A2BGameModule {

    @Override
    public void sendChatMessage(ChatMessage message) {

    }

    @Override
    public void registerCallback(Callback callback) {

    }

    @Override
    public void unregisterCallBack(Callback callback) {

    }

    @Override
    public void setPlayerAnswer(Player player, String answer) {

    }

    @Override
    public void guess(String guess) {

    }
}
