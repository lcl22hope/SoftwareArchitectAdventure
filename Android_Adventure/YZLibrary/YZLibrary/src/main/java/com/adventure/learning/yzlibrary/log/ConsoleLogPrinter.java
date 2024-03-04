package com.adventure.learning.yzlibrary.log;

import static com.adventure.learning.yzlibrary.log.LogConfig.MAX_LEN;

import android.util.Log;

import androidx.annotation.NonNull;

public class ConsoleLogPrinter implements LogPrinter{
    @Override
    public void print(@NonNull LogConfig config, int level, String tag, @NonNull String printString) {
        int len = printString.length();
        int countOfSub = printString.length() / MAX_LEN;
        if (countOfSub > 0) {
            int index = 0;
            for (int i = 0; i < countOfSub; i++) {
               Log.println(level, tag, printString.substring(index, index + MAX_LEN)); //
               index += MAX_LEN;
            }
            if (index != len) {
                Log.println(level, tag, printString.substring(index, len)); // 如果行数不能整除，打印剩余部分
            }
        } else {
            Log.println(level, tag, printString);
        }
    }
}
