package com.adventure.learning.library.demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.adventure.learning.library.R;
import com.adventure.learning.yzlibrary.log.Logger;

public class YZLogDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yzlog_demo);

        findViewById(R.id.btn_log).setOnClickListener(v -> {
            printLog();
        });
    }

    private void printLog() {
        Logger.a("yuzhao hello logger");
    }
}