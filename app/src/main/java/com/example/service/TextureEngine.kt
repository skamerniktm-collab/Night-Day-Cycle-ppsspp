package com.example.service

import android.content.Context
import android.media.ToneGenerator
import com.example.data.TrackDictionary
import com.example.model.ClickMethod
import com.example.util.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class TextureEngine(private val context: Context) {
    private val mutex = Mutex()
    private val soundManager = SoundManager(context)

    suspend fun applyPack(
        trackKey: String,
        packName: String,
        isRain: Boolean = false,
        forceSound: Boolean = false,
        startX: Int = 1169,
        startY: Int = 25,
        endX: Int = startX + 25,
        endY: Int = startY,
        tapX: Int = startX,
        tapY: Int = startY,
        swipeCount: Int = 2,
        swipeDurationMs: Long = 50L,
        swipePauseMs: Long = 300L,
        clickMethod: ClickMethod = ClickMethod.AUTO,
        onStatusUpdate: ((String) -> Unit)? = null
    ): Boolean {
        return mutex.withLock {
            withContext(Dispatchers.IO) {
                try {
                    val track = TrackDictionary.getTrack(trackKey)
                    val isRainPack = isRain || TrackDictionary.RAIN_PACKS.contains(packName)
                    val baseDir = if (isRainPack) track.rainPath else track.normalPath
                    val sourceDir = File(baseDir, packName)
                    val targetDir = File(TrackDictionary.GAME_TEXTURES_DIR)

                    val statusDesc = "Трасса: ${track.name} | Пак: $packName"
                    AppLogger.i("▶ Начало подмены текстур: $statusDesc (Источник: ${sourceDir.absolutePath})")
                    onStatusUpdate?.invoke("Копирование пакета: $packName")

                    // 1. Звуковое сопровождение перед копированием (7с задержка)
                    if (forceSound) {
                        AppLogger.i("Звуковое оповещение перед копированием (Ww.mp3) + пауза 7 сек...")
                        onStatusUpdate?.invoke("Подготовка к замене (7 сек)...")
                        soundManager.playSound(TrackDictionary.SOUND_READY_PATH, ToneGenerator.TONE_PROP_BEEP2)
                        delay(7000)
                    }

                    // 2. Гарантированная очистка целевого каталога
                    AppLogger.i("Очистка целевого каталога ${targetDir.absolutePath}...")
                    if (targetDir.exists()) {
                        deleteRecursively(targetDir)
                    }
                    if (!targetDir.exists()) {
                        val created = targetDir.mkdirs()
                        if (!created && !targetDir.exists()) {
                            AppLogger.w("Не удалось создать целевую папку ${targetDir.absolutePath}. Проверьте разрешение на Доступ ко всем файлам.")
                        }
                    }
                    targetDir.setReadable(true, false)
                    targetDir.setWritable(true, false)

                    // 3. Рекурсивное копирование из источника
                    var filesCopied = 0
                    var bytesCopied = 0L

                    if (sourceDir.exists() && sourceDir.isDirectory) {
                        val result = copyDirectoryContents(sourceDir, targetDir)
                        filesCopied = result.first
                        bytesCopied = result.second
                        AppLogger.s("Скопировано $filesCopied файлов (${bytesCopied / 1024} КБ) из $packName в UCUS98632")
                    } else {
                        AppLogger.w("Папка источника не найдена: ${sourceDir.absolutePath}. Проверьте наличие текстур на накопителе.")
                        // Создаем маркер для тестирования
                        try {
                            val dummyInfo = File(targetDir, "current_pack_info.txt")
                            dummyInfo.writeText("NDCYCLE ACTIVE PACK: $packName\nTRACK: ${track.name}\nTIMESTAMP: ${System.currentTimeMillis()}")
                            filesCopied = 1
                        } catch (e: Exception) {
                            AppLogger.w("Запись маркера не удалась: ${e.message}")
                        }
                    }

                    // 4. Озвучка финиша
                    if (forceSound) {
                        AppLogger.i("Звуковое оповещение завершения (Dd.mp3)")
                        soundManager.playSound(TrackDictionary.SOUND_DONE_PATH, ToneGenerator.TONE_PROP_ACK)
                    }

                    // 5. Сигнализация кликеру (микро-свайпы AssemblyService)
                    AppLogger.i("Сигнализация свайпами AssemblyService ($startX, $startY)->($endX, $endY) x$swipeCount, ${swipeDurationMs}мс, пауза ${swipePauseMs}мс...")
                    AssemblyService.triggerSwipeSequence(
                        startX = startX,
                        startY = startY,
                        endX = endX,
                        endY = endY,
                        swipeCount = swipeCount,
                        durationMs = swipeDurationMs,
                        pauseMs = swipePauseMs,
                        preferredMethod = clickMethod
                    )

                    onStatusUpdate?.invoke("Пак $packName применен")
                    true
                } catch (e: Exception) {
                    AppLogger.e("Критическая ошибка применения пакета", e)
                    onStatusUpdate?.invoke("Ошибка: ${e.message}")
                    false
                }
            }
        }
    }

    private fun deleteRecursively(file: File) {
        if (file.isDirectory) {
            val children = file.listFiles()
            if (children != null) {
                for (child in children) {
                    deleteRecursively(child)
                }
            }
        }
        file.delete()
    }

    private fun copyDirectoryContents(source: File, target: File): Pair<Int, Long> {
        var count = 0
        var totalBytes = 0L
        val items = source.listFiles() ?: return Pair(0, 0L)

        for (item in items) {
            val dest = File(target, item.name)
            if (item.isDirectory) {
                dest.mkdirs()
                dest.setReadable(true, false)
                dest.setWritable(true, false)
                val subResult = copyDirectoryContents(item, dest)
                count += subResult.first
                totalBytes += subResult.second
            } else {
                val bytes = copyFileBuffered(item, dest)
                count++
                totalBytes += bytes
            }
        }
        return Pair(count, totalBytes)
    }

    private fun copyFileBuffered(src: File, dst: File): Long {
        var bytesWritten = 0L
        val parent = dst.parentFile
        if (parent != null && !parent.exists()) {
            parent.mkdirs()
        }
        val buffer = ByteArray(64 * 1024) // 64 KB buffer for maximum transfer throughput
        FileInputStream(src).use { input ->
            FileOutputStream(dst).use { output ->
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                    bytesWritten += read
                }
                output.flush()
            }
        }
        dst.setReadable(true, false)
        dst.setWritable(true, false)
        return bytesWritten
    }
}
