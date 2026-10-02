package com.example.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.model.ControlsPosition
import com.example.ui.MainViewModel
import com.example.ui.components.AchievementToaster
import com.example.ui.components.AssemblyWarningDialog
import com.example.ui.components.ControlsSection
import com.example.ui.components.CustomWallpaperCanvas
import com.example.ui.components.EngineStatusHeader
import com.example.ui.components.LogsTerminalCard
import com.example.ui.components.ManualCoordinatesCard
import com.example.ui.components.SystemPermissionsCard
import com.example.ui.components.TexturePackMatrix
import com.example.ui.components.TrackSelectorCard
import com.example.ui.components.UninstallConfirmDialog
import com.example.ui.theme.ImmersiveBackground
import com.example.ui.theme.ImmersiveCardBorder
import com.example.ui.theme.ImmersiveCardBorderSubtle
import com.example.ui.theme.ImmersiveLilac
import com.example.ui.theme.ImmersiveLilacContainer
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.util.PermissionHelper
import kotlinx.coroutines.launch

enum class MainTab(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    DASHBOARD("Панель", Icons.Default.Dashboard),
    TRACKS("Трассы", Icons.Default.Extension),
    SHOP("Магазин", Icons.Default.ShoppingCart),
    SYSTEM("Настройки", Icons.Default.Settings),
    THEMES("Темы", Icons.Default.Palette),
    EXPERIMENTS("Эксперименты", Icons.Default.Science),
    TERMINAL("Терминал", Icons.Default.Terminal),
    EDITOR("Редактор", Icons.Default.Edit)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val engineState by viewModel.engineState.collectAsState()
    val tracks by viewModel.tracks.collectAsState()
    val normalPacks by viewModel.normalPacks.collectAsState()
    val rainPacks by viewModel.rainPacks.collectAsState()
    val dayPacks by viewModel.dayPacks.collectAsState()
    val logs by viewModel.logs.collectAsState()
    val permissions by viewModel.permissions.collectAsState()
    val isGeneratingData by viewModel.isGeneratingData.collectAsState()
    val warningShowCount by viewModel.warningShowCount.collectAsState()
    val showAssemblyWarningEvent by viewModel.showAssemblyWarningEvent.collectAsState()
    val isVersionBonusClaimed by viewModel.isVersionBonusClaimed.collectAsState()
    val controlsPosition by viewModel.controlsPosition.collectAsState()
    val customWallpaper by viewModel.customDrawingWallpaper.collectAsState()

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var showExitWarningDialog by rememberSaveable { mutableStateOf(false) }
    var showUninstallConfirmDialog by rememberSaveable { mutableStateOf(false) }

    var versionTapCount by remember { mutableIntStateOf(0) }
    var lastVersionTapTime by remember { mutableStateOf(0L) }

    val activity = context as? Activity

    // Intercept back navigation / gesture when AssemblyService is active and warning count < MAX_WARNING_COUNT (3)
    BackHandler {
        if (permissions.isAccessibilityActive && warningShowCount < MainViewModel.MAX_WARNING_COUNT) {
            showExitWarningDialog = true
        } else {
            activity?.finish()
        }
    }

    // Refresh permissions on resume
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshPermissions(isFromUserAction = true)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Permission request launchers
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.refreshPermissions(isFromUserAction = isGranted)
    }

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val isAnyGranted = results.values.any { it }
        viewModel.refreshPermissions(isFromUserAction = isAnyGranted)
        coroutineScope.launch {
            if (isAnyGranted) {
                snackbarHostState.showSnackbar("Доступ к памяти разрешён")
            } else {
                snackbarHostState.showSnackbar("Доступ к памяти отклонён")
            }
        }
    }

    val achievementPopup by viewModel.achievementPopup.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "NDCYCLE",
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ImmersiveSurface
                ),
                actions = {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (versionTapCount > 0) ImmersiveLilac.copy(alpha = (versionTapCount * 0.04f).coerceAtMost(0.35f)) else Color.Transparent,
                        border = BorderStroke(
                            1.dp,
                            if (versionTapCount > 0) ImmersiveLilac.copy(alpha = 0.7f) else ImmersiveLilac.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                val currentTime = System.currentTimeMillis()
                                if (currentTime - lastVersionTapTime > 850L) {
                                    versionTapCount = 1
                                } else {
                                    versionTapCount++
                                }
                                lastVersionTapTime = currentTime

                                if (versionTapCount >= 9) {
                                    versionTapCount = 0
                                    val success = viewModel.claimVersionBonus(250)
                                    coroutineScope.launch {
                                        if (success) {
                                            snackbarHostState.showSnackbar("🎁 Секретный бонус активирован! +250 Nd добавлено к балансу")
                                        } else {
                                            snackbarHostState.showSnackbar("ℹ️ Секретный бонус версии (+250 Nd) уже был получен ранее")
                                        }
                                    }
                                }
                            }
                            .testTag("app_version_badge")
                    ) {
                        Text(
                            text = "v1.20.4 • PPSSPP",
                            color = ImmersiveLilac,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            val glowTransition = rememberInfiniteTransition(label = "bottom_bar_icon_glow")
            val glowPulse by glowTransition.animateFloat(
                initialValue = 0.50f,
                targetValue = 1.0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1300, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "bottom_bar_glow_pulse"
            )

            Surface(
                color = ImmersiveSurfaceVariant,
                tonalElevation = 8.dp,
                shadowElevation = 12.dp,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                border = BorderStroke(1.2.dp, ImmersiveCardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(20f)
                    .testTag("bottom_nav_bar")
            ) {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    val itemWidth = maxWidth / 4
                    val navScrollState = rememberScrollState()
                    val activeColor = ImmersiveLilac
                    val inactiveColor = TextSecondary.copy(alpha = 0.65f)
                    val textInactiveColor = TextSecondary

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(66.dp)
                            .horizontalScroll(navScrollState)
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MainTab.values().forEachIndexed { index, tab ->
                            val isSelected = selectedTab == index
                            Box(
                                modifier = Modifier
                                    .width(itemWidth)
                                    .fillMaxHeight()
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        selectedTab = index
                                    }
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                                    .testTag("tab_${tab.name.lowercase()}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    // Burning / glowing icon (NO background podlozhka, NO pill container)
                                    Box(
                                        modifier = Modifier.size(28.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isSelected) {
                                            // Soft radiant aura bloom (glow without any background oval)
                                            Canvas(modifier = Modifier.size(38.dp)) {
                                                drawCircle(
                                                    brush = Brush.radialGradient(
                                                        colors = listOf(
                                                            activeColor.copy(alpha = 0.45f * glowPulse),
                                                            activeColor.copy(alpha = 0.18f * glowPulse),
                                                            Color.Transparent
                                                        ),
                                                        center = center,
                                                        radius = size.width * 0.5f
                                                    )
                                                )
                                            }

                                            // Burning halo silhouette of the icon
                                            Icon(
                                                imageVector = tab.icon,
                                                contentDescription = null,
                                                tint = activeColor.copy(alpha = 0.55f * glowPulse),
                                                modifier = Modifier
                                                    .size(22.dp)
                                                    .graphicsLayer {
                                                        scaleX = 1.18f
                                                        scaleY = 1.18f
                                                    }
                                            )
                                        }

                                        // Primary icon
                                        Icon(
                                            imageVector = tab.icon,
                                            contentDescription = tab.title,
                                            tint = if (isSelected) activeColor else inactiveColor,
                                            modifier = Modifier.size(if (isSelected) 22.dp else 20.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(3.dp))

                                    Text(
                                        text = tab.title,
                                        color = if (isSelected) activeColor else textInactiveColor,
                                        fontSize = 11.sp,
                                        maxLines = 1,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        containerColor = ImmersiveBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background Wallpaper Layer: render custom user artwork if set
            customWallpaper?.let { wallpaper ->
                if (wallpaper.strokes.isNotEmpty()) {
                    CustomWallpaperCanvas(
                        wallpaper = wallpaper,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.TopCenter
            ) {
                AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "tab_content_transition",
                modifier = Modifier.fillMaxSize()
            ) { tabIndex ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    when (tabIndex) {
                        0 -> {
                            // 0: DASHBOARD (Панель управления)
                            val controlsSectionBlock = @Composable {
                                ControlsSection(
                                    state = engineState,
                                    onStart = {
                                        if (!permissions.hasStorage) {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Предоставьте доступ к файлам в разделе 'Права'")
                                            }
                                        }
                                        viewModel.startEngine()
                                    },
                                    onTogglePause = { viewModel.togglePause() },
                                    onStop = { viewModel.stopEngine() },
                                    onNextTrack = { viewModel.nextTrack() }
                                )
                            }

                            when (controlsPosition) {
                                ControlsPosition.TOP -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .widthIn(max = 720.dp)
                                            .verticalScroll(rememberScrollState())
                                            .padding(horizontal = 16.dp, vertical = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        EngineStatusHeader(
                                            state = engineState,
                                            contentBelowStandby = {
                                                controlsSectionBlock()
                                            }
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }

                                ControlsPosition.BOTTOM,
                                ControlsPosition.DRAWING -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .widthIn(max = 720.dp)
                                            .verticalScroll(rememberScrollState())
                                            .padding(horizontal = 16.dp, vertical = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        EngineStatusHeader(state = engineState)
                                        controlsSectionBlock()
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }

                                ControlsPosition.DOCKED_BOTTOM -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .widthIn(max = 720.dp),
                                        contentAlignment = Alignment.TopCenter
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .verticalScroll(rememberScrollState())
                                                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 120.dp),
                                            verticalArrangement = Arrangement.spacedBy(16.dp)
                                        ) {
                                            EngineStatusHeader(state = engineState)
                                        }

                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.BottomCenter)
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp, vertical = 12.dp)
                                        ) {
                                            controlsSectionBlock()
                                        }
                                    }
                                }
                            }
                        }

                        1 -> {
                            // 1: TRACKS & PACKS (Трассы и Паки)
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .widthIn(max = 720.dp)
                                    .verticalScroll(rememberScrollState())
                                    .padding(horizontal = 16.dp, vertical = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                TrackSelectorCard(
                                    currentTrackKey = engineState.currentTrackKey,
                                    tracks = tracks,
                                    onSelectTrack = { viewModel.setTrack(it) }
                                )

                                TexturePackMatrix(
                                    currentPack = engineState.currentPack,
                                    normalPacks = normalPacks,
                                    rainPacks = rainPacks,
                                    dayPacks = dayPacks,
                                    onApplyPack = { pack, isRain ->
                                        viewModel.applySinglePack(pack, isRain)
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Запущена подмена: $pack")
                                        }
                                    }
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        2 -> {
                            // 2: SHOP (Магазин тем)
                            QuestsScreen(viewModel = viewModel)
                        }

                        3 -> {
                            // 3: SYSTEM & PERMISSIONS & CLICKER (Права и Кликер)
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .widthIn(max = 720.dp)
                                    .verticalScroll(rememberScrollState())
                                    .padding(horizontal = 16.dp, vertical = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                SystemPermissionsCard(
                                    permissions = permissions,
                                    state = engineState,
                                    isGeneratingData = isGeneratingData,
                                    onRequestStorage = {
                                        val perms = PermissionHelper.getStoragePermissions()
                                        storagePermissionLauncher.launch(perms)
                                    },
                                    onRequestBattery = {
                                        try {
                                            context.startActivity(PermissionHelper.requestBatteryOptimizationIntent(context))
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Откройте настройки батареи вручную", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    onRequestNotification = {
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                        } else {
                                            viewModel.unlockAchievement(com.example.model.Achievement.NOTIFICATIONS_GRANTED)
                                            viewModel.refreshPermissions()
                                        }
                                    },
                                    onRequestAccessibility = {
                                        try {
                                            context.startActivity(PermissionHelper.requestAccessibilityIntent())
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Откройте специальные возможности", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    onTestClick = {
                                        viewModel.testSwipeNow()
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Микро-свайп (${engineState.swipeCount}x, ${engineState.swipeDurationMs} мс, пауза ${engineState.swipePauseMs} мс) запущен")
                                        }
                                    },
                                    onGenerateSampleStructure = {
                                        viewModel.generateSampleStructure { msg ->
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(msg)
                                            }
                                        }
                                    },
                                    onSetRandomMode = { viewModel.setRandomMode(it) },
                                    onSetForceSound = { viewModel.setForceSound(it) },
                                    onSetCycleInterval = { viewModel.setCycleInterval(it) },
                                    onSetRainProbability = { viewModel.setRainProbability(it) },
                                    onSetSwipeCount = { viewModel.setSwipeCount(it) },
                                    onSetSwipeDuration = { viewModel.setSwipeDuration(it) },
                                    onSetSwipePause = { viewModel.setSwipePause(it) },
                                    onClearCache = {
                                        viewModel.clearCache { _, formatted ->
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Кеш очищен: освобождено $formatted")
                                            }
                                        }
                                    },
                                    onUninstallApp = {
                                        showUninstallConfirmDialog = true
                                    }
                                )

                                ManualCoordinatesCard(
                                    state = engineState,
                                    onCoordinatesChanged = { startX, startY, endX, endY ->
                                        viewModel.setGestureCoordinates(startX, startY, endX, endY)
                                    },
                                    onTestSwipe = {
                                        viewModel.testSwipeNow()
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Тест траектории (${engineState.startX}, ${engineState.startY}) → (${engineState.endX}, ${engineState.endY})")
                                        }
                                    }
                                )

                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }

                        4 -> {
                            // 4: THEMES (Темы)
                            ThemesScreen(viewModel = viewModel)
                        }

                        5 -> {
                            // 5: EXPERIMENTS (Эксперименты)
                            ExperimentsScreen(viewModel = viewModel)
                        }

                        6 -> {
                            // 6: TERMINAL & EVENT STREAM (Терминал)
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .widthIn(max = 720.dp)
                                    .padding(horizontal = 16.dp, vertical = 16.dp)
                            ) {
                                LogsTerminalCard(
                                    logs = logs,
                                    onClearLogs = { viewModel.clearLogs() },
                                    isFullScreen = true
                                )
                            }
                        }

                        7 -> {
                            // 7: EDITOR (Редактор)
                            EditorScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }

    // Top notification curtain/toaster for achievements (2 seconds auto-dismiss)
    AchievementToaster(popup = achievementPopup)

    // AssemblyService active warning dialog on back/exit or notification stop (shown up to 3 times)
    if (showExitWarningDialog || showAssemblyWarningEvent) {
        AssemblyWarningDialog(
            warningCount = (warningShowCount + 1).coerceAtMost(MainViewModel.MAX_WARNING_COUNT),
            maxCount = MainViewModel.MAX_WARNING_COUNT,
            onNavigateToSettings = {
                showExitWarningDialog = false
                viewModel.dismissAssemblyWarning()
                viewModel.incrementWarningCount()
                try {
                    context.startActivity(PermissionHelper.requestAccessibilityIntent())
                } catch (e: Exception) {
                    Toast.makeText(context, "Откройте специальные возможности в настройках", Toast.LENGTH_SHORT).show()
                }
            },
            onDismiss = {
                showExitWarningDialog = false
                viewModel.dismissAssemblyWarning()
                viewModel.incrementWarningCount()
            },
            onForceExit = {
                showExitWarningDialog = false
                viewModel.dismissAssemblyWarning()
                viewModel.incrementWarningCount()
                activity?.finish()
            }
        )
    }

    // Confirmation dialog before triggering system uninstall intent
    if (showUninstallConfirmDialog) {
        UninstallConfirmDialog(
            onConfirmUninstall = {
                showUninstallConfirmDialog = false
                try {
                    context.startActivity(PermissionHelper.createUninstallIntent(context))
                } catch (e: Exception) {
                    Toast.makeText(context, "Не удалось запустить деинсталлятор: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            onDismiss = {
                showUninstallConfirmDialog = false
            }
        )
    }
}
}
}

