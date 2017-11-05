package com.ood.clean.waterball.a1a2bsdk.core;

import com.ood.clean.waterball.a1a2bsdk.core.base.GameCallBack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import container.protocol.Protocol;


public final class ReflectionEventBus implements EventBus{
    private static ReflectionEventBus instance;
    private Map<Class<? extends GameCallBack>,GameCallBack> callBackMap; // <type of listener, registered listener>

    ReflectionEventBus(){
        callBackMap = new HashMap<>();
    }

    public static ReflectionEventBus getInstance(){
        if (instance == null)
            instance = new ReflectionEventBus();
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

    public Collection<GameCallBack> getCallbacks(){
        return callBackMap.values();
    }

    @Override
    public void registerCallback(GameCallBack gameCallBack) {

    }

    @Override
    public void error(Exception err) {

    }

    @Override
    public void invoke(Protocol protocol) {

    }
}
