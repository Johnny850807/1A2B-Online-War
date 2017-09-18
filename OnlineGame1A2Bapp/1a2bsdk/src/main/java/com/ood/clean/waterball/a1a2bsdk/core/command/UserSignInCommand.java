package com.ood.clean.waterball.a1a2bsdk.core.command;

import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import command.base.Command;


public class UserSignInCommand implements Command {
    private UserSigningModule.Callback callback;
    private String name;

    public UserSignInCommand(String name, UserSigningModule.Callback callback) {
        this.callback = callback;
        this.name = name;
    }

    @Override
    public void execute() {

    }
}
