package com.victor.hdtv.data

import java.io.Serializable

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: ChannelReq
 * Author: Victor
 * Date: 2019/12/24 16:30
 * Description: 
 * -----------------------------------------------------------------
 */

class ChannelReq: Serializable {
    var count: Int = 0
    var categorys: List<CategoryInfo>? = null
}