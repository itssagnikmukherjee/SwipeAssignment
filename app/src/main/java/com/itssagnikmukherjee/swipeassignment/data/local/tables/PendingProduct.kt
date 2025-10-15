package com.itssagnikmukherjee.swipeassignment.data.local.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itssagnikmukherjee.swipeassignment.utils.SyncStatus

@Entity(tableName = "pending_products")
data class PendingProduct(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val productName: String,
    val productType: String,
    val price: String,
    val tax: String,
    val imageUri: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val syncStatus: SyncStatus = SyncStatus.PENDING
)