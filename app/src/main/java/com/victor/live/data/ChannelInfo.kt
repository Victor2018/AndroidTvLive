package com.victor.hdtv.data

import java.io.Serializable

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: ChannelInfo
 * Author: Victor
 * Date: 2019/12/24 16:33
 * Description: 
 * -----------------------------------------------------------------
 */

class ChannelInfo: Serializable {
    var channel_name: String? = null
    var epg: String? = null
    var icon: String? = null
    var play_urls: List<PlayUrl>? = null
}