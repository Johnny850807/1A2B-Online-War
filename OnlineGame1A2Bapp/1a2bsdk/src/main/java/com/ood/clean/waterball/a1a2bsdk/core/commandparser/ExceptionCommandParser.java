package com.ood.clean.waterball.a1a2bsdk.core.commandparser;


import android.support.annotation.Nullable;

import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.GameCoreException;
import com.ood.clean.waterball.a1a2bsdk.core.command.ExceptionHandlingCommand;
import com.ood.clean.waterball.a1a2bsdk.core.commandparser.factory.ExceptionFactory;
import com.ood.clean.waterball.a1a2bsdk.core.commandparser.factory.ReleaseExceptionFactory;
import com.ood.clean.waterball.a1a2bsdk.core.model.ErrorMessage;

import command.base.Command;
import communication.message.Message;
import communication.message.MessageUtils;
import communication.message.Status;
import communication.protocol.Protocol;

public class ExceptionCommandParser extends CommandParser{
    protected ExceptionFactory exceptionFactory;

    public ExceptionCommandParser(@Nullable CommandParser next) {
        super(next);
        exceptionFactory = new ReleaseExceptionFactory();
    }

    @Override
    public Command parse(Protocol protocol) {
        Status status = Status.valueOf(protocol.getStatus());
        if (status == Status.failed)
        {
            Message<ErrorMessage> message = MessageUtils.protocolToMessage(protocol, ErrorMessage.class);
            ErrorMessage error = message.getData();
            GameCoreException gameCoreException = exceptionFactory.create(error.getCode(), error.getErrorMessage());
            return new ExceptionHandlingCommand(gameCoreException);
        }

        return nextParse(protocol);
    }

}
