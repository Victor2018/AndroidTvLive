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

package com.victor.live.ui.fragment

import java.util.Collections

import android.os.Bundle
import android.support.v17.leanback.app.BrowseFragment
import android.support.v17.leanback.widget.*
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast

import com.victor.hdtv.data.ChannelInfo
import com.victor.hdtv.data.ChannelReq
import com.victor.hdtv.interfaces.OnChannelListener
import com.victor.live.MainActivity
import com.victor.live.R
import com.victor.live.presenter.CardPresenter
import com.victor.live.presenter.ChannelPresenterImpl
import com.victor.live.presenter.VideoCardPresener
import com.victor.live.ui.view.ChannelView
import com.victor.live.util.Constant
import com.victor.live.util.WebConfig
import org.victor.khttp.library.module.ChannelHelper
import kotlin.reflect.KClass

/**
 * Loads a grid of cards with movies to browse.
 */
class MainFragment : BrowseFragment(), OnChannelListener,ChannelView {

    var TAG = "MainFragment"
    var channelPresenter: ChannelPresenterImpl? = null
    var channelHelper: ChannelHelper? = null;
    var mChannelReq: ChannelReq ? = null;

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onActivityCreated(savedInstanceState)

        setupUIElements()

        setupEventListeners()

        initialize()
        iniData()
    }

    fun initialize () {
        channelPresenter = ChannelPresenterImpl(this)

        channelHelper = ChannelHelper(activity,ChannelReq::class as KClass<Any>)
        channelHelper?.mOnChannelListener = this

    }

    fun iniData () {
        channelPresenter?.sendRequest(WebConfig.CHANNEL_URL,null,null)
    }

    private fun setupUIElements() {
        title = getString(R.string.browse_title)
        // over title
        headersState = BrowseFragment.HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // set fastLane (or headers) background color
        brandColor = ContextCompat.getColor(activity, R.color.fastlane_background)
        // set search icon color
        searchAffordanceColor = ContextCompat.getColor(activity,
            R.color.search_opaque
        )
    }

    private fun loadRows(data: ChannelReq) {
        mChannelReq = data
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val rowCellPresenter = CardPresenter();
//        val rowCellPresenter = VideoCardPresener();

        for (i in 0 until data.categorys!!.size) {
            if (i != 0) {
                Collections.shuffle(data.categorys!![i].channels)
            }
            val listRowAdapter = ArrayObjectAdapter(rowCellPresenter)
            for (j in 0 until data.categorys!![i].channels!!.size) {
                data.categorys!![i].channels!![j].position = j
                listRowAdapter.add(data.categorys!![i].channels!![j])
            }
            val header = HeaderItem(i.toLong(), data.categorys!![i].channel_category)
            rowsAdapter.add(ListRow(header, listRowAdapter))
        }

        adapter = rowsAdapter
    }

    override fun OnChannel(data: Any?, msg: String) {
        Log.e(TAG,"OnChannel()......")
        if (data == null) {
            Log.e(TAG,"OnChannel()......data == null")
            channelHelper?.sendRequest(Constant.Msg.REQUEST_CHANNEL)
            return
        }
        var channelReq = data as ChannelReq
        loadRows(channelReq)
    }

    override fun OnChannel(data: ChannelReq) {
        loadRows(data)
    }

    private fun setupEventListeners() {
        setOnSearchClickedListener {
            Toast.makeText(activity, "Implement your own in-app search", Toast.LENGTH_LONG)
                .show()
        }

        onItemViewClickedListener = ItemViewClickedListener()
        onItemViewSelectedListener = ItemViewSelectedListener()
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {
            if (item is ChannelInfo) {
                Log.e(TAG,"onItemClicked-row.headerItem.id = " + row.headerItem.id)
                Log.e(TAG,"onItemClicked-row.headerItem.name = " + row.headerItem.name)
                Log.e(TAG,"onItemClicked-item.position = " + item.position)

                (activity as MainActivity).play(mChannelReq,row.headerItem.id.toInt(),item.position,item)
//                PlayActivity.intentStart(activity,item)
            }
        }
    }

    private inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?, item: Any?,
            rowViewHolder: RowPresenter.ViewHolder, row: Row
        ) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        channelPresenter?.detachView()
        channelPresenter = null

        channelHelper?.onDestroy()
        channelHelper = null

    }
}
