package com.ood.clean.waterball.a1a2bsdk.core.modules.signIn;


import android.support.annotation.NonNull;

import com.ood.clean.waterball.a1a2bsdk.core.model.Player;
import com.ood.clean.waterball.a1a2bsdk.core.service.SocketService;
import com.ood.clean.waterball.a1a2bsdk.eventbus.EventBus;

import communication.message.Event;
import communication.message.Message;
import communication.message.Status;
import gamecore.entity.Entity;


public class UserSigningModuleImp implements UserSigningModule {
    private SocketService socketService = SocketService.getInstance();
    private EventBus eventBus = EventBus.getInstance();
    private Player currentPlayer;


    @Override
    public void signIn(@NonNull String name, @NonNull UserSigningModule.Callback callback) {
        try{
            Player userData = new Player(name);
            eventBus.registerCallback(UserSigningModule.Callback.class, callback);
            socketService.respond(new Message<Entity>(Event.signIn, Status.request, userData));
        }catch (Exception err){
            callback.onError(err);
        }
    }

    public Player getCurrentPlayer() {
        if (currentPlayer == null)
            throw new IllegalStateException("There is no signed in user.");
        return currentPlayer;
    }
}
