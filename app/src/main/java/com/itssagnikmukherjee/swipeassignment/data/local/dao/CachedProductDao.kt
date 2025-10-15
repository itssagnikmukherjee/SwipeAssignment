package com.itssagnikmukherjee.swipeassignment.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itssagnikmukherjee.swipeassignment.data.local.tables.CachedProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedProductDao {
    @Query("SELECT * FROM cached_products ORDER BY lastUpdated ASC")
    fun getAllCachedProducts(): Flow<List<CachedProduct>>

    @Query("SELECT * FROM cached_products ORDER BY lastUpdated ASC")
    suspend fun getAllCachedProductsList(): List<CachedProduct>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<CachedProduct>)

    @Query("DELETE FROM cached_products")
    suspend fun clearCache()

    @Query("SELECT * FROM cached_products WHERE productType = :type")
    fun getProductsByType(type: String): Flow<List<CachedProduct>>
}