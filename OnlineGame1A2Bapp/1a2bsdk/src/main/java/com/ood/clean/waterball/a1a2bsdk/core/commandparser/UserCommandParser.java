package com.ood.clean.waterball.a1a2bsdk.core.commandparser;


import android.support.annotation.Nullable;
import android.util.Log;

import java.util.regex.Pattern;

import command.base.Command;
import communication.protocol.Protocol;

public class UserCommandParser extends CommandParser{

    public UserCommandParser(@Nullable CommandParser next) {
        super(next);
    }

    @Override
    public Command parse(Protocol protocol) {
        String event = protocol.getEvent();
        if (Pattern.matches(event, "^(sign).*"))
        {
            Log.d(CommandParser.TAG, "User Command Parser parsing ...");
        }
        return null;
    }
}
