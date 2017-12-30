package com.ood.clean.waterball.a1a2bsdk.core;


import com.ood.clean.waterball.a1a2bsdk.core.client.ClientSocket;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import container.base.Client;
import container.protocol.ProtocolFactory;
import container.protocol.XOXOXDelimiterFactory;

/**
 * A collection of global variables doing dependency injection.
 */
public class Component {
    private static ThreadExecutor threadExecutor = new HandlerExecutor();
    private static Client client = new ClientSocket(threadExecutor);
    private static EventBus eventBus = new ReflectionEventBus();
    private static ProtocolFactory protocolFactory = new XOXOXDelimiterFactory();


    /**
     * @param obj the instance of the obj needed to be injected.
     */
    public static void inject(Object obj){
        Class<?> clazz = obj.getClass();
        List<Field> fields = getAllFields(new ArrayList<>(), clazz);

        // Trace all annotations of each field of the obj. if the field has an Inject annotation, inject the value to the field.
        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class))
                try {
                    inject(obj, field);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
        }
    }

    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    private static void inject(Object obj, Field field) throws IllegalAccessException {
        Field[] fields = Component.class.getDeclaredFields();

        // Automatically match all static fields of Component.class to the injected field.
        // If matching, then inject the value to it.
        for (Field f : fields)
        {
            if (f.getType().equals(field.getType()))
            {

                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                field.set(obj, f.get(null));  // injecting
                field.setAccessible(accessible);
                f.setAccessible(false);
            }
        }
    }
}
