package com.adventure.learning.yzlibrary.log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogManager {
    static LogManager instance;
    private LogConfig config;
    private List<LogPrinter> printers = new ArrayList<>();

    private LogManager(LogConfig config, LogPrinter[] printers) {
        this.config = config;
        this.printers.addAll(Arrays.asList(printers));
        for (LogPrinter printer : printers) {
            this.config.addPrinter(printer);
        }
    }

    public static LogManager getInstance() {
        return instance;
    }

    /**
     * 为了调用方便，引入可变参数的 printers
     * */
    public static void init(@NonNull LogConfig config, LogPrinter... printers) {
        instance = new LogManager(config, printers);
    }

    public LogConfig getConfig() {
        return config;
    }

    public void addPrinter(LogPrinter printer) {
        this.printers.add(printer);
        this.config.addPrinter(printer);
    }

    public void removePrinter(LogPrinter printer) {
        if (this.printers != null || this.printers.contains(printer)) {
            this.printers.remove(printer);
            this.config.removePrinter(printer);
        }
    }
}
