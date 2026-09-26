package com.example.data.remote

import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    val contents: List<GeminiContent>
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    val text: String
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    val candidates: List<GeminiCandidate>?
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    val content: GeminiContent?
)

interface GeminiRetrofitApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

data class AiCatalogResult(
    val titleEn: String,
    val titleHi: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val extractedMaterialCost: Int,
    val extractedHours: Float,
    val seoKeywords: List<String>,
    val isLiveGeminiResponse: Boolean
)

object GeminiCatalogService {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private val api: GeminiRetrofitApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiRetrofitApi::class.java)
    }

    suspend fun processVoiceToCatalog(
        voiceTranscript: String,
        language: String,
        craftCategory: String
    ): AiCatalogResult = withContext(Dispatchers.IO) {
        val localExtractedCost = extractNumberNearKeyword(
            voiceTranscript,
            defaultCost = 45
        ).coerceIn(20, 200)
        val localExtractedHours = extractHoursFromText(voiceTranscript, defaultHours = 1.5f)

        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = """
                    You are an AI Cataloging Assistant for rural Indian artisans selling on ONDC.
                    Artisan Voice Note ($language): "$voiceTranscript"
                    Craft Category: "$craftCategory"
                    
                    Respond in EXACTLY 6 lines separated by "|" (pipe character) with no extra markdown:
                    Line 1: SEO English Product Title (under 65 chars)
                    Line 2: SEO Hindi Product Title (हिंदी में शीर्षक)
                    Line 3: Compelling 2-sentence English e-commerce description highlighting handmade authenticity
                    Line 4: Compelling 2-sentence Hindi description (हिंदी विवरण)
                    Line 5: Extracted Raw Material Cost in INR as integer between 25 and 180 (default $localExtractedCost)
                    Line 6: Comma-separated 4 SEO tags
                """.trimIndent()

                val response = api.generateContent(
                    apiKey = apiKey,
                    request = GeminiRequest(
                        contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt))))
                    )
                )
                val rawText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!rawText.isNullOrBlank()) {
                    val lines = rawText.lines().map { it.trim() }.filter { it.isNotEmpty() }
                    if (lines.size >= 4) {
                        val cost = lines.getOrNull(4)?.filter { it.isDigit() }?.toIntOrNull()
                            ?.coerceIn(20, 250) ?: localExtractedCost
                        val tags = lines.getOrNull(5)?.split(",")?.map { it.trim() }
                            ?: listOf("Handmade India", "ONDC Karigar", craftCategory, "Eco-Friendly")
                        return@withContext AiCatalogResult(
                            titleEn = lines[0].removePrefix("Line 1:").trim(),
                            titleHi = lines[1].removePrefix("Line 2:").trim(),
                            descriptionEn = lines[2].removePrefix("Line 3:").trim(),
                            descriptionHi = lines[3].removePrefix("Line 4:").trim(),
                            extractedMaterialCost = cost,
                            extractedHours = localExtractedHours,
                            seoKeywords = tags,
                            isLiveGeminiResponse = true
                        )
                    }
                }
            } catch (_: Exception) {
                // Fall back gracefully to deterministic local NLP cataloger
            }
        }

        buildDeterministicCatalog(
            voiceTranscript = voiceTranscript,
            craftCategory = craftCategory,
            cost = localExtractedCost,
            hours = localExtractedHours
        )
    }

    private fun extractNumberNearKeyword(text: String, defaultCost: Int): Int {
        val regex = Regex("""(\d{2,3})""")
        val matches = regex.findAll(text).mapNotNull { it.value.toIntOrNull() }.toList()
        return matches.firstOrNull { it in 20..300 } ?: defaultCost
    }

    private fun extractHoursFromText(text: String, defaultHours: Float): Float {
        val lower = text.lowercase()
        return when {
            lower.contains("डेढ़") || lower.contains("1.5") -> 1.5f
            lower.contains("ढाई") || lower.contains("2.5") -> 2.5f
            lower.contains("तीन") || lower.contains("3 ") || lower.contains("three") -> 3.0f
            lower.contains("दो") || lower.contains("2 ") || lower.contains("two") -> 2.0f
            lower.contains("एक") || lower.contains("1 ") || lower.contains("one") -> 1.0f
            else -> defaultHours
        }
    }

    private fun buildDeterministicCatalog(
        voiceTranscript: String,
        craftCategory: String,
        cost: Int,
        hours: Float
    ): AiCatalogResult {
        return when {
            craftCategory.contains("Pottery", ignoreCase = true) ||
                voiceTranscript.contains("पॉटरी") ||
                voiceTranscript.contains("फूलदान") -> AiCatalogResult(
                titleEn = "Handcrafted Jaipur Blue Pottery Floral Mini Vase",
                titleHi = "जयपुर ब्लू पॉटरी हस्तनिर्मित फूलदान (मिनी वास)",
                descriptionEn = "Authentic quartz-stone handcrafted Jaipur Blue Pottery vase with cobalt blue floral motifs. Hand-glazed by master artisans in ${hours}h; perfect for tabletop decor & gifting.",
                descriptionHi = "पारंपरिक कारीगरों द्वारा ${hours} घंटे की मेहनत से बनाया गया असली जयपुर ब्लू पॉटरी फूलदान। घर की सजावट और उपहार के लिए उत्तम।",
                extractedMaterialCost = cost,
                extractedHours = hours,
                seoKeywords = listOf("Jaipur Blue Pottery", "Handmade Vase", "ONDC Craft", "GI Tagged Art"),
                isLiveGeminiResponse = false
            )

            craftCategory.contains("Bamboo", ignoreCase = true) ||
                voiceTranscript.contains("बांस") ||
                voiceTranscript.contains("कोस्टर") -> AiCatalogResult(
                titleEn = "Eco-Friendly Handwoven Assam Bamboo Tea Coaster Set (6 Pcs)",
                titleHi = "असम बांस से बुने इको-फ्रेंडली टी-कोस्टर सेट (6 पीस)",
                descriptionEn = "100% organic treated bamboo cane tea coasters hand-woven in ${hours}h with natural matte finish. Heat-resistant, washable, and zero-plastic.",
                descriptionHi = "ग्रामीण कारीगरों द्वारा प्राकृतिक बांस से ${hours} घंटे में बुना गया मजबूत और सुंदर टी-कोस्टर सेट।",
                extractedMaterialCost = cost,
                extractedHours = hours,
                seoKeywords = listOf("Assam Bamboo Craft", "Eco Coasters", "Sustainable Dining", "Rural Artisan"),
                isLiveGeminiResponse = false
            )

            else -> AiCatalogResult(
                titleEn = "Hand-Painted Terracotta Clay Diya & Kulhad Set (4 Pcs)",
                titleHi = "हाथ से रंगे प्राकृतिक मिट्टी के दीये और कुल्हड़ सेट (4 पीस)",
                descriptionEn = "Natural river-clay terracotta diyas and kulhad cups hand-painted with pastel folk motifs in ${hours}h. Eco-friendly, lead-free, and directly supporting rural potter families.",
                descriptionHi = "शुद्ध प्राकृतिक मिट्टी से ${hours} घंटे में हाथ से बनाकर रंगे गए सुंदर दीये और कुल्हड़। त्योहार और दैनिक उपयोग के लिए श्रेष्ठ।",
                extractedMaterialCost = cost,
                extractedHours = hours,
                seoKeywords = listOf("Terracotta Diya", "Handmade Kulhad", "VocalForLocal", "ONDC Pottery"),
                isLiveGeminiResponse = false
            )
        }
    }
}
