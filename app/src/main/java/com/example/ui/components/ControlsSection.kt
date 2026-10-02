package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EngineState
import com.example.model.EngineStatus
import com.example.ui.theme.ImmersiveBackground
import com.example.ui.theme.ImmersiveCardBorder
import com.example.ui.theme.ImmersiveCardBorderSubtle
import com.example.ui.theme.ImmersiveError
import com.example.ui.theme.ImmersiveErrorBg
import com.example.ui.theme.ImmersiveLilac
import com.example.ui.theme.ImmersiveMint
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveOnLilac
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.TextTertiary

@Composable
fun ControlsSection(
    state: EngineState,
    onStart: () -> Unit,
    onTogglePause: () -> Unit,
    onStop: () -> Unit,
    onNextTrack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("controls_section"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
        border = BorderStroke(1.dp, ImmersiveCardBorder)
    ) {
        val isRunning = state.status == EngineStatus.RUNNING || state.status == EngineStatus.RAIN_SEQUENCE
        val isPaused = state.status == EngineStatus.PAUSED

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Start / Pause Button
            Button(
                onClick = {
                    if (isRunning || isPaused) {
                        onTogglePause()
                    } else {
                        onStart()
                    }
                },
                modifier = Modifier
                    .weight(1.2f)
                    .height(44.dp)
                    .testTag("main_action_button"),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when {
                        isRunning -> Color(0xFFFFB74D)
                        isPaused -> ImmersiveMint
                        else -> ImmersiveLilac
                    },
                    contentColor = when {
                        isRunning -> ImmersiveBackground
                        isPaused -> Color(0xFF183B1A)
                        else -> ImmersiveOnLilac
                    }
                )
            ) {
                Icon(
                    imageVector = when {
                        isRunning -> Icons.Default.Pause
                        isPaused -> Icons.Default.PlayArrow
                        else -> Icons.Default.PlayArrow
                    },
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = when {
                        isRunning -> "ПАУЗА"
                        isPaused -> "ПРОДОЛЖИТЬ"
                        else -> "СТАРТ"
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // 2. Stop Button
            Button(
                onClick = onStop,
                enabled = state.status != EngineStatus.STOPPED,
                modifier = Modifier
                    .weight(0.9f)
                    .height(44.dp)
                    .testTag("stop_button"),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ImmersiveErrorBg,
                    contentColor = ImmersiveError,
                    disabledContainerColor = ImmersiveSurfaceVariant,
                    disabledContentColor = TextTertiary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "СТОП",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // 3. Next Track Button
            OutlinedButton(
                onClick = onNextTrack,
                modifier = Modifier
                    .weight(1.3f)
                    .height(44.dp)
                    .testTag("next_track_button"),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(ImmersiveCardBorderSubtle)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = ImmersiveLilac
                )
            ) {
                Icon(
                    imageVector = Icons.Default.FastForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "СЛЕД. ТРАССА",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.3.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

