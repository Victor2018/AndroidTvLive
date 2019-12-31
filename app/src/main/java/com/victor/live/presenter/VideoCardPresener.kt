package com.victor.live.presenter

import android.graphics.drawable.Drawable
import android.support.v17.leanback.widget.ImageCardView
import android.support.v17.leanback.widget.Presenter
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.victor.hdtv.data.ChannelInfo
import com.victor.live.R
import com.victor.live.ui.widget.VideoCardView
import kotlinx.android.synthetic.main.lb_video_card_view.view.*
import kotlin.properties.Delegates

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: VideoCardPresener
 * Author: Victor
 * Date: 2019/12/31 16:14
 * Description: 
 * -----------------------------------------------------------------
 */

class VideoCardPresener: Presenter() {
    private var TAG = "VideoCardPresener"
    private var mDefaultCardImage: Drawable? = null
    private var sSelectedBackgroundColor: Int by Delegates.notNull()
    private var sDefaultBackgroundColor: Int by Delegates.notNull()

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        Log.d(TAG, "onCreateViewHolder().....")

        sDefaultBackgroundColor = ContextCompat.getColor(parent.context,
            R.color.default_background
        )
        sSelectedBackgroundColor =
            ContextCompat.getColor(parent.context, R.color.selected_background)
        mDefaultCardImage = ContextCompat.getDrawable(parent.context, R.mipmap.live)
        val cardView = object : VideoCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
                updateCardBackgroundColor(this, selected)
                super.setSelected(selected)
            }
        }

        cardView.setOnClickListener { cardView.stopVideo() }

        cardView.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                cardView.startVideo()
            } else {
                cardView.stopVideo()
            }
        }

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true

        updateCardBackgroundColor(cardView, false)
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any?) {
        if (item is ChannelInfo) {
            val cardView = viewHolder.view as VideoCardView
            if (item.play_urls != null) {
                cardView.setTitleText(item.channel_name)
                cardView.setContentText(item.epg)
                cardView.setMainContainerDimensions(CARD_WIDTH, CARD_HEIGHT)
                cardView.setVideoUrl(item.play_urls!![0].play_url)

                Glide.with(cardView.context)
                    .load(item.icon)
                    .centerCrop()
                    .error(mDefaultCardImage)
                    .into(cardView.getMainImageView())
            }
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        if (viewHolder.view is ImageCardView) {
            val cardView = viewHolder.view as ImageCardView
            // Remove references to images so that the garbage collector can free up memory
            cardView.badgeImage = null
            cardView.mainImage = null
        }
    }

    private fun updateCardBackgroundColor(view: VideoCardView, selected: Boolean) {
        val color = if (selected) sSelectedBackgroundColor else sDefaultBackgroundColor
        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        view.setBackgroundColor(color)
        view.info_field.setBackgroundColor(color)
    }

    companion object {
        private val TAG = "CardPresenter"

        private val CARD_WIDTH = 313
        private val CARD_HEIGHT = 176
    }
}