package com.redsponge.sponge.util;

import com.badlogic.gdx.math.MathUtils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

public final class UJava {

    public static String getStackTrace(Throwable throwable) {
        StringBuilder output = new StringBuilder();

        output.append(throwable.toString()).append('\n');
        for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
            output.append(stackTraceElement).append('\n');

        }
        return output.toString();
    }

    public static <T> T randomValue(T[] array) {
        return array[MathUtils.random(array.length - 1)];
    }

}
