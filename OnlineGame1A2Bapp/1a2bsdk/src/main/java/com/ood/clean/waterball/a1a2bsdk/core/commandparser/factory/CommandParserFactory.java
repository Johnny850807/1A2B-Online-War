package com.ood.clean.waterball.a1a2bsdk.core.commandparser.factory;


import com.ood.clean.waterball.a1a2bsdk.core.commandparser.CommandParser;
import com.ood.clean.waterball.a1a2bsdk.core.commandparser.ExceptionCommandParser;
import com.ood.clean.waterball.a1a2bsdk.core.commandparser.UserCommandParser;

import utils.Singleton;

@Singleton
public class CommandParserFactory {
    private static CommandParserFactory instance;

    public static CommandParserFactory getInstance(){
        if (instance == null)
            instance = new CommandParserFactory();
        return instance;
    }

    public CommandParser createCommandParserLinkedList(){
        return new ExceptionCommandParser(new UserCommandParser(null));
    }

}
