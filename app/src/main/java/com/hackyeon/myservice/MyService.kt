package com.hackyeon.myservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.lang.Exception

class MyService: Service() {

    private lateinit var controller: MyController


    override fun onCreate() {
        super.onCreate()

        MyConnection.isServiceStart = true
        controller = MyController {
            updateEvent(it)
        }
    }

    /**
     * Activity 로 부터 받은 event 처리
     */
    private fun updateEvent(msg: MyMessage){
        try {
            when(msg) {
                is MyMessage.SEND_SERVICE -> {
                    console("엑티비티로 부터 메세지를 받음")
                    controller.sendMsgToActivity(MyMessage.SEND_ACTIVITY)
                }
            }

        } catch (e: Exception) {}
    }

    override fun onBind(p0: Intent?): IBinder? {
        return controller.getMessenger().binder
    }

    private fun console(msg: Any?){
        MyLog.d("## ${this.javaClass.simpleName} ## $msg")
    }
}