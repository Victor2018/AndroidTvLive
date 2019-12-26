package com.victor.live.util

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.util.LinkedHashSet

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: MainHandler
 * Author: Victor
 * Date: 2019/12/26 15:17
 * Description: 
 * -----------------------------------------------------------------
 */

class MainHandler: Handler(Looper.getMainLooper()) {
    private object Holder { val instance = MainHandler()}
    private val mainHandlers = LinkedHashSet<OnMainHandlerImpl>()
    private var sMainHandler: MainHandler? = null

    companion object {
        val  instance: MainHandler by lazy { MainHandler.Holder.instance }
    }

    override fun handleMessage(message: Message) {
        super.handleMessage(message)
        for (onMainHandlerImpl in mainHandlers) {
            onMainHandlerImpl?.handleMainMessage(message)
        }
    }

    fun register(onMainHandlerImpl: OnMainHandlerImpl?): MainHandler {
        if (onMainHandlerImpl != null) {
            mainHandlers.add(onMainHandlerImpl)
        }
        return this
    }

    fun unregister(onMainHandlerImpl: OnMainHandlerImpl?) {
        if (onMainHandlerImpl != null) {
            mainHandlers.remove(onMainHandlerImpl)
        }
    }

    fun clear() {
        mainHandlers.clear()
        if (sMainHandler != null) {
            sMainHandler?.removeCallbacksAndMessages(null)
        }
    }

    /**
     * 主线程执行
     *
     * @param runnable
     */
    fun runMainThread(runnable: Runnable) {
        instance.post(runnable)
    }

    interface OnMainHandlerImpl {
        fun handleMainMessage(message: Message)
    }
}