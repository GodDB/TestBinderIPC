package com.example.appa

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.example.shared.ICallback
import com.example.shared.IExampleService
import kotlin.math.log

var mClientCallback: ICallback? = null

val logList = mutableStateListOf<String>()

class ExampleService : Service() {

    private val mBinder: IExampleService.Stub = object : IExampleService.Stub() {
        @Throws(RemoteException::class)
        override fun registerCallback(callback: ICallback?) {
            logList.add("registerCallback")
            mClientCallback = callback
        }

        @Throws(RemoteException::class)
        override fun sendMessageToServer(message: String) {
            logList.add("클라이언트로부터 메시지 옴 ${message}")
        }
    }

    override fun onCreate() {
        super.onCreate()
        logList.add("ExampleService onCreate")
    }

    override fun onBind(intent: Intent?): IBinder {
        logList.add("ExampleService onBind")
        return mBinder
    }

    override fun onDestroy() {
        mClientCallback = null
        super.onDestroy()
    }
}