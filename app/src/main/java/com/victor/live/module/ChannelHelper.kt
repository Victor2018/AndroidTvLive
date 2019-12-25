package org.victor.khttp.library.module

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON
import com.victor.hdtv.data.ChannelReq
import com.victor.hdtv.interfaces.OnChannelListener
import com.victor.live.util.Constant
import com.victor.live.util.MainHandler
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlin.reflect.KClass
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception


/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: ChannelHelperkt.kt
 * Author: Victor
 * Date: 2018/8/22 15:47
 * Description: 使用HandlerThread读取本地json文件
 * -----------------------------------------------------------------
 */
class ChannelHelper (var context: Context?,var responseCls: KClass<Any>?) {
    private var TAG = "ChannelHelper"
    private var mRequestHandler: Handler? = null
    private var mRequestHandlerThread: HandlerThread? = null
    var mOnChannelListener: OnChannelListener? = null

    //由于primary constructor不能包含任何代码，因此使用 init 代码块对其初始化，同时可以在初始化代码块中使用构造函数的参数
    init {
        startRequestTask()
    }

    fun startRequestTask() {
        mRequestHandlerThread = HandlerThread("HttpRequestTask")
        mRequestHandlerThread?.start()
        mRequestHandler = object : Handler(mRequestHandlerThread?.looper) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    Constant.Msg.REQUEST_CHANNEL -> {
                        var result = getJsonFromAssets("live.json")
                        onReponse(result)
                    }
                }
            }
        }
    }

    fun sendRequest (msg: Int) {
        mRequestHandler?.sendEmptyMessage(msg);
    }

    fun onDestroy() {
        mRequestHandlerThread?.quit()
        mRequestHandlerThread = null
    }

    inline fun <reified T: Any> parseObject(text: String?, clazz: Class<T>?): T? {
        try {
            return JSON.parseObject(text, clazz)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }

    inline fun <reified T: Any> parseArray(text: String?, clazz: Class<T>?): List<T>? {
        try {
            return JSON.parseArray(text, clazz)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }

    fun parseReponse (result: String?): Any? {
        var reponse:Any? = null
        try {
            if (responseCls!!.toString().contains("String")) {
                Log.e(TAG,"reponse data is String")
                if (TextUtils.isEmpty(result)) {
                    return null
                }
                return result
            }
            val json = JSONTokener(result).nextValue()
            if (json is JSONObject) {
                Log.e(TAG,"reponse data is JSONObject")
                reponse  = parseObject(result,responseCls!!.java)
            } else if (json is JSONArray) {
                Log.e(TAG,"reponse data is JSONArray")
                reponse = parseArray(result,responseCls!!.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return reponse
    }

    fun onReponse(result: String?) {
        MainHandler.runMainThread {
            var reponse = parseReponse(result) as ChannelReq
            mOnChannelListener?.OnChannel(reponse)
        }
    }

    fun getJsonFromAssets(fileName: String): String? {
        var result: String? = ""
        var bufferedReader: BufferedReader? = null
        try {
            val assetManager = context?.getAssets()
            //通过管理器打开文件并读取
            bufferedReader = BufferedReader(InputStreamReader(assetManager?.open(fileName)))
            try {
                var read: String? = null
                while ({ read = bufferedReader.readLine();read }() != null) {
                    result = result + read + "\r\n"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close()
            }
        }
        return result
    }

}