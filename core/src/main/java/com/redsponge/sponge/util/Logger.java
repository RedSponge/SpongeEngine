package com.redsponge.sponge.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.TimeUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    public static final int DEBUG = 0;
    public static final int INFO = 1;
    public static final int WARN = 2;
    public static final int ERROR = 3;

    private static final String[] LOG_TITLES = {"DEBUG", "INFO", "WARN", "ERROR"};

    private static FileHandle logFile;

    private static final DateFormat FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final DateFormat FILE_FORMAT = new SimpleDateFormat("dd-MM-yy-HH-mm-ss");
    private static int logLevel = INFO;

    public static void setLogLevel(int logLevel) {
        Logger.logLevel = logLevel;
    }

    public static int getLogLevel() {
        return logLevel;
    }

    public static void beginLog() {

        logFile = Gdx.files.local("logs/"  +FILE_FORMAT.format(new Date()) + ".log");
        Logger.info(Logger.class, "Began logging on file", logFile.path());
    }

    //region INSTANCE METHODS

    public static void debug(Object from, Object... toLog) {
        logUnderLevel(DEBUG, from.getClass(), toLog);
    }

    public static void info(Object from, Object... toLog) {
        logUnderLevel(INFO, from.getClass(), toLog);
    }

    public static void warn(Object from, Object... toLog) {
        logUnderLevel(WARN, from.getClass(), toLog);
    }

    public static void error(Object from, Object... toLog) {
        logUnderLevel(ERROR, from.getClass(), toLog);
    }

    public static void error(Object from, Throwable thrown) {
        error(from, UJava.getStackTrace(thrown));
    }

    // endregion

    // region CLASS METHODS
    public static void debug(Class<?> from, Object... toLog) {
        logUnderLevel(DEBUG, from, toLog);
    }

    public static void info(Class<?> from, Object... toLog) {
        logUnderLevel(INFO, from, toLog);
    }

    public static void warn(Class<?> from, Object... toLog) {
        logUnderLevel(WARN, from, toLog);
    }

    public static void error(Class<?> from, Object... toLog) {
        logUnderLevel(ERROR, from, toLog);
    }

    public static void error(Class<?> from, Throwable thrown) {
        error(from, UJava.getStackTrace(thrown));
    }
    // endregion`


    private static void logUnderLevel(int level, Class<?> from, Object... toLog) {
        StringBuilder sb = new StringBuilder();
        for (Object o : toLog) {
            sb.append(o).append(" ");
        }
        String tag = LOG_TITLES[level] + "] [" + FORMAT.format(new Date(TimeUtils.millis())) + "] [" + from.getSimpleName();
        String message = sb.toString();
        if(logLevel <= level) {
            if(level == ERROR) {
                Gdx.app.error(tag, message);
            } else {
                Gdx.app.log(tag, message);
            }
        }
        logFile.writeString("[" + tag + "] " + message + '\n', true);
    }

}
