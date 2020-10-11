package com.redsponge.sponge.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Date;

public class Logger {

    public static final int DEBUG = 0;
    public static final int INFO = 1;
    public static final int WARN = 2;
    public static final int ERROR = 3;

    private static final String[] LOG_TITLES = {"DEBUG", "INFO", "WARN", "ERROR"};

    private static FileHandle logFile;

    private static final String FORMAT = "HH:mm:ss";
    private static final String FILE_FORMAT = "dd-MM-yy-HH-mm-ss";
    private static int logLevel = INFO;

    public static void setLogLevel(int logLevel) {
        Logger.logLevel = logLevel;
    }

    public static int getLogLevel() {
        return logLevel;
    }

    public static void beginLog() {
        try {
            logFile = Gdx.files.local("logs/" + DateFormatter.formatDate(new Date(TimeUtils.millis()), FILE_FORMAT) + ".log");
            Logger.info(Logger.class, "Began logging on file", logFile.path());
        } catch (GdxRuntimeException e) {
            Logger.error(Logger.class, "Couldn't create log file - probably running on GWT!");
            logFile = null;
        }
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

    public static void error(Object from, Exception thrown) {
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
        String tag = LOG_TITLES[level] + "] [" + DateFormatter.formatDate(new Date(TimeUtils.millis()), FORMAT) + "] [" + from.getSimpleName();
        String message = sb.toString();
        if(logLevel <= level) {
            if(level == ERROR) {
                Gdx.app.error(tag, message);
            } else {
                Gdx.app.log(tag, message);
            }
        }
        if(logFile != null) {
            logFile.writeString("[" + tag + "] " + message + '\n', true);
        }
    }

}
