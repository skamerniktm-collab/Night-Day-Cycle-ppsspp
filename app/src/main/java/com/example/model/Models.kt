package com.example.model

import java.util.concurrent.atomic.AtomicLong

enum class EngineStatus {
    STOPPED,
    RUNNING,
    PAUSED,
    COPYING,
    RAIN_SEQUENCE
}

enum class ClickMethod(val displayName: String) {
    AUTO("Автовыбор (Доступность -> Root -> Shell)"),
    ACCESSIBILITY("Служба доступности (Accessibility)"),
    ROOT("Суперпользователь (Root / su)"),
    SHELL("Команда оболочки (Shell input)")
}

data class TrackInfo(
    val key: String,
    val name: String,
    val normalPath: String,
    val rainPath: String,
    val location: String = ""
)

private val logIdGenerator = AtomicLong(1L)

data class LogEntry(
    val id: Long = logIdGenerator.getAndIncrement(),
    val timestamp: String,
    val level: LogLevel,
    val message: String
)

enum class LogLevel {
    INFO,
    SUCCESS,
    WARN,
    ERROR
}

data class EngineState(
    val status: EngineStatus = EngineStatus.STOPPED,
    val isRandomMode: Boolean = false,
    val isForceSound: Boolean = false,
    val cycleIntervalSeconds: Int = 15,
    val rainProbabilityPercent: Int = 20,
    val currentTrackKey: String = "SARTE",
    val currentPack: String = "Ожидание",
    val isRainActive: Boolean = false,
    val countdownSeconds: Int = 15,
    val currentPackIndex: Int = 0,
    val totalReplacements: Int = 0,
    val lastActionTime: Long = 0L,
    val lastMessage: String = "Движок готов к запуску",
    val startX: Int = 1169,
    val startY: Int = 25,
    val endX: Int = 1194,
    val endY: Int = 25,
    val tapX: Int = 1169,
    val tapY: Int = 25,
    val swipeCount: Int = 2,
    val swipeDurationMs: Long = 50L,
    val swipePauseMs: Long = 300L,
    val clickMethod: ClickMethod = ClickMethod.AUTO,
    val isEmulatorActive: Boolean = false,
    val ndBalance: Int = 0,
    val isIdleScreenDetected: Boolean = false,
    val isGtIconScreenDetected: Boolean = false,
    val isGamePausedDetected: Boolean = false,
    val isMainMenuDetected: Boolean = false,
    val isRealGameplayConfirmed: Boolean = false,
    val lastScreenCheckTime: Long = 0L
) {
    val isGameplayBlocked: Boolean
        get() = !isRealGameplayConfirmed || isMainMenuDetected || isIdleScreenDetected || isGtIconScreenDetected || isGamePausedDetected
}

enum class SplashStepStatus {
    PENDING,
    IN_PROGRESS,
    SUCCESS,
    WARNING
}

data class SplashCheckStep(
    val id: String,
    val title: String,
    val detail: String = "",
    val status: SplashStepStatus = SplashStepStatus.PENDING
)

