package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DrawingStroke
import com.example.ui.theme.ImmersiveCardBorder
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.LocalAppThemeColors
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import kotlinx.coroutines.delay

/**
 * Interactive finger-drawing canvas component positioned above the control panel.
 * Allows drawing with fingers and applying the artwork as the application background.
 */
@Composable
fun DrawingCanvasCard(
    modifier: Modifier = Modifier,
    title: String = "ХОЛСТ РИСОВАНИЯ",
    initialStrokes: List<DrawingStroke> = emptyList(),
    hasCustomBackground: Boolean = false,
    onApplyAsBackground: ((strokes: List<DrawingStroke>, width: Float, height: Float) -> Unit)? = null,
    onClearBackground: (() -> Unit)? = null
) {
    val palette = LocalAppThemeColors.current

    val availableColors = remember {
        listOf(
            Color(0xFFD0BCFF), // Neon Lilac
            Color(0xFFB6FFB4), // Mint Green
            Color(0xFFFFB74D), // Solar Amber
            Color(0xFFFF5252), // Coral Red
            Color(0xFF40C4FF), // Sky Blue
            Color(0xFFFFFFFF)  // White
        )
    }

    var strokes by remember { mutableStateOf(initialStrokes) }
    var currentPoints by remember { mutableStateOf(listOf<Offset>()) }
    var selectedColor by remember { mutableStateOf(availableColors[0]) }
    var selectedStrokeWidth by remember { mutableFloatStateOf(8f) }
    var canvasSize by remember { mutableStateOf(IntSize(720, 480)) }
    var justApplied by remember { mutableStateOf(false) }

    // If initialStrokes changes and local strokes are empty, initialize them
    LaunchedEffect(initialStrokes) {
        if (strokes.isEmpty() && initialStrokes.isNotEmpty()) {
            strokes = initialStrokes
        }
    }

    // Reset "Applied" state badge after 2.5 seconds
    LaunchedEffect(justApplied) {
        if (justApplied) {
            delay(2500)
            justApplied = false
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("drawing_canvas_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
        border = BorderStroke(1.dp, ImmersiveCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Header: Title + Tools (Undo, Clear)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Draw,
                        contentDescription = null,
                        tint = selectedColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = title,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Undo button
                    IconButton(
                        onClick = {
                            if (strokes.isNotEmpty()) {
                                strokes = strokes.dropLast(1)
                            }
                        },
                        enabled = strokes.isNotEmpty(),
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("btn_undo_drawing")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Undo,
                            contentDescription = "Отмена",
                            tint = if (strokes.isNotEmpty()) TextPrimary else TextTertiary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Clear button
                    IconButton(
                        onClick = {
                            strokes = emptyList()
                            currentPoints = emptyList()
                        },
                        enabled = strokes.isNotEmpty(),
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("btn_clear_drawing")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Очистить",
                            tint = if (strokes.isNotEmpty()) Color(0xFFFF5252) else TextTertiary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Interactive Canvas Pad
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF101114))
                    .border(1.dp, Color(0xFF24262C), RoundedCornerShape(14.dp))
                    .onSizeChanged { size ->
                        if (size.width > 0 && size.height > 0) {
                            canvasSize = size
                        }
                    }
                    .testTag("drawing_canvas_pad")
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(selectedColor, selectedStrokeWidth) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    currentPoints = listOf(offset)
                                },
                                onDrag = { change, _ ->
                                    change.consume()
                                    currentPoints = currentPoints + change.position
                                },
                                onDragEnd = {
                                    if (currentPoints.isNotEmpty()) {
                                        strokes = strokes + DrawingStroke(
                                            points = currentPoints,
                                            color = selectedColor,
                                            strokeWidth = selectedStrokeWidth
                                        )
                                        currentPoints = emptyList()
                                    }
                                },
                                onDragCancel = {
                                    currentPoints = emptyList()
                                }
                            )
                        }
                ) {
                    // Render existing finished strokes
                    for (stroke in strokes) {
                        renderStroke(stroke.points, stroke.color, stroke.strokeWidth)
                    }
                    // Render current in-progress stroke
                    if (currentPoints.isNotEmpty()) {
                        renderStroke(currentPoints, selectedColor, selectedStrokeWidth)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Footer Toolbar: Color picker & Brush size
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Color dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    availableColors.forEach { color ->
                        val isColorSelected = color == selectedColor
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (isColorSelected) 2.5.dp else 1.dp,
                                    color = if (isColorSelected) TextPrimary else Color(0xFF33353D),
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = color }
                        )
                    }
                }

                // Brush width presets (Thin / Medium / Thick)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(
                        4f to "Тонко",
                        8f to "Средне",
                        16f to "Жирно"
                    ).forEach { (size, label) ->
                        val isSizeSelected = selectedStrokeWidth == size
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSizeSelected) palette.primary else Color(0xFF202227),
                            modifier = Modifier
                                .clickable { selectedStrokeWidth = size }
                        ) {
                            Text(
                                text = label,
                                color = if (isSizeSelected) palette.onPrimary else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = if (isSizeSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Row: "Применить как фон" + "Сбросить фон"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (hasCustomBackground) {
                    OutlinedButton(
                        onClick = {
                            onClearBackground?.invoke()
                            justApplied = false
                        },
                        modifier = Modifier
                            .height(38.dp)
                            .testTag("btn_clear_wallpaper"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFFF8A80)
                        ),
                        border = BorderStroke(1.dp, Color(0xFFFF5252).copy(alpha = 0.5f)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Сбросить фон",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Button(
                    onClick = {
                        if (strokes.isNotEmpty()) {
                            val w = if (canvasSize.width > 0) canvasSize.width.toFloat() else 720f
                            val h = if (canvasSize.height > 0) canvasSize.height.toFloat() else 480f
                            onApplyAsBackground?.invoke(strokes, w, h)
                            justApplied = true
                        }
                    },
                    enabled = strokes.isNotEmpty(),
                    modifier = Modifier
                        .height(38.dp)
                        .testTag("btn_apply_background"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (justApplied) Color(0xFF2E7D32) else palette.primary,
                        contentColor = if (justApplied) Color.White else palette.onPrimary,
                        disabledContainerColor = Color(0xFF202227),
                        disabledContentColor = TextTertiary
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp)
                ) {
                    Icon(
                        imageVector = if (justApplied) Icons.Default.Check else Icons.Default.Wallpaper,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (justApplied) "Фон применен!" else "Применить как фон",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Helper to render smooth connected strokes or single dots.
 */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.renderStroke(
    points: List<Offset>,
    color: Color,
    strokeWidth: Float
) {
    if (points.isEmpty()) return

    if (points.size == 1) {
        drawCircle(
            color = color,
            radius = strokeWidth / 2f,
            center = points[0]
        )
        return
    }

    val path = Path().apply {
        moveTo(points[0].x, points[0].y)
        for (i in 1 until points.size) {
            lineTo(points[i].x, points[i].y)
        }
    }

    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}
