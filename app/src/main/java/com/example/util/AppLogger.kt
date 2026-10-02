package com.example.util

import android.util.Log
import com.example.model.LogEntry
import com.example.model.LogLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AppLogger {
    private const val TAG = "NDCYCLE"
    private val timeFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

    private val _logs = MutableStateFlow<List<LogEntry>>(emptyList())
    val logs: StateFlow<List<LogEntry>> = _logs.asStateFlow()

    fun i(message: String) {
        Log.i(TAG, message)
        append(LogLevel.INFO, message)
    }

    fun d(message: String) {
        Log.d(TAG, message)
        append(LogLevel.INFO, message)
    }

    fun s(message: String) {
        Log.i(TAG, "SUCCESS: $message")
        append(LogLevel.SUCCESS, message)
    }

    fun w(message: String) {
        Log.w(TAG, message)
        append(LogLevel.WARN, message)
    }

    fun e(message: String, throwable: Throwable? = null) {
        val fullMsg = if (throwable != null) "$message: ${throwable.localizedMessage}" else message
        Log.e(TAG, fullMsg, throwable)
        append(LogLevel.ERROR, fullMsg)
    }

    private fun append(level: LogLevel, message: String) {
        val time = timeFormat.format(Date())
        val entry = LogEntry(timestamp = time, level = level, message = message)
        synchronized(this) {
            val current = _logs.value.toMutableList()
            if (current.size > 200) {
                current.removeAt(0)
            }
            current.add(entry)
            _logs.value = current
        }
    }

    fun clear() {
        synchronized(this) {
            _logs.value = emptyList()
        }
    }
}
