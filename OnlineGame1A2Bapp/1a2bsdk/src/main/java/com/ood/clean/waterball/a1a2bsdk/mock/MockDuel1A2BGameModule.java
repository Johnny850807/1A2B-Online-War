package com.ood.clean.waterball.a1a2bsdk.mock;

import android.os.Handler;

import com.ood.clean.waterball.a1a2bsdk.core.model.ChatMessage;
import com.ood.clean.waterball.a1a2bsdk.core.model.Player;
import com.ood.clean.waterball.a1a2bsdk.core.modules.game.game1a2b.duel.Duel1A2BGameModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.game.game1a2b.duel.model.Game1A2BDuelStatus;
import com.ood.clean.waterball.a1a2bsdk.core.modules.game.model.GuessRecord;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class MockDuel1A2BGameModule implements Duel1A2BGameModule {
    private boolean gameStarted = false;
    private Duel1A2BGameModule.Callback callback;
    private Game1A2BDuelStatus gameStatus = new Game1A2BDuelStatus();
    private Map<Player, String> answers = new HashMap<>(); // <player, answer>
    private Map<Player, GuessRecord> guessRecords = new HashMap<>(); //<player, record>
    private AI ai = new AI();
    private Handler handler = new Handler();

    @Override
    public void startGame() {
        gameStarted = true;
    }

    @Override
    public void sendChatMessage(ChatMessage message) {
        callback.onMessageReceived(message);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onMessageReceived(new ChatMessage(ai, ai.replyMessage()));
            }
        }, 1500);
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
    public void setPlayerAnswer(Player player, final String answer) {
        answers.put(player, answer);
        callback.onPlayerAnswerSetCompleted(player);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {  // the AI set the answer
                answers.put(ai, "1234");
                callback.onPlayerAnswerSetCompleted(ai);
                callback.onDuelStarting();
                handler.postDelayed(ai, 2000); // start the ai
            }
        }, 3000);
    }


    @Override
    public void loadGameStatus() {
        throw new RuntimeException("先不要用這個函數..");
    }

    @Override
    public void guess(String guess) {

    }


    private class AI extends Player implements Runnable{

        public AI() {
            super("水球Bot");
        }

        @Override
        public void run() {
            final String[] temp = new String[]{"1234", "2345", "5678", "9876"};
            guess(temp[new Random().nextInt(temp.length)]);
        }

        public String replyMessage(){
            return "專心玩遊戲好嗎?";
        }
    }
}
