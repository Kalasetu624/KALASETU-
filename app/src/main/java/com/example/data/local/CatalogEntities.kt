package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "catalog_products")
data class CatalogProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titleEn: String,
    val titleHi: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val category: String,
    val rawMaterialCost: Int,
    val hoursTaken: Float,
    val sellingPrice: Int,
    val stockCount: Int,
    val marketplaces: String,
    val presetImageKey: String,
    val isBackgroundRemoved: Boolean = true,
    val isLightingEnhanced: Boolean = true,
    val voiceTranscript: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "artisan_orders")
data class ArtisanOrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderCode: String,
    val productName: String,
    val productNameHi: String,
    val buyerCity: String,
    val marketplace: String,
    val quantity: Int,
    val unitPrice: Int,
    val totalAmount: Int,
    val isOrderDone: Boolean,
    val orderTimeLabel: String
)
