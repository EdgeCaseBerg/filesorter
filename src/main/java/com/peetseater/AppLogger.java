package com.peetseater;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public final class AppLogger {

    private static ConsoleHandler logger = new java.util.logging.ConsoleHandler();

    private AppLogger() {
        // Prevent instantiation of this class since it's meant to just be called statically.
    }

    public static void info(String msg) {
        logger.publish(new LogRecord(Level.INFO, msg));
    }

    public static void warn(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        logger.publish(new LogRecord(Level.WARNING, sw.toString()));
    }
}
