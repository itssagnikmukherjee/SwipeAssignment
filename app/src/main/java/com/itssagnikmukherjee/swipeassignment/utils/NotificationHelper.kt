package com.itssagnikmukherjee.swipeassignment.utils

import android.Manifest
import com.itssagnikmukherjee.swipeassignment.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationHelper(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "product_sync_channel"
        private const val CHANNEL_NAME = "Product Sync"
        private const val CHANNEL_DESCRIPTION = "Notifications for product sync status"
        private const val NOTIFICATION_ID_SUCCESS = 1001
        private const val NOTIFICATION_ID_PENDING = 1002
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun showProductAddedNotification(productName: String) {
        if (!hasNotificationPermission()) return

        try {
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_check_circle_24)
                .setContentTitle("Product Added Successfully")
                .setContentText("$productName has been added to your store")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()

            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_SUCCESS, notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    fun showProductPendingNotification(productName: String) {
        if (!hasNotificationPermission()) return

        try {
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_info_24)
                .setContentTitle("Product Saved Offline")
                .setContentText("$productName will be synced when you're online")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()

            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_PENDING, notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    fun showSyncCompleteNotification(count: Int) {
        if (!hasNotificationPermission()) return

        try {
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_upload_done)
                .setContentTitle("Sync Complete")
                .setContentText("$count product(s) uploaded successfully")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()

            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_SUCCESS, notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}