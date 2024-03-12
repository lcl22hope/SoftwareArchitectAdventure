package com.adventure.learning.library

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.adventure.learning.library.demo.YZLogDemoActivity
import com.adventure.learning.library.demo.tab.YZTabBottomDemoActivity

/**
 * Kotlin 和 Java 代码混编
 * */

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);

        findViewById<View>(R.id.btn_navigate_log).setOnClickListener(this)
        findViewById<View>(R.id.btn_navigate_bottom).setOnClickListener(this);
    }

    override fun onClick(p0: View?) {
        when(p0!!.id) {
            R.id.btn_navigate_log -> {
                Log.i("MainActivity", "onClick");
                startActivity(Intent(this, YZLogDemoActivity::class.java))
            }

            R.id.btn_navigate_bottom -> {
                Log.i("MainActivity", "onClick");
                startActivity(Intent(this, YZTabBottomDemoActivity::class.java))
            }
        }
    }
}