package com.adventure.learning.yzlibrary.log;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class LogItemModel {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    public long timeMillis;
    public int level;
    public String tag;
    public String log;

    public LogItemModel(long timeMillis, int level, String tag, String log) {
        this.timeMillis = timeMillis;
        this.level = level;
        this.tag = tag;
        this.log = log;
    }

    public String getFlatenedLog() {
        return getFlattended() + "\n" + log;
    }

    public String getFlattended() {
        return format(timeMillis) + "|" + level + "|" + tag + "|:";
    }

    public String format(long timeMillis) {
        return simpleDateFormat.format(timeMillis);
    }
}
