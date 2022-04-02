package com.hackyeon.myservice

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import java.lang.Exception


@SuppressLint("StaticFieldLeak")
object MyConnection {
    const val MSG_REGISTER_CLIENT :Int = 1
    const val MSG_SEND_TO_SERVICE  :Int = 2
    const val MSG_SEND_TO_ACTIVITY  :Int = 3

    /**
     * 서비스 바인드 유무
     */
    var isBound = false
    /**
     * 서비스 시작 유무
     */
    var isServiceStart = false

    /**
     * service event callback
     */
    // Service로 부터 받은 이벤트를 Activity로 보내는 callback
    private var eventCallback: MyConnectionCallback? = null
    fun setEventCallback(callback: MyConnectionCallback){
        this.eventCallback = callback
    }

    /**
     * Service 가 작동중인 activity context
     */
    private var mCurrentRunContext: Context? = null

    /**
     * client to service
     */
    // Service 로 부터 받아온 Messenger
    private var mSendToServiceMessenger: Messenger? = null
    // Service 로 보내는 Messenger
    private var mReceiveMsgHandler = Messenger(Handler(Looper.getMainLooper()) { msg ->
        if(msg.obj is MyMessage) {
            eventCallback?.onMessage(msg.obj as MyMessage)
        }
        false
    })

    private val mConnection = object: ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, iBinder: IBinder?) {
            mSendToServiceMessenger = Messenger(iBinder)
            isBound = true
            val msg = Message.obtain(null, MSG_REGISTER_CLIENT).apply {
                replyTo = mReceiveMsgHandler
            }
            mSendToServiceMessenger?.send(msg)
            eventCallback?.onBind(true)
        }

        override fun onServiceDisconnected(componentName: ComponentName?) {
            closeService()
        }
    }

    /**
     * Service 로 Message를 보낸다
     */
    private fun sendMsgToService(obj: MyMessage){
        if(isBound) {
            if(mSendToServiceMessenger != null) {
                try {
                    val msg = Message.obtain(null, MSG_SEND_TO_SERVICE, obj)
                    mSendToServiceMessenger?.send(msg)
                } catch (e: Exception) {}
            }

        }
    }

    /**
     * Activity로 부터 받은 이벤트
     */
    fun sendServiceMsg(){
        sendMsgToService(MyMessage.SEND_SERVICE)
    }

    /**
     * bind 와 동시에 Service를 시작한다
     */
    fun bindWithStartService(activity: Context) {
        bind(activity)
        startService()
    }

    fun bind(activity: Context){
        if(!isBound) {
            mCurrentRunContext = activity
            val bindIntent = Intent(mCurrentRunContext, MyService::class.java)
            mCurrentRunContext?.bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE)
        }
    }
    fun startService(){
        if(!isServiceStart) {
            val bindIntent = Intent(mCurrentRunContext, MyService::class.java)
            mCurrentRunContext?.startService(bindIntent)
        }
    }

    /**
     * unbind 및 Service를 stop한다
     */
    fun closeService(){
        unBind()
        stopService()
    }
    fun unBind(){
        if(isBound) {
            eventCallback?.onBind(false)
            eventCallback = null
            mCurrentRunContext?.unbindService(mConnection)
            mCurrentRunContext = null
            mSendToServiceMessenger = null
            isBound = false
        }
    }
    fun stopService(){
        if(isServiceStart) {
            mCurrentRunContext?.let{
                val bindIntent = Intent(it, MyService::class.java)
                it.stopService(bindIntent)
            }
        }
    }

}