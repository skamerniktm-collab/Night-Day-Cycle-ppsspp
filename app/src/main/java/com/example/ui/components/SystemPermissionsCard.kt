package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Swipe
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EngineState
import com.example.service.AssemblyService
import com.example.ui.PermissionStatus
import com.example.ui.theme.ImmersiveBackground
import com.example.ui.theme.ImmersiveAmberWarm
import com.example.ui.theme.ImmersiveCardBorder
import com.example.ui.theme.ImmersiveCardBorderSubtle
import com.example.ui.theme.ImmersiveLilac
import com.example.ui.theme.ImmersiveLilacContainer
import com.example.ui.theme.ImmersiveMint
import com.example.ui.theme.ImmersiveMintDark
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import kotlin.math.roundToInt

@Composable
fun SystemPermissionsCard(
    permissions: PermissionStatus,
    state: EngineState,
    isGeneratingData: Boolean = false,
    onRequestStorage: () -> Unit,
    onRequestBattery: () -> Unit,
    onRequestNotification: () -> Unit,
    onRequestAccessibility: () -> Unit,
    onTestClick: () -> Unit,
    onGenerateSampleStructure: () -> Unit = {},
    onSetRandomMode: (Boolean) -> Unit,
    onSetForceSound: (Boolean) -> Unit,
    onSetCycleInterval: (Int) -> Unit,
    onSetRainProbability: (Int) -> Unit,
    onSetSwipeCount: (Int) -> Unit = {},
    onSetSwipeDuration: (Long) -> Unit = {},
    onSetSwipePause: (Long) -> Unit = {},
    onClearCache: () -> Unit = {},
    onUninstallApp: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("permissions_and_clicker_card"),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
        border = BorderStroke(1.dp, ImmersiveCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "SYSTEM PERMISSIONS & INTEGRATION",
                color = TextTertiary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Storage Permission Row
            PermissionItem(
                title = "Память устройства",
                subtitle = "Доступ к файлам и медиа",
                isGranted = permissions.hasStorage,
                icon = Icons.Default.FolderShared,
                actionLabel = "Разрешить",
                onAction = onRequestStorage
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Battery Optimization
            PermissionItem(
                title = "Оптимизация батареи",
                subtitle = "Фоновая работа без ограничений",
                isGranted = permissions.isBatteryOptimizedIgnored,
                icon = Icons.Default.BatteryChargingFull,
                actionLabel = "Исключить",
                onAction = onRequestBattery
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Notification Permission
            PermissionItem(
                title = "Уведомления",
                subtitle = "POST_NOTIFICATIONS",
                isGranted = permissions.hasNotification,
                icon = Icons.Default.Notifications,
                actionLabel = "Разрешить",
                onAction = onRequestNotification
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 4. Accessibility Service (NDCycleService)
            PermissionItem(
                title = "Спец. возможности",
                subtitle = "NDCycleService",
                isGranted = permissions.isAccessibilityActive,
                icon = Icons.Default.AccessibilityNew,
                actionLabel = "Настройки",
                actionLabelGranted = "Настройки",
                alwaysShowAction = true,
                onAction = onRequestAccessibility
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Engine Configuration Section (Settings)
            Text(
                text = "ПАРАМЕТРЫ ДВИЖКА И ЦИКЛА",
                color = TextTertiary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(18.dp),
                color = ImmersiveSurfaceVariant,
                border = BorderStroke(1.dp, ImmersiveCardBorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    // Slider 1: Interval (Seconds / Minutes)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = ImmersiveLilac,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Интервал смены",
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Text(
                                text = formatInterval(state.cycleIntervalSeconds),
                                color = ImmersiveLilac,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Slider(
                            value = state.cycleIntervalSeconds.toFloat().coerceIn(5f, 3600f),
                            onValueChange = { onSetCycleInterval(it.roundToInt()) },
                            valueRange = 5f..3600f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .testTag("interval_slider"),
                            colors = SliderDefaults.colors(
                                thumbColor = ImmersiveLilac,
                                activeTrackColor = ImmersiveLilac,
                                inactiveTrackColor = ImmersiveBackground,
                                activeTickColor = Color.Transparent,
                                inactiveTickColor = Color.Transparent
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Slider 2: Weather (Rain probability 0-100%)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WaterDrop,
                                    contentDescription = null,
                                    tint = ImmersiveMint,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Вероятность дождя",
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Text(
                                text = "${state.rainProbabilityPercent}%",
                                color = ImmersiveMint,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Slider(
                            value = state.rainProbabilityPercent.toFloat().coerceIn(0f, 100f),
                            onValueChange = { onSetRainProbability(it.roundToInt()) },
                            valueRange = 0f..100f,
                            steps = 19,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .testTag("rain_probability_slider"),
                            colors = SliderDefaults.colors(
                                thumbColor = ImmersiveMint,
                                activeTrackColor = ImmersiveMint,
                                inactiveTrackColor = ImmersiveBackground,
                                activeTickColor = Color.Transparent,
                                inactiveTickColor = Color.Transparent
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Slider 3: Micro-Swipe Count
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Repeat,
                                    contentDescription = null,
                                    tint = ImmersiveLilac,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Количество микро-свайпов",
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Text(
                                text = "${state.swipeCount}",
                                color = ImmersiveLilac,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Slider(
                            value = state.swipeCount.toFloat().coerceIn(1f, 10f),
                            onValueChange = { onSetSwipeCount(it.roundToInt()) },
                            valueRange = 1f..10f,
                            steps = 8,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .testTag("swipe_count_slider"),
                            colors = SliderDefaults.colors(
                                thumbColor = ImmersiveLilac,
                                activeTrackColor = ImmersiveLilac,
                                inactiveTrackColor = ImmersiveBackground,
                                activeTickColor = Color.Transparent,
                                inactiveTickColor = Color.Transparent
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Slider 4: Micro-Swipe Speed (Duration in ms)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = null,
                                    tint = ImmersiveMint,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Скорость микро-свайпов",
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Text(
                                text = "${state.swipeDurationMs} мс",
                                color = ImmersiveMint,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Slider(
                            value = state.swipeDurationMs.toFloat().coerceIn(20f, 500f),
                            onValueChange = { onSetSwipeDuration(it.roundToInt().toLong()) },
                            valueRange = 20f..500f,
                            steps = 23,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .testTag("swipe_speed_slider"),
                            colors = SliderDefaults.colors(
                                thumbColor = ImmersiveMint,
                                activeTrackColor = ImmersiveMint,
                                inactiveTrackColor = ImmersiveBackground,
                                activeTickColor = Color.Transparent,
                                inactiveTickColor = Color.Transparent
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Slider 5: Micro-Swipe Pause (Delay between swipes in ms)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.HourglassEmpty,
                                    contentDescription = null,
                                    tint = ImmersiveAmberWarm,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Пауза между свайпами",
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Text(
                                text = "${state.swipePauseMs} мс",
                                color = ImmersiveAmberWarm,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Slider(
                            value = state.swipePauseMs.toFloat().coerceIn(50f, 1500f),
                            onValueChange = { onSetSwipePause(((it / 25).roundToInt() * 25).toLong()) },
                            valueRange = 50f..1500f,
                            steps = 28,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .testTag("swipe_pause_slider"),
                            colors = SliderDefaults.colors(
                                thumbColor = ImmersiveAmberWarm,
                                activeTrackColor = ImmersiveAmberWarm,
                                inactiveTrackColor = ImmersiveBackground,
                                activeTickColor = Color.Transparent,
                                inactiveTickColor = Color.Transparent
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Divider
                    Surface(
                        color = ImmersiveCardBorderSubtle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    ) {}

                    Spacer(modifier = Modifier.height(12.dp))

                    // Random Mode Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (state.isRandomMode) Icons.Default.Casino else Icons.Default.Shuffle,
                                contentDescription = null,
                                tint = if (state.isRandomMode) ImmersiveLilac else TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Случайный режим (random_mode)",
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (state.isRandomMode) "Пакеты выбираются случайно (${state.rainProbabilityPercent}% дождь)" else "Строго последовательная смена по кругу",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        Switch(
                            checked = state.isRandomMode,
                            onCheckedChange = onSetRandomMode,
                            modifier = Modifier.testTag("random_mode_switch"),
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = ImmersiveLilac,
                                checkedTrackColor = ImmersiveLilac.copy(alpha = 0.3f),
                                uncheckedThumbColor = TextTertiary,
                                uncheckedTrackColor = ImmersiveBackground
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Sound FX Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (state.isForceSound) Icons.Default.MusicNote else Icons.Default.MusicOff,
                                contentDescription = null,
                                tint = if (state.isForceSound) ImmersiveMint else TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Звуковые эффекты (force_sound)",
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (state.isForceSound) "Ww.mp3 (7с пауза) + Dd.mp3 на финише" else "Копирование без звуковых пауз",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        Switch(
                            checked = state.isForceSound,
                            onCheckedChange = onSetForceSound,
                            modifier = Modifier.testTag("sound_switch"),
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = ImmersiveMint,
                                checkedTrackColor = ImmersiveMint.copy(alpha = 0.3f),
                                uncheckedThumbColor = TextTertiary,
                                uncheckedTrackColor = ImmersiveBackground
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Swipe Action Header
            Text(
                text = "ASSEMBLYSERVICE MICRO-SWIPE EMULATION",
                color = TextTertiary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Test Swipe Button
            Button(
                onClick = onTestClick,
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("test_swipe_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ImmersiveLilacContainer,
                    contentColor = ImmersiveLilac
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Swipe,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ТЕСТ МИКРО-СВАЙПА (${state.swipeCount}x, ${state.swipeDurationMs}мс, ${state.swipePauseMs}мс)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Service & Maintenance Header
            Text(
                text = "СЛУЖЕБНЫЕ ОПЕРАЦИИ И ОБСЛУЖИВАНИЕ",
                color = TextTertiary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Button 1: Clear Cache
                OutlinedButton(
                    onClick = onClearCache,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("clear_cache_button"),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, ImmersiveCardBorderSubtle),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ImmersiveLilac)
                ) {
                    Icon(
                        imageVector = Icons.Default.CleaningServices,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Очистить кеш",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Button 2: Uninstall App
                Button(
                    onClick = onUninstallApp,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("uninstall_app_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B181E),
                        contentColor = Color(0xFFFF6B6B)
                    ),
                    border = BorderStroke(1.dp, Color(0xFF6E282E))
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Удалить приложение",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun PermissionItem(
    title: String,
    subtitle: String? = null,
    isGranted: Boolean,
    icon: ImageVector,
    actionLabel: String,
    actionLabelGranted: String? = null,
    alwaysShowAction: Boolean = false,
    onAction: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = ImmersiveSurfaceVariant,
        border = BorderStroke(1.dp, ImmersiveCardBorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isGranted) ImmersiveMint else Color(0xFFFFB74D),
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = subtitle,
                        color = TextTertiary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Action button when not granted or when alwaysShowAction is true
            if (!isGranted || alwaysShowAction) {
                OutlinedButton(
                    onClick = onAction,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(30.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    border = BorderStroke(1.dp, ImmersiveCardBorderSubtle)
                ) {
                    Text(
                        text = if (isGranted && actionLabelGranted != null) actionLabelGranted else actionLabel,
                        fontSize = 11.sp,
                        color = ImmersiveLilac,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Green checkmark status icon: always aligned on the rightmost edge / vertical axis
            if (isGranted) {
                if (alwaysShowAction) {
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Box(
                    modifier = Modifier.size(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Разрешено",
                        tint = ImmersiveMint,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

private fun formatInterval(seconds: Int): String {
    return when {
        seconds < 60 -> "$seconds сек"
        seconds == 3600 -> "1 час"
        seconds % 60 == 0 -> "${seconds / 60} мин"
        else -> "${seconds / 60} мин ${seconds % 60} сек"
    }
}

