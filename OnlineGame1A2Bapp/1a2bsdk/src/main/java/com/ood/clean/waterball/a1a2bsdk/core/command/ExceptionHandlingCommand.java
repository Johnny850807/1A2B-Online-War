package com.ood.clean.waterball.a1a2bsdk.core.command;


import com.ood.clean.waterball.a1a2bsdk.core.base.GameCallBack;
import com.ood.clean.waterball.a1a2bsdk.eventbus.EventBus;

import java.util.Collection;

import command.base.Command;

public class ExceptionHandlingCommand implements Command{
    private EventBus eventBus = EventBus.getInstance();
    private Exception exception;

    public ExceptionHandlingCommand(Exception exception) {
        this.exception = exception;
    }

    @Override
    public void execute() {
        Collection<GameCallBack> callBacks = EventBus.getInstance().getCallbacks();

        for (GameCallBack callBack : callBacks)
            callBack.onError(exception);
    }
}
