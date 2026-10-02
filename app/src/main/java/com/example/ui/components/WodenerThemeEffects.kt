package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.isActive
import kotlin.random.Random

/**
 * Types of materials in the Wodener Worc theme.
 */
enum class WodenerBlockType {
    WOOD,               // Dark brown with deep vertical grain lines
    PLANKS,             // Straw/light-yellow with horizontal plank seams
    POLISHED_GRANITE,   // Pinkish-beige with fine specks/fleck inclusions
    BRICKS,             // Earthy clay-brown separated into rectangular brick masonry
    PRISMARINE_BRICKS   // Deep cyan-turquoise/teal with lattice grid tiles
}

/**
 * High fidelity custom canvas swatch illustrating the 5 material textures.
 */
@Composable
fun WodenerBlockSwatch(
    type: WodenerBlockType,
    modifier: Modifier = Modifier,
    size: Dp = 22.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(4.dp))
            .border(0.8.dp, Color.White.copy(alpha = 0.22f), RoundedCornerShape(4.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            when (type) {
                WodenerBlockType.WOOD -> drawWoodBlock(this)
                WodenerBlockType.PLANKS -> drawPlanksBlock(this)
                WodenerBlockType.POLISHED_GRANITE -> drawGraniteBlock(this)
                WodenerBlockType.BRICKS -> drawBricksBlock(this)
                WodenerBlockType.PRISMARINE_BRICKS -> drawPrismarineBricksBlock(this)
            }
        }
    }
}

private fun drawWoodBlock(drawScope: DrawScope) {
    val w = drawScope.size.width
    val h = drawScope.size.height

    // Base dark brown bark/wood
    drawScope.drawRect(color = Color(0xFF382012))

    // Vertical grain texture lines
    val grainColorDark = Color(0xFF221208)
    val grainColorLight = Color(0xFF4F2E1B)
    val grainColorAccent = Color(0xFF2E190E)

    val xCoords = listOf(0.12f, 0.28f, 0.44f, 0.62f, 0.78f, 0.90f)
    xCoords.forEachIndexed { i, frac ->
        val x = frac * w
        val col = if (i % 2 == 0) grainColorDark else grainColorLight
        drawScope.drawLine(
            color = col,
            start = Offset(x, 0f),
            end = Offset(x, h),
            strokeWidth = if (i == 1 || i == 4) 1.6f else 1.0f
        )
    }

    // Additional subtle vertical wood bark fibers
    listOf(0.20f, 0.52f, 0.70f).forEach { frac ->
        val x = frac * w
        drawScope.drawLine(
            color = grainColorAccent,
            start = Offset(x, h * 0.15f),
            end = Offset(x, h * 0.85f),
            strokeWidth = 1f
        )
    }

    // Border bevel
    drawScope.drawRect(
        color = Color(0xFF5A3520).copy(alpha = 0.5f),
        size = Size(w, 1.5f)
    )
    drawScope.drawRect(
        color = Color(0xFF1B0C05).copy(alpha = 0.6f),
        topLeft = Offset(0f, h - 1.5f),
        size = Size(w, 1.5f)
    )
}

private fun drawPlanksBlock(drawScope: DrawScope) {
    val w = drawScope.size.width
    val h = drawScope.size.height

    // Base warm straw yellow / light wood plank color
    drawScope.drawRect(color = Color(0xFFD69C54))

    // Horizontal plank seams (4 boards)
    val seamColor = Color(0xFF8C5C26)
    val highlightColor = Color(0xFFF0BE7A)

    val plankCount = 4
    val plankH = h / plankCount

    for (i in 0 until plankCount) {
        val y = i * plankH
        // Subtle plank inner gradient / grain
        drawScope.drawRect(
            color = if (i % 2 == 0) Color(0xFFDC9E53) else Color(0xFFCF934B),
            topLeft = Offset(0f, y),
            size = Size(w, plankH)
        )
        // Light edge at top of each plank
        drawScope.drawLine(
            color = highlightColor,
            start = Offset(0f, y + 0.8f),
            end = Offset(w, y + 0.8f),
            strokeWidth = 0.8f
        )
        // Dark horizontal dividing seam
        if (i > 0) {
            drawScope.drawLine(
                color = seamColor,
                start = Offset(0f, y),
                end = Offset(w, y),
                strokeWidth = 1.2f
            )
        }
    }

    // A few vertical stagger nail/joints
    val jointY1 = 0f
    drawScope.drawLine(
        color = seamColor,
        start = Offset(w * 0.45f, jointY1),
        end = Offset(w * 0.45f, plankH),
        strokeWidth = 1.1f
    )
    drawScope.drawLine(
        color = seamColor,
        start = Offset(w * 0.75f, plankH),
        end = Offset(w * 0.75f, plankH * 2),
        strokeWidth = 1.1f
    )
    drawScope.drawLine(
        color = seamColor,
        start = Offset(w * 0.30f, plankH * 2),
        end = Offset(w * 0.30f, plankH * 3),
        strokeWidth = 1.1f
    )
    drawScope.drawLine(
        color = seamColor,
        start = Offset(w * 0.65f, plankH * 3),
        end = Offset(w * 0.65f, h),
        strokeWidth = 1.1f
    )
}

private fun drawGraniteBlock(drawScope: DrawScope) {
    val w = drawScope.size.width
    val h = drawScope.size.height

    // Base polished granite: pinkish-beige
    drawScope.drawRect(color = Color(0xFFBE8A7B))

    // Specular / polished highlight diagonal gloss
    val glossBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFCE9B8C),
            Color(0xFFBE8A7B),
            Color(0xFFB37B6D)
        ),
        start = Offset(0f, 0f),
        end = Offset(w, h)
    )
    drawScope.drawRect(brush = glossBrush)

    // Fine granite specks & fleck inclusions
    val darkFlecks = listOf(
        Offset(0.20f, 0.25f), Offset(0.48f, 0.18f), Offset(0.82f, 0.32f),
        Offset(0.32f, 0.60f), Offset(0.68f, 0.55f), Offset(0.18f, 0.82f),
        Offset(0.85f, 0.78f), Offset(0.52f, 0.85f)
    )
    val lightFlecks = listOf(
        Offset(0.35f, 0.22f), Offset(0.72f, 0.15f), Offset(0.15f, 0.45f),
        Offset(0.58f, 0.38f), Offset(0.88f, 0.50f), Offset(0.42f, 0.72f),
        Offset(0.74f, 0.88f)
    )

    val darkFleckColor = Color(0xFF6B4235)
    val lightFleckColor = Color(0xFFDFC0B6)

    darkFlecks.forEach { pt ->
        drawScope.drawRect(
            color = darkFleckColor,
            topLeft = Offset(pt.x * w, pt.y * h),
            size = Size(1.5f, 1.5f)
        )
    }

    lightFlecks.forEach { pt ->
        drawScope.drawRect(
            color = lightFleckColor,
            topLeft = Offset(pt.x * w, pt.y * h),
            size = Size(1.5f, 1.5f)
        )
    }

    // Polished edge rim
    drawScope.drawRect(
        color = Color(0xFFE5C8BF).copy(alpha = 0.45f),
        size = Size(w, 1.2f)
    )
    drawScope.drawRect(
        color = Color(0xFF633A30).copy(alpha = 0.5f),
        topLeft = Offset(0f, h - 1.2f),
        size = Size(w, 1.2f)
    )
}

private fun drawBricksBlock(drawScope: DrawScope) {
    val w = drawScope.size.width
    val h = drawScope.size.height

    // Grout / mortar background (clayish beige/gray)
    drawScope.drawRect(color = Color(0xFFBCA194))

    val rowCount = 4
    val rowH = h / rowCount
    val brickMargin = 1.0f

    val brickBase = Color(0xFF984931)
    val brickAlt = Color(0xFFA55239)
    val brickDark = Color(0xFF883E28)

    for (r in 0 until rowCount) {
        val y = r * rowH + brickMargin
        val bH = rowH - brickMargin * 1.5f

        if (r % 2 == 0) {
            // Two full bricks
            val bW = (w - brickMargin * 3) / 2f
            // Brick 1
            drawScope.drawRect(
                color = brickBase,
                topLeft = Offset(brickMargin, y),
                size = Size(bW, bH)
            )
            // Brick 2
            drawScope.drawRect(
                color = brickAlt,
                topLeft = Offset(brickMargin * 2 + bW, y),
                size = Size(bW, bH)
            )
        } else {
            // Half brick, full brick, half brick
            val bHalf = (w - brickMargin * 4) / 4f
            val bFull = (w - brickMargin * 4) / 2f

            // Half 1
            drawScope.drawRect(
                color = brickDark,
                topLeft = Offset(brickMargin, y),
                size = Size(bHalf, bH)
            )
            // Full
            drawScope.drawRect(
                color = brickBase,
                topLeft = Offset(brickMargin * 2 + bHalf, y),
                size = Size(bFull, bH)
            )
            // Half 2
            drawScope.drawRect(
                color = brickAlt,
                topLeft = Offset(brickMargin * 3 + bHalf + bFull, y),
                size = Size(bHalf, bH)
            )
        }
    }
}

private fun drawPrismarineBricksBlock(drawScope: DrawScope) {
    val w = drawScope.size.width
    val h = drawScope.size.height

    // Deep cyan-teal / ocean prismarine base
    drawScope.drawRect(color = Color(0xFF1B6A66))

    val gridCols = 3
    val gridRows = 3
    val tileW = w / gridCols
    val tileH = h / gridRows

    val seamColorDark = Color(0xFF0F3E3B)
    val tileHighlight = Color(0xFF389A94)
    val tileBaseA = Color(0xFF1B6A66)
    val tileBaseB = Color(0xFF237A75)

    for (r in 0 until gridRows) {
        for (c in 0 until gridCols) {
            val x = c * tileW
            val y = r * tileH

            // Individual prismarine tile
            val tileColor = if ((r + c) % 2 == 0) tileBaseA else tileBaseB
            drawScope.drawRect(
                color = tileColor,
                topLeft = Offset(x, y),
                size = Size(tileW, tileH)
            )

            // Inner lattice bevel highlight on tile top-left
            drawScope.drawLine(
                color = tileHighlight,
                start = Offset(x + 1f, y + 1f),
                end = Offset(x + tileW - 1f, y + 1f),
                strokeWidth = 0.8f
            )
            drawScope.drawLine(
                color = tileHighlight,
                start = Offset(x + 1f, y + 1f),
                end = Offset(x + 1f, y + tileH - 1f),
                strokeWidth = 0.8f
            )

            // Grid tile boundary seams
            drawScope.drawRect(
                color = seamColorDark,
                topLeft = Offset(x, y),
                size = Size(tileW, tileH),
                style = Stroke(width = 1.0f)
            )
        }
    }
}

/**
 * Animated subtle floating wood grain particles, straw flecks, and prismarine teal embers.
 */
@Composable
fun WodenerParticlesOverlay(
    modifier: Modifier = Modifier,
    particleCount: Int = 16,
    alphaMultiplier: Float = 0.35f
) {
    val density = LocalDensity.current
    val particles = remember(particleCount) {
        val colors = listOf(
            // Prismarine cyan
            Color(0xFF1B6A66),
            Color(0xFF5AB6A8),
            // Planks straw
            Color(0xFFD69C54),
            Color(0xFFF2C280),
            // Granite pinkish-beige
            Color(0xFFBE8A7B),
            // Wood amber-brown
            Color(0xFF5A3520)
        )
        List(particleCount) { i ->
            val size = Random.nextFloat() * 4f + 3f
            val wobble = Random.nextFloat() * 22f + 8f
            val speed = Random.nextFloat() * 0.18f + 0.10f
            Triple(
                Offset(Random.nextFloat(), Random.nextFloat()),
                size,
                colors[i % colors.size]
            )
        }
    }

    var animTime by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        while (isActive) {
            withFrameNanos { time ->
                animTime = (time / 1_000_000L % 100000L).toFloat() / 1000f
            }
        }
    }

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        particles.forEachIndexed { index, (initialPos, sizeDp, color) ->
            val phase = index * 0.45f
            val currentProgress = (initialPos.y - (animTime * 0.045f) + phase) % 1.0f
            val yPos = (if (currentProgress < 0f) currentProgress + 1.0f else currentProgress) * h
            val xWobble = kotlin.math.sin((animTime * 1.2f + phase).toDouble()).toFloat() * 14f
            val xPos = (initialPos.x * w + xWobble).coerceIn(0f, w)

            val pSize = with(density) { sizeDp.dp.toPx() }
            drawRect(
                color = color.copy(alpha = alphaMultiplier * 0.85f),
                topLeft = Offset(xPos, yPos),
                size = Size(pSize, pSize)
            )
        }
    }
}

/**
 * Animated subtle shimmering banner/border effect for Wodener Worc cards.
 */
@Composable
fun WodenerCardShimmerOverlay(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wodener_shimmer")
    val shimmerShift by infiniteTransition.animateFloat(
        initialValue = -0.4f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wodener_shimmer_shift"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val startX = shimmerShift * (w + h) - h
        val shimmerBrush = Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                Color(0xFF5AB6A8).copy(alpha = 0.08f),
                Color(0xFFD69C54).copy(alpha = 0.12f),
                Color(0xFFBE8A7B).copy(alpha = 0.08f),
                Color.Transparent
            ),
            start = Offset(startX, 0f),
            end = Offset(startX + 180f, h)
        )
        drawRect(brush = shimmerBrush)
    }
}
