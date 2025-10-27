package com.itssagnikmukherjee.swipeassignment

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.itssagnikmukherjee.swipeassignment.ui.navigation.AppNavigation
import com.itssagnikmukherjee.swipeassignment.ui.theme.SwipeAssignmentTheme
import com.itssagnikmukherjee.swipeassignment.utils.AirplaneModeBroadcastReceiver
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    lateinit var AirplaneModeBroadcastReceiver : AirplaneModeBroadcastReceiver
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        AirplaneModeBroadcastReceiver = AirplaneModeBroadcastReceiver()
        IntentFilter("android.intent.action.AIRPLANE_MODE").also {
            registerReceiver(AirplaneModeBroadcastReceiver, it)
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(AirplaneModeBroadcastReceiver)
    }
}