package com.victor.live.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.victor.live.R
import kotlinx.android.synthetic.main.widget_preview_card.view.*

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: PreviewCardView
 * Author: Victor
 * Date: 2019/12/31 16:29
 * Description: 
 * -----------------------------------------------------------------
 */

class PreviewCardView: FrameLayout {
    private var mVideoUrl: String? = null
    var view: View? = null

    constructor(context: Context): this(context,null)
    constructor(context: Context,attributeSet: AttributeSet?): this(context,attributeSet,0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int): super(context,attributeSet,defStyleAttr) {
        view = View.inflate(getContext(), R.layout.widget_preview_card, this)
    }

    fun setVideoUrl(videoUrl: String?) {
        mVideoUrl = videoUrl
    }

    fun getImageView(): ImageView? {
        return view?.main_image
    }

    fun setLoading() {
        view?.view_overlay?.setVisibility(View.VISIBLE)
        view?.progress_card?.setVisibility(View.VISIBLE)
        view?.main_video?.setVisibility(View.VISIBLE)
        view?.main_video?.setupMediaPlayer(mVideoUrl!!, object : LoopingVideoView.OnVideoReadyListener {
            override fun onVideoReady() {
                view?.view_overlay?.setVisibility(View.INVISIBLE)
                view?.progress_card?.setVisibility(View.INVISIBLE)
                view?.main_image?.setVisibility(View.INVISIBLE)
            }
        })
    }

    fun setFinished() {
        view?.main_video?.setVisibility(View.INVISIBLE)
        view?.main_video?.stopMediaPlayer()
        view?.main_image?.setVisibility(View.VISIBLE)
        view?.view_overlay?.setVisibility(View.INVISIBLE)
        view?.progress_card?.setVisibility(View.INVISIBLE)
    }

}