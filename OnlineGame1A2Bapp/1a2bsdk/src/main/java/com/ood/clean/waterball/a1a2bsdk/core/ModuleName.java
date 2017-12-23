package com.ood.clean.waterball.a1a2bsdk.core;


/**
 * the enum represents the key of the module,
 * you can use this enum to get the corresponding module from CoreGameServer.getModule(ModuleName key);
 */
public enum ModuleName {
    /**
     * member signing system
     */
    SIGNING,

    /**
     * room list system where manages the rooms and presents the join request to the room
     */
    ROOMLIST,

    /**
     * the chatting place
     */
    CHAT,

    /**
     *  room system to do chat or launching a game inside the room
     */
    INROOM,

    /**
     * the 1a2b game in duel mode
     */
    GAME1A2BDUEL;

}
