package com.redsponge.sponge.event;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.redsponge.sponge.util.Logger;

import java.util.HashMap;
import java.util.Map;

public class EventBus {

    private static final EventBus instance = new EventBus();

    public static EventBus getInstance() {
        return instance;
    }

    private final Map<Class<?>, Array<Invokation>> invokations;

    private EventBus() {
        invokations = new HashMap<>();
    }

    public void registerListener(Object invoked) {
        for (Method method : ClassReflection.getDeclaredMethods(invoked.getClass())) {
            if(method.isAnnotationPresent(EventHandler.class)) {
                Class<?>[] paramTypes = method.getParameterTypes();
                if(paramTypes.length == 0) {
                    Logger.warn(this, "Tried to register object", invoked, "as event listener and method", method, "was declared listener but didn't have parameters!");
                } else {
                    Logger.debug(this, "Registered method", method, "for object", invoked, "for event type", paramTypes[0]);
                    addInvokation(paramTypes[0], new Invokation(invoked, method));
                }
            }
        }
    }

    private void addInvokation(Class<?> paramType, Invokation invokation) {
        if(invokations.containsKey(paramType)) {
            invokations.get(paramType).add(invokation);
        } else {
            Array<Invokation> invokations = new Array<>();
            invokations.add(invokation);
            this.invokations.put(paramType, invokations);
        }
    }

    public void dispatch(Object eventObject) {
        if(invokations.containsKey(eventObject.getClass())) {
            Array<Invokation> invokations = this.invokations.get(eventObject.getClass());
            for (int i = 0; i < invokations.size; i++) {
                invokations.get(i).invoke(eventObject);
            }
        }
    }
}
