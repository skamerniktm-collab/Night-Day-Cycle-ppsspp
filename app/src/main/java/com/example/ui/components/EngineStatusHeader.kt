package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TrackDictionary
import com.example.model.EngineState
import com.example.model.EngineStatus
import com.example.ui.theme.ImmersiveCardBorder
import com.example.ui.theme.ImmersiveCardBorderSubtle
import com.example.ui.theme.ImmersiveCodeBg
import com.example.ui.theme.ImmersiveError
import com.example.ui.theme.ImmersiveFieldBg
import com.example.ui.theme.ImmersiveLilac
import com.example.ui.theme.ImmersiveMint
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceContainer
import com.example.ui.theme.ImmersiveSurfaceDark
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun EngineStatusHeader(
    state: EngineState,
    modifier: Modifier = Modifier,
    contentBelowStandby: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        EngineStandbyHeader(state = state)

        if (contentBelowStandby != null) {
            contentBelowStandby()
        }

        EngineSessionDetailsCard(state = state)
    }
}

/**
 * Top standby/status session header ("Standby Session" or "Active Session" with status badge).
 */
@Composable
fun EngineStandbyHeader(
    state: EngineState,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val isLive = state.status == EngineStatus.RUNNING || state.status == EngineStatus.RAIN_SEQUENCE || state.status == EngineStatus.COPYING
    val statusBadgeColor = when {
        state.isGamePausedDetected -> Color(0xFFFFB74D)
        state.isMainMenuDetected -> Color(0xFFFFB74D)
        state.isIdleScreenDetected || state.isGtIconScreenDetected -> Color(0xFFFFB74D)
        state.status == EngineStatus.RUNNING && state.isRealGameplayConfirmed -> ImmersiveMint
        state.status == EngineStatus.RUNNING -> Color(0xFFFFB74D)
        state.status == EngineStatus.RAIN_SEQUENCE -> ImmersiveLilac
        state.status == EngineStatus.COPYING -> ImmersiveLilac
        state.status == EngineStatus.PAUSED -> Color(0xFFFFB74D)
        state.status == EngineStatus.STOPPED -> ImmersiveError
        else -> ImmersiveError
    }

    val statusBadgeText = when {
        state.isGamePausedDetected -> "GAME PAUSED / SETTINGS"
        state.isMainMenuDetected -> "PPSSPP MENU (WAITING)"
        state.isGtIconScreenDetected -> "IDLE: GT ICON (WAITING)"
        state.isIdleScreenDetected -> "IDLE: NO MOTION"
        state.status == EngineStatus.RUNNING && state.isRealGameplayConfirmed -> "ACTIVE GAMEPLAY"
        state.status == EngineStatus.RUNNING -> "AWAITING GAMEPLAY"
        state.status == EngineStatus.RAIN_SEQUENCE -> "RAIN CYCLE ACTIVE"
        state.status == EngineStatus.COPYING -> "COPYING TEXTURES"
        state.status == EngineStatus.PAUSED -> "PAUSED"
        state.status == EngineStatus.STOPPED -> "STOPPED"
        else -> "UNKNOWN"
    }

    // Immersive Top Header block
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "NDCYCLE ENGINE V5.0",
                color = ImmersiveLilac,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 2.sp
            )

            // Compact balance / currency widget "Nd X" (clean text without background)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Nd",
                    color = ImmersiveLilac,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "${state.ndBalance}",
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isLive) "Active Session" else "Standby Session",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = (-0.5).sp,
                modifier = Modifier.weight(1f, fill = false)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Status indicator (dot + label centered with main session text)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .scale(if (isLive) pulseScale else 1f)
                        .clip(CircleShape)
                        .background(statusBadgeColor)
                        .shadow(if (isLive) 8.dp else 0.dp, CircleShape, spotColor = statusBadgeColor)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = statusBadgeText,
                    color = statusBadgeColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

/**
 * Main session details card (Location, Progress, Weather, Mode).
 */
@Composable
fun EngineSessionDetailsCard(
    state: EngineState,
    modifier: Modifier = Modifier
) {
    val track = TrackDictionary.getTrack(state.currentTrackKey)
    val isLive = state.status == EngineStatus.RUNNING || state.status == EngineStatus.RAIN_SEQUENCE || state.status == EngineStatus.COPYING

    // Main Immersive Session Card (Gradient from #1D1B20 to #161419 with rounded-[32px])
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("engine_status_header"),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = androidx.compose.foundation.BorderStroke(1.dp, ImmersiveCardBorder)
    ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(ImmersiveSurface, ImmersiveSurfaceDark)
                        )
                    )
                    .padding(22.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Location Header
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "CURRENT LOCATION",
                            color = TextTertiary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = track.name,
                            color = TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                        if (track.location.isNotEmpty()) {
                            Text(
                                text = track.location,
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Texture Pack + 15s Progress Card
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = ImmersiveSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, ImmersiveCardBorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Texture Pack: ",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = state.currentPack,
                                    color = ImmersiveLilac,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                val totalSeconds = state.cycleIntervalSeconds.coerceAtLeast(1)
                                val elapsedSeconds = (totalSeconds - state.countdownSeconds).coerceAtLeast(0)
                                Text(
                                    text = "$elapsedSeconds / ${totalSeconds}s",
                                    color = ImmersiveLilac,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = FontFamily.Monospace
                                )
                            }

                            // Glowing linear progress bar
                            val totalSecondsFloat = state.cycleIntervalSeconds.coerceAtLeast(1).toFloat()
                            val progress = if (isLive && state.countdownSeconds > 0) {
                                (totalSecondsFloat - state.countdownSeconds.toFloat()) / totalSecondsFloat
                            } else if (isLive) 1f else 0f

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(percent = 50))
                                    .background(ImmersiveFieldBg)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(percent = 50))
                                        .background(ImmersiveLilac)
                                        .shadow(8.dp, RoundedCornerShape(percent = 50), spotColor = ImmersiveLilac)
                                )
                            }
                        }
                    }

                    // 2-Column Info Grid: Weather & Mode
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Weather Card
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = ImmersiveSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, ImmersiveCardBorderSubtle),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "WEATHER",
                                    color = TextTertiary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 1.sp
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val icon = if (state.isRainActive || TrackDictionary.RAIN_PACKS.contains(state.currentPack)) {
                                        Icons.Default.Thunderstorm
                                    } else {
                                        Icons.Default.WbSunny
                                    }
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = ImmersiveLilac,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    val weatherDesc = if (state.isRainActive) {
                                        "Rain Cycle (${state.rainProbabilityPercent}%)"
                                    } else if (state.rainProbabilityPercent == 0) {
                                        "Clear (Rain Off)"
                                    } else {
                                        "Clear Sky (${state.rainProbabilityPercent}%)"
                                    }
                                    Text(
                                        text = weatherDesc,
                                        color = TextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        // Mode Card
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = ImmersiveSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, ImmersiveCardBorderSubtle),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "MODE",
                                    color = TextTertiary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = if (state.isRandomMode) "Random (${state.rainProbabilityPercent}% ⛈)" else "Sequential",
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
}

