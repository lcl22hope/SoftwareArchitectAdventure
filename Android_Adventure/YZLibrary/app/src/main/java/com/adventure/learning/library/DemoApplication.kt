package com.adventure.learning.library

import android.app.Application
import com.adventure.learning.yzlibrary.log.LogConfig
import com.adventure.learning.yzlibrary.log.LogManager

class DemoApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        LogManager.init(object :LogConfig() {
            override fun getGlobalTag(): String {
                return "DemoApplication"
            }

            override fun enable(): Boolean {
                return true
            }
        })
    }
}