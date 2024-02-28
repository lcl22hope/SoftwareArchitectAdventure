package com.adventure.learning.yzlibrary.log;

import java.util.ArrayList;

public abstract class LogConfig {
    JsonParser jsonParser;
    ArrayList<LogPrinter> printers;

    public String getGlobalTag() {
        return "YuzhaoLog";
    };
    public boolean enable() {
        return true;
    }
//    abstract boolean includeThread();
//    abstract int stackTraceDepth();
}
