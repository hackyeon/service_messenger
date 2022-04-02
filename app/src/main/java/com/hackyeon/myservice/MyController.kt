package com.hackyeon.myservice

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.Messenger
import com.hackyeon.myservice.MyConnection.MSG_REGISTER_CLIENT
import com.hackyeon.myservice.MyConnection.MSG_SEND_TO_ACTIVITY
import java.lang.Exception

class MyController(val callback: ((MyMessage) -> Unit)?) {


    /**
     * Activity 로 보내는 Messenger
     */
    private var mSendToClientMessenger: Messenger

    /**
     * Activity 로 부터 받은 Messenger
     */
    private var mActivityMessenger: Messenger? = null

    fun getMessenger(): Messenger{
        return mSendToClientMessenger
    }

    /**
     * Activity 로 부터 받은 event 처리
     */
    private val handlerCallback = Handler.Callback { msg ->
        if(msg.what == MSG_REGISTER_CLIENT) {
            mActivityMessenger = msg.replyTo
        }
        when(msg.obj) {
            is MyMessage -> callback?.let { it(msg.obj as MyMessage)}
        }
        false
    }
    private var serviceHandler = Handler(Looper.getMainLooper(), handlerCallback)

    init {
        mSendToClientMessenger = Messenger(serviceHandler)
    }

    /**
     * Activity 로 보내는 Message
     */
    fun sendMsgToActivity(obj: MyMessage){
        try {
            val msg = Message.obtain(null, MSG_SEND_TO_ACTIVITY, obj)
            mActivityMessenger?.send(msg)
        } catch (e: Exception) {}
    }

}