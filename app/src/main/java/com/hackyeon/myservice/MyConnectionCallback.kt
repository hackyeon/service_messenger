package com.hackyeon.myservice

interface MyConnectionCallback {
    fun onMessage(msg: MyMessage)

    fun onBind(isBind: Boolean)
}