package com.example.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.local.ArtisanOrderEntity
import com.example.data.local.CatalogProductEntity
import com.example.data.local.KalaSetuRepository
import com.example.data.remote.GeminiCatalogService
import com.example.util.ImageEnhancerUtil
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class KalaSetuUiState(
    // Navigation: Step 1 (Login) is default as requested
    val currentStep: Int = 1,
    val showArchitectureModal: Boolean = false,
    val statusMessage: String? = null,

    // Step 1: Progressive Onboarding (Login)
    val phoneNumber: String = "9876543210",
    val artisanName: String = "Ramesh Kumhar",
    val artisanCluster: String = "Jaipur Terracotta & Pottery",
    val preferredLanguage: String = "English + हिंदी",
    val isOtpSent: Boolean = false,
    val otpInput: String = "",
    val demoOtpCode: String = "4829",
    val isLoggedIn: Boolean = false,

    // Step 2: Photo Capture & Gallery Upload (with Permission flow)
    val showPermissionPromptDialog: Boolean = false,
    val pendingMediaSource: String = "CAMERA", // "CAMERA" or "GALLERY"
    val cameraPermissionGranted: Boolean = false,
    val galleryPermissionGranted: Boolean = false,
    val selectedPresetKey: String = "terracotta",
    val capturedBitmap: Bitmap? = null,
    val photoSourceLabel: String = "Terracotta Diya & Kulhad Set",

    // Step 3: AI Studio Enhancement (rembg + OpenCV Histogram Equalization)
    val removeBackgroundEnabled: Boolean = true,
    val lightingEqualizationEnabled: Boolean = true,
    val studioShadowEnabled: Boolean = true,
    val enhancedBitmap: Bitmap? = null,
    val isEnhancingImage: Boolean = false,
    val showOriginalPreview: Boolean = false,

    // Step 4: Voice-to-Catalog (Multilingual NLP)
    val selectedVoiceLang: String = "हिंदी (Hindi)",
    val voiceTranscript: String = "यह हाथ से बना मिट्टी का दीया और कुल्हड़ सेट है, इसे बनाने में डेढ़ घंटे लगे और मिट्टी व रंग का खर्च 40 रुपये आया।",
    val isListeningVoice: Boolean = false,
    val isGeneratingCatalog: Boolean = false,
    val catalogTitleEn: String = "Hand-Painted Terracotta Clay Diya & Kulhad Set (4 Pcs)",
    val catalogTitleHi: String = "हाथ से रंगे प्राकृतिक मिट्टी के दीये और कुल्हड़ सेट (4 पीस)",
    val catalogDescEn: String = "Natural river-clay terracotta diyas and kulhad cups hand-painted with pastel folk motifs. Eco-friendly, lead-free, and directly supporting rural potter families.",
    val catalogDescHi: String = "शुद्ध प्राकृतिक मिट्टी से हाथ से बनाकर रंगे गए सुंदर दीये और कुल्हड़। त्योहार और दैनिक उपयोग के लिए श्रेष्ठ।",
    val craftCategory: String = "Terracotta & Clay",
    val seoKeywords: List<String> = listOf("Terracotta Diya", "Handmade Kulhad", "VocalForLocal", "ONDC Pottery"),
    val isLiveGeminiUsed: Boolean = false,

    // Step 5: Dynamic Pricing (Smaller Price Range ₹50 - ₹350)
    val rawMaterialCost: Int = 40,
    val hoursTaken: Float = 1.5f,
    val fairHourlyWage: Int = 36,
    val packagingFee: Int = 12,
    val suggestedSellingPrice: Int = 120,
    val selectedSellingPrice: Int = 120,
    val initialStockCount: Int = 15,

    // Step 6: Market Linkage & Progressive KYC
    val selectedMarketplaces: Set<String> = setOf("ONDC Mystore", "Meesho Craft"),
    val artisanPehchanId: String = "PEHCHAN-RJ-48291",
    val upiId: String = "ramesh.karigar@upi",
    val isGstExemptSmallArtisan: Boolean = true,
    val gstNumber: String = "",
    val isKycSaved: Boolean = true,

    // Step 7: One-Tap ONDC Onboarding
    val isPublishingOndc: Boolean = false,
    val isPublishedSuccess: Boolean = false,
    val publishedListingId: String = "ONDC-KARIGAR-8921",

    // Step 8: Dashboard Filter
    val orderFilter: String = "ALL" // "ALL", "PENDING", "DONE"
)

class KalaSetuViewModel(
    private val repository: KalaSetuRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(KalaSetuUiState())
    val uiState: StateFlow<KalaSetuUiState> = _uiState.asStateFlow()

    val products: StateFlow<List<CatalogProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<ArtisanOrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }
        recalculateDynamicPrice(rawCost = 40, hours = 1.5f)
    }

    fun goToStep(step: Int) {
        val target = step.coerceIn(1, 8)
        _uiState.update { it.copy(currentStep = target, statusMessage = null) }
    }

    fun nextStep() {
        val current = _uiState.value.currentStep
        if (current < 8) {
            _uiState.update { it.copy(currentStep = current + 1, statusMessage = null) }
        }
    }

    fun previousStep() {
        val current = _uiState.value.currentStep
        if (current > 1) {
            _uiState.update { it.copy(currentStep = current - 1, statusMessage = null) }
        }
    }

    fun setArchitectureModalVisible(visible: Boolean) {
        _uiState.update { it.copy(showArchitectureModal = visible) }
    }

    fun clearStatusMessage() {
        _uiState.update { it.copy(statusMessage = null) }
    }

    // --- Step 1: Progressive Onboarding (Login) ---
    fun updatePhoneNumber(phone: String) {
        val cleaned = phone.filter { it.isDigit() }.take(10)
        _uiState.update { it.copy(phoneNumber = cleaned) }
    }

    fun updateArtisanName(name: String) {
        _uiState.update { it.copy(artisanName = name) }
    }

    fun updatePreferredLanguage(lang: String) {
        _uiState.update { it.copy(preferredLanguage = lang) }
    }

    fun sendOtp() {
        val phone = _uiState.value.phoneNumber
        if (phone.length < 10) {
            _uiState.update { it.copy(statusMessage = "Please enter a valid 10-digit mobile number") }
            return
        }
        _uiState.update {
            it.copy(
                isOtpSent = true,
                otpInput = "4829",
                statusMessage = "Demo OTP 4829 auto-detected via SMS!"
            )
        }
    }

    fun updateOtpInput(otp: String) {
        val cleaned = otp.filter { it.isDigit() }.take(4)
        _uiState.update { it.copy(otpInput = cleaned) }
    }

    fun verifyOtpAndProceed() {
        val state = _uiState.value
        if (state.otpInput.isEmpty() && !state.isOtpSent) {
            _uiState.update {
                it.copy(
                    isOtpSent = true,
                    otpInput = "4829",
                    isLoggedIn = true,
                    currentStep = 2,
                    statusMessage = "Logged in as ${state.artisanName}!"
                )
            }
            return
        }
        _uiState.update {
            it.copy(
                isLoggedIn = true,
                currentStep = 2,
                statusMessage = "Welcome ${state.artisanName}! Let's capture your product photo."
            )
        }
    }

    // --- Step 2: Camera & Gallery Upload with Permission Flow ---
    fun requestMediaWithPermissionDialog(source: String) {
        _uiState.update {
            it.copy(
                showPermissionPromptDialog = true,
                pendingMediaSource = source
            )
        }
    }

    fun dismissPermissionDialog() {
        _uiState.update { it.copy(showPermissionPromptDialog = false) }
    }

    fun onPermissionResult(source: String, granted: Boolean) {
        _uiState.update {
            it.copy(
                showPermissionPromptDialog = false,
                cameraPermissionGranted = if (source == "CAMERA") granted else it.cameraPermissionGranted,
                galleryPermissionGranted = if (source == "GALLERY") granted else it.galleryPermissionGranted,
                statusMessage = if (granted) "$source access granted!" else "$source permission granted for sample studio mode."
            )
        }
    }

    fun onBitmapCapturedOrPicked(bitmap: Bitmap, label: String) {
        _uiState.update {
            it.copy(
                capturedBitmap = bitmap,
                enhancedBitmap = null,
                photoSourceLabel = label,
                statusMessage = "Photo ready! Tap 'Next Step' for AI Studio Background Removal."
            )
        }
        runStudioImageEnhancement(bitmap)
    }

    fun selectCraftPreset(context: Context, presetKey: String) {
        val (resId, label, category, defaultVoice, cost, hours) = when (presetKey) {
            "pottery" -> PresetConfig(
                resId = R.drawable.img_craft_pottery_1790431500975,
                label = "Jaipur Blue Pottery Mini Vase",
                category = "Blue Pottery",
                voice = "जयपुर की ब्लू पॉटरी का छोटा फूलदान, 2 घंटे मेहनत और 65 रुपये कच्चा माल लगा है।",
                cost = 65,
                hours = 2.0f
            )
            "bamboo" -> PresetConfig(
                resId = R.drawable.img_craft_bamboo_1790431524732,
                label = "Handwoven Assam Bamboo Coasters",
                category = "Bamboo & Cane",
                voice = "बांस से बुना हुआ 6 कोस्टर का सेट, डेढ़ घंटा लगा और बांस का खर्च 45 रुपये है।",
                cost = 45,
                hours = 1.5f
            )
            else -> PresetConfig(
                resId = R.drawable.img_craft_terracotta_1790431512586,
                label = "Terracotta Diya & Kulhad Set (4 Pcs)",
                category = "Terracotta & Clay",
                voice = "यह हाथ से बना मिट्टी का दीया और कुल्हड़ सेट है, इसे बनाने में डेढ़ घंटे लगे और मिट्टी व रंग का खर्च 40 रुपये आया।",
                cost = 40,
                hours = 1.5f
            )
        }

        viewModelScope.launch {
            val bmp = BitmapFactory.decodeResource(context.resources, resId)
            _uiState.update {
                it.copy(
                    selectedPresetKey = presetKey,
                    capturedBitmap = bmp,
                    photoSourceLabel = label,
                    craftCategory = category,
                    voiceTranscript = defaultVoice
                )
            }
            recalculateDynamicPrice(rawCost = cost, hours = hours)
            if (bmp != null) {
                runStudioImageEnhancement(bmp)
            }
        }
    }

    private data class PresetConfig(
        val resId: Int,
        val label: String,
        val category: String,
        val voice: String,
        val cost: Int,
        val hours: Float
    )

    // --- Step 3: AI Enhancement (rembg + OpenCV Histogram Equalization) ---
    fun toggleRemoveBackground(enabled: Boolean) {
        _uiState.update { it.copy(removeBackgroundEnabled = enabled) }
        _uiState.value.capturedBitmap?.let { runStudioImageEnhancement(it) }
    }

    fun toggleLightingEqualization(enabled: Boolean) {
        _uiState.update { it.copy(lightingEqualizationEnabled = enabled) }
        _uiState.value.capturedBitmap?.let { runStudioImageEnhancement(it) }
    }

    fun toggleStudioShadow(enabled: Boolean) {
        _uiState.update { it.copy(studioShadowEnabled = enabled) }
        _uiState.value.capturedBitmap?.let { runStudioImageEnhancement(it) }
    }

    fun toggleOriginalPreview(showOriginal: Boolean) {
        _uiState.update { it.copy(showOriginalPreview = showOriginal) }
    }

    fun runStudioImageEnhancement(sourceBitmap: Bitmap? = _uiState.value.capturedBitmap) {
        val bmp = sourceBitmap ?: return
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isEnhancingImage = true) }
            val result = ImageEnhancerUtil.processStudioImage(
                source = bmp,
                removeBackground = state.removeBackgroundEnabled,
                enhanceLighting = state.lightingEqualizationEnabled,
                addStudioShadow = state.studioShadowEnabled
            )
            _uiState.update {
                it.copy(
                    enhancedBitmap = result,
                    isEnhancingImage = false,
                    statusMessage = "Studio White Background & Histogram Lighting Applied!"
                )
            }
        }
    }

    // --- Step 4: Voice-to-Catalog (Multilingual NLP) ---
    fun selectVoiceLanguage(lang: String) {
        _uiState.update { it.copy(selectedVoiceLang = lang) }
    }

    fun updateVoiceTranscript(text: String) {
        _uiState.update { it.copy(voiceTranscript = text) }
    }

    fun setListeningVoice(listening: Boolean) {
        _uiState.update { it.copy(isListeningVoice = listening) }
    }

    fun applyVoiceSamplePreset(sampleText: String, lang: String) {
        _uiState.update {
            it.copy(
                voiceTranscript = sampleText,
                selectedVoiceLang = lang
            )
        }
        generateAiCatalogFromVoice()
    }

    fun updateCatalogTitleEn(title: String) {
        _uiState.update { it.copy(catalogTitleEn = title) }
    }

    fun updateCatalogTitleHi(title: String) {
        _uiState.update { it.copy(catalogTitleHi = title) }
    }

    fun updateCatalogDescEn(desc: String) {
        _uiState.update { it.copy(catalogDescEn = desc) }
    }

    fun generateAiCatalogFromVoice() {
        val state = _uiState.value
        if (state.voiceTranscript.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isGeneratingCatalog = true, statusMessage = null) }
            val result = GeminiCatalogService.processVoiceToCatalog(
                voiceTranscript = state.voiceTranscript,
                language = state.selectedVoiceLang,
                craftCategory = state.craftCategory
            )
            _uiState.update {
                it.copy(
                    isGeneratingCatalog = false,
                    catalogTitleEn = result.titleEn,
                    catalogTitleHi = result.titleHi,
                    catalogDescEn = result.descriptionEn,
                    catalogDescHi = result.descriptionHi,
                    seoKeywords = result.seoKeywords,
                    isLiveGeminiUsed = result.isLiveGeminiResponse,
                    statusMessage = "AI SEO Catalog & Hindi Translation Generated!"
                )
            }
            recalculateDynamicPrice(
                rawCost = result.extractedMaterialCost,
                hours = result.extractedHours
            )
        }
    }

    // --- Step 5: Dynamic Pricing (Smaller Price Range ₹50 - ₹350) ---
    fun updateRawMaterialCost(cost: Int) {
        val clamped = cost.coerceIn(15, 180)
        recalculateDynamicPrice(rawCost = clamped, hours = _uiState.value.hoursTaken)
    }

    fun updateHoursTaken(hours: Float) {
        val rounded = ((hours * 2f).roundToInt() / 2f).coerceIn(0.5f, 5.0f)
        recalculateDynamicPrice(rawCost = _uiState.value.rawMaterialCost, hours = rounded)
    }

    fun updateSelectedSellingPrice(price: Int) {
        _uiState.update { it.copy(selectedSellingPrice = price.coerceIn(45, 400)) }
    }

    fun updateInitialStockCount(count: Int) {
        _uiState.update { it.copy(initialStockCount = count.coerceIn(1, 200)) }
    }

    private fun recalculateDynamicPrice(rawCost: Int, hours: Float) {
        val state = _uiState.value
        val laborCost = (hours * state.fairHourlyWage).roundToInt()
        val baseCost = rawCost + laborCost + state.packagingFee
        // Add ~18% sustainable artisan margin, kept in smaller affordable range (₹55 - ₹320)
        val suggested = ((baseCost * 1.18f).roundToInt() / 5 * 5).coerceIn(55, 320)
        _uiState.update {
            it.copy(
                rawMaterialCost = rawCost,
                hoursTaken = hours,
                suggestedSellingPrice = suggested,
                selectedSellingPrice = suggested
            )
        }
    }

    // --- Step 6: Market Linkage & Progressive KYC ---
    fun toggleMarketplace(marketplace: String) {
        val current = _uiState.value.selectedMarketplaces.toMutableSet()
        if (current.contains(marketplace)) {
            if (current.size > 1) current.remove(marketplace)
        } else {
            current.add(marketplace)
        }
        _uiState.update { it.copy(selectedMarketplaces = current) }
    }

    fun updateArtisanPehchanId(id: String) {
        _uiState.update { it.copy(artisanPehchanId = id) }
    }

    fun updateUpiId(upi: String) {
        _uiState.update { it.copy(upiId = upi) }
    }

    fun toggleGstExempt(exempt: Boolean) {
        _uiState.update { it.copy(isGstExemptSmallArtisan = exempt) }
    }

    fun updateGstNumber(gst: String) {
        _uiState.update { it.copy(gstNumber = gst) }
    }

    // --- Step 7: One-Tap ONDC Onboarding & Publish ---
    fun publishToOndcAndSaveCatalog() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isPublishingOndc = true, isPublishedSuccess = false) }
            delay(850)
            val newProduct = CatalogProductEntity(
                titleEn = state.catalogTitleEn,
                titleHi = state.catalogTitleHi,
                descriptionEn = state.catalogDescEn,
                descriptionHi = state.catalogDescHi,
                category = state.craftCategory,
                rawMaterialCost = state.rawMaterialCost,
                hoursTaken = state.hoursTaken,
                sellingPrice = state.selectedSellingPrice,
                stockCount = state.initialStockCount,
                marketplaces = state.selectedMarketplaces.joinToString(", "),
                presetImageKey = state.selectedPresetKey,
                isBackgroundRemoved = state.removeBackgroundEnabled,
                isLightingEnhanced = state.lightingEqualizationEnabled,
                voiceTranscript = state.voiceTranscript
            )
            repository.insertProduct(newProduct)
            // Also create a new incoming sample order for this newly listed item!
            repository.insertOrder(
                ArtisanOrderEntity(
                    orderCode = "ONDC-${(1000..9999).random()}",
                    productName = state.catalogTitleEn.take(32),
                    productNameHi = state.catalogTitleHi.take(32),
                    buyerCity = "Mumbai, MH",
                    marketplace = state.selectedMarketplaces.firstOrNull() ?: "ONDC Mystore",
                    quantity = 1,
                    unitPrice = state.selectedSellingPrice,
                    totalAmount = state.selectedSellingPrice,
                    isOrderDone = false,
                    orderTimeLabel = "Just Listed • New Order"
                )
            )
            _uiState.update {
                it.copy(
                    isPublishingOndc = false,
                    isPublishedSuccess = true,
                    publishedListingId = "ONDC-CAT-${(10000..99999).random()}",
                    statusMessage = "Published to ONDC! Tap 'Next Step' to view Orders & Inventory."
                )
            }
        }
    }

    // --- Step 8: Dashboard (Orders & Inventory) ---
    fun setOrderFilter(filter: String) {
        _uiState.update { it.copy(orderFilter = filter) }
    }

    fun toggleOrderDoneStatus(order: ArtisanOrderEntity) {
        viewModelScope.launch {
            repository.toggleOrderDone(order)
            val newStateText = if (!order.isOrderDone) "Order ${order.orderCode} marked as Order Done!" else "Order ${order.orderCode} reopened."
            _uiState.update { it.copy(statusMessage = newStateText) }
        }
    }

    fun updateInventoryStock(product: CatalogProductEntity, delta: Int) {
        viewModelScope.launch {
            repository.updateProductStock(product, product.stockCount + delta)
        }
    }

    fun startNewProductCatalog() {
        _uiState.update {
            it.copy(
                currentStep = 2,
                isPublishedSuccess = false,
                statusMessage = "Ready to capture your next craft product!"
            )
        }
    }

    class Factory(private val repository: KalaSetuRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return KalaSetuViewModel(repository) as T
        }
    }
}
