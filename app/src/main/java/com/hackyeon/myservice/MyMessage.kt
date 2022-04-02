package com.hackyeon.myservice

sealed class MyMessage {
    object SEND_SERVICE: MyMessage()
    object SEND_ACTIVITY: MyMessage()
}