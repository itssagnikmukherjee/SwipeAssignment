package com.itssagnikmukherjee.swipeassignment.utils

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromSyncStatus(value: SyncStatus): String {
        return value.name
    }

    @TypeConverter
    fun toSyncStatus(value: String): SyncStatus {
        return SyncStatus.valueOf(value)
    }
}