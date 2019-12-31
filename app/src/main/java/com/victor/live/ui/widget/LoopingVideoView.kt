package com.victor.live.ui.widget

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.widget.VideoView

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: LoopingVideoView
 * Author: Victor
 * Date: 2019/12/31 16:23
 * Description: 
 * -----------------------------------------------------------------
 */

class LoopingVideoView: VideoView {

    private var mMediaPlayer: MediaPlayer? = null

    constructor(context: Context): super(context)
    constructor(context: Context,attributeSet: AttributeSet): super(context,attributeSet)
    constructor(context: Context,attributeSet: AttributeSet,defStyleAttr: Int): super(context,attributeSet,defStyleAttr)

    fun setupMediaPlayer(url: String, onVideoReadyListener: OnVideoReadyListener) {
        setOnPreparedListener { mp ->
            mMediaPlayer = mp
            mMediaPlayer?.setOnBufferingUpdateListener(MediaPlayer.OnBufferingUpdateListener { mp, percent -> if (percent > 20) onVideoReadyListener.onVideoReady() })
            mMediaPlayer?.setLooping(true)
            mMediaPlayer?.setVolume(0f, 0f)
            mMediaPlayer?.start()
        }
        setVideoURI(Uri.parse(url))
    }

    fun stopMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer?.release()
        }
    }

    interface OnVideoReadyListener {
        fun onVideoReady()
    }
}