package com.example.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.os.PowerManager
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.TrackDictionary
import com.example.model.Achievement
import com.example.model.AchievementPopup
import com.example.model.ClickMethod
import com.example.model.ControlsPosition
import com.example.model.CustomWallpaper
import com.example.model.DrawingStroke
import com.example.model.EngineState
import com.example.model.EngineStatus
import com.example.model.LogEntry
import com.example.model.SplashCheckStep
import com.example.model.SplashStepStatus
import com.example.model.TrackInfo
import com.example.service.AssemblyService
import com.example.service.NDCycleService
import com.example.service.TextureEngine
import com.example.ui.theme.AppThemeMode
import com.example.util.AppLogger
import com.example.util.PermissionHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

data class PermissionStatus(
    val hasStorage: Boolean = false,
    val hasNotification: Boolean = false,
    val isBatteryOptimizedIgnored: Boolean = false,
    val isAccessibilityActive: Boolean = false,
    val isRootActive: Boolean = false
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val PREFS_NAME = "ndcycle_app_prefs"
        private const val KEY_WARNING_SHOW_COUNT = "key_warning_show_count"
        const val MAX_WARNING_COUNT = 3

        private const val KEY_CYCLE_INTERVAL = "key_cycle_interval"
        private const val KEY_RAIN_PROBABILITY = "key_rain_probability"
        private const val KEY_SWIPE_COUNT = "key_swipe_count"
        private const val KEY_SWIPE_DURATION = "key_swipe_duration"
        private const val KEY_SWIPE_PAUSE = "key_swipe_pause"
        private const val KEY_RANDOM_MODE = "key_random_mode"
        private const val KEY_FORCE_SOUND = "key_force_sound"
        private const val KEY_START_X = "key_start_x"
        private const val KEY_START_Y = "key_start_y"
        private const val KEY_END_X = "key_end_x"
        private const val KEY_END_Y = "key_end_y"
        private const val KEY_CLICK_METHOD = "key_click_method"
        private const val KEY_TRACK_KEY = "key_track_key"
        private const val KEY_APP_THEME = "key_app_theme"
        private const val KEY_UNLOCKED_THEMES = "key_unlocked_themes"
        private const val KEY_UNLOCKED_ACHIEVEMENTS = "key_unlocked_achievements"
        const val KEY_COMPLETED_PACKS = "key_completed_packs"
        const val KEY_VERSION_BONUS_CLAIMED = "key_version_bonus_claimed"
        const val KEY_CONTROLS_POSITION = "key_controls_position"
        const val KEY_CUSTOM_WALLPAPER = "key_custom_wallpaper"
        const val SPLASH_DURATION_MS = 5000L
    }

    private val sharedPrefs by lazy {
        getApplication<Application>().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private val _unlockedAchievements = MutableStateFlow<Set<String>>(
        try {
            application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getStringSet(KEY_UNLOCKED_ACHIEVEMENTS, emptySet()) ?: emptySet()
        } catch (e: Exception) {
            emptySet()
        }
    )
    val unlockedAchievements: StateFlow<Set<String>> = _unlockedAchievements.asStateFlow()

    private val _achievementPopup = MutableStateFlow<AchievementPopup?>(null)
    val achievementPopup: StateFlow<AchievementPopup?> = _achievementPopup.asStateFlow()

    private val achievementQueue = java.util.concurrent.ConcurrentLinkedQueue<Achievement>()
    private var popupQueueJob: Job? = null

    /**
     * Unlocks an achievement if not already earned, persists it, grants Nd reward,
     * and shows a top notification curtain for exactly 2 seconds.
     */
    fun unlockAchievement(achievement: Achievement) {
        if (_unlockedAchievements.value.contains(achievement.id)) {
            return
        }
        val updated = _unlockedAchievements.value + achievement.id
        _unlockedAchievements.value = updated
        sharedPrefs.edit().putStringSet(KEY_UNLOCKED_ACHIEVEMENTS, updated).apply()

        AppLogger.s("🏆 Достижение: «${achievement.title}»! ${achievement.description}")

        // Enqueue achievement toaster display (2 seconds per achievement)
        achievementQueue.add(achievement)
        processAchievementQueue()
    }

    private val _completedPacks = MutableStateFlow<Set<String>>(
        try {
            application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getStringSet(KEY_COMPLETED_PACKS, emptySet()) ?: emptySet()
        } catch (e: Exception) {
            emptySet()
        }
    )
    val completedPacks: StateFlow<Set<String>> = _completedPacks.asStateFlow()

    fun markPackCompleted(packName: String) {
        val hasStorage = PermissionHelper.hasStoragePermission(getApplication())
        if (!hasStorage) {
            AppLogger.w("⚠️ Прохождение пака '$packName' не засчитано: отсутствует разрешение на доступ к памяти")
            return
        }
        val isPpssppRunning = PermissionHelper.isPpssppProcessRunning(getApplication())
        if (!isPpssppRunning) {
            AppLogger.w("⚠️ Прохождение пака '$packName' заблокировано: процесс ${PermissionHelper.PPSSPP_PACKAGE_NAME} не запущен! Попытка обхода пресечена.")
            return
        }
        val state = engineState.value
        if (state.isGameplayBlocked || !state.isRealGameplayConfirmed) {
            val reason = when {
                state.isGamePausedDetected -> "игра в эмуляторе на паузе / меню настроек"
                state.isMainMenuDetected -> "пользователь находится в главном меню PPSSPP (заезд не начат)"
                state.isGtIconScreenDetected -> "на экране статично стоит значок GT"
                state.isIdleScreenDetected -> "экран статичен без движения"
                else -> "простая проверка процесса не даёт прогресса — требуется реальный игровой процесс Gran Turismo"
            }
            AppLogger.w("⚠️ Прохождение пака '$packName' заблокировано: $reason. Накрутка невозможна.")
            return
        }
        if (!TrackDictionary.INITIAL_NORMAL_PACKS.contains(packName)) return
        val current = _completedPacks.value
        if (!current.contains(packName)) {
            val updated = current + packName
            _completedPacks.value = updated
            sharedPrefs.edit().putStringSet(KEY_COMPLETED_PACKS, updated).apply()
            AppLogger.i("📦 Зафиксировано прохождение пака: $packName (${updated.size}/${TrackDictionary.INITIAL_NORMAL_PACKS.size})")
            checkPackProgressForMinecraftTheme()
        }
    }

    fun checkPackProgressForMinecraftTheme() {
        val hasStorage = PermissionHelper.hasStoragePermission(getApplication())
        if (!hasStorage) {
            AppLogger.d("Проверка разблокировки Dixel Mine пропущена: нет доступа к памяти")
            return
        }
        val total = TrackDictionary.INITIAL_NORMAL_PACKS.size
        val count = _completedPacks.value.count { TrackDictionary.INITIAL_NORMAL_PACKS.contains(it) }
        if (count >= total) {
            if (!_unlockedThemes.value.contains(AppThemeMode.MINECRAFT_BLOCKS.name)) {
                unlockTheme(AppThemeMode.MINECRAFT_BLOCKS)
                AppLogger.s("🎉 Все $total паков успешно пройдены! Тема «Dixel Mine» автоматически разблокирована и перемещена в «Темы»!")
                Toast.makeText(getApplication(), "🎉 Все $total паков пройдены! Тема «Dixel Mine» разблокирована!", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun notifyThemeLocked(
        completedCount: Int = _completedPacks.value.count { TrackDictionary.INITIAL_NORMAL_PACKS.contains(it) },
        totalRequired: Int = TrackDictionary.INITIAL_NORMAL_PACKS.size,
        hasStorage: Boolean = PermissionHelper.hasStoragePermission(getApplication()),
        isEmulatorRunning: Boolean = PermissionHelper.isPpssppProcessRunning(getApplication()),
        isIdleScreen: Boolean = engineState.value.isIdleScreenDetected || engineState.value.isGtIconScreenDetected,
        isGamePaused: Boolean = engineState.value.isGamePausedDetected,
        isMainMenu: Boolean = engineState.value.isMainMenuDetected,
        isRealGameplayConfirmed: Boolean = engineState.value.isRealGameplayConfirmed
    ) {
        val msg = if (!hasStorage) {
            "⚠️ Нет доступа к памяти! Прогресс паков заблокирован (0 из $totalRequired). Предоставьте разрешение на хранилище."
        } else if (!isEmulatorRunning && completedCount < totalRequired) {
            "🔒 Тема «Dixel Mine» заблокирована ($completedCount из $totalRequired). Запустите эмулятор PPSSPP (${PermissionHelper.PPSSPP_PACKAGE_NAME}) для фиксации прогресса!"
        } else if (isMainMenu && completedCount < totalRequired) {
            "⏸ Открыто главное меню PPSSPP. Запустите заезд в Gran Turismo для фиксации прогресса темы!"
        } else if (isGamePaused && completedCount < totalRequired) {
            "⏸ Игра на паузе или открыто меню настроек. Снимите игру с паузы для фиксации реального геймплея!"
        } else if (isIdleScreen && completedCount < totalRequired) {
            "⏸ Защита от idle: на экране статичный значок Gran Turismo. Начните заезд в игре для прогресса!"
        } else if (!isRealGameplayConfirmed && completedCount < totalRequired) {
            "⏸ Простой запуск эмулятора не засчитывает прогресс! Требуется реальный игровой процесс Gran Turismo."
        } else {
            "🔒 Тема «Dixel Mine» заблокирована. Пройдено: $completedCount из $totalRequired паков."
        }
        AppLogger.i(msg)
        Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show()
    }

    fun checkAchievementThemeUnlocks(showMessage: Boolean = false) {
        if (showMessage) {
            notifyThemeLocked()
        }
    }

    private fun processAchievementQueue() {
        if (popupQueueJob?.isActive == true) return
        popupQueueJob = viewModelScope.launch {
            while (true) {
                val next = achievementQueue.poll() ?: break
                _achievementPopup.value = AchievementPopup(next)
                delay(2000L) // Display for exactly 2 seconds
                _achievementPopup.value = null
                delay(300L) // Wait for slideOut transition before next
            }
        }
    }

    private val _isVersionBonusClaimed = MutableStateFlow(
        try {
            application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_VERSION_BONUS_CLAIMED, false)
        } catch (e: Exception) {
            false
        }
    )
    val isVersionBonusClaimed: StateFlow<Boolean> = _isVersionBonusClaimed.asStateFlow()

    private val _unlockedThemes = MutableStateFlow<Set<String>>(
        try {
            val saved = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getStringSet(KEY_UNLOCKED_THEMES, emptySet()) ?: emptySet()
            saved
        } catch (e: Exception) {
            emptySet()
        }
    )
    val unlockedThemes: StateFlow<Set<String>> = _unlockedThemes.asStateFlow()

    private val _appTheme = MutableStateFlow(
        try {
            val saved = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(KEY_APP_THEME, AppThemeMode.CYBER_LILAC.name) ?: AppThemeMode.CYBER_LILAC.name
            val mode = AppThemeMode.valueOf(saved)
            val unlocked = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getStringSet(KEY_UNLOCKED_THEMES, emptySet()) ?: emptySet()
            if (mode.palette.isLocked && !unlocked.contains(mode.name)) AppThemeMode.CYBER_LILAC else mode
        } catch (e: Exception) {
            AppThemeMode.CYBER_LILAC
        }
    )
    val appTheme: StateFlow<AppThemeMode> = _appTheme.asStateFlow()

    fun isThemeUnlocked(theme: AppThemeMode): Boolean {
        return !theme.palette.isLocked || _unlockedThemes.value.contains(theme.name)
    }

    fun unlockTheme(theme: AppThemeMode, activateImmediately: Boolean = false) {
        val updated = _unlockedThemes.value + theme.name
        _unlockedThemes.value = updated
        sharedPrefs.edit().putStringSet(KEY_UNLOCKED_THEMES, updated).apply()
        if (activateImmediately) {
            _appTheme.value = theme
            sharedPrefs.edit().putString(KEY_APP_THEME, theme.name).apply()
        }
        AppLogger.s("🎁 Тема '${theme.palette.displayName}' успешно разблокирована!")
    }

    fun purchaseTheme(theme: AppThemeMode, cost: Int = 24) {
        val currentBalance = NDCycleService.getNdBalance(getApplication())
        if (currentBalance < cost) {
            AppLogger.w("Недостаточно Nd для покупки темы '${theme.palette.displayName}': требуется $cost Nd, текущий баланс: $currentBalance Nd")
            return
        }
        val newBalance = currentBalance - cost
        getApplication<Application>().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt(NDCycleService.KEY_ND_BALANCE, newBalance)
            .apply()

        val updated = _unlockedThemes.value + theme.name
        _unlockedThemes.value = updated
        sharedPrefs.edit().putStringSet(KEY_UNLOCKED_THEMES, updated).apply()
        _appTheme.value = theme
        sharedPrefs.edit().putString(KEY_APP_THEME, theme.name).apply()
        AppLogger.s("🎉 Тема '${theme.palette.displayName}' успешно приобретена за $cost Nd и активирована! Баланс: $newBalance Nd")
        unlockAchievement(Achievement.THEME_PURCHASED)
    }

    fun setAppTheme(theme: AppThemeMode) {
        if (_appTheme.value == theme) return
        if (!isThemeUnlocked(theme)) {
            AppLogger.w("Попытка выбора заблокированной темы: ${theme.palette.displayName}")
            return
        }
        _appTheme.value = theme
        sharedPrefs.edit().putString(KEY_APP_THEME, theme.name).apply()
        AppLogger.i("Тема оформления переключена на: ${theme.palette.displayName}")
        unlockAchievement(Achievement.THEME_CHANGED)
    }

    fun claimVersionBonus(amount: Int = 250): Boolean {
        if (_isVersionBonusClaimed.value) {
            AppLogger.w("Пасхалка версии: секретный бонус +$amount Nd уже был получен ранее!")
            return false
        }
        _isVersionBonusClaimed.value = true
        sharedPrefs.edit().putBoolean(KEY_VERSION_BONUS_CLAIMED, true).apply()
        NDCycleService.addNdBalance(getApplication(), amount, "Секретный бонус версии (9 тапов)")
        unlockAchievement(Achievement.EASTER_EGG_NINE_TAPS)
        return true
    }

    fun addBonusNd(amount: Int = 250, reason: String = "Секретный бонус версии (9 тапов)") {
        NDCycleService.addNdBalance(getApplication(), amount, reason)
    }

    val engineState: StateFlow<EngineState> = NDCycleService.engineState

    val tracks: StateFlow<Map<String, TrackInfo>> = TrackDictionary.tracksFlow
    val normalPacks: StateFlow<List<String>> = TrackDictionary.normalPacksFlow
    val rainPacks: StateFlow<List<String>> = TrackDictionary.rainPacksFlow
    val dayPacks: StateFlow<Set<String>> = TrackDictionary.dayPacksFlow

    val logs: StateFlow<List<LogEntry>> = AppLogger.logs

    private val _permissions = MutableStateFlow(PermissionStatus())
    val permissions: StateFlow<PermissionStatus> = _permissions.asStateFlow()

    private val _isGeneratingData = MutableStateFlow(false)
    val isGeneratingData: StateFlow<Boolean> = _isGeneratingData.asStateFlow()

    // Dashboard layout / controls placement state
    private val _controlsPosition = MutableStateFlow(
        try {
            val savedName = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(KEY_CONTROLS_POSITION, ControlsPosition.BOTTOM.name)
            ControlsPosition.valueOf(savedName ?: ControlsPosition.BOTTOM.name)
        } catch (e: Exception) {
            ControlsPosition.BOTTOM
        }
    )
    val controlsPosition: StateFlow<ControlsPosition> = _controlsPosition.asStateFlow()

    fun setControlsPosition(position: ControlsPosition) {
        _controlsPosition.value = position
        sharedPrefs.edit().putString(KEY_CONTROLS_POSITION, position.name).apply()
    }

    // Custom drawing wallpaper state (persisted across sessions)
    private val _customDrawingWallpaper = MutableStateFlow<CustomWallpaper?>(
        try {
            val savedData = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(KEY_CUSTOM_WALLPAPER, null)
            if (!savedData.isNullOrBlank()) {
                CustomWallpaper.deserializeFromString(savedData)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    )
    val customDrawingWallpaper: StateFlow<CustomWallpaper?> = _customDrawingWallpaper.asStateFlow()

    /**
     * Applies the given drawing strokes as the global application background.
     */
    fun applyDrawingAsBackground(
        strokes: List<DrawingStroke>,
        canvasWidth: Float,
        canvasHeight: Float,
        opacity: Float = 0.85f
    ): Boolean {
        if (strokes.isEmpty()) {
            AppLogger.w("Попытка установить пустой холст как фон приложения")
            return false
        }
        val wallpaper = CustomWallpaper(
            strokes = strokes,
            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight,
            opacity = opacity
        )
        _customDrawingWallpaper.value = wallpaper
        val serialized = wallpaper.serializeToString()
        sharedPrefs.edit().putString(KEY_CUSTOM_WALLPAPER, serialized).apply()
        AppLogger.s("🎨 Рисунок пользователя (${strokes.size} штрихов) применен как фон приложения!")
        unlockAchievement(Achievement.CUSTOM_WALLPAPER)
        return true
    }

    /**
     * Removes the custom drawing wallpaper and reverts to standard dark theme background.
     */
    fun clearCustomDrawingBackground() {
        _customDrawingWallpaper.value = null
        sharedPrefs.edit().remove(KEY_CUSTOM_WALLPAPER).apply()
        AppLogger.i("Пользовательский фон приложения успешно сброшен.")
    }

    /**
     * Updates opacity of the currently active wallpaper.
     */
    fun setCustomDrawingOpacity(opacity: Float) {
        val current = _customDrawingWallpaper.value ?: return
        val updated = current.copy(opacity = opacity.coerceIn(0.1f, 1.0f))
        _customDrawingWallpaper.value = updated
        sharedPrefs.edit().putString(KEY_CUSTOM_WALLPAPER, updated.serializeToString()).apply()
    }

    // Splash Screen initialization state
    private val _splashProgress = MutableStateFlow(0f)
    val splashProgress: StateFlow<Float> = _splashProgress.asStateFlow()

    private val _splashStatusText = MutableStateFlow("Инициализация")
    val splashStatusText: StateFlow<String> = _splashStatusText.asStateFlow()

    private val _isSplashComplete = MutableStateFlow(false)
    val isSplashComplete: StateFlow<Boolean> = _isSplashComplete.asStateFlow()

    private val _splashSteps = MutableStateFlow<List<SplashCheckStep>>(
        listOf(
            SplashCheckStep(
                id = "components",
                title = "Инициализация компонентов",
                detail = "Ожидание проверки системных служб...",
                status = SplashStepStatus.PENDING
            ),
            SplashCheckStep(
                id = "textures",
                title = "Проверка каталогов текстур",
                detail = "Ожидание сканирования директорий PSP...",
                status = SplashStepStatus.PENDING
            ),
            SplashCheckStep(
                id = "config",
                title = "Проверка конфигураций",
                detail = "Ожидание проверки пресетов и темы...",
                status = SplashStepStatus.PENDING
            ),
            SplashCheckStep(
                id = "environment",
                title = "Проверка рабочей среды",
                detail = "Ожидание анализа хранилища и памяти...",
                status = SplashStepStatus.PENDING
            )
        )
    )
    val splashSteps: StateFlow<List<SplashCheckStep>> = _splashSteps.asStateFlow()

    private var splashJob: Job? = null

    fun completeSplashImmediately() {
        splashJob?.cancel()
        _splashProgress.value = 1.0f
        _splashStatusText.value = "Инициализация завершена"
        _splashSteps.value = _splashSteps.value.map { it.copy(status = SplashStepStatus.SUCCESS) }
        _isSplashComplete.value = true
    }

    private fun startSplashInitialization() {
        splashJob?.cancel()
        splashJob = viewModelScope.launch(Dispatchers.IO) {
            if (_isSplashComplete.value) return@launch
            val context = getApplication<Application>()
            _splashProgress.value = 0.05f
            _splashStatusText.value = "Инициализация системы..."

            fun updateStep(id: String, status: SplashStepStatus, detail: String) {
                _splashSteps.value = _splashSteps.value.map { step ->
                    if (step.id == id) step.copy(status = status, detail = detail) else step
                }
            }

            // 1. Инициализация компонентов: Реальная проверка сервисов и разрешений
            updateStep("components", SplashStepStatus.IN_PROGRESS, "Проверка системных служб и прав доступа...")
            _splashStatusText.value = "Инициализация компонентов: проверка системных служб"
            _splashProgress.value = 0.15f
            delay(280)

            val notifManager = NotificationManagerCompat.from(context)
            val notifsEnabled = notifManager.areNotificationsEnabled()
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
            val isPowerSave = powerManager?.isPowerSaveMode == true
            val hasStorage = PermissionHelper.hasStoragePermission(context)
            val isAccessibility = PermissionHelper.isAccessibilityEnabled(context)

            val compDetail = buildString {
                append("Службы активны")
                if (hasStorage) append(" • Память: доступна") else append(" • Память: требуется")
                if (notifsEnabled) append(" • Уведомления: ВКЛ")
                if (isAccessibility) append(" • Accessibility: OK")
            }
            updateStep("components", if (hasStorage) SplashStepStatus.SUCCESS else SplashStepStatus.WARNING, compDetail)
            AppLogger.i("Инициализация компонентов: $compDetail")
            _splashProgress.value = 0.35f
            delay(320)

            if (_isSplashComplete.value) return@launch

            // 2. Проверка каталогов текстур: Реальное сканирование директорий и паков
            updateStep("textures", SplashStepStatus.IN_PROGRESS, "Сканирование структуры каталогов PSP/TEXTURES...")
            _splashStatusText.value = "Инициализация каталога текстур: сканирование директорий"
            _splashProgress.value = 0.45f
            delay(280)

            val pspTexturesDir = File(TrackDictionary.GAME_TEXTURES_DIR)
            val pspPluginsDir = File(TrackDictionary.BASE_PLUGINS_DIR)
            val soundDir = File(TrackDictionary.BASE_PLUGINS_DIR, "Sound")

            var totalTracksCount = 0
            var existingPacksCount = 0

            for ((_, track) in TrackDictionary.TRACKS) {
                totalTracksCount++
                val trackNormalDir = File(track.normalPath)
                if (trackNormalDir.exists() && trackNormalDir.isDirectory) {
                    val packs = trackNormalDir.listFiles()?.count { it.isDirectory } ?: 0
                    existingPacksCount += packs
                }
            }

            if (hasStorage) {
                try {
                    pspTexturesDir.mkdirs()
                    pspPluginsDir.mkdirs()
                    soundDir.mkdirs()
                    if (existingPacksCount == 0) {
                        PermissionHelper.createSampleTextureStructure(context)
                    }
                } catch (e: Exception) {
                    AppLogger.w("Проверка текстур: ${e.message}")
                }
            }

            val texturesDetail = buildString {
                append("Трасс: $totalTracksCount")
                if (existingPacksCount > 0) {
                    append(" • Найдено паков: $existingPacksCount")
                } else if (pspTexturesDir.exists()) {
                    append(" • Каталог игры готов")
                } else {
                    append(" • Структура /sdcard/PSP/ проверена")
                }
            }
            updateStep("textures", SplashStepStatus.SUCCESS, texturesDetail)
            AppLogger.i("Каталоги текстур: $texturesDetail")
            _splashProgress.value = 0.65f
            delay(320)

            if (_isSplashComplete.value) return@launch

            // 3. Проверка конфигураций: Реальная валидация настроек и параметров
            updateStep("config", SplashStepStatus.IN_PROGRESS, "Валидация конфигурации, пресетов и темы...")
            _splashStatusText.value = "Инициализация конфигурации: загрузка пресетов"
            _splashProgress.value = 0.75f
            delay(280)

            val savedTrackKey = sharedPrefs.getString(KEY_TRACK_KEY, "SARTE") ?: "SARTE"
            val validTrackKey = if (TrackDictionary.TRACKS.containsKey(savedTrackKey)) savedTrackKey else "SARTE"
            val savedInterval = sharedPrefs.getInt(KEY_CYCLE_INTERVAL, 15).coerceIn(5, 3600)
            val activeThemeName = _appTheme.value.palette.displayName
            val hasCustomBg = _customDrawingWallpaper.value != null

            val configDetail = buildString {
                append("Трасса: $validTrackKey • Интервал: ${savedInterval}с • Тема: $activeThemeName")
                if (hasCustomBg) append(" • Свой фон")
            }
            updateStep("config", SplashStepStatus.SUCCESS, configDetail)
            AppLogger.i("Конфигурация: $configDetail")
            _splashProgress.value = 0.85f
            delay(320)

            if (_isSplashComplete.value) return@launch

            // 4. Проверка рабочей среды: Реальная проверка доступной памяти и файловой системы
            updateStep("environment", SplashStepStatus.IN_PROGRESS, "Анализ свободной памяти и среды выполнения...")
            _splashStatusText.value = "Инициализация рабочей среды: проверка ресурсов"
            _splashProgress.value = 0.95f
            delay(280)

            val isMounted = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
            val extStorage = Environment.getExternalStorageDirectory()
            val freeMb = try {
                extStorage.usableSpace / (1024 * 1024)
            } catch (e: Exception) {
                context.filesDir.usableSpace / (1024 * 1024)
            }
            val runtime = Runtime.getRuntime()
            val maxRamMb = runtime.maxMemory() / (1024 * 1024)

            val envDetail = buildString {
                if (isMounted) append("Хранилище смонтировано • ")
                append("Свободно: $freeMb МБ • Лимит RAM: $maxRamMb МБ")
            }
            updateStep("environment", SplashStepStatus.SUCCESS, envDetail)
            AppLogger.i("Рабочая среда: $envDetail")
            _splashProgress.value = 1.0f
            delay(320)

            // Завершение
            _splashStatusText.value = "Инициализация завершена"
            delay(250)
            _isSplashComplete.value = true
        }
    }

    private val _warningShowCount = MutableStateFlow(
        application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_WARNING_SHOW_COUNT, 0)
    )
    val warningShowCount: StateFlow<Int> = _warningShowCount.asStateFlow()

    private val _showAssemblyWarningEvent = MutableStateFlow(false)
    val showAssemblyWarningEvent: StateFlow<Boolean> = _showAssemblyWarningEvent.asStateFlow()

    fun incrementWarningCount() {
        val newCount = _warningShowCount.value + 1
        _warningShowCount.value = newCount
        sharedPrefs.edit().putInt(KEY_WARNING_SHOW_COUNT, newCount).apply()
    }

    fun handleStopFromNotification(): Boolean {
        completeSplashImmediately()
        stopEngine()
        refreshPermissions()
        val context = getApplication<Application>()
        val isServiceActive = PermissionHelper.isAccessibilityEnabled(context)
        val shouldShowWarning = isServiceActive && _warningShowCount.value < MAX_WARNING_COUNT
        if (shouldShowWarning) {
            _showAssemblyWarningEvent.value = true
            return true
        } else {
            _showAssemblyWarningEvent.value = false
            return false
        }
    }

    fun dismissAssemblyWarning() {
        _showAssemblyWarningEvent.value = false
    }

    init {
        loadSavedSettings()
        refreshPermissions()
        startAutoSaveObserver()
        startSplashInitialization()
        startPackProgressObserver()
        checkPackProgressForMinecraftTheme()
    }

    private fun startPackProgressObserver() {
        viewModelScope.launch {
            engineState.collect { state ->
                val hasStorage = PermissionHelper.hasStoragePermission(getApplication())
                val isPpssppRunning = PermissionHelper.isPpssppProcessRunning(getApplication())
                val isRealGameplay = state.isRealGameplayConfirmed && !state.isGameplayBlocked
                if (hasStorage && isPpssppRunning && isRealGameplay && !state.isRainActive && TrackDictionary.INITIAL_NORMAL_PACKS.contains(state.currentPack)) {
                    markPackCompleted(state.currentPack)
                }
            }
        }
    }

    private fun startAutoSaveObserver() {
        viewModelScope.launch {
            engineState.collect { state ->
                autoSaveSettings(state)
            }
        }
    }

    private fun autoSaveSettings(state: EngineState) {
        sharedPrefs.edit()
            .putInt(KEY_CYCLE_INTERVAL, state.cycleIntervalSeconds)
            .putInt(KEY_RAIN_PROBABILITY, state.rainProbabilityPercent)
            .putInt(KEY_SWIPE_COUNT, state.swipeCount)
            .putLong(KEY_SWIPE_DURATION, state.swipeDurationMs)
            .putLong(KEY_SWIPE_PAUSE, state.swipePauseMs)
            .putBoolean(KEY_RANDOM_MODE, state.isRandomMode)
            .putBoolean(KEY_FORCE_SOUND, state.isForceSound)
            .putInt(KEY_START_X, state.startX)
            .putInt(KEY_START_Y, state.startY)
            .putInt(KEY_END_X, state.endX)
            .putInt(KEY_END_Y, state.endY)
            .putString(KEY_CLICK_METHOD, state.clickMethod.name)
            .putString(KEY_TRACK_KEY, state.currentTrackKey)
            .putString(KEY_APP_THEME, _appTheme.value.name)
            .apply()
    }

    fun saveAllSettings(): Boolean {
        autoSaveSettings(engineState.value)
        AppLogger.s("Все параметры и конфигурация сохранены в локальное хранилище")
        return true
    }

    private fun loadSavedSettings() {
        NDCycleService.getNdBalance(getApplication())

        if (!sharedPrefs.contains(KEY_CYCLE_INTERVAL)) {
            return
        }

        val interval = sharedPrefs.getInt(KEY_CYCLE_INTERVAL, 15)
        val rainProb = sharedPrefs.getInt(KEY_RAIN_PROBABILITY, 20)
        val swipeCount = sharedPrefs.getInt(KEY_SWIPE_COUNT, 2)
        val swipeDuration = sharedPrefs.getLong(KEY_SWIPE_DURATION, 50L)
        val swipePause = sharedPrefs.getLong(KEY_SWIPE_PAUSE, 300L)
        val randomMode = sharedPrefs.getBoolean(KEY_RANDOM_MODE, false)
        val forceSound = sharedPrefs.getBoolean(KEY_FORCE_SOUND, false)
        val startX = sharedPrefs.getInt(KEY_START_X, 1169)
        val startY = sharedPrefs.getInt(KEY_START_Y, 25)
        val endX = sharedPrefs.getInt(KEY_END_X, 1194)
        val endY = sharedPrefs.getInt(KEY_END_Y, 25)
        val clickMethodName = sharedPrefs.getString(KEY_CLICK_METHOD, ClickMethod.AUTO.name) ?: ClickMethod.AUTO.name
        val clickMethod = try {
            ClickMethod.valueOf(clickMethodName)
        } catch (e: Exception) {
            ClickMethod.AUTO
        }
        val trackKey = sharedPrefs.getString(KEY_TRACK_KEY, "SARTE") ?: "SARTE"

        NDCycleService.restoreSettings(
            interval = interval,
            rainProb = rainProb,
            swipeCount = swipeCount,
            swipeDuration = swipeDuration,
            swipePause = swipePause,
            randomMode = randomMode,
            forceSound = forceSound,
            startX = startX,
            startY = startY,
            endX = endX,
            endY = endY,
            clickMethod = clickMethod,
            trackKey = trackKey
        )
    }

    fun refreshPermissions(isFromUserAction: Boolean = false) {
        val context = getApplication<Application>()
        val hasStorage = PermissionHelper.hasStoragePermission(context)
        val hasNotification = PermissionHelper.hasNotificationPermission(context)
        val isAccessibilityActive = PermissionHelper.isAccessibilityEnabled(context)
        if (isFromUserAction) {
            if (hasStorage) {
                unlockAchievement(Achievement.STORAGE_GRANTED)
            }
            if (hasNotification || isAccessibilityActive) {
                unlockAchievement(Achievement.NOTIFICATIONS_GRANTED)
            }
        }
        _permissions.value = PermissionStatus(
            hasStorage = hasStorage,
            hasNotification = hasNotification,
            isBatteryOptimizedIgnored = PermissionHelper.isIgnoringBatteryOptimizations(context),
            isAccessibilityActive = isAccessibilityActive,
            isRootActive = AssemblyService.isRootAvailable()
        )
        if (hasStorage) {
            checkPackProgressForMinecraftTheme()
        }
    }

    fun startEngine() {
        unlockAchievement(Achievement.FIRST_LAUNCH)
        unlockAchievement(Achievement.NOTIFICATIONS_GRANTED)
        val context = getApplication<Application>()
        val state = engineState.value
        val intent = Intent(context, NDCycleService::class.java).apply {
            action = NDCycleService.ACTION_START
            putExtra(NDCycleService.EXTRA_RANDOM_MODE, state.isRandomMode)
            putExtra(NDCycleService.EXTRA_TRACK_KEY, state.currentTrackKey)
            putExtra(NDCycleService.EXTRA_FORCE_SOUND, state.isForceSound)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    fun togglePause() {
        val context = getApplication<Application>()
        val intent = Intent(context, NDCycleService::class.java).apply {
            action = NDCycleService.ACTION_TOGGLE_PAUSE
        }
        context.startService(intent)
    }

    fun stopEngine() {
        val context = getApplication<Application>()
        val intent = Intent(context, NDCycleService::class.java).apply {
            action = NDCycleService.ACTION_STOP_SERVICE
        }
        context.startService(intent)
    }

    fun nextTrack() {
        val context = getApplication<Application>()
        val intent = Intent(context, NDCycleService::class.java).apply {
            action = NDCycleService.ACTION_NEXT_TRACK
        }
        context.startService(intent)
    }

    fun setTrack(trackKey: String) {
        unlockAchievement(Achievement.TRACK_EXPLORER)
        NDCycleService.updateTrackKey(trackKey)
        sharedPrefs.edit().putString(KEY_TRACK_KEY, trackKey).apply()

        // Only send intent to service if session is already active/running
        if (NDCycleService.engineState.value.status != EngineStatus.STOPPED) {
            val context = getApplication<Application>()
            val intent = Intent(context, NDCycleService::class.java).apply {
                action = NDCycleService.ACTION_SET_TRACK
                putExtra(NDCycleService.EXTRA_TRACK_KEY, trackKey)
            }
            context.startService(intent)
        }
    }

    fun renameDirectory(
        parentPath: String,
        oldName: String,
        newName: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val cleanOld = oldName.trim()
            val cleanNew = newName.trim()
            val cleanParent = parentPath.trim()

            if (cleanOld.isEmpty() || cleanNew.isEmpty()) {
                onResult(false, "Заполните текущее и новое имя папки")
                return@launch
            }

            if (cleanOld == cleanNew) {
                onResult(false, "Новое имя совпадает с текущим")
                return@launch
            }

            val result = withContext(Dispatchers.IO) {
                val parent = File(cleanParent)
                val source = File(parent, cleanOld)
                val target = File(parent, cleanNew)

                if (!source.exists()) {
                    return@withContext Pair(false, "Папка '$cleanOld' не найдена в $cleanParent")
                }
                if (target.exists()) {
                    return@withContext Pair(false, "Папка с именем '$cleanNew' уже существует")
                }

                val success = source.renameTo(target)
                if (success) {
                    AppLogger.s("Переименование директории: '$cleanOld' -> '$cleanNew'")
                    Pair(true, "Папка успешно переименована в '$cleanNew'")
                } else {
                    AppLogger.e("Не удалось переименовать '$cleanOld' в '$cleanNew'")
                    Pair(false, "Ошибка переименования (проверьте права доступа)")
                }
            }

            if (result.first) {
                // Dynamically update TrackDictionary reactive state (tracks, normal packs, rain packs)
                val updatedTrackKey = TrackDictionary.notifyDirectoryRenamed(cleanParent, cleanOld, cleanNew)

                // If active track was the renamed directory (or updated by TrackDictionary), reactively update active track
                val currentTrackKey = engineState.value.currentTrackKey
                if (currentTrackKey.equals(cleanOld, ignoreCase = true) || updatedTrackKey != null) {
                    val nextActiveKey = updatedTrackKey ?: cleanNew
                    setTrack(nextActiveKey)
                }

                // If active texture pack was the renamed folder, reactively update active pack
                NDCycleService.updatePackIfMatching(cleanOld, cleanNew)
            }

            onResult(result.first, result.second)
        }
    }

    fun setRandomMode(randomMode: Boolean) {
        NDCycleService.updateRandomMode(randomMode)
        autoSaveSettings(engineState.value)
    }

    fun setForceSound(forceSound: Boolean) {
        NDCycleService.updateForceSound(forceSound)
        autoSaveSettings(engineState.value)
    }

    fun setCycleInterval(seconds: Int) {
        unlockAchievement(Achievement.TIME_WARP)
        NDCycleService.updateCycleInterval(seconds)
        autoSaveSettings(engineState.value)
    }

    fun setRainProbability(percent: Int) {
        unlockAchievement(Achievement.RAIN_MAKER)
        NDCycleService.updateRainProbability(percent)
        autoSaveSettings(engineState.value)
    }

    fun setSwipeCount(count: Int) {
        NDCycleService.updateSwipeCount(count)
        autoSaveSettings(engineState.value)
    }

    fun setSwipeDuration(durationMs: Long) {
        NDCycleService.updateSwipeDuration(durationMs)
        autoSaveSettings(engineState.value)
    }

    fun setSwipePause(pauseMs: Long) {
        NDCycleService.updateSwipePause(pauseMs)
        autoSaveSettings(engineState.value)
    }

    fun setTapCoordinates(x: Int, y: Int) {
        val current = engineState.value
        val deltaX = current.endX - current.startX
        val deltaY = current.endY - current.startY
        NDCycleService.updateGestureCoordinates(x, y, x + deltaX, y + deltaY)
        autoSaveSettings(engineState.value)
    }

    fun setGestureCoordinates(startX: Int, startY: Int, endX: Int, endY: Int) {
        NDCycleService.updateGestureCoordinates(startX, startY, endX, endY)
        autoSaveSettings(engineState.value)
    }

    fun setClickMethod(method: ClickMethod) {
        NDCycleService.updateClickMethod(method)
        NDCycleService.instance?.setClickMethod(method)
        autoSaveSettings(engineState.value)
    }

    fun applySinglePack(packName: String, isRain: Boolean = false) {
        val hasStorage = PermissionHelper.hasStoragePermission(getApplication())
        val isPpssppRunning = PermissionHelper.isPpssppProcessRunning(getApplication())
        val isBlocked = engineState.value.isGameplayBlocked || !engineState.value.isRealGameplayConfirmed
        if (hasStorage && isPpssppRunning && !isBlocked && !isRain && TrackDictionary.INITIAL_NORMAL_PACKS.contains(packName)) {
            markPackCompleted(packName)
        }
        val activeService = NDCycleService.instance
        if (activeService != null) {
            activeService.applySinglePackDirect(packName, isRain)
        } else {
            viewModelScope.launch {
                val context = getApplication<Application>()
                val state = engineState.value
                val textureEngine = TextureEngine(context)

                NDCycleService.updateSinglePackState(packName, isRain)

                withContext(Dispatchers.IO) {
                    val success = textureEngine.applyPack(
                        trackKey = state.currentTrackKey,
                        packName = packName,
                        isRain = isRain,
                        forceSound = state.isForceSound,
                        startX = state.startX,
                        startY = state.startY,
                        endX = state.endX,
                        endY = state.endY,
                        tapX = state.startX,
                        tapY = state.startY,
                        swipeCount = state.swipeCount,
                        swipeDurationMs = state.swipeDurationMs,
                        swipePauseMs = state.swipePauseMs,
                        clickMethod = state.clickMethod
                    ) { msg ->
                        NDCycleService.updateLastMessage(msg)
                    }
                    if (success) {
                        NDCycleService.incrementReplacements()
                    }
                }
                NDCycleService.resetCopyingStatus()
            }
        }
    }

    fun generateSampleStructure(onFinished: (String) -> Unit) {
        viewModelScope.launch {
            _isGeneratingData.value = true
            val context = getApplication<Application>()
            val msg = PermissionHelper.createSampleTextureStructure(context)
            _isGeneratingData.value = false
            onFinished(msg)
        }
    }

    fun testSwipeNow() {
        unlockAchievement(Achievement.SWIPE_TESTED)
        val state = engineState.value
        AssemblyService.triggerSwipeSequence(
            startX = state.startX,
            startY = state.startY,
            endX = state.endX,
            endY = state.endY,
            swipeCount = state.swipeCount,
            durationMs = state.swipeDurationMs,
            pauseMs = state.swipePauseMs,
            preferredMethod = state.clickMethod
        )
    }

    fun testTapNow() {
        testSwipeNow()
    }

    fun clearLogs() {
        AppLogger.clear()
    }

    fun clearCache(onResult: (freedBytes: Long, formatted: String) -> Unit) {
        unlockAchievement(Achievement.CACHE_CLEANED)
        viewModelScope.launch(Dispatchers.IO) {
            val context = getApplication<Application>()
            val freed = PermissionHelper.clearCache(context)
            val formatted = PermissionHelper.formatBytes(freed)
            AppLogger.s("Очищен кеш приложения: освобождено $formatted")
            withContext(Dispatchers.Main) {
                onResult(freed, formatted)
            }
        }
    }
}
