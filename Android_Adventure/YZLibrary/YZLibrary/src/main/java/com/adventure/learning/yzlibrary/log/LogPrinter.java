package com.adventure.learning.yzlibrary.log;

import androidx.annotation.NonNull;

public interface LogPrinter {
    void print(@NonNull LogConfig config, int level, String tag, @NonNull String printString);
}
