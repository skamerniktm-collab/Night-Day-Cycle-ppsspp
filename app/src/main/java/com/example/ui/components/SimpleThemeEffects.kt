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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.isActive
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// -----------------------------------------------------------------------------
// Data models for particles
// -----------------------------------------------------------------------------

private data class MovingParticle(
    val initialX: Float,
    val initialY: Float,
    val size: Float,
    val speedX: Float,
    val speedY: Float,
    val color: Color,
    val phase: Float,
    val wobbleSpeed: Float,
    val wobbleAmp: Float
)

// -----------------------------------------------------------------------------
// 1. Electric Charge: Fast electric sparks and trailing charges
// -----------------------------------------------------------------------------
@Composable
fun ElectricChargeEffect(modifier: Modifier = Modifier) {
    val density = LocalDensity.current.density
    var timeSeconds by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startNanos = withFrameNanos { it }
        while (isActive) {
            withFrameNanos { frameNanos ->
                timeSeconds = (frameNanos - startNanos) / 1_000_000_000f
            }
        }
    }

    val sparks = remember {
        val rnd = Random(42)
        val colors = listOf(
            Color(0xFF00E5FF), // Электрический синий
            Color(0xFFFF6D00), // Кислотный апельсин
            Color(0xFF76FF03), // Едкий салатовый
            Color(0xFFFF1774)  // Розовый неон
        )
        List(22) { i ->
            MovingParticle(
                initialX = rnd.nextFloat(),
                initialY = rnd.nextFloat(),
                size = rnd.nextFloat() * 4f + 3f, // 3 to 7 dp
                speedX = rnd.nextFloat() * 0.12f + 0.08f, // fast horizontal
                speedY = rnd.nextFloat() * 0.15f + 0.10f, // fast vertical
                color = colors[i % colors.size],
                phase = rnd.nextFloat() * 6.28f,
                wobbleSpeed = rnd.nextFloat() * 6f + 4f,
                wobbleAmp = rnd.nextFloat() * 20f + 10f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return@Canvas

        val t = timeSeconds

        sparks.forEachIndexed { idx, spark ->
            val curXRatio = (spark.initialX + t * spark.speedX) % 1f
            val curYRatio = (spark.initialY + t * spark.speedY) % 1f

            val wobble = sin(t * spark.wobbleSpeed + spark.phase) * spark.wobbleAmp * density
            val x = (curXRatio * w + wobble).coerceIn(0f, w)
            val y = curYRatio * h

            val sparkSize = spark.size * density
            val tailLen = (spark.speedY * 180f + 16f) * density

            // Draw glowing trailing electric tail
            drawLine(
                color = spark.color.copy(alpha = 0.25f),
                start = Offset(x - (spark.speedX * 60f * density), y - tailLen),
                end = Offset(x, y),
                strokeWidth = sparkSize * 0.6f
            )

            // Outer soft glow halo
            drawCircle(
                color = spark.color.copy(alpha = 0.20f),
                radius = sparkSize * 2.2f,
                center = Offset(x, y)
            )

            // Bright core
            drawCircle(
                color = spark.color.copy(alpha = 0.85f),
                radius = sparkSize,
                center = Offset(x, y)
            )

            // White hot center
            drawCircle(
                color = Color.White.copy(alpha = 0.90f),
                radius = sparkSize * 0.45f,
                center = Offset(x, y)
            )
        }
    }
}

// -----------------------------------------------------------------------------
// 2. Neon Spectrum: Smoothly gliding spectral orbs across the screen
// -----------------------------------------------------------------------------
@Composable
fun NeonSpectrumEffect(modifier: Modifier = Modifier) {
    val density = LocalDensity.current.density
    var timeSeconds by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startNanos = withFrameNanos { it }
        while (isActive) {
            withFrameNanos { frameNanos ->
                timeSeconds = (frameNanos - startNanos) / 1_000_000_000f
            }
        }
    }

    val orbs = remember {
        val rnd = Random(99)
        val colors = listOf(
            Color(0xFF00BFFF), // Лазурный
            Color(0xFFFF007F), // Маджентовый неон
            Color(0xFFF4FF52), // Неоновый лимон
            Color(0xFF00FFB2)  // Ярко-мятный
        )
        List(18) { i ->
            MovingParticle(
                initialX = rnd.nextFloat(),
                initialY = rnd.nextFloat(),
                size = rnd.nextFloat() * 8f + 6f, // 6 to 14 dp
                speedX = (rnd.nextFloat() * 0.08f + 0.04f) * (if (i % 2 == 0) 1f else -1f),
                speedY = rnd.nextFloat() * 0.06f + 0.03f,
                color = colors[i % colors.size],
                phase = rnd.nextFloat() * 6.28f,
                wobbleSpeed = rnd.nextFloat() * 2f + 1f,
                wobbleAmp = rnd.nextFloat() * 35f + 15f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return@Canvas

        val t = timeSeconds

        orbs.forEach { orb ->
            val curXRatio = ((orb.initialX + t * orb.speedX) % 1f + 1f) % 1f
            val curYRatio = ((orb.initialY + t * orb.speedY) % 1f + 1f) % 1f

            val wave = sin(t * orb.wobbleSpeed + orb.phase) * orb.wobbleAmp * density
            val x = (curXRatio * w).coerceIn(0f, w)
            val y = ((curYRatio * h) + wave).coerceIn(0f, h)

            val r = orb.size * density

            // Large outer glow
            drawCircle(
                color = orb.color.copy(alpha = 0.15f),
                radius = r * 2.8f,
                center = Offset(x, y)
            )

            // Medium ring
            drawCircle(
                color = orb.color.copy(alpha = 0.35f),
                radius = r * 1.5f,
                center = Offset(x, y)
            )

            // Core
            drawCircle(
                color = orb.color.copy(alpha = 0.75f),
                radius = r * 0.7f,
                center = Offset(x, y)
            )
        }
    }
}

// -----------------------------------------------------------------------------
// 3. Toxic Burst: Toxic bubbles & glowing spores rising upwards with wobble
// -----------------------------------------------------------------------------
@Composable
fun ToxicBurstEffect(modifier: Modifier = Modifier) {
    val density = LocalDensity.current.density
    var timeSeconds by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startNanos = withFrameNanos { it }
        while (isActive) {
            withFrameNanos { frameNanos ->
                timeSeconds = (frameNanos - startNanos) / 1_000_000_000f
            }
        }
    }

    val bubbles = remember {
        val rnd = Random(1337)
        val colors = listOf(
            Color(0xFF00FF40), // Ядовито-зеленый
            Color(0xFFFF1493), // Горячий розовый
            Color(0xFF00F0FF), // Неоново-голубой
            Color(0xFFFF5500)  // Оранжевый ультра
        )
        List(24) { i ->
            MovingParticle(
                initialX = rnd.nextFloat(),
                initialY = rnd.nextFloat(),
                size = rnd.nextFloat() * 9f + 5f, // 5 to 14 dp
                speedX = 0f,
                speedY = rnd.nextFloat() * 0.12f + 0.07f, // rises up
                color = colors[i % colors.size],
                phase = rnd.nextFloat() * 6.28f,
                wobbleSpeed = rnd.nextFloat() * 3.5f + 2f,
                wobbleAmp = rnd.nextFloat() * 24f + 10f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return@Canvas

        val t = timeSeconds

        bubbles.forEach { b ->
            // Upward translation
            val curYRatio = ((b.initialY - t * b.speedY) % 1f + 1f) % 1f
            val wobbleX = sin(t * b.wobbleSpeed + b.phase) * b.wobbleAmp * density
            val x = (b.initialX * w + wobbleX).coerceIn(0f, w)
            val y = curYRatio * h

            val r = b.size * density

            // Bubble outer glow
            drawCircle(
                color = b.color.copy(alpha = 0.16f),
                radius = r * 1.8f,
                center = Offset(x, y)
            )

            // Bubble border ring (Stroke)
            drawCircle(
                color = b.color.copy(alpha = 0.65f),
                radius = r,
                center = Offset(x, y),
                style = Stroke(width = 1.8f * density)
            )

            // Bubble inner core / highlight
            drawCircle(
                color = Color.White.copy(alpha = 0.50f),
                radius = r * 0.35f,
                center = Offset(x - r * 0.3f, y - r * 0.3f)
            )
        }
    }
}

// -----------------------------------------------------------------------------
// 4. Cyber Peach: Soft warm peach & cyan petals gently gliding diagonally
// -----------------------------------------------------------------------------
@Composable
fun CyberPeachEffect(modifier: Modifier = Modifier) {
    val density = LocalDensity.current.density
    var timeSeconds by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startNanos = withFrameNanos { it }
        while (isActive) {
            withFrameNanos { frameNanos ->
                timeSeconds = (frameNanos - startNanos) / 1_000_000_000f
            }
        }
    }

    val petals = remember {
        val rnd = Random(777)
        val colors = listOf(
            Color(0xFFFF7E67), // Кислотный персик
            Color(0xFF00F5D4), // Бирюзово-неоновый
            Color(0xFF9D4EDD), // Фиолетовый неон
            Color(0xFF76FF03)  // Мятный лайм
        )
        List(20) { i ->
            MovingParticle(
                initialX = rnd.nextFloat(),
                initialY = rnd.nextFloat(),
                size = rnd.nextFloat() * 8f + 6f, // 6 to 14 dp
                speedX = rnd.nextFloat() * 0.07f + 0.04f, // drift right
                speedY = rnd.nextFloat() * 0.08f + 0.05f, // drift down
                color = colors[i % colors.size],
                phase = rnd.nextFloat() * 6.28f,
                wobbleSpeed = rnd.nextFloat() * 2.5f + 1.5f,
                wobbleAmp = rnd.nextFloat() * 30f + 12f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return@Canvas

        val t = timeSeconds

        petals.forEach { p ->
            val curXRatio = ((p.initialX + t * p.speedX) % 1f + 1f) % 1f
            val curYRatio = ((p.initialY + t * p.speedY) % 1f + 1f) % 1f

            val wave = cos(t * p.wobbleSpeed + p.phase) * p.wobbleAmp * density
            val x = (curXRatio * w + wave).coerceIn(0f, w)
            val y = curYRatio * h

            val r = p.size * density

            // Warm halo
            drawCircle(
                color = p.color.copy(alpha = 0.18f),
                radius = r * 2.4f,
                center = Offset(x, y)
            )

            // Filled petal orb
            drawCircle(
                color = p.color.copy(alpha = 0.60f),
                radius = r,
                center = Offset(x, y)
            )

            // Highlight glint
            drawCircle(
                color = Color.White.copy(alpha = 0.70f),
                radius = r * 0.35f,
                center = Offset(x - r * 0.25f, y - r * 0.25f)
            )
        }
    }
}

// -----------------------------------------------------------------------------
// 5. Neon Emerald: Sparkling crystals and diamond stars falling smoothly
// -----------------------------------------------------------------------------
@Composable
fun NeonEmeraldEffect(modifier: Modifier = Modifier) {
    val density = LocalDensity.current.density
    var timeSeconds by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startNanos = withFrameNanos { it }
        while (isActive) {
            withFrameNanos { frameNanos ->
                timeSeconds = (frameNanos - startNanos) / 1_000_000_000f
            }
        }
    }

    val crystals = remember {
        val rnd = Random(555)
        val colors = listOf(
            Color(0xFF00E676), // Неоновый изумруд
            Color(0xFFFF007F), // Ультра-розовый
            Color(0xFFFFEE00), // Солнечно-лимонный
            Color(0xFF00B0FF)  // Кибер-синий
        )
        List(22) { i ->
            MovingParticle(
                initialX = rnd.nextFloat(),
                initialY = rnd.nextFloat(),
                size = rnd.nextFloat() * 7f + 5f, // 5 to 12 dp
                speedX = (rnd.nextFloat() - 0.5f) * 0.04f,
                speedY = rnd.nextFloat() * 0.08f + 0.05f, // falls down
                color = colors[i % colors.size],
                phase = rnd.nextFloat() * 6.28f,
                wobbleSpeed = rnd.nextFloat() * 4f + 2f,
                wobbleAmp = rnd.nextFloat() * 18f + 8f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return@Canvas

        val t = timeSeconds

        crystals.forEach { c ->
            val curYRatio = ((c.initialY + t * c.speedY) % 1f + 1f) % 1f
            val sway = sin(t * c.wobbleSpeed + c.phase) * c.wobbleAmp * density
            val x = (c.initialX * w + sway).coerceIn(0f, w)
            val y = curYRatio * h

            val sz = c.size * density
            val twinkle = (sin(t * 5f + c.phase) * 0.3f + 0.7f).coerceIn(0.4f, 1f)

            // Diamond crystal path
            val path = Path().apply {
                moveTo(x, y - sz * 1.3f * twinkle)
                lineTo(x + sz * twinkle, y)
                lineTo(x, y + sz * 1.3f * twinkle)
                lineTo(x - sz * twinkle, y)
                close()
            }

            // Glow around crystal
            drawCircle(
                color = c.color.copy(alpha = 0.20f * twinkle),
                radius = sz * 2.2f * twinkle,
                center = Offset(x, y)
            )

            // Crystal body
            drawPath(
                path = path,
                color = c.color.copy(alpha = 0.70f * twinkle)
            )

            // White center spark
            drawCircle(
                color = Color.White.copy(alpha = 0.85f * twinkle),
                radius = sz * 0.35f,
                center = Offset(x, y)
            )
        }
    }
}
