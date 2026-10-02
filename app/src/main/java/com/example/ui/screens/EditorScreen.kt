package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.ViewStream
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ControlsPosition
import com.example.ui.MainViewModel
import com.example.ui.components.DrawingCanvasCard
import com.example.ui.theme.LocalAppThemeColors
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

/**
 * Screen for customizing UI layout and structural arrangement of UI blocks.
 */
@Composable
fun EditorScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val palette = LocalAppThemeColors.current
    val currentPosition by viewModel.controlsPosition.collectAsState()
    val customWallpaper by viewModel.customDrawingWallpaper.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .testTag("editor_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Card: Title and Active Preset
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 720.dp),
            shape = RoundedCornerShape(16.dp),
            color = palette.surface,
            border = BorderStroke(1.dp, palette.cardBorderSubtle)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = palette.primary.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, palette.primary.copy(alpha = 0.5f))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = "Конструктор",
                                    tint = palette.primary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "Редактор макета",
                                color = TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Layout Switcher",
                                color = palette.secondary,
                                fontSize = 11.5.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Active mode badge
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = palette.primary.copy(alpha = 0.12f),
                        border = BorderStroke(0.8.dp, palette.primary.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = currentPosition.badge,
                            color = palette.primary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Layout Switcher Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 720.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "ВАРИАНТЫ РАСПОЛОЖЕНИЯ",
                color = palette.secondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 4.dp)
            )

            // Option 1: TOP
            LayoutOptionCard(
                position = ControlsPosition.TOP,
                isSelected = currentPosition == ControlsPosition.TOP,
                onClick = { viewModel.setControlsPosition(ControlsPosition.TOP) },
                icon = Icons.Default.ViewAgenda,
                testTag = "layout_option_top"
            )

            // Option 2: BOTTOM
            LayoutOptionCard(
                position = ControlsPosition.BOTTOM,
                isSelected = currentPosition == ControlsPosition.BOTTOM,
                onClick = { viewModel.setControlsPosition(ControlsPosition.BOTTOM) },
                icon = Icons.Default.ViewStream,
                testTag = "layout_option_bottom"
            )

            // Option 3: DOCKED_BOTTOM
            LayoutOptionCard(
                position = ControlsPosition.DOCKED_BOTTOM,
                isSelected = currentPosition == ControlsPosition.DOCKED_BOTTOM,
                onClick = { viewModel.setControlsPosition(ControlsPosition.DOCKED_BOTTOM) },
                icon = Icons.Default.Layers,
                testTag = "layout_option_docked"
            )

            // Option 4: DRAWING (Режим рисования пальцем)
            LayoutOptionCard(
                position = ControlsPosition.DRAWING,
                isSelected = currentPosition == ControlsPosition.DRAWING,
                onClick = { viewModel.setControlsPosition(ControlsPosition.DRAWING) },
                icon = Icons.Default.Draw,
                testTag = "layout_option_drawing"
            )
        }

        // Live drawing workspace inside Editor tab if DRAWING mode is active
        if (currentPosition == ControlsPosition.DRAWING) {
            Spacer(modifier = Modifier.height(16.dp))
            DrawingCanvasCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 720.dp)
                    .height(380.dp),
                title = "РАБОЧАЯ ЗОНА • РИСОВАНИЕ ПАЛЬЦЕМ",
                initialStrokes = customWallpaper?.strokes ?: emptyList(),
                hasCustomBackground = customWallpaper != null && customWallpaper!!.strokes.isNotEmpty(),
                onApplyAsBackground = { strokes, width, height ->
                    viewModel.applyDrawingAsBackground(strokes, width, height)
                },
                onClearBackground = {
                    viewModel.clearCustomDrawingBackground()
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Interactive card representing one layout arrangement option in the Layout Switcher.
 */
@Composable
private fun LayoutOptionCard(
    position: ControlsPosition,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    testTag: String
) {
    val palette = LocalAppThemeColors.current

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) palette.primary else palette.cardBorderSubtle,
        animationSpec = tween(200),
        label = "card_border_anim"
    )

    val bgColor by animateColorAsState(
        targetValue = if (isSelected) palette.primary.copy(alpha = 0.08f) else palette.surface,
        animationSpec = tween(200),
        label = "card_bg_anim"
    )

    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        color = bgColor,
        border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Mini wireframe visual diagram
            MiniWireframeDiagram(
                position = position,
                isSelected = isSelected
            )

            // Title and badge block
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = position.title,
                    color = if (isSelected) palette.primary else TextPrimary,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (isSelected) palette.primary.copy(alpha = 0.15f) else palette.surfaceVariant,
                    border = BorderStroke(0.6.dp, if (isSelected) palette.primary.copy(alpha = 0.5f) else palette.cardBorderSubtle)
                ) {
                    Text(
                        text = position.badge,
                        color = if (isSelected) palette.primary else TextSecondary,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            // Radio Button
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = palette.primary,
                    unselectedColor = TextTertiary
                )
            )
        }
    }
}

/**
 * Illustrated mini-diagram displaying the structural layout blocks.
 */
@Composable
private fun MiniWireframeDiagram(
    position: ControlsPosition,
    isSelected: Boolean
) {
    val palette = LocalAppThemeColors.current

    Surface(
        modifier = Modifier.size(54.dp, 60.dp),
        shape = RoundedCornerShape(8.dp),
        color = palette.surfaceDark,
        border = BorderStroke(
            1.dp,
            if (isSelected) palette.primary.copy(alpha = 0.7f) else palette.cardBorderSubtle
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            when (position) {
                ControlsPosition.TOP -> {
                    // Standby header block at TOP
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(9.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(palette.surfaceVariant)
                    )
                    // Controls block (highlighted) immediately below Standby
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isSelected) palette.primary else palette.primary.copy(alpha = 0.4f))
                    )
                    // Main Session details card below controls
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(18.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(palette.surfaceVariant.copy(alpha = 0.7f))
                    )
                }

                ControlsPosition.BOTTOM -> {
                    // Status block at TOP
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(18.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(palette.surfaceVariant)
                    )
                    // Controls block (highlighted) BELOW status
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isSelected) palette.primary else palette.primary.copy(alpha = 0.4f))
                    )
                    // Secondary content
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(palette.surfaceVariant.copy(alpha = 0.6f))
                    )
                }

                ControlsPosition.DOCKED_BOTTOM -> {
                    // Status & Content
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(palette.surfaceVariant)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(palette.surfaceVariant.copy(alpha = 0.6f))
                    )
                    // Docked bar at bottom edge
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isSelected) palette.secondary else palette.secondary.copy(alpha = 0.4f))
                    )
                }

                ControlsPosition.DRAWING -> {
                    // Full Drawing Canvas Area
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isSelected) palette.primary.copy(alpha = 0.25f) else palette.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Draw,
                            contentDescription = null,
                            tint = if (isSelected) palette.primary else palette.textSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
