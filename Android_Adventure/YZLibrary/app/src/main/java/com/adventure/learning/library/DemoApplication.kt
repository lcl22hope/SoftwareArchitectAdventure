package com.adventure.learning.library

import android.app.Application
import com.adventure.learning.yzlibrary.log.printers.ConsoleLogPrinter
import com.adventure.learning.yzlibrary.log.LogConfig
import com.adventure.learning.yzlibrary.log.LogConfig.JsonParser
import com.adventure.learning.yzlibrary.log.LogManager
import com.google.gson.Gson

class DemoApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        LogManager.init(object :LogConfig() {
            override fun injectJsonParser(): JsonParser {
                return JsonParser { src -> Gson().toJson(src) }
            }

            override fun getGlobalTag(): String {
                return "DemoApplication"
            }

            override fun enable(): Boolean {
                return true
            }

            override fun includeThread(): Boolean {
                return false;
            }

            override fun stackTraceDepth(): Int {
                return 0;
            }
        }, ConsoleLogPrinter())
    }
}