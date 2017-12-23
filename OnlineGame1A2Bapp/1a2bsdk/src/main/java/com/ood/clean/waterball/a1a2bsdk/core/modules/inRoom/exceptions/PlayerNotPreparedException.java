package com.ood.clean.waterball.a1a2bsdk.core.modules.inRoom.exceptions;


public class PlayerNotPreparedException extends IllegalStateException{
    public PlayerNotPreparedException() {
        super("Players have not already prepared.");
    }
}
