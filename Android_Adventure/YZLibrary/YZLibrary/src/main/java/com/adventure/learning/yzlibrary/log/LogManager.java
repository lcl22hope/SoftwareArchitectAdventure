package com.adventure.learning.yzlibrary.log;

import androidx.annotation.NonNull;

import java.util.List;

public class LogManager {
    private LogConfig config;

    private LogManager(LogConfig config) {
        this.config = config;
    }
    static LogManager instance;

    static LogManager getInstance() {
        return instance;
    }

    public static void init(@NonNull LogConfig config) {
        instance = new LogManager(config);
    }

    public LogConfig getConfig() {
        return config;
    }

    List<LogPrinter> getPrinters() {
        return null;
    }

    void addPrinter(LogPrinter printer) {
    }

    void removePrinter(LogPrinter printer) {
    }
}
