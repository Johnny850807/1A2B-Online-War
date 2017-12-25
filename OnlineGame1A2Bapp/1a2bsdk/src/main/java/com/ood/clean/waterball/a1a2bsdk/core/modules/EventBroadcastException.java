package com.ood.clean.waterball.a1a2bsdk.core.modules;


public class EventBroadcastException extends IllegalStateException{
    public EventBroadcastException(String event){
        super("you should not receive the event " + event + ", it's a mistake from the server.");
    }
}
