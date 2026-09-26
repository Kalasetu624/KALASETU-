package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface KalaSetuDao {
    @Query("SELECT * FROM catalog_products ORDER BY createdAt DESC")
    fun getAllProducts(): Flow<List<CatalogProductEntity>>

    @Query("SELECT COUNT(*) FROM catalog_products")
    suspend fun getProductCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: CatalogProductEntity): Long

    @Update
    suspend fun updateProduct(product: CatalogProductEntity)

    @Query("DELETE FROM catalog_products WHERE id = :id")
    suspend fun deleteProductById(id: Int)

    @Query("SELECT * FROM artisan_orders ORDER BY isOrderDone ASC, id DESC")
    fun getAllOrders(): Flow<List<ArtisanOrderEntity>>

    @Query("SELECT COUNT(*) FROM artisan_orders")
    suspend fun getOrderCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: ArtisanOrderEntity): Long

    @Update
    suspend fun updateOrder(order: ArtisanOrderEntity)
}
