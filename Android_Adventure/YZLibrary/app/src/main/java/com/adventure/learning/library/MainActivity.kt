package com.adventure.learning.library

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.adventure.learning.library.demo.YZLogDemoActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);

        findViewById<View>(R.id.btn_navigate).setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id) {
            R.id.btn_navigate -> {
                Log.i("MainActivity", "onClick");
                startActivity(Intent(this, YZLogDemoActivity::class.java))
            }
        }
    }
}