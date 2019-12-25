package com.victor.clips.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.victor.clips.util.StatusBarUtil

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: BaseActivity.java
 * Author: Victor
 * Date: 2018/9/5 15:55
 * Description: 
 * -----------------------------------------------------------------
 */
abstract class BaseActivity: AppCompatActivity() {
    protected var TAG = javaClass.simpleName

    abstract fun getLayoutResource(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
        initializeSuper()
    }

    fun initializeSuper () {
        StatusBarUtil.translucentStatusBar(this, true,false,false)
        StatusBarUtil.hideBottomUIMenu(window)
    }


    override fun onResume() {
        super.onResume()
    }


    override fun onDestroy() {
        super.onDestroy()
    }

}