package com.example.data.local

import kotlinx.coroutines.flow.Flow

class KalaSetuRepository(private val dao: KalaSetuDao) {
    val allProducts: Flow<List<CatalogProductEntity>> = dao.getAllProducts()
    val allOrders: Flow<List<ArtisanOrderEntity>> = dao.getAllOrders()

    suspend fun insertProduct(product: CatalogProductEntity): Long = dao.insertProduct(product)

    suspend fun updateProductStock(product: CatalogProductEntity, newStock: Int) {
        dao.updateProduct(product.copy(stockCount = newStock.coerceAtLeast(0)))
    }

    suspend fun deleteProduct(id: Int) = dao.deleteProductById(id)

    suspend fun toggleOrderDone(order: ArtisanOrderEntity) {
        dao.updateOrder(order.copy(isOrderDone = !order.isOrderDone))
    }

    suspend fun insertOrder(order: ArtisanOrderEntity) = dao.insertOrder(order)

    suspend fun seedInitialDataIfNeeded() {
        if (dao.getProductCount() == 0) {
            dao.insertProduct(
                CatalogProductEntity(
                    titleEn = "Hand-Painted Terracotta Diya & Kulhad Set (4 Pcs)",
                    titleHi = "हाथ से रंगे मिट्टी के दीये और कुल्हड़ सेट (4 पीस)",
                    descriptionEn = "Eco-friendly natural river clay diyas and kulhad cups hand-painted with pastel blue & white folk motifs. Ideal for home decor and festive gifting.",
                    descriptionHi = "प्राकृतिक मिट्टी से बने और हाथ से रंगे सुंदर दीये व कुल्हड़। त्योहार और घर की सजावट के लिए उत्तम।",
                    category = "Terracotta & Clay",
                    rawMaterialCost = 40,
                    hoursTaken = 1.5f,
                    sellingPrice = 120,
                    stockCount = 18,
                    marketplaces = "ONDC Mystore, Meesho Craft",
                    presetImageKey = "terracotta",
                    voiceTranscript = "यह हाथ से बना मिट्टी का दीया और कुल्हड़ है, इसे बनाने में डेढ़ घंटे लगे और मिट्टी व रंग का खर्च 40 रुपये आया।"
                )
            )
            dao.insertProduct(
                CatalogProductEntity(
                    titleEn = "Jaipur Blue Pottery Floral Mini Vase",
                    titleHi = "जयपुर ब्लू पॉटरी फूलदान (मिनी वास)",
                    descriptionEn = "Authentic quartz-stone handcrafted Jaipur Blue Pottery vase with cobalt floral glaze. Lead-free and water-resistant.",
                    descriptionHi = "असली जयपुर ब्लू पॉटरी से बना हाथ का काम वाला छोटा फूलदान।",
                    category = "Blue Pottery",
                    rawMaterialCost = 65,
                    hoursTaken = 2.0f,
                    sellingPrice = 185,
                    stockCount = 12,
                    marketplaces = "ONDC Mystore, Amazon Karigar",
                    presetImageKey = "pottery",
                    voiceTranscript = "जयपुर की ब्लू पॉटरी का छोटा फूलदान, 2 घंटे मेहनत और 65 रुपये कच्चा माल लगा है।"
                )
            )
            dao.insertProduct(
                CatalogProductEntity(
                    titleEn = "Handwoven Assam Bamboo Tea Coaster Set (6 Pcs)",
                    titleHi = "बांस से बने चाय कोस्टर और टोकरी सेट (6 पीस)",
                    descriptionEn = "Finely woven organic bamboo cane coasters crafted by rural artisans. Durable, washable, and 100% biodegradable.",
                    descriptionHi = "ग्रामीण कारीगरों द्वारा बांस से बुने गए मजबूत और सुंदर टी-कोस्टर।",
                    category = "Bamboo & Cane",
                    rawMaterialCost = 45,
                    hoursTaken = 1.5f,
                    sellingPrice = 145,
                    stockCount = 24,
                    marketplaces = "ONDC Mystore, IndiaMART B2B",
                    presetImageKey = "bamboo",
                    voiceTranscript = "बांस से बुना हुआ 6 कोस्टर का सेट, डेढ़ घंटा लगा और बांस का खर्च 45 रुपये है।"
                )
            )
        }

        if (dao.getOrderCount() == 0) {
            dao.insertOrder(
                ArtisanOrderEntity(
                    orderCode = "ONDC-8492",
                    productName = "Terracotta Diya & Kulhad Set",
                    productNameHi = "मिट्टी के दीये और कुल्हड़ सेट",
                    buyerCity = "Jaipur, RJ",
                    marketplace = "ONDC Mystore",
                    quantity = 1,
                    unitPrice = 120,
                    totalAmount = 120,
                    isOrderDone = false,
                    orderTimeLabel = "Today, 10:15 AM"
                )
            )
            dao.insertOrder(
                ArtisanOrderEntity(
                    orderCode = "MSH-3910",
                    productName = "Assam Bamboo Tea Coasters",
                    productNameHi = "बांस टी-कोस्टर सेट",
                    buyerCity = "Pune, MH",
                    marketplace = "Meesho Craft",
                    quantity = 2,
                    unitPrice = 145,
                    totalAmount = 290,
                    isOrderDone = true,
                    orderTimeLabel = "Yesterday, 5:40 PM"
                )
            )
            dao.insertOrder(
                ArtisanOrderEntity(
                    orderCode = "ONDC-7741",
                    productName = "Jaipur Blue Pottery Mini Vase",
                    productNameHi = "ब्लू पॉटरी फूलदान",
                    buyerCity = "Delhi NCR",
                    marketplace = "ONDC Mystore",
                    quantity = 1,
                    unitPrice = 185,
                    totalAmount = 185,
                    isOrderDone = true,
                    orderTimeLabel = "2 Days Ago"
                )
            )
            dao.insertOrder(
                ArtisanOrderEntity(
                    orderCode = "KRG-5120",
                    productName = "Terracotta Diya & Kulhad Set",
                    productNameHi = "मिट्टी के दीये और कुल्हड़ सेट",
                    buyerCity = "Bengaluru, KA",
                    marketplace = "Amazon Karigar",
                    quantity = 1,
                    unitPrice = 120,
                    totalAmount = 120,
                    isOrderDone = false,
                    orderTimeLabel = "Just Now"
                )
            )
        }
    }
}
