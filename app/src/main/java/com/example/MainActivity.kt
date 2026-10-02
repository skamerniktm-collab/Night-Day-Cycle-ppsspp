package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.service.NDCycleService
import com.example.ui.MainViewModel
import com.example.ui.screens.MainScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.NDCycleTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleIntent(intent)
        setContent {
            val currentTheme by viewModel.appTheme.collectAsState()
            val isSplashComplete by viewModel.isSplashComplete.collectAsState()

            NDCycleTheme(themeMode = currentTheme) {
                Crossfade(
                    targetState = isSplashComplete,
                    animationSpec = tween(durationMillis = 400),
                    label = "splash_crossfade"
                ) { completed ->
                    if (!completed) {
                        SplashScreen(viewModel = viewModel)
                    } else {
                        MainScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent == null) return
        val action = intent.action
        val showWarning = intent.getBooleanExtra(NDCycleService.EXTRA_SHOW_WARNING, false)

        if (action == NDCycleService.ACTION_NOTIFICATION_STOP || showWarning) {
            val shouldShowDialog = viewModel.handleStopFromNotification()
            if (!shouldShowDialog) {
                finish()
            }
        }
    }
}


