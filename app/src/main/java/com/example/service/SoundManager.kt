package com.example.service

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.ToneGenerator
import android.net.Uri
import com.example.util.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class SoundManager(private val context: Context) {

    suspend fun playSound(soundPath: String, fallbackToneType: Int = ToneGenerator.TONE_PROP_BEEP) {
        withContext(Dispatchers.IO) {
            try {
                val file = File(soundPath)
                if (file.exists() && file.canRead()) {
                    AppLogger.i("Воспроизведение звука: $soundPath")
                    val player = MediaPlayer().apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .setUsage(AudioAttributes.USAGE_GAME)
                                .build()
                        )
                        setDataSource(context, Uri.fromFile(file))
                        prepare()
                        setOnCompletionListener { mp ->
                            mp.release()
                        }
                        start()
                    }
                } else {
                    AppLogger.i("Аудиофайл не найден ($soundPath), воспроизведение сигнального тона")
                    playSyntheticTone(fallbackToneType)
                }
            } catch (e: Exception) {
                AppLogger.w("Ошибка воспроизведения звука: ${e.message}")
                playSyntheticTone(fallbackToneType)
            }
        }
    }

    private fun playSyntheticTone(toneType: Int) {
        try {
            val toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 80)
            toneGen.startTone(toneType, 200)
            // ToneGenerator releases automatically after tone ends or upon GC
        } catch (e: Exception) {
            AppLogger.w("ToneGenerator ошибка: ${e.message}")
        }
    }
}
