package com.example.util

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.PowerManager
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.data.TrackDictionary
import com.example.service.AssemblyAccessibilityService
import com.example.service.AssemblyService
import com.example.service.NDCycleService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object PermissionHelper {

    /**
     * Returns the array of standard runtime storage permissions to request via system permission dialog.
     * On Android 13+ (API 33+), requests granular media permissions.
     * On Android 12 and below, requests standard READ/WRITE_EXTERNAL_STORAGE.
     */
    fun getStoragePermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO
            )
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    var storagePermissionOverrideForTesting: Boolean? = null
    var emulatorProcessRunningOverrideForTesting: Boolean? = null

    const val PPSSPP_PACKAGE_NAME = "org.ppsspp.ppsspp"

    fun isPpssppProcessRunning(context: Context): Boolean {
        emulatorProcessRunningOverrideForTesting?.let { return it }

        // 1. Check real-time tracking from AssemblyAccessibilityService
        if (NDCycleService.isEmulatorActive) return true
        if (NDCycleService.engineState.value.isEmulatorActive) return true

        // 2. Check running processes via ActivityManager
        try {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            val runningProcesses = am?.runningAppProcesses
            if (runningProcesses != null) {
                for (proc in runningProcesses) {
                    val pName = proc.processName ?: ""
                    if (pName == PPSSPP_PACKAGE_NAME ||
                        pName.startsWith("$PPSSPP_PACKAGE_NAME:") ||
                        proc.pkgList?.any { it == PPSSPP_PACKAGE_NAME } == true) {
                        return true
                    }
                }
            }
        } catch (e: Throwable) {
            // Ignore
        }

        // 3. Fallback: check recent tasks
        try {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            @Suppress("DEPRECATION")
            val runningTasks = am?.getRunningTasks(10)
            if (runningTasks != null) {
                for (task in runningTasks) {
                    val topPkg = task.topActivity?.packageName
                    val basePkg = task.baseActivity?.packageName
                    if (topPkg == PPSSPP_PACKAGE_NAME || basePkg == PPSSPP_PACKAGE_NAME) {
                        return true
                    }
                }
            }
        } catch (e: Throwable) {
            // Ignore
        }

        return false
    }

    fun hasStoragePermission(context: Context): Boolean {
        storagePermissionOverrideForTesting?.let { return it }
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val hasImages = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                val hasVideo = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED
                val hasAudio = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
                val hasManage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Environment.isExternalStorageManager() else false
                hasImages || hasVideo || hasAudio || hasManage
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val hasRead = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                val hasManage = Environment.isExternalStorageManager()
                hasRead || hasManage
            } else {
                val read = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                val write = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                read || write
            }
        } catch (e: Throwable) {
            false
        }
    }

    fun requestManageStorageIntent(context: Context): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    data = Uri.parse("package:${context.packageName}")
                }
            } catch (e: Exception) {
                Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            }
        } else {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:${context.packageName}")
            }
        }
    }

    fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                pm.isIgnoringBatteryOptimizations(context.packageName)
            } else {
                true
            }
        } catch (e: Throwable) {
            true
        }
    }

    fun requestBatteryOptimizationIntent(context: Context): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                data = Uri.parse("package:${context.packageName}")
            }
        } else {
            Intent(Settings.ACTION_SETTINGS)
        }
    }

    fun hasNotificationPermission(context: Context): Boolean {
        return try {
            val compatEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) || compatEnabled
            } else {
                compatEnabled
            }
        } catch (e: Throwable) {
            true
        }
    }

    fun isAccessibilityEnabled(context: Context): Boolean {
        return try {
            if (AssemblyAccessibilityService.isServiceEnabled) return true
            val serviceName = "${context.packageName}/${AssemblyAccessibilityService::class.java.canonicalName}"
            val enabledServices = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            ) ?: ""
            enabledServices.contains(serviceName)
        } catch (e: Throwable) {
            false
        }
    }

    fun requestAccessibilityIntent(): Intent {
        return Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    }

    fun clearCache(context: Context): Long {
        var totalFreed = 0L
        try {
            totalFreed += deleteDirectoryContents(context.cacheDir)
            context.externalCacheDir?.let {
                totalFreed += deleteDirectoryContents(it)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                totalFreed += deleteDirectoryContents(context.codeCacheDir)
            }
        } catch (e: Exception) {
            AppLogger.e("Ошибка при очистке кеша: ${e.message}")
        }
        return totalFreed
    }

    private fun deleteDirectoryContents(dir: File?): Long {
        var freed = 0L
        if (dir == null || !dir.exists()) return 0L
        val files = dir.listFiles() ?: return 0L
        for (file in files) {
            if (file.isDirectory) {
                freed += deleteDirectoryContents(file)
                try {
                    file.delete()
                } catch (_: Exception) {}
            } else {
                val length = file.length()
                if (file.delete()) {
                    freed += length
                }
            }
        }
        return freed
    }

    fun formatBytes(bytes: Long): String {
        return when {
            bytes <= 0 -> "0 КБ"
            bytes < 1024 -> "$bytes Б"
            bytes < 1024 * 1024 -> String.format(java.util.Locale.US, "%.1f КБ", bytes / 1024.0)
            else -> String.format(java.util.Locale.US, "%.2f МБ", bytes / (1024.0 * 1024.0))
        }
    }

    fun createUninstallIntent(context: Context): Intent {
        val packageUri = Uri.parse("package:${context.packageName}")
        return Intent(Intent.ACTION_DELETE, packageUri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    suspend fun createSampleTextureStructure(context: Context? = null): String = withContext(Dispatchers.IO) {
        try {
            AppLogger.i("Создание базовой файловой структуры для PPSSPP / NDCYCLE...")

            if (context != null && !hasStoragePermission(context)) {
                AppLogger.w("Разрешение на доступ к памяти не предоставлено. Предоставьте доступ в настройках.")
            }

            // Ensure base directories
            val baseDir = File(TrackDictionary.BASE_PLUGINS_DIR)
            val gameDir = File(TrackDictionary.GAME_TEXTURES_DIR)
            val soundDir = File(TrackDictionary.BASE_PLUGINS_DIR, "Sound")

            baseDir.mkdirs()
            gameDir.mkdirs()
            soundDir.mkdirs()

            // Safe placeholder sound files creation
            val soundReady = File(TrackDictionary.SOUND_READY_PATH)
            safeCreateFile(soundReady)

            val soundDone = File(TrackDictionary.SOUND_DONE_PATH)
            safeCreateFile(soundDone)

            var createdCount = 0
            // Create packs for all tracks
            for ((key, track) in TrackDictionary.TRACKS) {
                // Normal packs
                for (pack in TrackDictionary.NORMAL_PACKS) {
                    val packDir = File(track.normalPath, pack)
                    val created = packDir.mkdirs()
                    if (created || packDir.exists()) {
                        // Create folder "time" instead of skybox.png
                        val timeDir = File(packDir, "time")
                        if (!timeDir.exists()) {
                            timeDir.mkdirs()
                        }

                        // Create textures.ini instead of texture_meta.ini
                        val texturesIni = File(packDir, "textures.ini")
                        if (!texturesIni.exists()) {
                            safeWriteText(texturesIni, TrackDictionary.TEXTURES_INI_CONTENT)
                        }

                        // Clean up legacy placeholder files if present
                        val oldSkybox = File(packDir, "skybox.png")
                        if (oldSkybox.exists()) oldSkybox.delete()
                        val oldMeta = File(packDir, "texture_meta.ini")
                        if (oldMeta.exists()) oldMeta.delete()

                        createdCount++
                    }
                }
                // Rain packs
                for (rainPack in TrackDictionary.RAIN_PACKS) {
                    val rainDir = File(track.rainPath, rainPack)
                    val created = rainDir.mkdirs()
                    if (created || rainDir.exists()) {
                        // Create folder "time" instead of skybox_rain.png
                        val timeDir = File(rainDir, "time")
                        if (!timeDir.exists()) {
                            timeDir.mkdirs()
                        }

                        // Create textures.ini instead of texture_meta.ini
                        val texturesIni = File(rainDir, "textures.ini")
                        if (!texturesIni.exists()) {
                            safeWriteText(texturesIni, TrackDictionary.TEXTURES_INI_CONTENT)
                        }

                        // Clean up legacy placeholder files if present
                        val oldSkyboxRain = File(rainDir, "skybox_rain.png")
                        if (oldSkyboxRain.exists()) oldSkyboxRain.delete()
                        val oldMeta = File(rainDir, "texture_meta.ini")
                        if (oldMeta.exists()) oldMeta.delete()

                        createdCount++
                    }
                }
            }

            if (createdCount > 0 || baseDir.exists()) {
                AppLogger.s("Файловая структура инициализирована ($createdCount пакетов создано: папка time и textures.ini)")
                "Готово! Создано $createdCount пакетов текстур (textures.ini и папка time) в ${TrackDictionary.BASE_PLUGINS_DIR}"
            } else {
                AppLogger.w("Не удалось создать каталоги на /sdcard/. Проверьте разрешение «Доступ ко всем файлам».")
                "Внимание: Не удалось создать папки. Предоставьте разрешение на память в Настройках."
            }
        } catch (e: Exception) {
            AppLogger.e("Ошибка создания файловой структуры: ${e.message}")
            "Ошибка: ${e.message ?: "Проверьте права доступа к памяти"}"
        }
    }

    private fun safeCreateFile(file: File): Boolean {
        return try {
            val parent = file.parentFile
            if (parent != null && !parent.exists()) {
                parent.mkdirs()
            }
            if (!file.exists()) {
                file.createNewFile()
            } else {
                true
            }
        } catch (e: Exception) {
            AppLogger.w("Пропуск создания файла ${file.name}: ${e.message}")
            false
        }
    }

    private fun safeWriteText(file: File, text: String): Boolean {
        return try {
            val parent = file.parentFile
            if (parent != null && !parent.exists()) {
                parent.mkdirs()
            }
            file.writeText(text)
            true
        } catch (e: Exception) {
            AppLogger.w("Пропуск записи текста в ${file.name}: ${e.message}")
            false
        }
    }

    private fun safeWriteBytes(file: File, bytes: ByteArray): Boolean {
        return try {
            val parent = file.parentFile
            if (parent != null && !parent.exists()) {
                parent.mkdirs()
            }
            file.writeBytes(bytes)
            true
        } catch (e: Exception) {
            AppLogger.w("Пропуск записи байт в ${file.name}: ${e.message}")
            false
        }
    }
}
