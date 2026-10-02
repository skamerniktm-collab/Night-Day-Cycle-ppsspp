package com.example.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.accessibility.AccessibilityEvent
import com.example.util.AppLogger
import com.example.util.GtScreenDetector

class AssemblyAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        AppLogger.s("Служба специальных возможностей AssemblyService подключена")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val packageName = event?.packageName?.toString() ?: return
        if (packageName == "org.ppsspp.ppsspp") {
            NDCycleService.updateEmulatorActive(true)
            val eventType = event.eventType
            if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED ||
                eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED ||
                eventType == AccessibilityEvent.TYPE_TOUCH_INTERACTION_START ||
                eventType == AccessibilityEvent.TYPE_GESTURE_DETECTION_START
            ) {
                GtScreenDetector.notifyUserActivity()
            }
        } else if (packageName.isNotEmpty() && packageName != "android" && packageName != "com.android.systemui") {
            NDCycleService.updateEmulatorActive(false)
        }
    }

    override fun onInterrupt() {
        AppLogger.w("Служба специальных возможностей прервана")
        NDCycleService.updateEmulatorActive(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (instance == this) {
            instance = null
        }
        NDCycleService.updateEmulatorActive(false)
        AppLogger.i("Служба специальных возможностей отключена")
    }

    fun performTap(x: Float, y: Float, durationMs: Long = 50L, callback: ((Boolean) -> Unit)? = null): Boolean {
        return performSwipe(x, y, x, y, durationMs, callback)
    }

    fun performSwipe(
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
        durationMs: Long = 50L,
        callback: ((Boolean) -> Unit)? = null
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val swipePath = Path().apply {
                moveTo(startX, startY)
                lineTo(endX, endY)
            }
            val gesture = GestureDescription.Builder()
                .addStroke(GestureDescription.StrokeDescription(swipePath, 0, durationMs.coerceAtLeast(1L)))
                .build()

            return dispatchGesture(gesture, object : GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription?) {
                    super.onCompleted(gestureDescription)
                    GtScreenDetector.notifyUserActivity()
                    callback?.invoke(true)
                }

                override fun onCancelled(gestureDescription: GestureDescription?) {
                    super.onCancelled(gestureDescription)
                    callback?.invoke(false)
                }
            }, null)
        }
        return false
    }

    fun captureScreen(callback: (Bitmap?) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            takeScreenshot(
                Display.DEFAULT_DISPLAY,
                mainExecutor,
                object : TakeScreenshotCallback {
                    override fun onSuccess(screenshot: ScreenshotResult) {
                        try {
                            val hardwareBuffer = screenshot.hardwareBuffer
                            val bitmap = Bitmap.wrapHardwareBuffer(hardwareBuffer, screenshot.colorSpace)
                            hardwareBuffer.close()
                            callback(bitmap)
                        } catch (e: Throwable) {
                            callback(null)
                        }
                    }

                    override fun onFailure(errorCode: Int) {
                        callback(null)
                    }
                }
            )
        } else {
            callback(null)
        }
    }

    companion object {
        @Volatile
        var instance: AssemblyAccessibilityService? = null
            private set

        val isServiceEnabled: Boolean
            get() = instance != null
    }
}
