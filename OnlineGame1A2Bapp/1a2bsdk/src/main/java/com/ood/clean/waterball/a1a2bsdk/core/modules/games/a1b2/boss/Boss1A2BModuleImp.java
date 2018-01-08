package com.ood.clean.waterball.a1a2bsdk.core.modules.games.a1b2.boss;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ood.clean.waterball.a1a2bsdk.core.base.BindCallback;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.CallbackException;
import com.ood.clean.waterball.a1a2bsdk.core.modules.games.AbstractOnlineGameModule;

import container.protocol.Protocol;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ContentModel;
import gamecore.model.ErrorMessage;
import gamecore.model.PlayerRoomModel;
import gamecore.model.RequestStatus;
import gamecore.model.games.GameOverModel;
import gamecore.model.games.a1b2.boss.core.AttackActionModel;
import gamecore.model.games.a1b2.boss.core.Monster;
import gamecore.model.games.a1b2.boss.core.NextTurnModel;
import gamecore.model.games.a1b2.boss.core.SpiritsModel;

import static container.core.Constants.Events.Games.Boss1A2B.ATTACK;
import static container.core.Constants.Events.Games.Boss1A2B.ATTACKING_STARTED;
import static container.core.Constants.Events.Games.Boss1A2B.ATTACK_RESULTS;
import static container.core.Constants.Events.Games.Boss1A2B.CHANGE_ANSWER;
import static container.core.Constants.Events.Games.Boss1A2B.NEXT_TURN;
import static container.core.Constants.Events.Games.Boss1A2B.SET_ANSWER;
import static container.core.Constants.Events.Games.GAMEOVER;
import static container.core.Constants.Events.Games.GAMESTARTED;
import static container.core.Constants.Events.InRoom.CLOSE_ROOM;
import static container.core.Constants.Events.InRoom.CLOSE_ROOM_TIME_EXPIRED;
import static container.core.Constants.Events.InRoom.LEAVE_ROOM;
import static container.core.Constants.Events.RECONNECTED;

public class Boss1A2BModuleImp extends AbstractOnlineGameModule implements Boss1A2BModule {
    private ProxyCallback proxyCallback;
    protected Player currentPlayer;
    protected GameRoom currentGameRoom;
    protected Context context;

    @Override
    public void registerCallback(Context context, Player currentPlayer, GameRoom currentGameRoom, Boss1A2BModule.Callback callback) {
        validate(currentPlayer);
        validate(currentGameRoom);
        this.context = context;
        this.currentPlayer = currentPlayer;
        this.currentGameRoom = currentGameRoom;

        if (this.proxyCallback != null)
            callback.onError(new CallbackException());
        this.proxyCallback = new Boss1A2BModuleImp.ProxyCallback(callback);
        eventBus.registerCallback(proxyCallback);
    }

    @Override
    public void unregisterCallBack(Boss1A2BModule.Callback callback) {
        if (this.proxyCallback == null || this.proxyCallback.callback != callback)
            callback.onError(new CallbackException());
        eventBus.unregisterCallback(proxyCallback);
        this.proxyCallback = null;
        this.currentPlayer = null;
        this.currentGameRoom = null;
    }

    @Override
    public void setAnswer(String answer) {
        Log.d(TAG, "Setting the answer: " + answer);
        Protocol protocol = protocolFactory.createProtocol(SET_ANSWER,
                RequestStatus.request.toString(), gson.toJson(new ContentModel(
                        currentPlayer.getId(), currentGameRoom.getId(), answer)));
        client.broadcast(protocol);
    }

    @Override
    public void attack(String guess) {
        Log.d(TAG, "Attacking: " + guess);
        Protocol protocol = protocolFactory.createProtocol(ATTACK,
                RequestStatus.request.toString(), gson.toJson(new ContentModel(
                        currentPlayer.getId(), currentGameRoom.getId(), guess)));
        client.broadcast(protocol);
    }

    @Override
    protected Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    protected GameRoom getCurrentGameRoom() {
        return currentGameRoom;
    }


    public class ProxyCallback implements Boss1A2BModule.Callback{
    private Boss1A2BModule.Callback callback;

        public ProxyCallback(Boss1A2BModule.Callback callback) {
            this.callback = callback;
        }

        @Override
        @BindCallback(event = GAMESTARTED, status = RequestStatus.success)
        public void onGameStarted(SpiritsModel spiritsModel) {
            Log.d(TAG, "the game " + currentGameRoom.getGameMode() + " started.");
            callback.onGameStarted(spiritsModel);
        }

        @Override
        @BindCallback(event = ATTACKING_STARTED, status = RequestStatus.success)
        public void onAttackingPhaseStarted() {
            Log.d(TAG, "the attacking phase started.");
            callback.onAttackingPhaseStarted();
        }

        @Override
        @BindCallback(event = GAMEOVER, status = RequestStatus.success)
        public void onGameOver(GameOverModel gameOverModel) {
            Log.d(TAG, "Game over, the winner's id is: " + gameOverModel.getWinnerId());
            callback.onGameOver(gameOverModel);
        }

        @Override
        @BindCallback(event = LEAVE_ROOM, status = RequestStatus.success)
        public void onPlayerLeft(PlayerRoomModel model) {
            Log.d(TAG, "The Opponent left.");
            if (!model.getPlayer().equals(currentPlayer))
                callback.onPlayerLeft(model);
        }

        @Override
        @BindCallback(event = CLOSE_ROOM, status = RequestStatus.success)
        public void onGameClosed(GameRoom gameRoom) {
            Log.d(TAG, "The game closed.");
            if (!gameRoom.equals(currentGameRoom))
                throw new IllegalStateException("The closed room is not the current room, how did it broadcast to the game?");
            callback.onGameClosed(gameRoom);
        }

        @Override
        @BindCallback(event = CLOSE_ROOM_TIME_EXPIRED, status = RequestStatus.success)
        public void onRoomExpired() {
            Log.d(TAG, "Room expired.");
            callback.onRoomExpired();
        }

        @Override
        @BindCallback(event = RECONNECTED, status = RequestStatus.success)
        public void onServerReconnected() {
            callback.onServerReconnected();
        }

        @Override
        public void onError(@NonNull Throwable err) {
            callback.onError(err);
        }

        @Override
        @BindCallback(event = SET_ANSWER, status = RequestStatus.success)
        public void onSetAnswerSuccessfully(ContentModel contentModel) {
            Log.d(TAG, "Set answer successfully: " + contentModel.getContent());
            callback.onSetAnswerSuccessfully(contentModel);
        }

        @Override
        @BindCallback(event = SET_ANSWER, status = RequestStatus.failed)
        public void onSetAnswerUnsuccessfully(ErrorMessage errorMessage) {
            Log.d(TAG, "Set answer unsuccessfully: " + errorMessage.getMessage());
            callback.onSetAnswerUnsuccessfully(errorMessage);
        }

        @Override
        @BindCallback(event = ATTACK, status = RequestStatus.success)
        public void onAttackSuccessfully(ContentModel contentModel) {
            Log.d(TAG, "Attack successfully: " + contentModel.getContent());
            callback.onAttackSuccessfully(contentModel);
        }

        @Override
        @BindCallback(event = ATTACK, status = RequestStatus.failed)
        public void onAttackUnsuccessfully(ErrorMessage errorMessage) {
            Log.d(TAG, "Attack unsuccessfully: " + errorMessage.getMessage());
            callback.onAttackUnsuccessfully(errorMessage);
        }

        @Override
        @BindCallback(event = ATTACK_RESULTS, status = RequestStatus.success)
        public void onNextAttackAction(AttackActionModel attackActionModel) {
            Log.d(TAG, "On next attack action model received: " + attackActionModel);
            callback.onNextAttackAction(attackActionModel);
        }

        @Override
        @BindCallback(event = NEXT_TURN, status = RequestStatus.success)
        public void onNextTurn(NextTurnModel nextTurnModel) {
            Log.d(TAG, "Turn changed: " + nextTurnModel.getWhosTurn().getName());
            callback.onNextTurn(nextTurnModel);
        }

        @Override
        @BindCallback(event = CHANGE_ANSWER, status = RequestStatus.success)
        public void onBossAnswerChanged(Monster boss) {
            Log.d(TAG, boss.getName() + "'s answer changed.");
            callback.onBossAnswerChanged(boss);
        }

    }
}
