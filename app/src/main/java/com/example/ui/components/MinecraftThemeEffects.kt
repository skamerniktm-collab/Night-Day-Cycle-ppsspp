package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.isActive
import kotlin.random.Random

enum class MinecraftParticleType {
    BLOCK_CUBE,
    SPARKLE_CROSS,
    PIXEL_ORB
}

private data class MinecraftParticle(
    val initialXRatio: Float,
    val initialYRatio: Float,
    val sizeDp: Float,
    val speed: Float,
    val type: MinecraftParticleType,
    val color: Color,
    val lightColor: Color,
    val darkColor: Color,
    val horizontalWobbleDp: Float,
    val phaseOffset: Float
)

val MinecraftParticlePalettes = listOf(
    // Prismarine
    Triple(Color(0xFF5AB6A8), Color(0xFF8CE3D7), Color(0xFF23554D)),
    // Gold Block
    Triple(Color(0xFFFCD836), Color(0xFFFFF07A), Color(0xFFB58A00)),
    // Amethyst Shard
    Triple(Color(0xFF9A5CC0), Color(0xFFC78BF0), Color(0xFF4C1D6E)),
    // Diamond / Sea Lantern
    Triple(Color(0xFF55FFFF), Color(0xFFBFFFFF), Color(0xFF138585)),
    // Nether Wart
    Triple(Color(0xFFB5222E), Color(0xFFF04D5C), Color(0xFF590C12)),
    // Copper Block
    Triple(Color(0xFFD47C59), Color(0xFFF2A385), Color(0xFF78361C)),
    // Emerald
    Triple(Color(0xFF17DD62), Color(0xFF75FFAB), Color(0xFF0B662C))
)

@Composable
fun MinecraftBlockParticlesOverlay(
    modifier: Modifier = Modifier,
    particleCount: Int = 22,
    alphaMultiplier: Float = 0.85f
) {
    val density = LocalDensity.current
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startNanos = withFrameNanos { it }
        while (isActive) {
            withFrameNanos { frameNanos ->
                val elapsedMillis = (frameNanos - startNanos) / 1_000_000L
                time = (elapsedMillis % 5500L) / 5500f
            }
        }
    }

    val particles = remember(particleCount) {
        val random = Random(128)
        List(particleCount) { index ->
            val palette = MinecraftParticlePalettes[index % MinecraftParticlePalettes.size]
            val type = when (index % 3) {
                0 -> MinecraftParticleType.BLOCK_CUBE
                1 -> MinecraftParticleType.SPARKLE_CROSS
                else -> MinecraftParticleType.PIXEL_ORB
            }
            MinecraftParticle(
                initialXRatio = random.nextFloat(),
                initialYRatio = random.nextFloat(),
                sizeDp = when (type) {
                    MinecraftParticleType.BLOCK_CUBE -> random.nextInt(8, 16).toFloat()
                    MinecraftParticleType.SPARKLE_CROSS -> random.nextInt(9, 15).toFloat()
                    MinecraftParticleType.PIXEL_ORB -> random.nextInt(5, 10).toFloat()
                },
                speed = random.nextFloat() * 0.45f + 0.65f,
                type = type,
                color = palette.first,
                lightColor = palette.second,
                darkColor = palette.third,
                horizontalWobbleDp = (random.nextFloat() - 0.5f) * 24f,
                phaseOffset = random.nextFloat()
            )
        }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        if (width <= 0 || height <= 0) return@Canvas

        particles.forEach { p ->
            val progress = (time * p.speed + p.phaseOffset) % 1f
            val currentY = (1f - progress) * (height + 60f) - 30f
            val wobbleXPx = with(density) {
                (kotlin.math.sin((progress * 2 * Math.PI.toFloat()) + p.phaseOffset * 8f) * p.horizontalWobbleDp).dp.toPx()
            }
            val sizePx = with(density) { p.sizeDp.dp.toPx() }
            val currentX = (p.initialXRatio * width + wobbleXPx).coerceIn(0f, width - sizePx)

            // Calculate smooth vertical alpha fading
            val alphaSine = kotlin.math.sin(progress * Math.PI.toFloat()).coerceIn(0f, 1f)
            val finalAlpha = (alphaSine * alphaMultiplier).coerceIn(0f, 1f)

            when (p.type) {
                MinecraftParticleType.BLOCK_CUBE -> {
                    drawMinecraftBlock(
                        x = currentX,
                        y = currentY,
                        size = sizePx,
                        faceColor = p.color.copy(alpha = finalAlpha),
                        lightBevel = p.lightColor.copy(alpha = finalAlpha),
                        darkShadow = p.darkColor.copy(alpha = finalAlpha)
                    )
                }
                MinecraftParticleType.SPARKLE_CROSS -> {
                    drawMinecraftSparkle(
                        x = currentX,
                        y = currentY,
                        size = sizePx,
                        coreColor = p.lightColor.copy(alpha = finalAlpha),
                        armColor = p.color.copy(alpha = (finalAlpha * 0.85f).coerceIn(0f, 1f))
                    )
                }
                MinecraftParticleType.PIXEL_ORB -> {
                    drawMinecraftPixelOrb(
                        x = currentX,
                        y = currentY,
                        size = sizePx,
                        color = p.color.copy(alpha = finalAlpha),
                        highlight = p.lightColor.copy(alpha = finalAlpha)
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawMinecraftBlock(
    x: Float,
    y: Float,
    size: Float,
    faceColor: Color,
    lightBevel: Color,
    darkShadow: Color
) {
    val bevel = (size * 0.22f).coerceAtLeast(2f)

    // Base face
    drawRect(
        color = faceColor,
        topLeft = Offset(x, y),
        size = Size(size, size)
    )
    // Top highlight bevel (light source from top-left)
    drawRect(
        color = lightBevel,
        topLeft = Offset(x, y),
        size = Size(size, bevel)
    )
    drawRect(
        color = lightBevel,
        topLeft = Offset(x, y),
        size = Size(bevel, size)
    )
    // Bottom & right shadow bevels
    drawRect(
        color = darkShadow,
        topLeft = Offset(x, y + size - bevel),
        size = Size(size, bevel)
    )
    drawRect(
        color = darkShadow,
        topLeft = Offset(x + size - bevel, y),
        size = Size(bevel, size)
    )
}

private fun DrawScope.drawMinecraftSparkle(
    x: Float,
    y: Float,
    size: Float,
    coreColor: Color,
    armColor: Color
) {
    val step = (size / 3f).coerceAtLeast(2f)
    // Center pixel
    drawRect(
        color = coreColor,
        topLeft = Offset(x + step, y + step),
        size = Size(step, step)
    )
    // Top, bottom, left, right sparkle arms
    drawRect(
        color = armColor,
        topLeft = Offset(x + step, y),
        size = Size(step, step)
    )
    drawRect(
        color = armColor,
        topLeft = Offset(x + step, y + 2 * step),
        size = Size(step, step)
    )
    drawRect(
        color = armColor,
        topLeft = Offset(x, y + step),
        size = Size(step, step)
    )
    drawRect(
        color = armColor,
        topLeft = Offset(x + 2 * step, y + step),
        size = Size(step, step)
    )
}

private fun DrawScope.drawMinecraftPixelOrb(
    x: Float,
    y: Float,
    size: Float,
    color: Color,
    highlight: Color
) {
    drawRect(
        color = color,
        topLeft = Offset(x, y),
        size = Size(size, size)
    )
    val dot = (size * 0.45f).coerceAtLeast(1.5f)
    drawRect(
        color = highlight,
        topLeft = Offset(x, y),
        size = Size(dot, dot)
    )
}

@Composable
fun MinecraftCardShimmerOverlay(
    modifier: Modifier = Modifier
) {
    var shimmerProgress by remember { mutableFloatStateOf(-0.5f) }

    LaunchedEffect(Unit) {
        val startNanos = withFrameNanos { it }
        while (isActive) {
            withFrameNanos { frameNanos ->
                val elapsedMillis = (frameNanos - startNanos) / 1_000_000L
                val fraction = (elapsedMillis % 3000L) / 3000f
                shimmerProgress = -0.5f + fraction * 2.0f
            }
        }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        if (width <= 0 || height <= 0) return@Canvas

        val startX = shimmerProgress * (width + 300f) - 150f
        val brush = Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                Color(0x305AB6A8), // Prismarine glint
                Color(0x75FCD836), // Vibrant Gold glint
                Color(0x8055FFFF), // Diamond flash
                Color(0x459A5CC0), // Amethyst glint
                Color.Transparent
            ),
            start = Offset(startX, 0f),
            end = Offset(startX + 180f, height)
        )

        drawRect(brush = brush)
    }
}
