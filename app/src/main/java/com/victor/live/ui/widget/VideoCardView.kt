package com.victor.live.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v17.leanback.widget.BaseCardView
import android.util.AttributeSet
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.victor.live.R
import com.victor.live.util.NetworkUtil
import kotlinx.android.synthetic.main.lb_video_card_view.view.*

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: VideoCardView
 * Author: Victor
 * Date: 2019/12/31 16:34
 * Description: 
 * -----------------------------------------------------------------
 */

open class VideoCardView: BaseCardView {
    val CARD_TYPE_FLAG_IMAGE_ONLY = 0
    val CARD_TYPE_FLAG_TITLE = 1
    val CARD_TYPE_FLAG_CONTENT = 2

    internal lateinit var mPreviewCard: PreviewCardView

    internal var mInfoArea: ViewGroup? = null

    private var mTitleView: TextView? = null
    private var mContentView: TextView? = null
    private var mAttachedToWindow: Boolean = false

    constructor(context: Context): this(context,null)
    constructor(context: Context,attrs: AttributeSet?): this(context,attrs,R.attr.imageCardViewStyle)

    constructor(context: Context, styleResId: Int): super(ContextThemeWrapper(context, styleResId),null,0) {
        buildImageCardView(styleResId)
    }
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int):
            super(getStyledContext(context, attributeSet, defStyleAttr), attributeSet, defStyleAttr) {
        buildImageCardView(getImageCardViewStyle(context, attributeSet, defStyleAttr))
    }

    override fun hasOverlappingRendering(): Boolean {
        return false
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mAttachedToWindow = true
        val mImageView = mPreviewCard.getImageView()
        if (mImageView?.alpha == 0f) fadeIn()
    }

    override fun onDetachedFromWindow() {
        val mImageView = mPreviewCard.getImageView()
        mAttachedToWindow = false
        mImageView?.animate()?.cancel()
        mImageView?.alpha = 1f
        super.onDetachedFromWindow()
    }

    private fun buildImageCardView(styleResId: Int) {
        // Make sure the ImageCardView is focusable.
        isFocusable = true
        isFocusableInTouchMode = true

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.lb_video_card_view, this)
        mPreviewCard = view.findViewById(R.id.layout_preview_card)
        mInfoArea = view.findViewById(R.id.info_field)

        val cardAttrs = context.obtainStyledAttributes(styleResId, R.styleable.lbImageCardView)
        val cardType = cardAttrs.getInt(
            R.styleable.lbImageCardView_lbImageCardViewType, CARD_TYPE_FLAG_IMAGE_ONLY
        )
        val hasImageOnly = cardType == CARD_TYPE_FLAG_IMAGE_ONLY
        val hasTitle = cardType and CARD_TYPE_FLAG_TITLE == CARD_TYPE_FLAG_TITLE
        val hasContent = cardType and CARD_TYPE_FLAG_CONTENT == CARD_TYPE_FLAG_CONTENT

        if (hasImageOnly) {
            removeView(mInfoArea)
            cardAttrs.recycle()
            return
        }

        // Create children
        if (hasTitle) {
            mTitleView = inflater.inflate(
                R.layout.lb_image_card_view_themed_title,
                mInfoArea,
                false
            ) as TextView
            info_field.addView(mTitleView)
        }

        if (hasContent) {
            mContentView = inflater.inflate(
                R.layout.lb_image_card_view_themed_content,
                mInfoArea,
                false
            ) as TextView
            info_field.addView(mContentView)
        }

        // Backward compatibility: Newly created ImageCardViews should change
        // the InfoArea's background color in XML using the corresponding style.
        // However, since older implementations might make use of the
        // 'infoAreaBackground' attribute, we have to make sure to support it.
        // If the user has set a specific value here, it will differ from null.
        // In this case, we do want to override the value set in the style.
        val background = cardAttrs.getDrawable(R.styleable.lbImageCardView_infoAreaBackground)
        if (null != background) {
            setInfoAreaBackground(background)
        }

        cardAttrs.recycle()
    }

    companion object {
        fun getStyledContext(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
        ): Context {
            val style = getImageCardViewStyle(context, attrs, defStyleAttr)
            return ContextThemeWrapper(context, style)
        }

        private fun getImageCardViewStyle(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
        ): Int {
            // Read style attribute defined in XML layout.
            var style = attrs?.styleAttribute ?: 0
            if (0 == style) {
                // Not found? Read global ImageCardView style from Theme attribute.
                val styledAttrs = context.obtainStyledAttributes(R.styleable.LeanbackTheme)
                style = styledAttrs.getResourceId(R.styleable.LeanbackTheme_imageCardViewStyle, 0)
                styledAttrs.recycle()
            }
            return style
        }
    }



    /**
     * Returns the main image view.
     */
    fun getMainImageView(): ImageView? {
        return mPreviewCard.getImageView()
    }

    /**
     * Sets the layout dimensions of the ImageView.
     */
    fun setMainContainerDimensions(width: Int, height: Int) {
        val lp = mPreviewCard.layoutParams
        lp.width = width
        lp.height = height
        mPreviewCard.layoutParams = lp
    }

    /**
     * Sets the info area background drawable.
     */
    fun setInfoAreaBackground(drawable: Drawable) {
        if (mInfoArea != null) {
            info_field.setBackground(drawable)
        }
    }

    fun setVideoUrl(url: String?) {
        mPreviewCard.setVideoUrl(url)
    }

    fun startVideo() {
        if (NetworkUtil.isNetworkConnected(context)) {
            mPreviewCard.setLoading()
        }
    }

    fun stopVideo() {
        mPreviewCard.setFinished()
    }

    /**
     * Sets the title text.
     */
    fun setTitleText(text: CharSequence?) {
        if (mTitleView == null) {
            return
        }
        mTitleView?.setText(text)
    }

    /**
     * Sets the content text.
     */
    fun setContentText(text: CharSequence?) {
        if (mContentView == null) {
            return
        }
        mContentView?.setText(text)
    }

    private fun fadeIn() {
        val mImageView = mPreviewCard.getImageView()
        mImageView?.alpha = 0f
        if (mAttachedToWindow) {
            val duration = mImageView?.resources?.getInteger(android.R.integer.config_shortAnimTime)
            mImageView?.animate()?.alpha(1f)?.duration = duration!!.toLong()
        }
    }
}