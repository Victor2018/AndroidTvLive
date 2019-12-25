package com.victor.hdtv.interfaces

import com.victor.hdtv.data.ChannelReq

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: OnChannelListener
 * Author: Victor
 * Date: 2019/12/24 17:31
 * Description: 
 * -----------------------------------------------------------------
 */

interface OnChannelListener {
    fun OnChannel(data: ChannelReq)
}