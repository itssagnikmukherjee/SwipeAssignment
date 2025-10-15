package com.itssagnikmukherjee.swipeassignment.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.itssagnikmukherjee.swipeassignment.data.local.tables.PendingProduct
import com.itssagnikmukherjee.swipeassignment.utils.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingProductsDao{

    @Query("SELECT * FROM pending_products WHERE syncStatus = 'PENDING' ORDER BY timestamp ASC")
    fun getPendingProducts(): Flow<List<PendingProduct>>

    @Query("SELECT * FROM pending_products WHERE syncStatus = 'PENDING' ORDER BY timestamp ASC")
    suspend fun getPendingProductsList(): List<PendingProduct>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPendingProduct(product: PendingProduct): Long

    @Update
    suspend fun updatePendingProduct(product: PendingProduct)

    @Query("DELETE FROM pending_products WHERE id = :id")
    suspend fun deletePendingProduct(id: Int)

    @Query("UPDATE pending_products SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: Int, status: SyncStatus)

}