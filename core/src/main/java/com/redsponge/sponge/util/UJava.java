package com.redsponge.sponge.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public final class UJava {

    public static String getStackTrace(Throwable throwable) {
        StringBuilder output = new StringBuilder();

        output.append(throwable.toString()).append('\n');
        for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
            output.append(stackTraceElement).append('\n');

        }
        return output.toString();
    }

}
