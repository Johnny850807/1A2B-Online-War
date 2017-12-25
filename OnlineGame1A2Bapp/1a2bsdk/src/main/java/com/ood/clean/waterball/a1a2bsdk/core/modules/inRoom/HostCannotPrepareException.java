package com.ood.clean.waterball.a1a2bsdk.core.modules.inRoom;


public class
HostCannotPrepareException extends IllegalStateException {
    public HostCannotPrepareException() {
        super("the host shouldn't be able to change the status.");
    }
}
