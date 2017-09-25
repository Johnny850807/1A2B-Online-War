package com.ood.clean.waterball.a1a2bsdk.core.modules.signIn;


import android.support.annotation.NonNull;

import com.ood.clean.waterball.a1a2bsdk.core.model.User;
import com.ood.clean.waterball.a1a2bsdk.core.service.SocketService;
import com.ood.clean.waterball.a1a2bsdk.eventbus.EventBus;

import communication.message.Event;
import communication.message.Message;
import communication.message.Status;
import gamecore.entity.Entity;

public class UserSigningModuleImp implements UserSigningModule {
    private SocketService socketService = SocketService.getInstance();
    private EventBus eventBus = EventBus.getInstance();
    private User currentUser;

    @Override
    public void signIn(@NonNull String name, @NonNull UserSigningModule.Callback callback) {
        try{
            User userData = new User(name);
            eventBus.registerCallback(UserSigningModule.Callback.class, callback);
            socketService.respond(new Message<Entity>(Event.signIn, Status.request, userData));
        }catch (Exception err){
            callback.onSignInFailed(err);
        }
    }

    @Override
    public User getCurrentUser() {
        if (currentUser == null)
            throw new IllegalStateException("There is no signed in user.");
        return currentUser;
    }
}
