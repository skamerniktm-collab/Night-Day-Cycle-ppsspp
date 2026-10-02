package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.isActive
import kotlin.random.Random

/**
 * Types of materials in the Cryptic Frost theme.
 */
enum class CrypticFrostBlockType {
    ICE,            // Светло-синий (ледяной голубой) с полупрозрачными морозными разводами
    MUSHROOM,       // Насыщенный красный с белыми пятнышками
    END_STONE,      // Бледно-желтоватый или бежево-кремовый с узором из мелких точек и пятен
    GLASS_MATTE,    // Полупрозрачный белый (стеклянный или светлый матовый оттенок)
    ORANGE_CERAMIC  // Яркий однородный оранжевый цвет
}

/**
 * High fidelity custom canvas swatch illustrating the 5 Cryptic Frost materials.
 */
@Composable
fun CrypticFrostBlockSwatch(
    type: CrypticFrostBlockType,
    modifier: Modifier = Modifier,
    size: Dp = 22.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(4.dp))
            .border(0.8.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(4.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            when (type) {
                CrypticFrostBlockType.ICE -> drawIceBlock(this)
                CrypticFrostBlockType.MUSHROOM -> drawMushroomBlock(this)
                CrypticFrostBlockType.END_STONE -> drawEndStoneBlock(this)
                CrypticFrostBlockType.GLASS_MATTE -> drawGlassMatteBlock(this)
                CrypticFrostBlockType.ORANGE_CERAMIC -> drawOrangeCeramicBlock(this)
            }
        }
    }
}

private fun drawIceBlock(drawScope: DrawScope) {
    val w = drawScope.size.width
    val h = drawScope.size.height

    // Base light ice blue
    drawScope.drawRect(
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFF72DAF8), Color(0xFF52BEE4), Color(0xFF65D2F4))
        )
    )

    // Translucent frost swirls and crystalline veins
    val frostColorPrimary = Color(0xCCFFFFFF)
    val frostColorSecondary = Color(0x88C4F1FE)
    val frostShadow = Color(0x44268CB0)

    val path1 = Path().apply {
        moveTo(0f, h * 0.25f)
        cubicTo(w * 0.35f, h * 0.15f, w * 0.45f, h * 0.5f, w * 0.9f, h * 0.35f)
    }
    drawScope.drawPath(path1, frostShadow, style = Stroke(width = 1.8f))
    drawScope.drawPath(path1, frostColorPrimary, style = Stroke(width = 1.1f))

    val path2 = Path().apply {
        moveTo(w * 0.15f, h * 0.8f)
        cubicTo(w * 0.4f, h * 0.65f, w * 0.65f, h * 0.95f, w, h * 0.7f)
    }
    drawScope.drawPath(path2, frostColorSecondary, style = Stroke(width = 1.4f))

    // Frost corner crystals
    drawScope.drawLine(
        color = frostColorPrimary,
        start = Offset(w * 0.05f, h * 0.05f),
        end = Offset(w * 0.35f, h * 0.28f),
        strokeWidth = 1.2f
    )
    drawScope.drawLine(
        color = frostColorSecondary,
        start = Offset(w * 0.65f, h * 0.75f),
        end = Offset(w * 0.92f, h * 0.92f),
        strokeWidth = 1.0f
    )
}

private fun drawMushroomBlock(drawScope: DrawScope) {
    val w = drawScope.size.width
    val h = drawScope.size.height

    // Rich saturated red base
    drawScope.drawRect(color = Color(0xFFD32638))

    // Subtle shaded border
    drawScope.drawRect(
        color = Color(0xFF9E1523),
        size = Size(w, h),
        style = Stroke(width = 1.0f)
    )

    // Distinct white spots/flecks
    val spots = listOf(
        Pair(Offset(w * 0.28f, h * 0.32f), w * 0.16f),
        Pair(Offset(w * 0.74f, h * 0.25f), w * 0.14f),
        Pair(Offset(w * 0.52f, h * 0.68f), w * 0.18f),
        Pair(Offset(w * 0.18f, h * 0.78f), w * 0.11f),
        Pair(Offset(w * 0.86f, h * 0.76f), w * 0.12f)
    )

    spots.forEach { (center, radius) ->
        // Outer soft glow
        drawScope.drawCircle(
            color = Color(0x66FFFFFF),
            center = center,
            radius = radius + 0.8f
        )
        // White spot core
        drawScope.drawCircle(
            color = Color.White,
            center = center,
            radius = radius
        )
    }
}

private fun drawEndStoneBlock(drawScope: DrawScope) {
    val w = drawScope.size.width
    val h = drawScope.size.height

    // Pale yellowish / beige-cream base
    drawScope.drawRect(color = Color(0xFFDFDC9B))

    // Stippling specks and mottled spots
    val darkDot = Color(0xFFABA662)
    val lightDot = Color(0xFFECEAA8)
    val midDot = Color(0xFFBFBA74)

    val dots = listOf(
        Triple(0.2f, 0.2f, darkDot),
        Triple(0.35f, 0.15f, lightDot),
        Triple(0.6f, 0.22f, midDot),
        Triple(0.82f, 0.18f, darkDot),
        Triple(0.15f, 0.45f, midDot),
        Triple(0.42f, 0.48f, darkDot),
        Triple(0.72f, 0.42f, lightDot),
        Triple(0.88f, 0.52f, midDot),
        Triple(0.28f, 0.75f, lightDot),
        Triple(0.55f, 0.78f, darkDot),
        Triple(0.78f, 0.82f, midDot),
        Triple(0.4f, 0.88f, darkDot),
        Triple(0.12f, 0.85f, midDot)
    )

    dots.forEach { (xf, yf, col) ->
        drawScope.drawRect(
            color = col,
            topLeft = Offset(xf * w, yf * h),
            size = Size(w * 0.08f, h * 0.08f)
        )
    }
}

private fun drawGlassMatteBlock(drawScope: DrawScope) {
    val w = drawScope.size.width
    val h = drawScope.size.height

    // Frosted matte translucent white base
    drawScope.drawRect(
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFFF2F8FC), Color(0xFFE2EFF6), Color(0xFFECF5FB))
        )
    )

    // Subtle glass bevel edge
    drawScope.drawRect(
        color = Color(0x66B0D5E8),
        size = Size(w, h),
        style = Stroke(width = 1.2f)
    )

    // Diagonal glass sheen lines
    drawScope.drawLine(
        color = Color(0xCCFFFFFF),
        start = Offset(w * 0.1f, h * 0.8f),
        end = Offset(w * 0.8f, h * 0.1f),
        strokeWidth = 1.6f
    )
    drawScope.drawLine(
        color = Color(0x77FFFFFF),
        start = Offset(w * 0.35f, h * 0.9f),
        end = Offset(w * 0.9f, h * 0.35f),
        strokeWidth = 1.0f
    )
}

private fun drawOrangeCeramicBlock(drawScope: DrawScope) {
    val w = drawScope.size.width
    val h = drawScope.size.height

    // Bright uniform orange
    drawScope.drawRect(color = Color(0xFFFF6D00))

    // Smooth ceramic top highlight
    drawScope.drawLine(
        color = Color(0x44FFA040),
        start = Offset(0f, 1f),
        end = Offset(w, 1f),
        strokeWidth = 1.8f
    )

    // Subtle ceramic contour border
    drawScope.drawRect(
        color = Color(0x33B23C00),
        size = Size(w, h),
        style = Stroke(width = 1.0f)
    )
}

/**
 * Animated frost crystal particles overlay for Cryptic Frost cards.
 */
@Composable
fun CrypticFrostParticlesOverlay(
    modifier: Modifier = Modifier,
    particleCount: Int = 14,
    alphaMultiplier: Float = 0.65f
) {
    var animClock by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        var lastTime = 0L
        while (isActive) {
            withFrameNanos { now ->
                if (lastTime != 0L) {
                    val dt = (now - lastTime) / 1_000_000_000f
                    animClock += dt
                }
                lastTime = now
            }
        }
    }

    val seed = remember { 77123L }
    val particles = remember {
        val rng = Random(seed)
        List(particleCount) {
            FrostParticle(
                xInit = rng.nextFloat(),
                yInit = rng.nextFloat(),
                speed = 0.08f + rng.nextFloat() * 0.12f,
                drift = (rng.nextFloat() - 0.5f) * 0.06f,
                radius = 1.5f + rng.nextFloat() * 2.0f,
                color = when (rng.nextInt(5)) {
                    0 -> Color(0xFF6AD6F5) // Ice
                    1 -> Color(0xFFFFFFFF) // Frost / Glass
                    2 -> Color(0xFFDFDC9B) // End Stone
                    3 -> Color(0xFFFF6D00) // Orange Ceramic
                    else -> Color(0xFFE52E2E) // Mushroom
                }
            )
        }
    }

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        particles.forEach { p ->
            val y = ((p.yInit + animClock * p.speed) % 1.0f) * h
            val x = ((p.xInit + animClock * p.drift) % 1.0f + 1.0f) % 1.0f * w

            drawCircle(
                color = p.color.copy(alpha = alphaMultiplier * 0.75f),
                radius = p.radius,
                center = Offset(x, y)
            )
        }
    }
}

private data class FrostParticle(
    val xInit: Float,
    val yInit: Float,
    val speed: Float,
    val drift: Float,
    val radius: Float,
    val color: Color
)

/**
 * Shimmer effect for Cryptic Frost theme cards.
 */
@Composable
fun CrypticFrostCardShimmerOverlay(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "frost_shimmer")
    val shimmerTranslate by transition.animateFloat(
        initialValue = -300f,
        targetValue = 900f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "frost_shimmer_translate"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val shimmerBrush = Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                Color(0xFF6AD6F5).copy(alpha = 0.08f),
                Color(0xFFFFFFFF).copy(alpha = 0.15f),
                Color(0xFF6AD6F5).copy(alpha = 0.08f),
                Color.Transparent
            ),
            start = Offset(shimmerTranslate, 0f),
            end = Offset(shimmerTranslate + 220f, h)
        )

        drawRect(brush = shimmerBrush)
    }
}

/**
 * Fullscreen ambient frosty atmosphere overlay for the Cryptic Frost theme.
 * Gives an ethereal icy crystal aura around the viewport edges, corners, and top header.
 */
@Composable
fun CrypticFrostAmbientAuraOverlay(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Top edge cold frosty cyan glow
        val topFrostGradient = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF6AD6F5).copy(alpha = 0.14f),
                Color(0xFF6AD6F5).copy(alpha = 0.05f),
                Color.Transparent
            ),
            startY = 0f,
            endY = h * 0.18f
        )
        drawRect(brush = topFrostGradient)

        // Bottom subtle frosty crystalline glow
        val bottomFrostGradient = Brush.verticalGradient(
            colors = listOf(
                Color.Transparent,
                Color(0xFF0F3B4F).copy(alpha = 0.15f),
                Color(0xFF6AD6F5).copy(alpha = 0.08f)
            ),
            startY = h * 0.85f,
            endY = h
        )
        drawRect(brush = bottomFrostGradient)

        // Top-left and top-right subtle frost auras
        val topLeftFrost = Brush.radialGradient(
            colors = listOf(Color(0xFF6AD6F5).copy(alpha = 0.09f), Color.Transparent),
            center = Offset(0f, 0f),
            radius = w * 0.5f
        )
        drawRect(brush = topLeftFrost)

        val topRightFrost = Brush.radialGradient(
            colors = listOf(Color(0xFF6AD6F5).copy(alpha = 0.07f), Color.Transparent),
            center = Offset(w, 0f),
            radius = w * 0.45f
        )
        drawRect(brush = topRightFrost)
    }
}

