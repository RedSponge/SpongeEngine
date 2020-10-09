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
    private static final DateFormat FILE_FORMAT = new SimpleDateFormat("ssmmHH-ddMMyy");
    private static int logLevel;

    public static void setLogLevel(int logLevel) {
        Logger.logLevel = logLevel;
    }

    public static int getLogLevel() {
        return logLevel;
    }

    public static void beginLog() {
        logFile = Gdx.files.local(FILE_FORMAT.format(new Date()) + ".log");
    }

    //public static void

    private static void logUnderLevel(int level, Class<?> from, Object... toLog) {
        StringBuilder sb = new StringBuilder();
        for (Object o : toLog) {
            sb.append(o).append(" ");
        }
        String tag = FORMAT.format(new Date(TimeUtils.millis())) + "] [" + from.getSimpleName() + "] [" + LOG_TITLES[level-1];
        String message = sb.toString();
        if(logLevel <= level) {
            Gdx.app.log(tag, message);
        }


    }

}
