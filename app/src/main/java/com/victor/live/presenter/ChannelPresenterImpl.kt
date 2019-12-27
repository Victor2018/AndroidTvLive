package com.victor.live.presenter

import com.victor.hdtv.data.ChannelReq
import com.victor.live.ui.view.ChannelView
import org.victor.khttp.library.annotation.HttpParms
import org.victor.khttp.library.data.Request
import org.victor.khttp.library.inject.HttpInject
import org.victor.khttp.library.presenter.impl.BasePresenterImpl

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: ChannelPresenterImpl.kt
 * Author: Victor
 * Date: 2018/8/24 13:49
 * Description: 获取节目单
 * -----------------------------------------------------------------
 */
class ChannelPresenterImpl(var channelView: ChannelView?): BasePresenterImpl() {
    var TAG = "ChannelPresenterImpl"
    /*Presenter作为中间层，持有View和Model的引用*/
    override fun onComplete(data: Any?, msg: String) {
        channelView?.OnChannel(data,msg)
    }

    override fun detachView() {
        channelView = null
    }

    @HttpParms (method = Request.GET,responseCls = ChannelReq::class)
    override fun sendRequest(url: String, header: HashMap<String, String>?, parms: String?) {
        HttpInject.inject(this);
        super.sendRequest(url, header, parms)
    }
}