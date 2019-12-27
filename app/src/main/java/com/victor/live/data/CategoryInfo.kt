package com.victor.hdtv.data

import java.io.Serializable

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: CategoryInfo
 * Author: Victor
 * Date: 2019/12/24 16:31
 * Description: 
 * -----------------------------------------------------------------
 */

class CategoryInfo: Serializable {
    var channel_category: String? = null
    var channels: List<ChannelInfo>? = null
}