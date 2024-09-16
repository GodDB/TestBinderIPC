package com.example.appa

import android.os.Bundle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContent {
                Column(modifier = Modifier.fillMaxSize()) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, true),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {


                        Button(onClick = { mClientCallback?.onMessageFromServer("서버 메시지 #22") }) {
                            Text(text = "클라이언트 앱에 메시지 날리기")
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
    }
}

