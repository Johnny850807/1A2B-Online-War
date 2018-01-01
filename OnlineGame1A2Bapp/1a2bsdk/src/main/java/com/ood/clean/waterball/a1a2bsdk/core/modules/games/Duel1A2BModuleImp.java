package com.ood.clean.waterball.a1a2bsdk.core.modules.games;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ood.clean.waterball.a1a2bsdk.core.base.BindCallback;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.CallbackException;

import java.util.List;

import container.protocol.Protocol;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ContentModel;
import gamecore.model.ErrorMessage;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.PlayerRoomModel;
import gamecore.model.RequestStatus;
import gamecore.model.games.a1b2.Duel1A2BPlayerBarModel;
import gamecore.model.games.a1b2.GameOverModel;

import static container.Constants.Events.Games.Duel1A2B.GUESS;
import static container.Constants.Events.Games.Duel1A2B.GUESSING_STARTED;
import static container.Constants.Events.Games.Duel1A2B.ONE_ROUND_OVER;
import static container.Constants.Events.Games.Duel1A2B.SET_ANSWER;
import static container.Constants.Events.Games.GAMEOVER;
import static container.Constants.Events.Games.GAMESTARTED;
import static container.Constants.Events.InRoom.LEAVE_ROOM;
import static container.Constants.Events.RECONNECTED;

public class Duel1A2BModuleImp extends AbstractOnlineGameModule implements Duel1A2BModule {
    private ProxyCallback proxyCallback;
    protected Player currentPlayer;
    protected GameRoom currentGameRoom;
    protected Context context;

    @Override
    public void registerCallback(Context context, Player currentPlayer, GameRoom currentGameRoom, Duel1A2BModule.Callback callback) {
        validate(currentPlayer);
        validate(currentGameRoom);
        this.context = context;
        this.currentPlayer = currentPlayer;
        this.currentGameRoom = currentGameRoom;

        if (this.proxyCallback != null)
            callback.onError(new CallbackException());
        this.proxyCallback = new Duel1A2BModuleImp.ProxyCallback(callback);
        eventBus.registerCallback(proxyCallback);
    }

    @Override
    public void unregisterCallBack(Duel1A2BModule.Callback callback) {
        if (this.proxyCallback == null || this.proxyCallback.callback != callback)
            callback.onError(new CallbackException());
        eventBus.unregisterCallback(proxyCallback);
        this.proxyCallback = null;
        this.currentPlayer = null;
        this.currentGameRoom = null;
    }

    @Override
    public void setAnswer(String answer) {
        Protocol protocol = protocolFactory.createProtocol(SET_ANSWER,
                RequestStatus.request.toString(), gson.toJson(new ContentModel(
                        currentPlayer.getId(), currentGameRoom.getId(), answer)));
        client.broadcast(protocol);

    }

    @Override
    public void guess(String guess) {
        Protocol protocol = protocolFactory.createProtocol(GUESS,
                RequestStatus.request.toString(), gson.toJson(new ContentModel(
                        currentPlayer.getId(), currentGameRoom.getId(), guess)));
        client.broadcast(protocol);
    }

    @Override
    public void leaveGame() {
        Protocol protocol = protocolFactory.createProtocol(LEAVE_ROOM, RequestStatus.request.toString(),
                gson.toJson(new PlayerRoomIdModel(currentPlayer.getId(), currentGameRoom.getId())));
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

    public class ProxyCallback implements Duel1A2BModule.Callback{
        private Duel1A2BModule.Callback callback;

        public ProxyCallback(Duel1A2BModule.Callback callback) {
            this.callback = callback;
        }


        @Override
        @BindCallback(event = GAMESTARTED, status = RequestStatus.success)
        public void onGameStarted() {
            Log.d(TAG, "the game " + currentGameRoom.getGameMode() + " started.");
            callback.onGameStarted();
        }

        @Override
        @BindCallback(event = SET_ANSWER, status = RequestStatus.success)
        public void onSetAnswerSuccessfully(ContentModel setAnswerModel) {
            Log.d(TAG, "answer set: " + setAnswerModel.getContent());
            callback.onSetAnswerSuccessfully(setAnswerModel);
        }

        @Override
        @BindCallback(event = SET_ANSWER, status = RequestStatus.failed)
        public void onSetAnswerUnsuccessfully(ErrorMessage errorMessage) {
            Log.d(TAG, "answer set unsuccessfully: " + errorMessage);
            callback.onSetAnswerUnsuccessfully(errorMessage);
        }

        @Override
        @BindCallback(event = GUESS, status = RequestStatus.success)
        public void onGuessSuccessfully(ContentModel guessModel) {
            Log.d(TAG, "guessed : " + guessModel.getContent());
            callback.onGuessSuccessfully(guessModel);
        }

        @Override
        @BindCallback(event = GUESS, status = RequestStatus.failed)
        public void onGuessUnsuccessfully(ErrorMessage errorMessage) {
            Log.d(TAG, "guessing unsuccessfully : " + errorMessage);
            callback.onGuessUnsuccessfully(errorMessage);
        }

        @Override
        @BindCallback(event = GUESSING_STARTED, status = RequestStatus.success)
        public void onGuessingStarted() {
            Log.d(TAG, "guessing phase started.");
            callback.onGuessingStarted();
        }

        @Override
        @BindCallback(event = ONE_ROUND_OVER, status = RequestStatus.success)
        public void onOneRoundOver(List<Duel1A2BPlayerBarModel> models) {
            Log.d(TAG, "One round over.");
            callback.onOneRoundOver(models);
        }

        @Override
        @BindCallback(event = GAMEOVER, status = RequestStatus.success)
        public void onGameOver(GameOverModel gameOverModel) {
            Log.d(TAG, "Game over, the winner's id is: " + gameOverModel.getWinnerId());
            callback.onGameOver(gameOverModel);
        }

        @Override
        @BindCallback(event = LEAVE_ROOM, status = RequestStatus.success)
        public void onOpponentLeft(PlayerRoomModel model) {
            if (!model.getPlayer().equals(currentPlayer))
                callback.onOpponentLeft(model);
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
    }

}
