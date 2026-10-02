package com.example.util

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.view.Display
import android.view.accessibility.AccessibilityNodeInfo
import com.example.service.AssemblyAccessibilityService
import com.example.service.NDCycleService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Отслеживает состояние экрана/интерфейса в эмуляторе PPSSPP каждые 2 секунды.
 * Защищает от idle-накрутки: если на экране статично отображается значок Gran Turismo
 * или главное меню/браузер игр без реального игрового процесса и движения,
 * прогресс-бар темы и игровая валюта не накручиваются.
 */
object GtScreenDetector {

    const val PPSSPP_PACKAGE = "org.ppsspp.ppsspp"
    const val GT_GAME_ID_USA = "ucus98632"
    const val GT_GAME_ID_EUR = "uces01245"
    const val GT_GAME_ID_JPN = "ucjs10100"
    const val GT_GAME_ID_ASIA = "ucas40277"
    const val GT_GAME_ID_KOR = "ucks45124"

    @Volatile
    var isIdleScreenOverrideForTesting: Boolean? = null
    @Volatile
    var isGamePausedOverrideForTesting: Boolean? = null
    @Volatile
    var isMainMenuOverrideForTesting: Boolean? = null
    @Volatile
    var isRealGameplayOverrideForTesting: Boolean? = null

    // Tracking static screen state & visual motion
    @Volatile
    private var lastScreenSignature: Long = 0L
    @Volatile
    private var staticCount: Int = 0
    @Volatile
    private var motionCount: Int = 0
    @Volatile
    private var lastActivityTimestamp: Long = System.currentTimeMillis()
    @Volatile
    private var lastMotionDetectedTime: Long = 0L
    @Volatile
    private var lastGtIconBitmapDetectedTime: Long = 0L

    fun notifyUserActivity() {
        lastActivityTimestamp = System.currentTimeMillis()
        staticCount = 0
    }

    data class ScreenStateResult(
        val isEmulatorActive: Boolean,
        val isGtIconScreen: Boolean,
        val isIdleNoGameplay: Boolean,
        val isGamePaused: Boolean = false,
        val isMainMenu: Boolean = false,
        val isRealGameplayConfirmed: Boolean = false,
        val statusMessage: String
    )

    /**
     * Выполняет анализ текущего экрана и интерфейса.
     * Запускается сервисом каждые 2 секунды.
     */
    suspend fun evaluateScreen(context: Context): ScreenStateResult = withContext(Dispatchers.Default) {
        isRealGameplayOverrideForTesting?.let { isRealGameplay ->
            return@withContext ScreenStateResult(
                isEmulatorActive = true,
                isGtIconScreen = !isRealGameplay,
                isIdleNoGameplay = !isRealGameplay,
                isGamePaused = false,
                isMainMenu = !isRealGameplay,
                isRealGameplayConfirmed = isRealGameplay,
                statusMessage = if (isRealGameplay) {
                    "Активный заезд Gran Turismo (реальный игровой процесс подтверждён)"
                } else {
                    "Пользователь находится в меню PPSSPP (заезд не начат, накрутка заблокирована)"
                }
            )
        }

        isGamePausedOverrideForTesting?.let { isPaused ->
            if (isPaused) {
                return@withContext ScreenStateResult(
                    isEmulatorActive = true,
                    isGtIconScreen = false,
                    isIdleNoGameplay = true,
                    isGamePaused = true,
                    isMainMenu = false,
                    isRealGameplayConfirmed = false,
                    statusMessage = "Игра в эмуляторе на паузе / меню настроек (прогресс остановлен)"
                )
            }
        }

        isMainMenuOverrideForTesting?.let { isMenu ->
            if (isMenu) {
                return@withContext ScreenStateResult(
                    isEmulatorActive = true,
                    isGtIconScreen = false,
                    isIdleNoGameplay = true,
                    isGamePaused = false,
                    isMainMenu = true,
                    isRealGameplayConfirmed = false,
                    statusMessage = "Пользователь находится в главном меню PPSSPP (заезд не начат, накрутка заблокирована)"
                )
            }
        }

        isIdleScreenOverrideForTesting?.let { isIdle ->
            return@withContext ScreenStateResult(
                isEmulatorActive = true,
                isGtIconScreen = isIdle,
                isIdleNoGameplay = isIdle,
                isGamePaused = false,
                isMainMenu = isIdle,
                isRealGameplayConfirmed = !isIdle,
                statusMessage = if (isIdle) {
                    "Обнаружен статичный экран со значком Gran Turismo (idle-защита активна)"
                } else {
                    "Активный заезд Gran Turismo (динамичный геймплей)"
                }
            )
        }

        // 1. Проверяем запущен ли и активен ли эмулятор PPSSPP
        val isEmulatorActive = NDCycleService.isEmulatorActive || PermissionHelper.isPpssppProcessRunning(context)
        if (!isEmulatorActive) {
            return@withContext ScreenStateResult(
                isEmulatorActive = false,
                isGtIconScreen = false,
                isIdleNoGameplay = true,
                isGamePaused = false,
                isMainMenu = false,
                isRealGameplayConfirmed = false,
                statusMessage = "Эмулятор PPSSPP не запущен"
            )
        }

        // 2. Инспектируем дерево узлов доступности (AccessibilityNodeInfo)
        var detectedGtIconOrTile = false
        var detectedMainMenuOrBrowser = false
        var detectedPauseOrSettings = false
        val a11y = AssemblyAccessibilityService.instance

        if (a11y != null) {
            try {
                val root = a11y.rootInActiveWindow
                if (root != null) {
                    val nodes = mutableListOf<AccessibilityNodeInfo>()
                    collectNodes(root, nodes)

                    for (node in nodes) {
                        val text = node.text?.toString()?.lowercase() ?: ""
                        val desc = node.contentDescription?.toString()?.lowercase() ?: ""
                        val viewId = node.viewIdResourceName?.lowercase() ?: ""

                        // Поиск конкретного экрана со значком / тайлом Gran Turismo
                        if (text.contains("gran turismo") || desc.contains("gran turismo") ||
                            text.contains("granturismo") || desc.contains("granturismo") ||
                            text.contains("polyphony") || desc.contains("polyphony") ||
                            text.contains(GT_GAME_ID_USA) || text.contains(GT_GAME_ID_EUR) ||
                            text.contains(GT_GAME_ID_JPN) || text.contains(GT_GAME_ID_ASIA) ||
                            text.contains(GT_GAME_ID_KOR) ||
                            desc.contains(GT_GAME_ID_USA) || desc.contains(GT_GAME_ID_EUR) ||
                            desc.contains(GT_GAME_ID_JPN) || desc.contains(GT_GAME_ID_ASIA) ||
                            desc.contains(GT_GAME_ID_KOR) ||
                            text.contains("ucus-98632") || desc.contains("ucus-98632") ||
                            text.contains("uces-01245") || desc.contains("uces-01245") ||
                            text.contains("icon0.png") || desc.contains("icon0.png")
                        ) {
                            detectedGtIconOrTile = true
                        }

                        // Проверка экрана паузы игры / эмулятора и меню настроек
                        if (text.contains("pause") || desc.contains("pause") ||
                            text.contains("пауза") || desc.contains("пауза") ||
                            text.contains("paused") || desc.contains("paused") ||
                            text == "continue" || text == "продолжить" || text == "resume" ||
                            desc == "continue" || desc == "продолжить" || desc == "resume" ||
                            text.contains("save state") || text.contains("сохранить состояние") ||
                            text.contains("load state") || text.contains("загрузить состояние") ||
                            text.contains("restart") || text.contains("перезапуск") ||
                            text.contains("cheats") || text.contains("читы") ||
                            text.contains("driving options") || text.contains("параметры вождения") ||
                            text.contains("game settings") || text.contains("настройки игры") ||
                            viewId.contains("pause") || viewId.contains("settings")
                        ) {
                            detectedPauseOrSettings = true
                        }

                        // Проверка главного меню / браузера игр эмулятора PPSSPP
                        if (text.contains("recent") || text.contains("games") || text.contains("homebrew") ||
                            text.contains("недавние") || text.contains("игры") || text.contains("демо") ||
                            text.contains("browse") || text.contains("обзор") ||
                            text.contains("grid view") || text.contains("list view") ||
                            text.contains("сетка") || text.contains("список") ||
                            text.contains("главное меню") || text.contains("main menu") ||
                            text.contains("how to get games") || text.contains("как получить игры") ||
                            text.contains("load...") || text.contains("загрузить...") ||
                            text.contains("install") || text.contains("установить") ||
                            text.contains("ppsspp") || desc.contains("ppsspp") ||
                            viewId.contains("browse") || viewId.contains("menu") || viewId.contains("tab")
                        ) {
                            detectedMainMenuOrBrowser = true
                        }
                    }
                }
            } catch (e: Throwable) {
                // Ignore accessibility hierarchy parsing error
            }
        }

        // Проверяем результат распознавания значка GT из графического буфера экрана
        val currentTime = System.currentTimeMillis()
        val isBitmapGtIcon = (currentTime - lastGtIconBitmapDetectedTime) < 5000L

        // Элементы меню и интерфейса эмулятора
        val isMainMenu = detectedMainMenuOrBrowser
        val isGtIconScreen = detectedGtIconOrTile || isBitmapGtIcon
        val isGamePaused = detectedPauseOrSettings

        // 3. Проверка статичности и отсутствия реального игрового движения (Idle):
        val timeSinceActivity = currentTime - lastActivityTimestamp
        val isStatic = staticCount >= 2

        // Если открыто главное меню, меню настроек, экран паузы, значок или экран статичен:
        val isIdleNoGameplay = isMainMenu || isGtIconScreen || isGamePaused || isStatic || (timeSinceActivity > 4000L && staticCount >= 2)

        // Просто запуск процесса эмулятора НИЧЕГО НЕ ДАЁТ сам по себе!
        // Прогресс подтверждается ТОЛЬКО если:
        // 1. Эмулятор активен
        // 2. НЕ открыто главное меню PPSSPP
        // 3. НЕ открыт значок/список игр
        // 4. Игра НЕ на паузе и НЕ в меню настроек
        // 5. Зафиксирован реальный активный игровой процесс (динамика кадров/геймплея)
        val isRealGameplayConfirmed = isEmulatorActive &&
                !isMainMenu &&
                !isGtIconScreen &&
                !isGamePaused &&
                !isIdleNoGameplay

        val message = when {
            !isEmulatorActive -> "Эмулятор PPSSPP не запущен"
            isGamePaused -> "Игра в эмуляторе на паузе / меню настроек (прогресс темы остановлен)"
            isMainMenu -> "Открыто главное меню PPSSPP (заезд не начат, накрутка заблокирована)"
            isGtIconScreen -> "На экране меню со значком Gran Turismo (нет активного заезда, накрутка заблокирована)"
            isIdleNoGameplay -> "Экран эмулятора статичен без движения (защита от idle-накрутки активна)"
            isRealGameplayConfirmed -> "Активный заезд Gran Turismo (реальный игровой процесс подтверждён)"
            else -> "Ожидание начала заезда в Gran Turismo..."
        }

        ScreenStateResult(
            isEmulatorActive = true,
            isGtIconScreen = isGtIconScreen,
            isIdleNoGameplay = isIdleNoGameplay,
            isGamePaused = isGamePaused,
            isMainMenu = isMainMenu,
            isRealGameplayConfirmed = isRealGameplayConfirmed,
            statusMessage = message
        )
    }

    /**
     * Обновляет цифровую подпись (хеш) экрана для детекции реального движения vs статики.
     */
    fun updateScreenSignature(signature: Long) {
        if (signature != 0L && signature == lastScreenSignature) {
            staticCount++
            motionCount = 0
        } else if (signature != 0L) {
            staticCount = 0
            motionCount++
            lastMotionDetectedTime = System.currentTimeMillis()
            lastScreenSignature = signature
            lastActivityTimestamp = System.currentTimeMillis()
        } else {
            staticCount = 0
            lastScreenSignature = signature
            lastActivityTimestamp = System.currentTimeMillis()
        }
    }

    /**
     * Вычисляет быстрый хеш распределённой выборки пикселей кадра
     * для точного определения наличия движения в игре.
     */
    fun computeFrameHash(bitmap: Bitmap): Long {
        return try {
            val width = bitmap.width
            val height = bitmap.height
            if (width < 8 || height < 8) return 0L
            var hash = 17L
            val stepX = (width / 8).coerceAtLeast(1)
            val stepY = (height / 8).coerceAtLeast(1)
            for (y in 0 until height step stepY) {
                for (x in 0 until width step stepX) {
                    hash = hash * 31 + bitmap.getPixel(x, y)
                }
            }
            hash
        } catch (e: Throwable) {
            0L
        }
    }

    /**
     * Анализирует кадр из экрана эмулятора: распознаёт значок GT и проверяет динамику движения.
     */
    fun analyzeAndRecordFrame(bitmap: Bitmap) {
        val isGt = analyzeGtIconBitmap(bitmap)
        if (isGt) {
            lastGtIconBitmapDetectedTime = System.currentTimeMillis()
        }
        val hash = computeFrameHash(bitmap)
        updateScreenSignature(hash)
    }

    /**
     * Анализирует захваченный фрейм на характерный спектр значка Gran Turismo
     * (преобладание глубокого тёмного фона с серебристо-белыми пикселями логотипа GT).
     */
    fun analyzeGtIconBitmap(bitmap: Bitmap): Boolean {
        return try {
            val width = bitmap.width
            val height = bitmap.height
            if (width < 8 || height < 8) return false

            var blackPixels = 0
            var metallicSilverPixels = 0
            val stepX = (width / 16).coerceAtLeast(1)
            val stepY = (height / 16).coerceAtLeast(1)
            var totalSamples = 0

            for (y in 0 until height step stepY) {
                for (x in 0 until width step stepX) {
                    val pixel = bitmap.getPixel(x, y)
                    val r = Color.red(pixel)
                    val g = Color.green(pixel)
                    val b = Color.blue(pixel)

                    totalSamples++
                    if (r < 30 && g < 30 && b < 30) {
                        blackPixels++
                    } else if (r > 120 && g > 120 && b > 120 && Math.abs(r - g) < 25 && Math.abs(g - b) < 25) {
                        metallicSilverPixels++
                    }
                }
            }

            if (totalSamples == 0) return false
            val blackRatio = blackPixels.toFloat() / totalSamples.toFloat()
            val silverRatio = metallicSilverPixels.toFloat() / totalSamples.toFloat()

            // Значок GT: выраженный тёмный фон (более 40%) с фрагментами серебристо-белого логотипа
            blackRatio > 0.40f && silverRatio > 0.03f
        } catch (e: Throwable) {
            false
        }
    }

    private fun collectNodes(node: AccessibilityNodeInfo, list: MutableList<AccessibilityNodeInfo>) {
        list.add(node)
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            collectNodes(child, list)
        }
    }
}

