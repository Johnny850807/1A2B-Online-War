package com.ood.clean.waterball.a1a2bsdk.eventbus;

import com.ood.clean.waterball.a1a2bsdk.core.base.GameCallBack;

import java.util.HashMap;
import java.util.Map;

import communication.message.Message;
import gamecore.entity.Entity;
import utils.Singleton;

@Singleton
public final class EventBus {
    private static EventBus instance;
    private Map<Class<? extends GameCallBack>,GameCallBack> callBackMap; // <type of listener, registered listener>

    EventBus(){
        callBackMap = new HashMap<>();
    }

    public static EventBus getInstance(){
        if (instance == null)
            instance = new EventBus();
        return instance;
    }

    public void registerCallback(Class<? extends GameCallBack> clazz, GameCallBack callBack){
        callBackMap.put(clazz, callBack);
    }

    public <T extends GameCallBack> T getCallback(Class<T> clazz){
        if (!callBackMap.containsKey(clazz))
            throw new IllegalStateException("callback : " + clazz.getName() + " , has not been registered.");
        return clazz.cast(callBackMap.get(clazz));
    }

    public void handleMessage(Message<? extends Entity> message){
        // todo
    }
}
