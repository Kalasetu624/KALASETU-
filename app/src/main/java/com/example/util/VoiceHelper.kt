package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class VoiceHelper(context: Context) {
    private var tts: TextToSpeech? = null
    private var isReady = false

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val hiResult = tts?.setLanguage(Locale("hi", "IN"))
                if (hiResult == TextToSpeech.LANG_MISSING_DATA ||
                    hiResult == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    tts?.setLanguage(Locale.US)
                }
                tts?.setSpeechRate(0.92f)
                isReady = true
            }
        }
    }

    fun speakGuidance(textEn: String, textHi: String, preferHindi: Boolean = false) {
        if (!isReady) return
        val textToSpeak = if (preferHindi) "$textHi. $textEn" else textEn
        tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "kalasetu_voice_guide")
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
