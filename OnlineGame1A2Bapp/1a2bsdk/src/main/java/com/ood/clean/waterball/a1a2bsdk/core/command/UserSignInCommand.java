package com.ood.clean.waterball.a1a2bsdk.core.command;

import com.ood.clean.waterball.a1a2bsdk.core.model.Player;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;
import com.ood.clean.waterball.a1a2bsdk.eventbus.EventBus;

import command.base.Command;
import communication.message.Message;


public class UserSignInCommand implements Command {
    private EventBus eventBus = EventBus.getInstance();
    private Message<Player> message;

    public UserSignInCommand(Message<Player> message) {
        this.message = message;
    }

    @Override
    public void execute() {
        UserSigningModule.Callback callback = eventBus.getCallback(UserSigningModule.Callback.class);

    }
}
