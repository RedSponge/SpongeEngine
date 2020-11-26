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
    private final Map<Object, Map<Class<?>, Array<Invokation>>> cachedInvokations;

    private EventBus() {
        invokations = new HashMap<>();
        cachedInvokations = new HashMap<>();
    }

    public void registerListener(Object invoked) {
        Map<Class<?>, Array<Invokation>> invokationMap = new HashMap<>();
        for (Method method : ClassReflection.getDeclaredMethods(invoked.getClass())) {
            if(method.isAnnotationPresent(EventHandler.class)) {
                Class<?>[] paramTypes = method.getParameterTypes();
                if(paramTypes.length == 0) {
                    Logger.warn(this, "Tried to register object", invoked, "as event listener and method", method, "was declared listener but didn't have parameters!");
                } else {
                    Logger.debug(this, "Registered method", method.getName(), "for object", invoked, "for event type", paramTypes[0]);
                    Invokation inv = new Invokation(invoked, method);
                    addToArrayMap(paramTypes[0], inv, invokations);
                    addToArrayMap(paramTypes[0], inv, invokationMap);
                }
            }
        }
        cachedInvokations.put(invokationMap, invokationMap);
    }

    public void removeListener(Object invoked) {
        if(cachedInvokations.containsKey(invoked)) {
            Logger.error(this, "Tried to remove listener", invoked, "but it isn't connected!");
            return;
        }
        Map<Class<?>, Array<Invokation>> map = cachedInvokations.get(invoked);
        map.forEach((cls, arr) -> invokations.get(cls).removeAll(arr, true));
        cachedInvokations.remove(invoked);
    }

    private <T, V> void addToArrayMap(T key, V value, Map<T,Array<V>> map) {
        if(map.containsKey(key)) {
            map.get(key).add(value);
        } else {
            Array<V> newArray = new Array<>();
            newArray.add(value);
            map.put(key, newArray);
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
