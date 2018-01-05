package com.ood.clean.waterball.a1a2bsdk.core;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ood.clean.waterball.a1a2bsdk.core.base.BindCallback;
import com.ood.clean.waterball.a1a2bsdk.core.base.GameCallBack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import container.protocol.Protocol;
import gamecore.entity.GameRoom;
import gamecore.model.RequestStatus;
import gamecore.model.games.a1b2.duel.core.Duel1A2BPlayerBarModel;
import utils.MyGson;


public final class ReflectionEventBus implements EventBus{
    private static final String TAG = "ReflectionEventBus";
    private static ReflectionEventBus instance;
    private Gson gson = MyGson.getGson();
    private Set<GameCallBack> callBackSet;
    private final List<Protocol> unhandledProtocols = Collections.synchronizedList(new ArrayList<Protocol>());

    public ReflectionEventBus(){
        callBackSet = new HashSet<>();
    }

    @Override
    public void registerCallback(GameCallBack gameCallBack) {
        callBackSet.add(gameCallBack);
    }

    @Override
    public void unregisterCallback(GameCallBack gameCallBack) {
        callBackSet.remove(gameCallBack);
    }

    @Override
    public void resendNonHandledEvent() {
        for (Protocol protocol : new ArrayList<>(unhandledProtocols))
                invoke(protocol);
        unhandledProtocols.clear();
    }

    @Override
    public void error(Exception err) {
        for (GameCallBack callBack : callBackSet)
            callBack.onError(err);
    }

    /**
     * @since 1.8
     */
    @Override
    public synchronized void invoke(Protocol protocol) {
        try {
            Log.v(TAG, "protocol invoked: " + protocol);
            bindMethods(protocol);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * @since 1.8
     */
    private void bindMethods(Protocol protocol) throws InvocationTargetException, IllegalAccessException {
        String event = protocol.getEvent();
        RequestStatus status = RequestStatus.valueOf(protocol.getStatus());
        boolean eventHasBeenConsumed = false;
        for (GameCallBack gameCallBack : callBackSet)
        {
            Method[] methods = gameCallBack.getClass().getDeclaredMethods();
            for (Method method : methods) {
                boolean hasCallback = method.isAnnotationPresent(BindCallback.class);
                if (hasCallback) {
                    BindCallback bindCallback = method.getAnnotation(BindCallback.class);
                    if (event.equals(bindCallback.event()) && status == bindCallback.status())
                    {
                        eventHasBeenConsumed = true;
                        Log.d(TAG, "Method found: " + method.getName());
                        invokeMethod(gameCallBack, method, protocol);
                    }
                }
            }
        }

        if (!eventHasBeenConsumed)
        {
            Log.e(TAG, "No callback consumes the event: " + event);
            unhandledProtocols.add(protocol);
        }
    }

    /**
     * @since 1.8
     */
    private void invokeMethod(GameCallBack callBack, Method method, Protocol protocol) throws InvocationTargetException, IllegalAccessException {
        Class[] parameters = method.getParameterTypes();
        if (parameters.length == 1)
        {
            Object data = gson.fromJson(protocol.getData(), parameters[0]);
            if (data instanceof List)
                data = gson.fromJson(protocol.getData(), parseGenericTypeByTheEvent(protocol.getEvent()));

            method.invoke(callBack, data);
        }
        else if (parameters.length == 0)
            method.invoke(callBack);
        else
            throw new IllegalStateException("The binding methods can only go with one parameter or none.");
    }

    private Type parseGenericTypeByTheEvent(String event){
        //TODO need a better algorithm to solve generic problem, not to parse it by an event name.
        if (event.toUpperCase().contains("ROOM"))
            return new TypeToken<List<GameRoom>>(){}.getType();
        if (event.toUpperCase().contains("ONEROUNDOVER"))
            return new TypeToken<List<Duel1A2BPlayerBarModel>>(){}.getType();
        return null;
    }
}
