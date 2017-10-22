package com.ood.clean.waterball.a1a2bsdk.mock;

import android.os.Handler;

import com.ood.clean.waterball.a1a2bsdk.core.model.ChatMessage;
import com.ood.clean.waterball.a1a2bsdk.core.model.Player;
import com.ood.clean.waterball.a1a2bsdk.core.modules.game.game1a2b.duel.Duel1A2BGameModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.game.game1a2b.duel.model.Game1A2BDuelStatus;

import java.util.List;


public class MockDuel1A2BGameModule implements Duel1A2BGameModule {
    private Duel1A2BGameModule.Callback callback;
    private Game1A2BDuelStatus gameStatus = new Game1A2BDuelStatus();
    private String player1Answer;
    private String player2Answer;
    private List<ChatMessage> chatMessages;
    private Handler handler = new Handler();

    @Override
    public void sendChatMessage(ChatMessage message) {
        chatMessages.add(message);
        callback.onMessageReceived(message);
    }

    @Override
    public void registerCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void unregisterCallBack(Callback callback) {
        this.callback = null;
    }

    @Override
    public void setPlayerAnswer(Player player, String answer) {
        gameStatus.updateStatus(player, gameStatus.new StatusOfPlayer());
        Game1A2BDuelStatus.StatusOfPlayer status = gameStatus.getStatusOfPlayer(player);
        player1Answer = answer;
    }

    @Override
    public void loadGameStatus() {

    }

    @Override
    public void guess(String guess) {

    }
}
