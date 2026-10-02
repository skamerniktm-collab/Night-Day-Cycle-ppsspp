package com.example.ui.components

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FitScreen
import androidx.compose.material.icons.filled.Gesture
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Swipe
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EngineState
import com.example.ui.theme.ImmersiveAmberWarm
import com.example.ui.theme.ImmersiveBackground
import com.example.ui.theme.ImmersiveCardBorder
import com.example.ui.theme.ImmersiveCardBorderSubtle
import com.example.ui.theme.ImmersiveCodeBg
import com.example.ui.theme.ImmersiveError
import com.example.ui.theme.ImmersiveErrorBg
import com.example.ui.theme.ImmersiveErrorBorder
import com.example.ui.theme.ImmersiveFieldBg
import com.example.ui.theme.ImmersiveLilac
import com.example.ui.theme.ImmersiveLilacContainer
import com.example.ui.theme.ImmersiveMint
import com.example.ui.theme.ImmersiveMintDark
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceContainer
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Manual Coordinates block for configuring micro-swipe trajectory.
 * Provides 4 validated numeric inputs, real-time screen bounds validation,
 * an interactive trajectory canvas editor, and fine-tuning sliders.
 */
@Composable
fun ManualCoordinatesCard(
    state: EngineState,
    onCoordinatesChanged: (startX: Int, startY: Int, endX: Int, endY: Int) -> Unit,
    onTestSwipe: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val displayMetrics = remember(context) { context.resources.displayMetrics }

    // Determine safe resolution bounds (landscape and portrait adaptive)
    val screenWidthPx = displayMetrics.widthPixels
    val screenHeightPx = displayMetrics.heightPixels
    val maxBoundX = remember(screenWidthPx, screenHeightPx) {
        maxOf(screenWidthPx, screenHeightPx, 2560)
    }
    val maxBoundY = remember(screenWidthPx, screenHeightPx) {
        maxOf(screenWidthPx, screenHeightPx, 1440)
    }

    // Local text states synchronized with state
    var startXText by remember(state.startX) { mutableStateOf(state.startX.toString()) }
    var startYText by remember(state.startY) { mutableStateOf(state.startY.toString()) }
    var endXText by remember(state.endX) { mutableStateOf(state.endX.toString()) }
    var endYText by remember(state.endY) { mutableStateOf(state.endY.toString()) }

    // Validation error states
    var startXError by remember { mutableStateOf<String?>(null) }
    var startYError by remember { mutableStateOf<String?>(null) }
    var endXError by remember { mutableStateOf<String?>(null) }
    var endYError by remember { mutableStateOf<String?>(null) }

    // Helper validation function
    fun validateAndApply() {
        val sx = startXText.toIntOrNull()
        val sy = startYText.toIntOrNull()
        val ex = endXText.toIntOrNull()
        val ey = endYText.toIntOrNull()

        var hasError = false

        if (sx == null || sx !in 0..maxBoundX) {
            startXError = "0..$maxBoundX"
            hasError = true
        } else {
            startXError = null
        }

        if (sy == null || sy !in 0..maxBoundY) {
            startYError = "0..$maxBoundY"
            hasError = true
        } else {
            startYError = null
        }

        if (ex == null || ex !in 0..maxBoundX) {
            endXError = "0..$maxBoundX"
            hasError = true
        } else {
            endXError = null
        }

        if (ey == null || ey !in 0..maxBoundY) {
            endYError = "0..$maxBoundY"
            hasError = true
        } else {
            endYError = null
        }

        if (!hasError && sx != null && sy != null && ex != null && ey != null) {
            onCoordinatesChanged(sx, sy, ex, ey)
        }
    }

    // Trajectory metrics
    val deltaX = state.endX - state.startX
    val deltaY = state.endY - state.startY
    val distancePx = hypot(deltaX.toDouble(), deltaY.toDouble()).roundToInt()

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
        border = BorderStroke(1.dp, ImmersiveCardBorderSubtle),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ImmersiveLilacContainer.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Gesture,
                            contentDescription = null,
                            tint = ImmersiveLilac,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "КООРДИНАТЫ ЖЕСТА",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                // Resolution Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ImmersiveSurfaceVariant,
                    border = BorderStroke(1.dp, ImmersiveCardBorderSubtle)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FitScreen,
                            contentDescription = null,
                            tint = ImmersiveMint,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${screenWidthPx}×${screenHeightPx} px",
                            color = TextSecondary,
                            fontSize = 10.5.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Trajectory Summary Pill Bar
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = ImmersiveCodeBg,
                border = BorderStroke(1.dp, ImmersiveCardBorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Точка A (Start)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(ImmersiveMint)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "A (${state.startX}, ${state.startY})",
                                color = ImmersiveMint,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                softWrap = false
                            )
                        }

                        // Горизонтальный отступ между A и стрелкой свайпа
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Направление свайпа",
                            tint = TextTertiary,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .size(14.dp)
                        )

                        // Точка B (End) - строго горизонтальная ориентация, исключающая вертикальный перенос
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(ImmersiveLilac)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "B (${state.endX}, ${state.endY})",
                                color = ImmersiveLilac,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    // Увеличенное расстояние между закрывающей скобкой координат B и блоком справа
                    Spacer(modifier = Modifier.width(16.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = ImmersiveSurfaceContainer
                    ) {
                        Text(
                            text = "ΔX:${if (deltaX >= 0) "+$deltaX" else "$deltaX"} ΔY:${if (deltaY >= 0) "+$deltaY" else "$deltaY"} ($distancePx px)",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            maxLines = 1,
                            softWrap = false,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // VISUAL TRAJECTORY EDITOR CANVAS
            Text(
                text = "ИНТЕРАКТИВНЫЙ ВИЗУАЛЬНЫЙ РЕДАКТОР",
                color = TextTertiary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 0.8.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            var activeDragPoint by remember { mutableStateOf<String?>(null) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 8.5f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(ImmersiveBackground)
                    .border(BorderStroke(1.dp, ImmersiveCardBorderSubtle), RoundedCornerShape(16.dp))
                    .testTag("trajectory_canvas")
                    .pointerInput(maxBoundX, maxBoundY, state.startX, state.startY, state.endX, state.endY) {
                        detectTapGestures { offset ->
                            val width = size.width.toFloat()
                            val height = size.height.toFloat()
                            val tapPxX = ((offset.x / width) * maxBoundX).roundToInt().coerceIn(0, maxBoundX)
                            val tapPxY = ((offset.y / height) * maxBoundY).roundToInt().coerceIn(0, maxBoundY)

                            // Check which point is closer to the tap
                            val distToStart = hypot(offset.x - (state.startX.toFloat() / maxBoundX * width), offset.y - (state.startY.toFloat() / maxBoundY * height))
                            val distToEnd = hypot(offset.x - (state.endX.toFloat() / maxBoundX * width), offset.y - (state.endY.toFloat() / maxBoundY * height))

                            if (distToStart <= distToEnd) {
                                onCoordinatesChanged(tapPxX, tapPxY, state.endX, state.endY)
                            } else {
                                onCoordinatesChanged(state.startX, state.startY, tapPxX, tapPxY)
                            }
                        }
                    }
                    .pointerInput(maxBoundX, maxBoundY, state.startX, state.startY, state.endX, state.endY) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                val width = size.width.toFloat()
                                val height = size.height.toFloat()
                                val startOffset = Offset(state.startX.toFloat() / maxBoundX * width, state.startY.toFloat() / maxBoundY * height)
                                val endOffset = Offset(state.endX.toFloat() / maxBoundX * width, state.endY.toFloat() / maxBoundY * height)

                                val dStart = (offset - startOffset).getDistance()
                                val dEnd = (offset - endOffset).getDistance()

                                activeDragPoint = if (dStart < dEnd && dStart < 120f) {
                                    "START"
                                } else if (dEnd < 120f) {
                                    "END"
                                } else {
                                    if (dStart < dEnd) "START" else "END"
                                }
                            },
                            onDragEnd = { activeDragPoint = null },
                            onDragCancel = { activeDragPoint = null },
                            onDrag = { change, _ ->
                                change.consume()
                                val width = size.width.toFloat()
                                val height = size.height.toFloat()
                                val targetX = ((change.position.x / width) * maxBoundX).roundToInt().coerceIn(0, maxBoundX)
                                val targetY = ((change.position.y / height) * maxBoundY).roundToInt().coerceIn(0, maxBoundY)

                                if (activeDragPoint == "START") {
                                    onCoordinatesChanged(targetX, targetY, state.endX, state.endY)
                                } else {
                                    onCoordinatesChanged(state.startX, state.startY, targetX, targetY)
                                }
                            }
                        )
                    }
            ) {
                val canvasLilac = ImmersiveLilac
                val canvasMint = ImmersiveMint
                val canvasBorderSubtle = ImmersiveCardBorderSubtle
                val canvasBg = ImmersiveBackground

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // 1. Grid Background
                    val gridCols = 8
                    val gridRows = 4
                    for (i in 1 until gridCols) {
                        val x = w * i / gridCols
                        drawLine(
                            color = canvasBorderSubtle.copy(alpha = 0.35f),
                            start = Offset(x, 0f),
                            end = Offset(x, h),
                            strokeWidth = 1f
                        )
                    }
                    for (j in 1 until gridRows) {
                        val y = h * j / gridRows
                        drawLine(
                            color = canvasBorderSubtle.copy(alpha = 0.35f),
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            strokeWidth = 1f
                        )
                    }

                    // 2. Trajectory Points in Canvas Space
                    val pStart = Offset(
                        x = (state.startX.toFloat() / maxBoundX * w).coerceIn(12f, w - 12f),
                        y = (state.startY.toFloat() / maxBoundY * h).coerceIn(12f, h - 12f)
                    )
                    val pEnd = Offset(
                        x = (state.endX.toFloat() / maxBoundX * w).coerceIn(12f, w - 12f),
                        y = (state.endY.toFloat() / maxBoundY * h).coerceIn(12f, h - 12f)
                    )

                    // 3. Trajectory Line
                    drawLine(
                        color = canvasLilac.copy(alpha = 0.3f),
                        start = pStart,
                        end = pEnd,
                        strokeWidth = 10f,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = canvasLilac,
                        start = pStart,
                        end = pEnd,
                        strokeWidth = 3.5f,
                        cap = StrokeCap.Round
                    )

                    // 4. Direction Arrow Head at pEnd
                    val angle = atan2((pEnd.y - pStart.y).toDouble(), (pEnd.x - pStart.x).toDouble())
                    val arrowLength = 22f
                    val arrowAngle = Math.toRadians(28.0)
                    val arrowP1 = Offset(
                        x = (pEnd.x - arrowLength * cos(angle - arrowAngle)).toFloat(),
                        y = (pEnd.y - arrowLength * sin(angle - arrowAngle)).toFloat()
                    )
                    val arrowP2 = Offset(
                        x = (pEnd.x - arrowLength * cos(angle + arrowAngle)).toFloat(),
                        y = (pEnd.y - arrowLength * sin(angle + arrowAngle)).toFloat()
                    )

                    val arrowPath = Path().apply {
                        moveTo(pEnd.x, pEnd.y)
                        lineTo(arrowP1.x, arrowP1.y)
                        lineTo(arrowP2.x, arrowP2.y)
                        close()
                    }
                    drawPath(arrowPath, color = canvasLilac)

                    // 5. Point A (Start) Circle
                    drawCircle(
                        color = canvasMint.copy(alpha = 0.25f),
                        radius = 16f,
                        center = pStart
                    )
                    drawCircle(
                        color = canvasBg,
                        radius = 9f,
                        center = pStart
                    )
                    drawCircle(
                        color = canvasMint,
                        radius = 6f,
                        center = pStart
                    )

                    // 6. Point B (End) Circle
                    drawCircle(
                        color = canvasLilac.copy(alpha = 0.25f),
                        radius = 16f,
                        center = pEnd
                    )
                    drawCircle(
                        color = canvasBg,
                        radius = 9f,
                        center = pEnd
                    )
                    drawCircle(
                        color = canvasLilac,
                        radius = 6f,
                        center = pEnd
                    )
                }

                // Instructions Overlay
                Text(
                    text = "Перетащите точки A/B или нажмите на холст для настройки",
                    color = TextTertiary,
                    fontSize = 9.5.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4 NUMERIC INPUT FIELDS (Start X, Start Y, End X, End Y)
            Text(
                text = "РУЧНОЙ ВВОД ЗНАЧЕНИЙ (NUMERIC INPUTS)",
                color = TextTertiary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Row 1: Start X / Start Y
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Start X
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = startXText,
                        onValueChange = { input ->
                            val filtered = input.filter { it.isDigit() }.take(5)
                            startXText = filtered
                            val num = filtered.toIntOrNull()
                            if (num != null && num in 0..maxBoundX) {
                                startXError = null
                                onCoordinatesChanged(num, state.startY, state.endX, state.endY)
                            } else if (filtered.isNotEmpty()) {
                                startXError = "макс $maxBoundX"
                            }
                        },
                        label = {
                            Text(
                                text = "Start X (Точка A)",
                                color = if (startXError != null) ImmersiveError else ImmersiveMint,
                                fontSize = 11.sp
                            )
                        },
                        placeholder = { Text("1169", color = TextTertiary, fontSize = 12.sp) },
                        isError = startXError != null,
                        supportingText = {
                            Text(
                                text = startXError ?: "0..$maxBoundX px",
                                color = if (startXError != null) ImmersiveError else TextTertiary,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ImmersiveFieldBg,
                            unfocusedContainerColor = ImmersiveFieldBg,
                            focusedBorderColor = ImmersiveMint,
                            unfocusedBorderColor = ImmersiveCardBorderSubtle,
                            errorBorderColor = ImmersiveError,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("coord_start_x")
                    )
                }

                // Start Y
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = startYText,
                        onValueChange = { input ->
                            val filtered = input.filter { it.isDigit() }.take(5)
                            startYText = filtered
                            val num = filtered.toIntOrNull()
                            if (num != null && num in 0..maxBoundY) {
                                startYError = null
                                onCoordinatesChanged(state.startX, num, state.endX, state.endY)
                            } else if (filtered.isNotEmpty()) {
                                startYError = "макс $maxBoundY"
                            }
                        },
                        label = {
                            Text(
                                text = "Start Y (Точка A)",
                                color = if (startYError != null) ImmersiveError else ImmersiveMint,
                                fontSize = 11.sp
                            )
                        },
                        placeholder = { Text("25", color = TextTertiary, fontSize = 12.sp) },
                        isError = startYError != null,
                        supportingText = {
                            Text(
                                text = startYError ?: "0..$maxBoundY px",
                                color = if (startYError != null) ImmersiveError else TextTertiary,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ImmersiveFieldBg,
                            unfocusedContainerColor = ImmersiveFieldBg,
                            focusedBorderColor = ImmersiveMint,
                            unfocusedBorderColor = ImmersiveCardBorderSubtle,
                            errorBorderColor = ImmersiveError,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("coord_start_y")
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Row 2: End X / End Y
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // End X
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = endXText,
                        onValueChange = { input ->
                            val filtered = input.filter { it.isDigit() }.take(5)
                            endXText = filtered
                            val num = filtered.toIntOrNull()
                            if (num != null && num in 0..maxBoundX) {
                                endXError = null
                                onCoordinatesChanged(state.startX, state.startY, num, state.endY)
                            } else if (filtered.isNotEmpty()) {
                                endXError = "макс $maxBoundX"
                            }
                        },
                        label = {
                            Text(
                                text = "End X (Точка B)",
                                color = if (endXError != null) ImmersiveError else ImmersiveLilac,
                                fontSize = 11.sp
                            )
                        },
                        placeholder = { Text("1194", color = TextTertiary, fontSize = 12.sp) },
                        isError = endXError != null,
                        supportingText = {
                            Text(
                                text = endXError ?: "0..$maxBoundX px",
                                color = if (endXError != null) ImmersiveError else TextTertiary,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ImmersiveFieldBg,
                            unfocusedContainerColor = ImmersiveFieldBg,
                            focusedBorderColor = ImmersiveLilac,
                            unfocusedBorderColor = ImmersiveCardBorderSubtle,
                            errorBorderColor = ImmersiveError,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("coord_end_x")
                    )
                }

                // End Y
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = endYText,
                        onValueChange = { input ->
                            val filtered = input.filter { it.isDigit() }.take(5)
                            endYText = filtered
                            val num = filtered.toIntOrNull()
                            if (num != null && num in 0..maxBoundY) {
                                endYError = null
                                onCoordinatesChanged(state.startX, state.startY, state.endX, num)
                            } else if (filtered.isNotEmpty()) {
                                endYError = "макс $maxBoundY"
                            }
                        },
                        label = {
                            Text(
                                text = "End Y (Точка B)",
                                color = if (endYError != null) ImmersiveError else ImmersiveLilac,
                                fontSize = 11.sp
                            )
                        },
                        placeholder = { Text("25", color = TextTertiary, fontSize = 12.sp) },
                        isError = endYError != null,
                        supportingText = {
                            Text(
                                text = endYError ?: "0..$maxBoundY px",
                                color = if (endYError != null) ImmersiveError else TextTertiary,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                validateAndApply()
                            }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ImmersiveFieldBg,
                            unfocusedContainerColor = ImmersiveFieldBg,
                            focusedBorderColor = ImmersiveLilac,
                            unfocusedBorderColor = ImmersiveCardBorderSubtle,
                            errorBorderColor = ImmersiveError,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("coord_end_y")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // SYNCHRONIZED SLIDERS SECTION
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = ImmersiveSurfaceVariant,
                border = BorderStroke(1.dp, ImmersiveCardBorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = ImmersiveLilac,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Ползунки тонкой подстройки",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // Presets Row
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            OutlinedButton(
                                onClick = {
                                    onCoordinatesChanged(1169, 25, 1194, 25)
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .height(28.dp)
                                    .testTag("btn_reset_coords"),
                                border = BorderStroke(1.dp, ImmersiveCardBorderSubtle)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = ImmersiveLilac,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Сброс", fontSize = 10.sp, color = ImmersiveLilac, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Slider: Start X
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Start X", color = ImmersiveMint, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        Text(text = "${state.startX} px", color = TextSecondary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                    Slider(
                        value = state.startX.toFloat().coerceIn(0f, maxBoundX.toFloat()),
                        onValueChange = { onCoordinatesChanged(it.roundToInt(), state.startY, state.endX, state.endY) },
                        valueRange = 0f..maxBoundX.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(28.dp)
                            .testTag("slider_start_x"),
                        colors = SliderDefaults.colors(
                            thumbColor = ImmersiveMint,
                            activeTrackColor = ImmersiveMint,
                            inactiveTrackColor = ImmersiveBackground
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Slider: Start Y
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Start Y", color = ImmersiveMint, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        Text(text = "${state.startY} px", color = TextSecondary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                    Slider(
                        value = state.startY.toFloat().coerceIn(0f, maxBoundY.toFloat()),
                        onValueChange = { onCoordinatesChanged(state.startX, it.roundToInt(), state.endX, state.endY) },
                        valueRange = 0f..maxBoundY.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(28.dp)
                            .testTag("slider_start_y"),
                        colors = SliderDefaults.colors(
                            thumbColor = ImmersiveMint,
                            activeTrackColor = ImmersiveMint,
                            inactiveTrackColor = ImmersiveBackground
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Slider: End X
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "End X", color = ImmersiveLilac, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        Text(text = "${state.endX} px", color = TextSecondary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                    Slider(
                        value = state.endX.toFloat().coerceIn(0f, maxBoundX.toFloat()),
                        onValueChange = { onCoordinatesChanged(state.startX, state.startY, it.roundToInt(), state.endY) },
                        valueRange = 0f..maxBoundX.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(28.dp)
                            .testTag("slider_end_x"),
                        colors = SliderDefaults.colors(
                            thumbColor = ImmersiveLilac,
                            activeTrackColor = ImmersiveLilac,
                            inactiveTrackColor = ImmersiveBackground
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Slider: End Y
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "End Y", color = ImmersiveLilac, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        Text(text = "${state.endY} px", color = TextSecondary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                    Slider(
                        value = state.endY.toFloat().coerceIn(0f, maxBoundY.toFloat()),
                        onValueChange = { onCoordinatesChanged(state.startX, state.startY, state.endX, it.roundToInt()) },
                        valueRange = 0f..maxBoundY.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(28.dp)
                            .testTag("slider_end_y"),
                        colors = SliderDefaults.colors(
                            thumbColor = ImmersiveLilac,
                            activeTrackColor = ImmersiveLilac,
                            inactiveTrackColor = ImmersiveBackground
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action: Test Trajectory Button
            Button(
                onClick = onTestSwipe,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ImmersiveLilac,
                    contentColor = ImmersiveBackground
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 54.dp)
                    .testTag("btn_test_trajectory")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Swipe,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "ПРОВЕРИТЬ ТРАЕКТОРИЮ",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "(${state.startX}, ${state.startY})",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                softWrap = false
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Направление свайпа",
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "(${state.endX}, ${state.endY})",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }
        }
    }
}
