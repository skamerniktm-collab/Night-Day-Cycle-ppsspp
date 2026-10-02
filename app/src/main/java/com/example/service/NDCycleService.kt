package com.example.service

import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.MainActivity
import com.example.R
import com.example.data.TrackDictionary
import com.example.model.ClickMethod
import com.example.model.EngineState
import com.example.model.EngineStatus
import com.example.util.AppLogger
import com.example.util.GtScreenDetector
import com.example.util.PermissionHelper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Random

class NDCycleService : Service() {

    var isEmulatorActive: Boolean = false

    private val serviceScope = CoroutineScope(Dispatchers.Default + Job())
    private var workerJob: Job? = null
    private var screenMonitorJob: Job? = null
    private var wakeLock: PowerManager.WakeLock? = null
    private lateinit var textureEngine: TextureEngine
    private val random = Random()

    override fun onCreate() {
        super.onCreate()
        instance = this
        textureEngine = TextureEngine(applicationContext)
        createNotificationChannel()

        val initialBalance = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getInt(KEY_ND_BALANCE, 0)
        _engineState.value = _engineState.value.copy(
            ndBalance = initialBalance,
            isEmulatorActive = isEmulatorActive
        )

        AppLogger.i("NDCycleService создан (баланс Nd: $initialBalance)")
        startScreenMonitoring()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        AppLogger.i("NDCycleService onStartCommand action: $action")

        if (action == ACTION_STOP_SERVICE) {
            stopEngine()
            return START_NOT_STICKY
        }

        // Always call startForeground to guarantee compliance with startForegroundService()
        startForegroundWithNotification()

        when (action) {
            ACTION_START -> {
                val isRandom = intent?.getBooleanExtra(EXTRA_RANDOM_MODE, _engineState.value.isRandomMode) ?: _engineState.value.isRandomMode
                val trackKey = intent?.getStringExtra(EXTRA_TRACK_KEY) ?: _engineState.value.currentTrackKey
                val forceSound = intent?.getBooleanExtra(EXTRA_FORCE_SOUND, _engineState.value.isForceSound) ?: _engineState.value.isForceSound
                startEngine(isRandom, trackKey, forceSound)
            }
            ACTION_PAUSE -> pauseEngine()
            ACTION_RESUME -> resumeEngine()
            ACTION_TOGGLE_PAUSE -> {
                if (_engineState.value.status == EngineStatus.PAUSED) {
                    resumeEngine()
                } else if (_engineState.value.status == EngineStatus.RUNNING || _engineState.value.status == EngineStatus.RAIN_SEQUENCE) {
                    pauseEngine()
                } else {
                    startEngine(_engineState.value.isRandomMode, _engineState.value.currentTrackKey, _engineState.value.isForceSound)
                }
            }
            ACTION_NEXT_TRACK -> switchNextTrack()
            ACTION_SET_TRACK -> {
                val trackKey = intent?.getStringExtra(EXTRA_TRACK_KEY)
                if (trackKey != null) {
                    setTrack(trackKey)
                }
            }
            ACTION_APPLY_SINGLE_PACK -> {
                val packName = intent?.getStringExtra(EXTRA_PACK_NAME)
                val isRain = intent?.getBooleanExtra(EXTRA_IS_RAIN, false) ?: false
                if (packName != null) {
                    applySinglePack(packName, isRain)
                }
            }
            else -> {
                // Service restarted or idle
            }
        }

        return START_STICKY
    }

    private fun startEngine(randomMode: Boolean, trackKey: String, forceSound: Boolean) {
        acquireWakeLock()
        _engineState.value = _engineState.value.copy(
            status = EngineStatus.RUNNING,
            isRandomMode = randomMode,
            currentTrackKey = trackKey,
            isForceSound = forceSound,
            lastMessage = "Движок запущен"
        )
        startForegroundWithNotification()
        startScreenMonitoring()

        workerJob?.cancel()
        workerJob = serviceScope.launch {
            runWorkerLoop()
        }
    }

    private fun pauseEngine() {
        if (_engineState.value.status == EngineStatus.RUNNING || _engineState.value.status == EngineStatus.RAIN_SEQUENCE) {
            _engineState.value = _engineState.value.copy(
                status = EngineStatus.PAUSED,
                lastMessage = "Движок на паузе"
            )
            updateNotification()
            AppLogger.i("Движок поставлен на паузу")
        }
    }

    private fun resumeEngine() {
        if (_engineState.value.status == EngineStatus.PAUSED) {
            _engineState.value = _engineState.value.copy(
                status = EngineStatus.RUNNING,
                lastMessage = "Движок возобновлен"
            )
            updateNotification()
            AppLogger.i("Движок возобновлен")
            startScreenMonitoring()
        }
    }

    private fun stopEngine() {
        AppLogger.i("Остановка сервиса NDCYCLE")
        workerJob?.cancel()
        workerJob = null
        screenMonitorJob?.cancel()
        screenMonitorJob = null
        releaseWakeLock()

        _engineState.value = _engineState.value.copy(
            status = EngineStatus.STOPPED,
            currentPack = "Ожидание",
            isRainActive = false,
            countdownSeconds = _engineState.value.cycleIntervalSeconds,
            lastMessage = "Движок остановлен"
        )

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun switchNextTrack() {
        val nextKey = TrackDictionary.getNextTrackKey(_engineState.value.currentTrackKey)
        setTrack(nextKey)
    }

    fun setTrack(trackKey: String) {
        val track = TrackDictionary.getTrack(trackKey)
        _engineState.value = _engineState.value.copy(
            currentTrackKey = trackKey,
            lastMessage = "Трасса изменена на: ${track.name}"
        )
        AppLogger.i("Активная трасса изменена на: ${track.name} ($trackKey)")
        updateNotification()
    }

    fun setRandomMode(randomMode: Boolean) {
        _engineState.value = _engineState.value.copy(
            isRandomMode = randomMode
        )
        AppLogger.i("Режим переключения: ${if (randomMode) "Случайный" else "Последовательный"}")
    }

    fun setForceSound(forceSound: Boolean) {
        _engineState.value = _engineState.value.copy(isForceSound = forceSound)
        AppLogger.i("Звуковые эффекты: ${if (forceSound) "Включены" else "Выключены"}")
    }

    fun setCycleInterval(seconds: Int) {
        _engineState.value = _engineState.value.copy(cycleIntervalSeconds = seconds)
        AppLogger.i("Интервал цикла изменен на: $seconds сек")
    }

    fun setRainProbability(percent: Int) {
        _engineState.value = _engineState.value.copy(rainProbabilityPercent = percent)
        AppLogger.i("Вероятность дождя изменена на: $percent%")
    }

    fun setTapCoordinates(x: Int, y: Int) {
        _engineState.value = _engineState.value.copy(
            startX = x,
            startY = y,
            tapX = x,
            tapY = y
        )
        AppLogger.i("Координаты кликера обновлены: ($x, $y)")
    }

    fun setGestureCoordinates(startX: Int, startY: Int, endX: Int, endY: Int) {
        _engineState.value = _engineState.value.copy(
            startX = startX,
            startY = startY,
            endX = endX,
            endY = endY,
            tapX = startX,
            tapY = startY
        )
        AppLogger.i("Координаты жеста обновлены: ($startX, $startY) -> ($endX, $endY)")
    }

    fun setSwipeCount(count: Int) {
        val safeCount = count.coerceIn(1, 20)
        _engineState.value = _engineState.value.copy(swipeCount = safeCount)
        AppLogger.i("Количество микро-свайпов установлено: $safeCount")
    }

    fun setSwipeDuration(durationMs: Long) {
        val safeDuration = durationMs.coerceIn(10L, 1000L)
        _engineState.value = _engineState.value.copy(swipeDurationMs = safeDuration)
        AppLogger.i("Скорость микро-свайпа установлена: ${safeDuration}мс")
    }

    fun setSwipePause(pauseMs: Long) {
        val safePause = pauseMs.coerceIn(50L, 2000L)
        _engineState.value = _engineState.value.copy(swipePauseMs = safePause)
        AppLogger.i("Пауза между микро-свайпами установлена: ${safePause}мс")
    }

    fun setClickMethod(method: ClickMethod) {
        _engineState.value = _engineState.value.copy(clickMethod = method)
        AppLogger.i("Метод кликера изменен на: ${method.displayName}")
    }

    fun applySinglePackDirect(packName: String, isRain: Boolean) {
        applySinglePack(packName, isRain)
    }

    private fun applySinglePack(packName: String, isRain: Boolean) {
        serviceScope.launch {
            acquireWakeLock()
            _engineState.value = _engineState.value.copy(
                status = EngineStatus.COPYING,
                currentPack = packName,
                isRainActive = isRain,
                lastMessage = "Ручная подмена: $packName"
            )
            updateNotification()

            val success = textureEngine.applyPack(
                trackKey = _engineState.value.currentTrackKey,
                packName = packName,
                isRain = isRain,
                forceSound = _engineState.value.isForceSound,
                startX = _engineState.value.startX,
                startY = _engineState.value.startY,
                endX = _engineState.value.endX,
                endY = _engineState.value.endY,
                tapX = _engineState.value.startX,
                tapY = _engineState.value.startY,
                swipeCount = _engineState.value.swipeCount,
                swipeDurationMs = _engineState.value.swipeDurationMs,
                swipePauseMs = _engineState.value.swipePauseMs,
                clickMethod = _engineState.value.clickMethod
            ) { msg ->
                _engineState.value = _engineState.value.copy(lastMessage = msg)
            }

            if (success) {
                if (!isRain) {
                    recordCompletedPack(packName)
                }
                _engineState.value = _engineState.value.copy(
                    totalReplacements = _engineState.value.totalReplacements + 1,
                    lastActionTime = System.currentTimeMillis()
                )
            }

            if (_engineState.value.status == EngineStatus.COPYING) {
                _engineState.value = _engineState.value.copy(
                    status = if (workerJob?.isActive == true) EngineStatus.RUNNING else EngineStatus.STOPPED
                )
            }
            updateNotification()
        }
    }

    private fun recordCompletedPack(packName: String) {
        if (!PermissionHelper.hasStoragePermission(this)) return
        if (!PermissionHelper.isPpssppProcessRunning(this)) return
        if (_engineState.value.isGameplayBlocked || !_engineState.value.isRealGameplayConfirmed) {
            val reason = when {
                _engineState.value.isGamePausedDetected -> "игра на паузе / меню настроек"
                _engineState.value.isMainMenuDetected -> "открыто главное меню PPSSPP (заезд не начат)"
                _engineState.value.isGtIconScreenDetected -> "на экране статично стоит значок GT"
                _engineState.value.isIdleScreenDetected -> "экран статичен без движения"
                else -> "простая проверка процесса не даёт прогресса (нет реального игрового процесса)"
            }
            AppLogger.w("⚠️ Защита от накрутки: $reason. Прохождение пака не засчитывается.")
            return
        }
        if (TrackDictionary.INITIAL_NORMAL_PACKS.contains(packName)) {
            try {
                val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                val current = prefs.getStringSet("key_completed_packs", emptySet()) ?: emptySet()
                if (!current.contains(packName)) {
                    val updated = current + packName
                    prefs.edit().putStringSet("key_completed_packs", updated).apply()
                }
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    private fun startScreenMonitoring() {
        if (screenMonitorJob?.isActive == true) return
        screenMonitorJob = serviceScope.launch {
            while (true) {
                try {
                    val result = GtScreenDetector.evaluateScreen(applicationContext)
                    val prevIdle = _engineState.value.isIdleScreenDetected
                    val prevGt = _engineState.value.isGtIconScreenDetected
                    val prevPaused = _engineState.value.isGamePausedDetected
                    val prevMenu = _engineState.value.isMainMenuDetected
                    val prevGameplay = _engineState.value.isRealGameplayConfirmed

                    if (prevIdle != result.isIdleNoGameplay ||
                        prevGt != result.isGtIconScreen ||
                        prevPaused != result.isGamePaused ||
                        prevMenu != result.isMainMenu ||
                        prevGameplay != result.isRealGameplayConfirmed
                    ) {
                        _engineState.value = _engineState.value.copy(
                            isIdleScreenDetected = result.isIdleNoGameplay,
                            isGtIconScreenDetected = result.isGtIconScreen,
                            isGamePausedDetected = result.isGamePaused,
                            isMainMenuDetected = result.isMainMenu,
                            isRealGameplayConfirmed = result.isRealGameplayConfirmed,
                            lastScreenCheckTime = System.currentTimeMillis()
                        )
                        if (result.isRealGameplayConfirmed) {
                            AppLogger.s("🎮 ${result.statusMessage}")
                        } else {
                            AppLogger.w("🛡 ${result.statusMessage}")
                        }
                    }

                    val a11y = AssemblyAccessibilityService.instance
                    if (a11y != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        a11y.captureScreen { bitmap ->
                            if (bitmap != null) {
                                GtScreenDetector.analyzeAndRecordFrame(bitmap)
                            }
                        }
                    }
                } catch (e: Throwable) {
                    // Ignore transient checking error
                }

                delay(2000L) // Проверка каждые 2 секунды
            }
        }
    }

    private suspend fun runWorkerLoop() {
        AppLogger.s("Фоновый цикл запущен (Трасса: ${_engineState.value.currentTrackKey})")

        var packIndex = _engineState.value.currentPackIndex % TrackDictionary.NORMAL_PACKS.size

        try {
            while (true) {
                // Check pause state
                while (_engineState.value.status == EngineStatus.PAUSED) {
                    delay(500)
                }

                // Защита от Idle-накрутки и главного меню: прогресс только при реальном игровом процессе
                while (_engineState.value.isGameplayBlocked || !_engineState.value.isRealGameplayConfirmed) {
                    val statusText = when {
                        _engineState.value.isGamePausedDetected -> "⏸ Игра на паузе / меню настроек (прогресс остановлен)"
                        _engineState.value.isMainMenuDetected -> "⏸ Главное меню PPSSPP: запустите заезд Gran Turismo для прогресса"
                        _engineState.value.isGtIconScreenDetected -> "⏸ Меню со значком GT: начните заезд для фиксации прогресса"
                        _engineState.value.isIdleScreenDetected -> "⏸ Защита от idle: экран статичен без игрового движения"
                        else -> "⏸ Ожидание реального игрового процесса Gran Turismo..."
                    }
                    _engineState.value = _engineState.value.copy(
                        lastMessage = statusText
                    )
                    updateNotification()
                    delay(2000L)
                }

                val currentTrack = _engineState.value.currentTrackKey
                val isRandom = _engineState.value.isRandomMode
                val packToApply: String

                if (isRandom) {
                    packToApply = TrackDictionary.NORMAL_PACKS[random.nextInt(TrackDictionary.NORMAL_PACKS.size)]
                } else {
                    packToApply = TrackDictionary.NORMAL_PACKS[packIndex % TrackDictionary.NORMAL_PACKS.size]
                }

                // Check rain trigger probability
                val rainChance = _engineState.value.rainProbabilityPercent / 100.0f
                val shouldTriggerRain = TrackDictionary.DAY_PACKS_FOR_RAIN_CHECK.contains(packToApply) &&
                        (_engineState.value.rainProbabilityPercent > 0 && random.nextFloat() < rainChance)

                if (shouldTriggerRain) {
                    AppLogger.w("⛈ Сработала вероятность дождя (${_engineState.value.rainProbabilityPercent}%)! Запуск цикла playRainSequence()...")
                    playRainSequence(currentTrack)

                    if (!isRandom) {
                        val resetIdx = TrackDictionary.NORMAL_PACKS.indexOf(TrackDictionary.RESUME_PACK_AFTER_RAIN)
                        packIndex = if (resetIdx != -1) resetIdx else 0
                        AppLogger.i("После дождя индекс переключен на: ${TrackDictionary.RESUME_PACK_AFTER_RAIN} (индекс: $packIndex)")
                    }
                } else {
                    // Apply normal pack
                    _engineState.value = _engineState.value.copy(
                        status = EngineStatus.COPYING,
                        currentPack = packToApply,
                        isRainActive = false,
                        currentPackIndex = packIndex,
                        lastMessage = "Применение: $packToApply"
                    )
                    updateNotification()

                    textureEngine.applyPack(
                        trackKey = currentTrack,
                        packName = packToApply,
                        isRain = false,
                        forceSound = _engineState.value.isForceSound,
                        startX = _engineState.value.startX,
                        startY = _engineState.value.startY,
                        endX = _engineState.value.endX,
                        endY = _engineState.value.endY,
                        tapX = _engineState.value.startX,
                        tapY = _engineState.value.startY,
                        swipeCount = _engineState.value.swipeCount,
                        swipeDurationMs = _engineState.value.swipeDurationMs,
                        swipePauseMs = _engineState.value.swipePauseMs,
                        clickMethod = _engineState.value.clickMethod
                    ) { msg ->
                        _engineState.value = _engineState.value.copy(lastMessage = msg)
                    }

                    recordCompletedPack(packToApply)

                    _engineState.value = _engineState.value.copy(
                        status = EngineStatus.RUNNING,
                        totalReplacements = _engineState.value.totalReplacements + 1,
                        lastActionTime = System.currentTimeMillis()
                    )
                    updateNotification()

                    // Anti-farming check: award +1 Nd only when session is active and isEmulatorActive == true
                    val isSessionActive = _engineState.value.status == EngineStatus.RUNNING || _engineState.value.status == EngineStatus.COPYING
                    if (isSessionActive && isEmulatorActive) {
                        incrementNdBalance(applicationContext)
                    } else {
                        AppLogger.d("Цикл завершен без начисления Nd (сессия: $isSessionActive, эмулятор: $isEmulatorActive)")
                    }

                    // Countdown for configured cycle interval
                    holdCountdown(_engineState.value.cycleIntervalSeconds)

                    if (!isRandom) {
                        packIndex = (packIndex + 1) % TrackDictionary.NORMAL_PACKS.size
                    }
                }
            }
        } catch (e: CancellationException) {
            AppLogger.i("Фоновый цикл остановлен.")
        } catch (e: Exception) {
            AppLogger.e("Ошибка в фоновом цикле", e)
        }
    }

    private suspend fun playRainSequence(trackKey: String) {
        _engineState.value = _engineState.value.copy(
            status = EngineStatus.RAIN_SEQUENCE,
            isRainActive = true
        )
        updateNotification()

        for (rainPack in TrackDictionary.RAIN_PACKS) {
            if (_engineState.value.status == EngineStatus.STOPPED) break

            while (_engineState.value.status == EngineStatus.PAUSED) {
                delay(500)
            }

            // Защита от Idle-накрутки и главного меню: прогресс только при реальном игровом процессе
            while (_engineState.value.isGameplayBlocked || !_engineState.value.isRealGameplayConfirmed) {
                val statusText = when {
                    _engineState.value.isGamePausedDetected -> "⏸ Игра на паузе / меню настроек (дождь на паузе)"
                    _engineState.value.isMainMenuDetected -> "⏸ Главное меню PPSSPP: заезд не начат (дождь на паузе)"
                    _engineState.value.isGtIconScreenDetected -> "⏸ Меню со значком GT (дождь на паузе)"
                    _engineState.value.isIdleScreenDetected -> "⏸ Защита от idle: экран статичен (дождь на паузе)"
                    else -> "⏸ Ожидание реального игрового процесса Gran Turismo..."
                }
                _engineState.value = _engineState.value.copy(
                    lastMessage = statusText
                )
                updateNotification()
                delay(2000L)
            }

            _engineState.value = _engineState.value.copy(
                currentPack = rainPack,
                lastMessage = "Дождевой пак: $rainPack"
            )
            updateNotification()

            textureEngine.applyPack(
                trackKey = trackKey,
                packName = rainPack,
                isRain = true,
                forceSound = _engineState.value.isForceSound,
                startX = _engineState.value.startX,
                startY = _engineState.value.startY,
                endX = _engineState.value.endX,
                endY = _engineState.value.endY,
                tapX = _engineState.value.startX,
                tapY = _engineState.value.startY,
                swipeCount = _engineState.value.swipeCount,
                swipeDurationMs = _engineState.value.swipeDurationMs,
                swipePauseMs = _engineState.value.swipePauseMs,
                clickMethod = _engineState.value.clickMethod
            ) { msg ->
                _engineState.value = _engineState.value.copy(lastMessage = msg)
            }

            _engineState.value = _engineState.value.copy(
                totalReplacements = _engineState.value.totalReplacements + 1,
                lastActionTime = System.currentTimeMillis()
            )

            // Anti-farming check: award +1 Nd only when session is active and isEmulatorActive == true
            val isSessionActive = _engineState.value.status == EngineStatus.RUNNING || _engineState.value.status == EngineStatus.RAIN_SEQUENCE
            if (isSessionActive && isEmulatorActive) {
                incrementNdBalance(applicationContext)
            } else {
                AppLogger.d("Дождевой этап завершен без начисления Nd (сессия: $isSessionActive, эмулятор: $isEmulatorActive)")
            }

            // Hold for each rain stage
            holdCountdown(_engineState.value.cycleIntervalSeconds)
        }

        _engineState.value = _engineState.value.copy(
            status = EngineStatus.RUNNING,
            isRainActive = false
        )
        updateNotification()
    }

    private suspend fun holdCountdown(totalSeconds: Int) {
        for (sec in totalSeconds downTo 1) {
            _engineState.value = _engineState.value.copy(countdownSeconds = sec)
            if (sec % 5 == 0 || sec == 1) {
                updateNotification()
            }
            delay(1000)
        }
        _engineState.value = _engineState.value.copy(countdownSeconds = 0)
    }

    private fun acquireWakeLock() {
        try {
            if (wakeLock == null) {
                val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
                wakeLock = powerManager.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK,
                    "NDCYCLE:EngineWakeLock"
                ).apply {
                    setReferenceCounted(false)
                }
            }
            if (wakeLock?.isHeld == false) {
                wakeLock?.acquire(3600000L) // 1 hour safety timeout
                AppLogger.s("WakeLock захвачен (PARTIAL_WAKE_LOCK)")
            }
        } catch (e: Exception) {
            AppLogger.e("Ошибка захвата WakeLock", e)
        }
    }

    private fun releaseWakeLock() {
        try {
            if (wakeLock?.isHeld == true) {
                wakeLock?.release()
                AppLogger.i("WakeLock освобожден")
            }
        } catch (e: Exception) {
            AppLogger.w("Ошибка освобождения WakeLock: ${e.message}")
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.notification_channel_description)
                setShowBadge(false)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        val state = _engineState.value
        val track = TrackDictionary.getTrack(state.currentTrackKey)

        val title = when (state.status) {
            EngineStatus.RUNNING -> "● Движок активен"
            EngineStatus.RAIN_SEQUENCE -> "⛈ Дождевой цикл активен"
            EngineStatus.COPYING -> "⏳ Идет копирование текстур..."
            EngineStatus.PAUSED -> "⏸ На паузе"
            EngineStatus.STOPPED -> "⏹ Остановлен"
        }

        val content = "Трасса: ${track.name} | Пак: ${state.currentPack} | Смена: ${state.countdownSeconds}с"

        val openAppIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Action 1: Start/Pause
        val pauseResumeTitle = if (state.status == EngineStatus.PAUSED) "Возобновить" else "Пауза"
        val togglePauseIntent = PendingIntent.getService(
            this,
            1,
            Intent(this, NDCycleService::class.java).apply {
                action = ACTION_TOGGLE_PAUSE
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Action 2: Next Track
        val nextTrackIntent = PendingIntent.getService(
            this,
            2,
            Intent(this, NDCycleService::class.java).apply {
                action = ACTION_NEXT_TRACK
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Action 3: Stop & Bring App to front with AssemblyService Warning Dialog
        val stopAppIntent = Intent(this, MainActivity::class.java).apply {
            action = ACTION_NOTIFICATION_STOP
            putExtra(EXTRA_SHOW_WARNING, true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val stopPendingIntent = PendingIntent.getActivity(
            this,
            3,
            stopAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(openAppIntent)
            .setOngoing(state.status != EngineStatus.STOPPED)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(
                android.R.drawable.ic_media_pause,
                pauseResumeTitle,
                togglePauseIntent
            )
            .addAction(
                android.R.drawable.ic_media_next,
                "След. трасса",
                nextTrackIntent
            )
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "Стоп",
                stopPendingIntent
            )

        return builder.build()
    }

    private fun startForegroundWithNotification() {
        val notification = buildNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceCompat.startForeground(
                this,
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun updateNotification() {
        if (_engineState.value.status != EngineStatus.STOPPED) {
            val notification = buildNotification()
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(NOTIFICATION_ID, notification)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopEngine()
        instance = null
        AppLogger.i("NDCycleService уничтожен")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val CHANNEL_ID = "ndcycle_foreground_channel"
        const val NOTIFICATION_ID = 10101

        const val ACTION_START = "com.example.ndcycle.START"
        const val ACTION_PAUSE = "com.example.ndcycle.PAUSE"
        const val ACTION_RESUME = "com.example.ndcycle.RESUME"
        const val ACTION_TOGGLE_PAUSE = "com.example.ndcycle.TOGGLE_PAUSE"
        const val ACTION_NEXT_TRACK = "com.example.ndcycle.NEXT_TRACK"
        const val ACTION_SET_TRACK = "com.example.ndcycle.SET_TRACK"
        const val ACTION_APPLY_SINGLE_PACK = "com.example.ndcycle.APPLY_PACK"
        const val ACTION_STOP_SERVICE = "com.example.ndcycle.STOP"
        const val ACTION_NOTIFICATION_STOP = "com.example.ndcycle.NOTIFICATION_STOP"

        const val EXTRA_RANDOM_MODE = "EXTRA_RANDOM_MODE"
        const val EXTRA_TRACK_KEY = "EXTRA_TRACK_KEY"
        const val EXTRA_FORCE_SOUND = "EXTRA_FORCE_SOUND"
        const val EXTRA_PACK_NAME = "EXTRA_PACK_NAME"
        const val EXTRA_IS_RAIN = "EXTRA_IS_RAIN"
        const val EXTRA_SHOW_WARNING = "EXTRA_SHOW_WARNING"

        const val PREFS_NAME = "ndcycle_app_prefs"
        const val KEY_ND_BALANCE = "key_nd_balance"
        const val EMULATOR_PACKAGE_NAME = "org.ppsspp.ppsspp"

        private val _engineState = MutableStateFlow(EngineState())
        val engineState: StateFlow<EngineState> = _engineState.asStateFlow()

        @Volatile
        var isEmulatorActive: Boolean = false
            private set

        @Volatile
        var instance: NDCycleService? = null
            private set

        fun updateEmulatorActive(active: Boolean) {
            if (isEmulatorActive != active) {
                isEmulatorActive = active
                instance?.isEmulatorActive = active
                _engineState.value = _engineState.value.copy(isEmulatorActive = active)
                AppLogger.i("Статус эмулятора $EMULATOR_PACKAGE_NAME: ${if (active) "АКТИВЕН" else "НЕАКТИВЕН"}")
            }
        }

        fun updateIdleAndGtState(
            isIdle: Boolean,
            isGtIcon: Boolean,
            isGamePaused: Boolean = false,
            isMainMenu: Boolean = false,
            isRealGameplayConfirmed: Boolean = (!isIdle && !isGtIcon && !isGamePaused && !isMainMenu)
        ) {
            _engineState.value = _engineState.value.copy(
                isIdleScreenDetected = isIdle,
                isGtIconScreenDetected = isGtIcon,
                isGamePausedDetected = isGamePaused,
                isMainMenuDetected = isMainMenu,
                isRealGameplayConfirmed = isRealGameplayConfirmed
            )
        }

        fun incrementNdBalance(context: Context) {
            if (_engineState.value.isGameplayBlocked || !_engineState.value.isRealGameplayConfirmed) {
                val reason = when {
                    _engineState.value.isGamePausedDetected -> "игра на паузе / меню настроек"
                    _engineState.value.isMainMenuDetected -> "главное меню PPSSPP"
                    _engineState.value.isGtIconScreenDetected -> "значок GT в меню"
                    _engineState.value.isIdleScreenDetected -> "idle статика"
                    else -> "нет подтверждения реального игрового процесса"
                }
                AppLogger.d("⚠️ Защита от накрутки: начисление валюты Nd заблокировано ($reason)")
                return
            }
            addNdBalance(context, 1, "Завершение этапа в эмуляторе")
        }

        fun addNdBalance(context: Context, amount: Int, reason: String = "Бонус") {
            if (_engineState.value.isGameplayBlocked || !_engineState.value.isRealGameplayConfirmed) {
                val cause = when {
                    _engineState.value.isGamePausedDetected -> "игра на паузе / меню настроек"
                    _engineState.value.isMainMenuDetected -> "главное меню PPSSPP"
                    _engineState.value.isGtIconScreenDetected -> "значок GT в меню"
                    _engineState.value.isIdleScreenDetected -> "idle статика"
                    else -> "нет подтверждения реального игрового процесса"
                }
                AppLogger.d("⚠️ Защита от накрутки: начисление валюты Nd заблокировано ($cause)")
                return
            }
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val current = prefs.getInt(KEY_ND_BALANCE, 0)
            val newBalance = current + amount
            prefs.edit().putInt(KEY_ND_BALANCE, newBalance).apply()
            _engineState.value = _engineState.value.copy(ndBalance = newBalance)
            AppLogger.s("🎉 $reason: +$amount Nd! Баланс: $newBalance Nd")
        }

        fun getNdBalance(context: Context): Int {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val balance = prefs.getInt(KEY_ND_BALANCE, 0)
            _engineState.value = _engineState.value.copy(ndBalance = balance)
            return balance
        }

        fun updateCycleInterval(seconds: Int) {
            _engineState.value = _engineState.value.copy(cycleIntervalSeconds = seconds)
            AppLogger.i("Интервал цикла: $seconds сек")
        }

        fun updateRainProbability(percent: Int) {
            _engineState.value = _engineState.value.copy(rainProbabilityPercent = percent)
            AppLogger.i("Вероятность дождя: $percent%")
        }

        fun updateSwipeCount(count: Int) {
            val safeCount = count.coerceIn(1, 20)
            _engineState.value = _engineState.value.copy(swipeCount = safeCount)
            AppLogger.i("Количество микро-свайпов: $safeCount")
        }

        fun updateSwipeDuration(durationMs: Long) {
            val safeDuration = durationMs.coerceIn(10L, 1000L)
            _engineState.value = _engineState.value.copy(swipeDurationMs = safeDuration)
            AppLogger.i("Скорость микро-свайпа: ${safeDuration}мс")
        }

        fun updateSwipePause(pauseMs: Long) {
            val safePause = pauseMs.coerceIn(50L, 2000L)
            _engineState.value = _engineState.value.copy(swipePauseMs = safePause)
            AppLogger.i("Пауза между микро-свайпами: ${safePause}мс")
        }

        fun updateRandomMode(randomMode: Boolean) {
            _engineState.value = _engineState.value.copy(isRandomMode = randomMode)
            AppLogger.i("Режим переключения: ${if (randomMode) "Случайный" else "Последовательный"}")
        }

        fun updateForceSound(forceSound: Boolean) {
            _engineState.value = _engineState.value.copy(isForceSound = forceSound)
            AppLogger.i("Звуковые эффекты: ${if (forceSound) "Включены" else "Выключены"}")
        }

        fun updateClickMethod(method: ClickMethod) {
            _engineState.value = _engineState.value.copy(clickMethod = method)
            AppLogger.i("Метод кликера изменен на: ${method.displayName}")
        }

        fun updateGestureCoordinates(startX: Int, startY: Int, endX: Int, endY: Int) {
            _engineState.value = _engineState.value.copy(
                startX = startX,
                startY = startY,
                endX = endX,
                endY = endY,
                tapX = startX,
                tapY = startY
            )
            AppLogger.i("Координаты жеста: ($startX, $startY) -> ($endX, $endY)")
        }

        fun updateTrackKey(trackKey: String) {
            val track = TrackDictionary.getTrack(trackKey)
            _engineState.value = _engineState.value.copy(
                currentTrackKey = trackKey,
                lastMessage = "Трасса изменена на: ${track.name}"
            )
            AppLogger.i("Активная трасса обновлена: ${track.name} ($trackKey)")
            instance?.updateNotification()
        }

        fun updatePackIfMatching(oldPack: String, newPack: String) {
            if (_engineState.value.currentPack.equals(oldPack, ignoreCase = true)) {
                _engineState.value = _engineState.value.copy(
                    currentPack = newPack,
                    lastMessage = "Активный пак обновлен: $newPack"
                )
                AppLogger.i("Активный пак обновлен: $oldPack -> $newPack")
                instance?.updateNotification()
            }
        }

        fun updateSinglePackState(packName: String, isRain: Boolean) {
            _engineState.value = _engineState.value.copy(
                status = EngineStatus.COPYING,
                currentPack = packName,
                isRainActive = isRain,
                lastMessage = "Ручная подмена: $packName"
            )
        }

        fun updateLastMessage(msg: String) {
            _engineState.value = _engineState.value.copy(lastMessage = msg)
        }

        fun incrementReplacements() {
            _engineState.value = _engineState.value.copy(
                totalReplacements = _engineState.value.totalReplacements + 1,
                lastActionTime = System.currentTimeMillis()
            )
        }

        fun resetCopyingStatus() {
            if (_engineState.value.status == EngineStatus.COPYING) {
                _engineState.value = _engineState.value.copy(
                    status = if (instance?.workerJob?.isActive == true) EngineStatus.RUNNING else EngineStatus.STOPPED
                )
            }
        }

        fun restoreSettings(
            interval: Int,
            rainProb: Int,
            swipeCount: Int,
            swipeDuration: Long,
            swipePause: Long,
            randomMode: Boolean,
            forceSound: Boolean,
            startX: Int,
            startY: Int,
            endX: Int,
            endY: Int,
            clickMethod: ClickMethod,
            trackKey: String
        ) {
            _engineState.value = _engineState.value.copy(
                cycleIntervalSeconds = interval,
                rainProbabilityPercent = rainProb,
                swipeCount = swipeCount,
                swipeDurationMs = swipeDuration,
                swipePauseMs = swipePause,
                isRandomMode = randomMode,
                isForceSound = forceSound,
                startX = startX,
                startY = startY,
                endX = endX,
                endY = endY,
                tapX = startX,
                tapY = startY,
                clickMethod = clickMethod,
                currentTrackKey = trackKey,
                currentPackIndex = 0,
                countdownSeconds = interval
            )
            AppLogger.i("Параметры успешно восстановлены: трасса $trackKey")
        }
    }
}
