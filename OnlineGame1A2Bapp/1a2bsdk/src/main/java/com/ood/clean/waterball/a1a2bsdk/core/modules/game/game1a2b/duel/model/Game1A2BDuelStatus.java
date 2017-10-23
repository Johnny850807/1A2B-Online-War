package com.ood.clean.waterball.a1a2bsdk.core.modules.game.game1a2b.duel.model;


import com.ood.clean.waterball.a1a2bsdk.core.model.Player;
import com.ood.clean.waterball.a1a2bsdk.core.modules.game.model.GuessRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game1A2BDuelStatus {
    private Map<Player, StatusOfPlayer> statusOfPlayerMap = new HashMap<>();

    public void updateStatus(Player player, StatusOfPlayer statusOfPlayer){
        statusOfPlayerMap.put(player, statusOfPlayer);
    }

    public StatusOfPlayer getStatusOfPlayer(Player player){
        return statusOfPlayerMap.get(player);
    }

    public boolean isGameOver() {
        for (StatusOfPlayer status : statusOfPlayerMap.values())
            if (status.isWin())
                return true;
        return false;
    }

    public class StatusOfPlayer{
        private Player player;
        private String answer;
        private List<GuessRecord> guessRecords = new ArrayList<>();

        public boolean isWin() {
            for (GuessRecord record : guessRecords)
                if (record.getResult().getA() == 4)
                    return true;
            return false;
        }

        public Player getPlayer() {
            return player;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public List<GuessRecord> getGuessRecords() {
            return guessRecords;
        }

        public void setGuessRecords(List<GuessRecord> guessRecords) {
            this.guessRecords = guessRecords;
        }
    }
}
