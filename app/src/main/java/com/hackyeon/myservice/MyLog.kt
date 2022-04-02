package com.hackyeon.myservice

import android.util.Log

object MyLog {
    private const val TAG = "hack"
    private const val isDebug = true

    fun d(msg: Any?) {
        if(isDebug) {
            Log.d(TAG, "$msg")
        }
    }
    fun i(msg: Any?) {
        if(isDebug) {
            Log.i(TAG, "$msg")
        }
    }
    fun e(msg: Any?) {
        if(isDebug) {
            Log.e(TAG, "$msg")
        }
    }

}