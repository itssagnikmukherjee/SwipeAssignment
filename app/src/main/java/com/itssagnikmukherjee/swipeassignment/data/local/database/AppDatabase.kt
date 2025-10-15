package com.itssagnikmukherjee.swipeassignment.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.itssagnikmukherjee.swipeassignment.data.local.dao.CachedProductDao
import com.itssagnikmukherjee.swipeassignment.data.local.dao.PendingProductsDao
import com.itssagnikmukherjee.swipeassignment.data.local.tables.CachedProduct
import com.itssagnikmukherjee.swipeassignment.data.local.tables.PendingProduct
import com.itssagnikmukherjee.swipeassignment.utils.Converters

@Database(
    entities = [PendingProduct::class, CachedProduct::class],
    version = 1,
    exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pendingProductDao(): PendingProductsDao
    abstract fun cachedProductDao(): CachedProductDao
}

