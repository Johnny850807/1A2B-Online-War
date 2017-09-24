package com.ood.clean.waterball.a1a2bsdk.core.commandparser;


import android.support.annotation.Nullable;

import command.base.Command;
import command.base.NullCommand;
import communication.protocol.Protocol;

public abstract class CommandParser {
    public static final String TAG = "commandparser";
    private CommandParser next;

    public CommandParser(@Nullable CommandParser next){
        this.next = next;
    }

    public abstract Command parse(Protocol protocol);

    public Command nextParse(Protocol protocol) {
        if (next != null)
            return next.parse(protocol);
        return new NullCommand();
    }

}
