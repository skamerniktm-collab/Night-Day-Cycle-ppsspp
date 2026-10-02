package com.example.service

import com.example.model.ClickMethod
import com.example.util.AppLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.DataOutputStream

object AssemblyService {
    const val DEFAULT_TAP_X = 1169
    const val DEFAULT_TAP_Y = 25
    const val DEFAULT_SWIPE_DELTA_PX = 25
    const val DEFAULT_REPETITIONS = 2
    const val DEFAULT_DURATION_MS = 50L
    const val DEFAULT_PAUSE_MS = 300L

    private val scope = CoroutineScope(Dispatchers.IO)

    fun triggerSwipeSequence(
        startX: Int = DEFAULT_TAP_X,
        startY: Int = DEFAULT_TAP_Y,
        endX: Int = startX + DEFAULT_SWIPE_DELTA_PX,
        endY: Int = startY,
        swipeCount: Int = DEFAULT_REPETITIONS,
        durationMs: Long = DEFAULT_DURATION_MS,
        pauseMs: Long = DEFAULT_PAUSE_MS,
        preferredMethod: ClickMethod = ClickMethod.AUTO
    ) {
        scope.launch {
            AppLogger.i("AssemblyService: Запуск микро-свайпов ($startX, $startY) -> ($endX, $endY) x$swipeCount, ${durationMs}мс [Режим: ${preferredMethod.name}]")

            for (i in 1..swipeCount) {
                val success = executeSingleSwipe(startX, startY, endX, endY, durationMs, preferredMethod)
                AppLogger.i("AssemblyService: Микро-свайп #$i/${swipeCount} ${if (success) "успешно" else "выполнен с предупреждением"}")
                if (i < swipeCount) {
                    delay(pauseMs)
                }
            }
        }
    }

    fun triggerSwipeByDelta(
        startX: Int,
        startY: Int,
        deltaX: Int,
        deltaY: Int,
        swipeCount: Int = DEFAULT_REPETITIONS,
        durationMs: Long = DEFAULT_DURATION_MS,
        pauseMs: Long = DEFAULT_PAUSE_MS,
        preferredMethod: ClickMethod = ClickMethod.AUTO
    ) {
        triggerSwipeSequence(
            startX = startX,
            startY = startY,
            endX = startX + deltaX,
            endY = startY + deltaY,
            swipeCount = swipeCount,
            durationMs = durationMs,
            pauseMs = pauseMs,
            preferredMethod = preferredMethod
        )
    }

    fun triggerClickSequence(
        tapX: Int = DEFAULT_TAP_X,
        tapY: Int = DEFAULT_TAP_Y,
        repetitions: Int = DEFAULT_REPETITIONS,
        durationMs: Long = DEFAULT_DURATION_MS,
        pauseMs: Long = DEFAULT_PAUSE_MS,
        preferredMethod: ClickMethod = ClickMethod.AUTO
    ) {
        triggerSwipeSequence(
            startX = tapX,
            startY = tapY,
            endX = tapX + DEFAULT_SWIPE_DELTA_PX,
            endY = tapY,
            swipeCount = repetitions,
            durationMs = durationMs,
            pauseMs = pauseMs,
            preferredMethod = preferredMethod
        )
    }

    private suspend fun executeSingleSwipe(
        x1: Int,
        y1: Int,
        x2: Int,
        y2: Int,
        durationMs: Long,
        preferredMethod: ClickMethod
    ): Boolean {
        return when (preferredMethod) {
            ClickMethod.ACCESSIBILITY -> swipeViaAccessibility(x1, y1, x2, y2, durationMs)
            ClickMethod.ROOT -> swipeViaRoot(x1, y1, x2, y2, durationMs)
            ClickMethod.SHELL -> swipeViaShell(x1, y1, x2, y2, durationMs)
            ClickMethod.AUTO -> {
                if (AssemblyAccessibilityService.isServiceEnabled) {
                    swipeViaAccessibility(x1, y1, x2, y2, durationMs)
                } else if (isRootAvailable()) {
                    swipeViaRoot(x1, y1, x2, y2, durationMs)
                } else {
                    swipeViaShell(x1, y1, x2, y2, durationMs)
                }
            }
        }
    }

    private fun swipeViaAccessibility(x1: Int, y1: Int, x2: Int, y2: Int, durationMs: Long): Boolean {
        val service = AssemblyAccessibilityService.instance
        if (service != null) {
            val dispatched = service.performSwipe(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat(), durationMs)
            if (dispatched) {
                AppLogger.s("Свайп: Отправлен через AccessibilityService ($x1, $y1)->($x2, $y2) [${durationMs}мс]")
                return true
            }
        }
        AppLogger.w("Свайп: Служба Accessibility недоступна, переход к резервным методам")
        return false
    }

    private fun swipeViaRoot(x1: Int, y1: Int, x2: Int, y2: Int, durationMs: Long): Boolean {
        return try {
            val process = Runtime.getRuntime().exec("su")
            val os = DataOutputStream(process.outputStream)
            val command = "input swipe $x1 $y1 $x2 $y2 $durationMs\n"
            os.writeBytes(command)
            os.writeBytes("exit\n")
            os.flush()
            val exitCode = process.waitFor()
            if (exitCode == 0) {
                AppLogger.s("Свайп: Отправлен через Root (su) ($x1, $y1)->($x2, $y2) [${durationMs}мс]")
                true
            } else {
                AppLogger.w("Свайп: Root вернул код $exitCode")
                false
            }
        } catch (e: Exception) {
            AppLogger.w("Свайп: Ошибка Root свайпа: ${e.message}")
            false
        }
    }

    private fun swipeViaShell(x1: Int, y1: Int, x2: Int, y2: Int, durationMs: Long): Boolean {
        return try {
            val cmd = arrayOf("sh", "-c", "input swipe $x1 $y1 $x2 $y2 $durationMs")
            val process = Runtime.getRuntime().exec(cmd)
            process.waitFor()
            AppLogger.i("Свайп: Отправлен через Shell команду ($x1, $y1)->($x2, $y2) [${durationMs}мс]")
            true
        } catch (e: Exception) {
            AppLogger.e("Свайп: Ошибка выполнения Shell команды", e)
            false
        }
    }

    fun isRootAvailable(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", "id"))
            val exitCode = process.waitFor()
            exitCode == 0
        } catch (e: Exception) {
            false
        }
    }
}
