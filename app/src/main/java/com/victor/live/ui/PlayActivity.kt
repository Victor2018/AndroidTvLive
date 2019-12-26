package com.victor.live

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.victor.clips.ui.BaseActivity
import com.victor.hdtv.data.ChannelInfo
import com.victor.kplayer.library.module.Player
import com.victor.live.util.Constant
import com.victor.live.util.MainHandler
import kotlinx.android.synthetic.main.activity_play.*
import java.util.*

class PlayActivity : BaseActivity(),MainHandler.OnMainHandlerImpl {

    var mPlayer: Player? = null
    var channelInfo: ChannelInfo? = null

    override fun handleMainMessage(message: Message) {
        when (message?.what) {
            Player.PLAYER_PREPARING -> {
            }
            Player.PLAYER_PREPARED -> {
            }
            Player.PLAYER_ERROR -> {
                Log.e(TAG,"handleMainMessage-PLAYER_ERROR")
                var random = Random()
                var index = random.nextInt(channelInfo?.play_urls!!.size)
                mPlayer?.playUrl(channelInfo?.play_urls!![index].play_url,true)
            }
            Player.PLAYER_BUFFERING_START -> {
            }
            Player.PLAYER_BUFFERING_END -> {
            }
            Player.PLAYER_PROGRESS_INFO -> {
            }
            Player.PLAYER_COMPLETE -> {
            }
        }
    }

    companion object {
        var SHARED_ELEMENT_NAME = "transe_album_img"

        fun  intentStart (activity: AppCompatActivity, data: ChannelInfo, sharedElement: View, sharedElementName: String) {
            var intent = Intent(activity, PlayActivity::class.java)
            var bundle = Bundle()
            bundle.putSerializable(Constant.INTENT_DATA_KEY,data)
            intent.putExtras(bundle)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,sharedElement, sharedElementName)
            ActivityCompat.startActivity(activity!!, intent, options.toBundle())
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_play
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
        initData()
    }

    fun initialize () {
        MainHandler.instance.register(this)
        mPlayer = Player(mSvPlay, MainHandler.instance)
    }

    fun initData () {
        channelInfo = intent.getSerializableExtra(Constant.INTENT_DATA_KEY) as ChannelInfo?
        mPlayer?.playUrl(channelInfo?.play_urls!![0].play_url,true)
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
