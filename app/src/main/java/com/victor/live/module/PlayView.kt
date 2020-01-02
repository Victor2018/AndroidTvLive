package com.victor.live.module

import android.content.Context
import android.view.SurfaceView
import com.victor.clips.app.App

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: PlayView
 * Author: Victor
 * Date: 2020/1/2 16:45
 * Description: 
 * -----------------------------------------------------------------
 */

class PlayView {
    private object Holder { val instance = PlayView()}
    var surfaceView: SurfaceView? = null

    constructor() {
        surfaceView = SurfaceView(App.get())
    }

    companion object {
        val  instance: PlayView by lazy { PlayView.Holder.instance }
    }
}