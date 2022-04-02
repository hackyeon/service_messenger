package com.hackyeon.myservice

import android.content.ComponentName
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import androidx.databinding.DataBindingUtil
import com.hackyeon.myservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MyConnectionCallback {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        MyConnection.setEventCallback(this)
        MyConnection.bindWithStartService(this)

        binding.testButton.setOnClickListener {
            MyConnection.sendServiceMsg()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MyConnection.closeService()
    }

    /**
     * Service 로 부터 받은 event 처리
     */
    override fun onMessage(msg: MyMessage) {
        when(msg) {
            MyMessage.SEND_ACTIVITY -> {
                console("서비스로 부터 메세지를 받음")
            }
        }
    }

    override fun onBind(isBind: Boolean) {
        console("isBind: $isBind")
    }


    private fun console(msg: Any?){
        MyLog.d("## ${this.javaClass.simpleName} ## ${msg}")
    }
}