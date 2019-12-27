package com.victor.clips.app

import android.app.Application
import com.victor.crash.library.SpiderCrashHandler

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: App.kt
 * Author: Victor
 * Date: 2018/8/16 11:38
 * Description: 
 * -----------------------------------------------------------------
 */
class App : Application () {
    companion object {
        private var instance : App ?= null
        fun get() = instance!!
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        //放在其他库初始化前
        SpiderCrashHandler.init(this)
    }
}