package com.redsponge.sponge.event;


import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.redsponge.sponge.util.Logger;

public class Invokation {

    private Object object;
    private Method method;

    public Invokation(Object object, Method method) {
        this.object = object;
        this.method = method;
    }

    public void invoke(Object... params) {
        try {
            method.invoke(object, params);
        } catch (ReflectionException e) {
            Logger.error(this, e);
        }
    }
}
