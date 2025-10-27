package com.itssagnikmukherjee.swipeassignment.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AirplaneModeBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        var isAirplaneModeEnabled = p1?.getBooleanExtra("state",false) ?: return
        if(isAirplaneModeEnabled){
            Toast.makeText(p0,"Airplane Mode Enabled", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(p0,"Airplane Mode Disabled", Toast.LENGTH_LONG).show()
        }
    }
}