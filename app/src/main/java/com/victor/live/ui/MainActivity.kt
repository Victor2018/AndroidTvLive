/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.victor.live

import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import com.victor.clips.app.App
import com.victor.clips.ui.BaseActivity
import com.victor.hdtv.data.ChannelInfo
import com.victor.hdtv.data.ChannelReq
import com.victor.kplayer.library.module.Player
import com.victor.live.util.MainHandler
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_play.mPbLoading
import kotlinx.android.synthetic.main.activity_play.mTvSource
import java.util.*

/**
 * Loads [MainFragment].
 */
class MainActivity : BaseActivity(), MainHandler.OnMainHandlerImpl {
    var mPlayer: Player? = null
    var channelInfo: ChannelInfo? = null
    var mSvPlay: SurfaceView? = null
    var isFullScreen: Boolean = false
    var mChannelReq: ChannelReq? = null
    var mCategoryPosition: Int = 0
    var mChannelPosition: Int = 0

    override fun handleMainMessage(message: Message) {
        if (!isFullScreen) return
        when (message?.what) {
            Player.PLAYER_PREPARING -> {
                mPbLoading.visibility = View.VISIBLE
                mTvSource.visibility = View.VISIBLE
            }
            Player.PLAYER_PREPARED -> {
                mPbLoading.visibility = View.GONE
                mTvSource.visibility = View.GONE
            }
            Player.PLAYER_ERROR -> {
                mPbLoading.visibility = View.VISIBLE
                mTvSource.visibility = View.VISIBLE
                var random = Random()
                var index = random.nextInt(channelInfo?.play_urls!!.size)
                var playUrl = channelInfo?.play_urls!![index].play_url
                mTvSource.setText("播放源：" + playUrl)
                mPlayer?.playUrl(playUrl,true)
            }
            Player.PLAYER_BUFFERING_START -> {
                mPbLoading.visibility = View.VISIBLE
                mTvSource.visibility = View.VISIBLE
            }
            Player.PLAYER_BUFFERING_END -> {
                mPbLoading.visibility = View.GONE
                mTvSource.visibility = View.GONE
            }
            Player.PLAYER_PROGRESS_INFO -> {
            }
            Player.PLAYER_COMPLETE -> {
            }
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    fun initialize () {
        MainHandler.instance.register(this)

        mSvPlay = SurfaceView(this)
        mPlayer = Player(mSvPlay!!, MainHandler.instance)
    }

    fun play(channelReq: ChannelReq?,categoryPosition: Int,channelPosition: Int,data: ChannelInfo?) {
        mChannelReq = channelReq
        mCategoryPosition = categoryPosition
        mChannelPosition = channelPosition
        channelInfo = data

        mFlMain.addView(mSvPlay,1)
        var playUrl = channelInfo?.play_urls!![0].play_url
        mTvSource.setText("播放源：" + playUrl)
        mPlayer?.playUrl(playUrl,true)
        isFullScreen = true
    }

    fun playNext () {
        channelInfo = mChannelReq?.categorys!![mCategoryPosition]?.channels!![mChannelPosition]

        var playUrl = channelInfo?.play_urls!![0].play_url
        mTvSource.setText("播放源：" + playUrl)
        mPlayer?.playUrl(playUrl,true)
        isFullScreen = true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (isFullScreen) {
                    mFlMain.removeView(mSvPlay)
                    mPbLoading.visibility = View.GONE
                    mTvSource.visibility = View.GONE
                    isFullScreen = false
                    return true
                }
            }
            KeyEvent.KEYCODE_DPAD_UP -> {
                if (mChannelReq == null) return true
                Log.e(TAG,"onKeyDown()------>KEYCODE_DPAD_UP")
                mChannelPosition = mChannelPosition + 1;
                if (mChannelPosition >= mChannelReq?.categorys?.get(mCategoryPosition)!!.channels?.size!!) {
                    mChannelPosition = 0
                    mCategoryPosition = mCategoryPosition + 1
                    if (mCategoryPosition >= mChannelReq?.categorys?.size!!) {
                        mCategoryPosition = 0
                        mChannelPosition = 0
                    }
                }
                Log.e(TAG,"onKeyDown()------>mCategoryPosition = " + mCategoryPosition)
                Log.e(TAG,"onKeyDown()------>mChannelPosition = " + mChannelPosition)
                playNext()
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                if (mChannelReq == null) return true
                Log.e(TAG,"onKeyDown()------>KEYCODE_DPAD_DOWN")
                mChannelPosition = mChannelPosition - 1;
                if (mChannelPosition <= 0) {
                    mCategoryPosition = mCategoryPosition - 1
                    if (mCategoryPosition <= 0) {
                        mCategoryPosition = mChannelReq?.categorys?.size!! - 1
                    }
                    mChannelPosition = mChannelReq?.categorys?.get(mCategoryPosition)!!.channels?.size!! - 1
                }
                Log.e(TAG,"onKeyDown()------>mCategoryPosition = " + mCategoryPosition)
                Log.e(TAG,"onKeyDown()------>mChannelPosition = " + mChannelPosition)
                playNext()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        super.onResume()
        mPlayer?.resume()
    }

    override fun onPause() {
        super.onPause()
        mPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        MainHandler.instance.unregister(this)
        mPlayer?.stop()
        mPlayer = null
    }

}