package com.example.appb

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shared.ICallback
import com.example.shared.IExampleService
import kotlin.math.log


class MainActivity : ComponentActivity() {

    private val logList: SnapshotStateList<String> = mutableStateListOf()

    private var mService: IExampleService? = null
    private var mBound: Boolean = false
    private val mCallback = object : ICallback.Stub() {
        override fun onMessageFromServer(message: String?) {
            logList.add("서버에서 메시지 옴 - $message")
        }
    }

    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            logList.add("onServiceConnected")
            mService = IExampleService.Stub.asInterface(service)
            mBound = true

            try {
                // 콜백 등록
                mService!!.registerCallback(mCallback)

                // 서버에 메시지 전송
                mService!!.sendMessageToServer("클라이언트 연결 됨")

            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            logList.add("onServiceDisconnected")
            mService = null
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column(modifier = Modifier.fillMaxSize()) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, true),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Button(onClick = { startService() }) {
                        Text(text = "서비스 연결 요청")
                    }

                    Button(onClick = { stopService() }) {
                        Text(text = "서비스 연결 해제")
                    }

                    Button(onClick = { mService?.sendMessageToServer("클라이언트에서 메시지 보냄 #1"); }) {
                        Text(text = "서버 앱에 메시지 날리기")
                    }

                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, true)
                        .padding(start = 20.dp)
                ) {
                    logList.forEach { log ->
                        item {
                            Text(
                                modifier = Modifier.padding(top = 10.dp),
                                text = log
                            )
                        }
                    }
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // 서비스 언바인딩 및 콜백 해제
        if (mBound) {
            stopService()
        }
    }


    private fun startService() {
        // 서버의 서비스 바인딩
        logList.add("start service!!!")
        val intent = Intent()
        intent.setAction("com.example.appa.ExampleService")
        intent.setPackage("com.example.appa")
        bindService(intent, mConnection, BIND_AUTO_CREATE)
    }

    private fun stopService() {
        logList.add("unbind service")
        try {
            mService?.registerCallback(null); // 콜백 해제
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
        unbindService(mConnection);
        mBound = false;
    }

}