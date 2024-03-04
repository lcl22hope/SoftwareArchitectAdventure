package com.adventure.learning.library.demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.adventure.learning.library.R;
import com.adventure.learning.yzlibrary.log.LogConfig;
import com.adventure.learning.yzlibrary.log.LogManager;
import com.adventure.learning.yzlibrary.log.LogType;
import com.adventure.learning.yzlibrary.log.Logger;
import com.adventure.learning.yzlibrary.log.ViewLogPrinter;

public class YZLogDemoActivity extends AppCompatActivity {
    private ViewLogPrinter viewLogPrinter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yzlog_demo);
        viewLogPrinter = new ViewLogPrinter(this);

        findViewById(R.id.btn_log).setOnClickListener(v -> {
            printLog();
        });

        viewLogPrinter.getViewLogPrinterProvider().showFloatingView();
    }

    private void printLog() {
        LogManager.getInstance().addPrinter(viewLogPrinter);

        LogConfig logConfig = new LogConfig() {
            @Override
            public boolean includeThread() {
                return false;
            }

            @Override
            public int stackTraceDepth() {
                return 0;
            }
        };
        Logger.log(logConfig, LogType.E, "------", "hello yuzhaoooo");
        Logger.a("90000");
    }
}