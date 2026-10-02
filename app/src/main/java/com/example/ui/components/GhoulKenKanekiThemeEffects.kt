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
 * Types of materials/motifs in the Ghoul Ken Kaneki theme.
 */
enum class GhoulBlockType {
    WHITE_REBIRTH,    // Белый — символ перерождения и потери наивности (световой блик / сход краски с волос)
    BLACK_ALTER_EGO,  // Черный — цвет фирменной одежды, альтер-эго (чернильная дымка и тени)
    GREY_HAISE,       // Серый — следователь Сасаки Хайсе (эффект старой пленки / монохромного шума)
    RED_KAKUGU,       // Красный — цвет хищной природы, какугу и ярости (свечение / мерцание какугу)
    GOLDEN_HOPE       // Золотой — символ надежды, примирения и счастья (теплые искорки и сияние)
}

/**
 * High fidelity custom canvas swatch illustrating the 5 Ghoul Ken Kaneki materials with their effects.
 */
@Composable
fun GhoulBlockSwatch(
    type: GhoulBlockType,
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
                GhoulBlockType.WHITE_REBIRTH -> drawWhiteRebirth(this)
                GhoulBlockType.BLACK_ALTER_EGO -> drawBlackAlterEgo(this)
                GhoulBlockType.GREY_HAISE -> drawGreyHaise(this)
                GhoulBlockType.RED_KAKUGU -> drawRedKakugu(this)
                GhoulBlockType.GOLDEN_HOPE -> drawGoldenHope(this)
            }
        }
    }
}

private fun drawWhiteRebirth(drawScope: DrawScope) {
    val w = drawScope.size.width
    val h = drawScope.size.height

    // White base with fading hair-dye transition
    drawScope.drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFFFFFFF), Color(0xFFF0F2F6), Color(0xFFD6DBE4))
        )
    )

    // Diagonal hair flare streak (light specular shine)
    val flareBrush = Brush.linearGradient(
        colors = listOf(Color.Transparent, Color.White, Color.Transparent),
        start = Offset(0f, h * 0.7f),
        end = Offset(w, h * 0.2f)
    )
    drawScope.drawLine(
        brush = flareBrush,
        start = Offset(0f, h * 0.7f),
        end = Offset(w, h * 0.2f),
        strokeWidth = 3.0f
    )

    // Faint residual black hair roots at the very bottom
    drawScope.drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color(0x33202025), Color(0x77101015)),
            startY = h * 0.75f,
            endY = h
        )
    )
}

private fun drawBlackAlterEgo(drawScope: DrawScope) {
    val w = drawScope.size.width
    val h = drawScope.size.height

    // Deep pitch black of Kaneki's battle suit
    drawScope.drawRect(color = Color(0xFF09090C))

    // Dark coagulated blood vignette along edges
    val bloodVignette = Brush.radialGradient(
        colors = listOf(Color(0xFF220A10), Color(0xFF140306), Color(0xFF050102)),
        center = Offset(w * 0.5f, h * 0.5f),
        radius = w * 0.70f
    )
    drawScope.drawRect(brush = bloodVignette)

    // Dripping crimson blood streaks running down
    val dripPath1 = Path().apply {
        moveTo(w * 0.28f, 0f)
        lineTo(w * 0.28f, h * 0.45f)
        cubicTo(w * 0.28f, h * 0.65f, w * 0.38f, h * 0.65f, w * 0.38f, h * 0.45f)
        lineTo(w * 0.38f, 0f)
        close()
    }
    drawScope.drawPath(
        path = dripPath1,
        brush = Brush.verticalGradient(
            colors = listOf(Color(0x88FF1744), Color(0xCCB71C1C), Color(0xFF6A000A))
        )
    )
    drawScope.drawCircle(
        color = Color(0xFFFF1744),
        radius = w * 0.065f,
        center = Offset(w * 0.33f, h * 0.55f)
    )

    // Secondary fine blood droplet
    drawScope.drawCircle(
        color = Color(0xCCFF1744),
        radius = w * 0.045f,
        center = Offset(w * 0.72f, h * 0.72f)
    )
}

private fun drawGreyHaise(drawScope: DrawScope) {
    val w = drawScope.size.width
    val h = drawScope.size.height

    // Monochrome balance base grey
    drawScope.drawRect(color = Color(0xFF5E6573))

    // Dual-tone split (white and black hair of Sasaki Haise)
    drawScope.drawRect(
        brush = Brush.horizontalGradient(
            colors = listOf(Color(0x55FFFFFF), Color(0x22FFFFFF), Color(0x44000000), Color(0x88000000))
        )
    )

    // Vintage film grain / monochrome scan lines
    val scanlines = listOf(0.18f, 0.38f, 0.58f, 0.78f, 0.92f)
    scanlines.forEach { frac ->
        drawScope.drawLine(
            color = Color(0x25FFFFFF),
            start = Offset(0f, frac * h),
            end = Offset(w, frac * h),
            strokeWidth = 0.8f
        )
    }

    // Fine monochrome noise dots
    val dots = listOf(
        Pair(0.2f, 0.3f), Pair(0.45f, 0.22f), Pair(0.7f, 0.45f),
        Pair(0.35f, 0.75f), Pair(0.8f, 0.8f), Pair(0.15f, 0.6f)
    )
    dots.forEach { (xf, yf) ->
        drawScope.drawCircle(
            color = Color(0x40FFFFFF),
            radius = 0.8f,
            center = Offset(xf * w, yf * h)
        )
    }
}

private fun drawRedKakugu(drawScope: DrawScope) {
    val w = drawScope.size.width
    val h = drawScope.size.height

    // Black sclera base
    drawScope.drawRect(color = Color(0xFF120306))

    // Glowing intense red iris / kakugan pupil center
    val kakuganGlow = Brush.radialGradient(
        colors = listOf(
            Color(0xFFFF2A4A),
            Color(0xFFE50914),
            Color(0xFF8B0000),
            Color(0x33330000)
        ),
        center = Offset(w * 0.5f, h * 0.5f),
        radius = w * 0.45f
    )
    drawScope.drawCircle(
        brush = kakuganGlow,
        radius = w * 0.42f,
        center = Offset(w * 0.5f, h * 0.5f)
    )

    // Intense central core
    drawScope.drawCircle(
        color = Color(0xFFFF6677),
        radius = w * 0.15f,
        center = Offset(w * 0.5f, h * 0.5f)
    )

    // Red branching ghoul veins (kagune capillaries)
    drawScope.drawLine(
        color = Color(0xCCFF1744),
        start = Offset(w * 0.5f, h * 0.5f),
        end = Offset(w * 0.15f, h * 0.2f),
        strokeWidth = 1.0f
    )
    drawScope.drawLine(
        color = Color(0xCCFF1744),
        start = Offset(w * 0.5f, h * 0.5f),
        end = Offset(w * 0.85f, h * 0.75f),
        strokeWidth = 1.0f
    )
}

private fun drawGoldenHope(drawScope: DrawScope) {
    val w = drawScope.size.width
    val h = drawScope.size.height

    // Warm radiant gold base
    drawScope.drawRect(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFFE066), Color(0xFFFFB300), Color(0xFFCC8400)),
            center = Offset(w * 0.5f, h * 0.5f),
            radius = w * 0.6f
        )
    )

    // Warm sparkling glint cross
    val cx = w * 0.5f
    val cy = h * 0.5f
    val sparkSize = w * 0.35f

    drawScope.drawLine(
        color = Color.White,
        start = Offset(cx - sparkSize, cy),
        end = Offset(cx + sparkSize, cy),
        strokeWidth = 1.2f
    )
    drawScope.drawLine(
        color = Color.White,
        start = Offset(cx, cy - sparkSize),
        end = Offset(cx, cy + sparkSize),
        strokeWidth = 1.2f
    )

    // Outer warm halo
    drawScope.drawCircle(
        color = Color(0x66FFF59D),
        radius = w * 0.2f,
        center = Offset(cx, cy)
    )
}

/**
 * Animated particles overlay for Ghoul Ken Kaneki theme (Kakugan red embers, white rebirth flares, golden hope sparks).
 */
@Composable
fun GhoulParticlesOverlay(
    modifier: Modifier = Modifier,
    particleCount: Int = 16,
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

    val seed = remember { 1007L }
    val particles = remember {
        val rng = Random(seed)
        List(particleCount) {
            GhoulParticle(
                xInit = rng.nextFloat(),
                yInit = rng.nextFloat(),
                speed = 0.07f + rng.nextFloat() * 0.12f,
                drift = (rng.nextFloat() - 0.5f) * 0.08f,
                radius = 1.4f + rng.nextFloat() * 2.2f,
                color = when (rng.nextInt(5)) {
                    0 -> Color(0xFFFF1744) // Kakugan Crimson
                    1 -> Color(0xFFFFD700) // Golden Hope
                    2 -> Color(0xFFFFFFFF) // White Rebirth
                    3 -> Color(0xFFD50000) // Deep Kagune Red
                    else -> Color(0xFF9E9E9E) // Haise Grey
                }
            )
        }
    }

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        particles.forEach { p ->
            // Particles drift upwards like rising embers and memories
            val y = ((p.yInit - animClock * p.speed) % 1.0f + 1.0f) % 1.0f * h
            val x = ((p.xInit + animClock * p.drift) % 1.0f + 1.0f) % 1.0f * w

            drawCircle(
                color = p.color.copy(alpha = alphaMultiplier * 0.8f),
                radius = p.radius,
                center = Offset(x, y)
            )
        }
    }
}

private data class GhoulParticle(
    val xInit: Float,
    val yInit: Float,
    val speed: Float,
    val drift: Float,
    val radius: Float,
    val color: Color
)

/**
 * Shimmer effect for Ghoul Ken Kaneki theme cards (slash-style swift light and blood crimson reflection).
 */
@Composable
fun GhoulCardShimmerOverlay(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "ghoul_shimmer")
    val shimmerTranslate by transition.animateFloat(
        initialValue = -350f,
        targetValue = 950f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ghoul_shimmer_translate"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val shimmerBrush = Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                Color(0xFFFF1744).copy(alpha = 0.08f),
                Color(0xFFFFFFFF).copy(alpha = 0.18f),
                Color(0xFFFFD700).copy(alpha = 0.09f),
                Color.Transparent
            ),
            start = Offset(shimmerTranslate, 0f),
            end = Offset(shimmerTranslate + 200f, h)
        )

        drawRect(brush = shimmerBrush)
    }
}

/**
 * Fullscreen ambient atmosphere overlay for the Ghoul Ken Kaneki theme.
 * Gives an atmospheric dark vignette with subtle pulsing kakugan crimson aura in top corners
 * and warm golden hopeful glimmer along the bottom edge.
 */
@Composable
fun GhoulAmbientAuraOverlay(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Top edge crimson Kakugan glow
        val topKakuganGradient = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFF1744).copy(alpha = 0.13f),
                Color(0xFF8B0000).copy(alpha = 0.05f),
                Color.Transparent
            ),
            startY = 0f,
            endY = h * 0.22f
        )
        drawRect(brush = topKakuganGradient)

        // Bottom hopeful golden dawn glow (reconciliation & happiness)
        val bottomGoldGradient = Brush.verticalGradient(
            colors = listOf(
                Color.Transparent,
                Color(0xFFFFB300).copy(alpha = 0.04f),
                Color(0xFFFFD700).copy(alpha = 0.10f)
            ),
            startY = h * 0.82f,
            endY = h
        )
        drawRect(brush = bottomGoldGradient)

        // Top-left Kakugan eye aura
        val topLeftKakugan = Brush.radialGradient(
            colors = listOf(Color(0xFFFF1744).copy(alpha = 0.10f), Color.Transparent),
            center = Offset(0f, 0f),
            radius = w * 0.55f
        )
        drawRect(brush = topLeftKakugan)

        // Top-right dark ink shadow
        val topRightInk = Brush.radialGradient(
            colors = listOf(Color(0x33000000), Color.Transparent),
            center = Offset(w, 0f),
            radius = w * 0.5f
        )
        drawRect(brush = topRightInk)
    }
}

/**
 * Animated dynamic border brush for the Ghoul Ken Kaneki card.
 * Flowing blood crimson, blinding white rebirth light, ink black, and golden hope.
 */
@Composable
fun rememberGhoulCardBorderBrush(
    isSelected: Boolean = false
): Brush {
    val transition = rememberInfiniteTransition(label = "ghoul_border")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isSelected) 3000 else 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ghoul_border_angle"
    )

    val rad = Math.toRadians(angle.toDouble())
    val cos = Math.cos(rad).toFloat()
    val sin = Math.sin(rad).toFloat()

    // Smoothly rotating gradient vector
    return Brush.linearGradient(
        colors = listOf(
            Color(0xFFFF1744), // Scarlet Kakugan
            Color(0xFFFFFFFF), // Rebirth White
            Color(0xFF221118), // Deep Shadow Black
            Color(0xFFFFD700), // Hope Gold
            Color(0xFFFF1744)  // Scarlet Loop
        ),
        start = Offset(500f * (1f - cos), 300f * (1f - sin)),
        end = Offset(500f * (1f + cos), 300f * (1f + sin))
    )
}

/**
 * Highly unique, fresh and bespoke visual effect specifically for the Ghoul Ken Kaneki card:
 * 1. Undulating Rinkaku Kagune tentacles snaking organically through the background
 * 2. Kakugan heartbeat eye with pulsing vascular capillary spiderwebs
 * 3. Fluttering Red Spider Lily (Ликорис) petals and white transformation hair sparks
 * 4. Sasaki Haise chromatic aberration memory glitch wave
 * 5. Golden hope glimmers rising softly from the footer
 */
@Composable
fun GhoulCardBespokeOverlay(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
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

    // Ликорис (Red Spider Lily) & transformation petal structures
    val petalRng = remember { Random(777L) }
    val petals = remember {
        List(14) {
            GhoulPetal(
                xNorm = petalRng.nextFloat(),
                yNorm = petalRng.nextFloat(),
                speed = 0.08f + petalRng.nextFloat() * 0.10f,
                swaySpeed = 1.2f + petalRng.nextFloat() * 2.0f,
                swayAmp = 18f + petalRng.nextFloat() * 24f,
                rotationSpeed = (petalRng.nextFloat() - 0.5f) * 120f,
                length = 7f + petalRng.nextFloat() * 8f,
                width = 2.2f + petalRng.nextFloat() * 2.5f,
                type = when (petalRng.nextInt(6)) {
                    0, 1, 2 -> 0 // Red Spider Lily (Higanbana petal)
                    3, 4 -> 1    // White hair fragment (Rebirth)
                    else -> 2    // Golden hope spark
                }
            )
        }
    }

    // Blood Drops system (gravitational dripping & falling droplets with fluid physics)
    val bloodDrops = remember {
        val bRng = Random(1007L)
        List(7) { idx ->
            BloodDropData(
                xNorm = 0.08f + (idx * 0.135f) + (bRng.nextFloat() - 0.5f) * 0.04f,
                speed = 0.35f + bRng.nextFloat() * 0.28f,
                phase = bRng.nextFloat(),
                radius = 3.2f + bRng.nextFloat() * 2.4f
            )
        }
    }

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // 1. Viscous Dark Coagulated Blood Vignette (replacing generic smoke)
        val bloodVignette = Brush.radialGradient(
            colors = listOf(
                Color.Transparent,
                Color(0x332B0007),
                Color(0x88120104)
            ),
            center = Offset(w * 0.45f, h * 0.5f),
            radius = w * 0.80f
        )
        drawRect(brush = bloodVignette)

        // 2. Coagulated blood pool with ripples along the bottom edge
        drawBottomBloodPool(
            drawScope = this,
            w = w,
            h = h,
            clock = animClock
        )

        // 3. Hanging & dripping blood streams along the top frame
        drawTopBloodDrips(
            drawScope = this,
            w = w,
            h = h,
            clock = animClock,
            drops = bloodDrops
        )

        // 4. Falling teardrop blood droplets with glossy wet highlights
        drawFallingBloodDrops(
            drawScope = this,
            w = w,
            h = h,
            clock = animClock,
            drops = bloodDrops
        )

        // 5. Periodic arterial blood slash & spray splatters
        drawArterialBloodSlash(
            drawScope = this,
            w = w,
            h = h,
            clock = animClock
        )

        // 6. Animated Undulating Rinkaku Kagune Tentacles with surging arterial blood
        drawRinkakuKagune(
            drawScope = this,
            w = w,
            h = h,
            clock = animClock,
            intensity = if (isSelected) 1.2f else 1.0f
        )

        // 7. Heartbeat Kakugan Eye with Branching Blood Capillaries in top-right
        drawKakuganEyePulse(
            drawScope = this,
            eyeCenter = Offset(w * 0.88f, h * 0.22f),
            clock = animClock,
            isSelected = isSelected
        )

        // 8. Sasaki Haise Memory Scanline / Chromatic Glitch Flash (every 4 seconds)
        drawHaiseMemoryGlitch(
            drawScope = this,
            w = w,
            h = h,
            clock = animClock
        )

        // 9. Drifting Higanbana (Spider Lily) Petals & Hair Strands
        petals.forEach { p ->
            val y = ((p.yNorm + animClock * p.speed) % 1.0f) * h
            val sway = kotlin.math.sin(animClock * p.swaySpeed + p.xNorm * 10f) * p.swayAmp
            val x = ((p.xNorm * w + sway) % w + w) % w
            val rot = p.rotationSpeed * animClock

            drawPetal(
                drawScope = this,
                center = Offset(x, y),
                length = p.length,
                width = p.width,
                rotDeg = rot,
                type = p.type
            )
        }
    }
}

private data class BloodDropData(
    val xNorm: Float,
    val speed: Float,
    val phase: Float,
    val radius: Float
)

private fun drawBottomBloodPool(
    drawScope: DrawScope,
    w: Float,
    h: Float,
    clock: Float
) {
    // Viscous organic blood accumulation pool at the bottom
    val poolHeight = h * 0.12f
    val poolTop = h - poolHeight

    val path = Path().apply {
        moveTo(0f, h)
        lineTo(0f, poolTop + kotlin.math.sin(clock * 1.8f) * 4f)
        cubicTo(
            w * 0.25f, poolTop + kotlin.math.cos(clock * 1.5f) * 6f,
            w * 0.65f, poolTop + kotlin.math.sin(clock * 1.2f) * 5f,
            w, poolTop + kotlin.math.cos(clock * 2.0f) * 3f
        )
        lineTo(w, h)
        close()
    }

    drawScope.drawPath(
        path = path,
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0x88FF1744), // Surface meniscus red
                Color(0xCCB71C1C), // Deep crimson
                Color(0xFF4A000A)  // Dark coagulated bottom
            ),
            startY = poolTop - 5f,
            endY = h
        )
    )

    // Blood splash ripple rings on surface
    val rippleCycle = (clock * 1.4f) % 1.0f
    val rippleRadius = rippleCycle * 28f
    val rippleAlpha = (1f - rippleCycle).coerceIn(0f, 1f) * 0.45f

    drawScope.drawCircle(
        color = Color(0xFFFF1744).copy(alpha = rippleAlpha),
        radius = rippleRadius,
        center = Offset(w * 0.35f, h - 8f),
        style = Stroke(width = 1.2f)
    )
    drawScope.drawCircle(
        color = Color(0xFFFF1744).copy(alpha = rippleAlpha * 0.8f),
        radius = rippleRadius * 0.7f,
        center = Offset(w * 0.68f, h - 6f),
        style = Stroke(width = 1.0f)
    )
}

private fun drawTopBloodDrips(
    drawScope: DrawScope,
    w: Float,
    h: Float,
    clock: Float,
    drops: List<BloodDropData>
) {
    // Multiple hanging blood drip stalactites along the top edge
    drops.forEachIndexed { i, d ->
        val x = d.xNorm * w
        val cycle = (clock * d.speed + d.phase) % 1.0f

        // Swelling phase: drip elongates from top before releasing droplet
        val dripLen = if (cycle < 0.45f) {
            val stretch = cycle / 0.45f
            8f + stretch * (14f + (i % 3) * 6f)
        } else {
            // Retracts slightly after pinch-off
            val retract = (cycle - 0.45f) / 0.55f
            8f + (1f - retract) * 6f
        }

        val dripWidth = d.radius * 1.5f

        val path = Path().apply {
            moveTo(x - dripWidth, 0f)
            lineTo(x - dripWidth * 0.5f, dripLen * 0.6f)
            cubicTo(
                x - dripWidth * 0.7f, dripLen,
                x + dripWidth * 0.7f, dripLen,
                x + dripWidth * 0.5f, dripLen * 0.6f
            )
            lineTo(x + dripWidth, 0f)
            close()
        }

        drawScope.drawPath(
            path = path,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF6B000C),
                    Color(0xFFB71C1C),
                    Color(0xFFFF1744)
                ),
                startY = 0f,
                endY = dripLen
            )
        )

        // Glossy bulbous droplet swelling at tip
        if (cycle < 0.45f) {
            val bulbRadius = d.radius * (0.8f + (cycle / 0.45f) * 0.6f)
            drawScope.drawCircle(
                color = Color(0xFFFF1744),
                radius = bulbRadius,
                center = Offset(x, dripLen)
            )
            drawScope.drawCircle(
                color = Color(0xCCFFFFFF),
                radius = bulbRadius * 0.35f,
                center = Offset(x - bulbRadius * 0.3f, dripLen - bulbRadius * 0.3f)
            )
        }
    }
}

private fun drawFallingBloodDrops(
    drawScope: DrawScope,
    w: Float,
    h: Float,
    clock: Float,
    drops: List<BloodDropData>
) {
    drops.forEach { d ->
        val cycle = (clock * d.speed + d.phase) % 1.0f

        // Falls down with gravitational acceleration after detachment (cycle >= 0.40f)
        if (cycle >= 0.40f) {
            val t = (cycle - 0.40f) / 0.60f
            val y = (t * t) * (h + 20f) // Quadratic gravitational acceleration
            val x = d.xNorm * w
            val r = d.radius

            if (y <= h) {
                // Teardrop path: rounded bottom with tapered pointed tail pointing up
                val teardrop = Path().apply {
                    val tailY = y - r * 2.8f
                    moveTo(x, tailY)
                    cubicTo(x + r * 1.3f, y - r * 0.8f, x + r * 1.3f, y + r, x, y + r)
                    cubicTo(x - r * 1.3f, y + r, x - r * 1.3f, y - r * 0.8f, x, tailY)
                    close()
                }

                // Rich crimson arterial gradient
                drawScope.drawPath(
                    path = teardrop,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFF5252),
                            Color(0xFFFF1744),
                            Color(0xFFB71C1C),
                            Color(0xFF5A000A)
                        ),
                        startY = y - r * 2.8f,
                        endY = y + r
                    )
                )

                // Glossy wet specular highlight reflecting light
                drawScope.drawCircle(
                    color = Color(0xDDFFFFFF),
                    radius = r * 0.35f,
                    center = Offset(x - r * 0.35f, y + r * 0.1f)
                )

                // Trailing micro-droplet
                drawScope.drawCircle(
                    color = Color(0xAAFF1744),
                    radius = r * 0.45f,
                    center = Offset(x, y - r * 4.2f)
                )
            }
        }
    }
}

private fun drawArterialBloodSlash(
    drawScope: DrawScope,
    w: Float,
    h: Float,
    clock: Float
) {
    // Dynamic arterial blood spurt / slash occurs periodically every 3.6 seconds
    val period = 3.6f
    val t = clock % period

    if (t < 0.70f) {
        val progress = t / 0.70f
        val alpha = (1f - progress).coerceIn(0f, 1f)

        // Dynamic slashing arc across the card
        val slashStart = Offset(w * 0.15f, h * 0.25f)
        val slashEnd = Offset(w * 0.15f + w * 0.70f * progress, h * 0.25f + h * 0.55f * progress)

        val slashPath = Path().apply {
            moveTo(slashStart.x, slashStart.y)
            quadraticBezierTo(
                w * 0.55f + kotlin.math.sin(clock * 4f) * 15f,
                h * 0.45f,
                slashEnd.x,
                slashEnd.y
            )
        }

        drawScope.drawPath(
            path = slashPath,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFFF1744).copy(alpha = alpha * 0.9f),
                    Color(0xFFB71C1C).copy(alpha = alpha * 0.7f),
                    Color(0xFFFF5252).copy(alpha = alpha * 0.85f)
                ),
                start = slashStart,
                end = slashEnd
            ),
            style = Stroke(width = (4.5f * (1f - progress * 0.5f)))
        )

        // Radial blood spray droplets from the cut
        val sprayCount = 8
        for (k in 0 until sprayCount) {
            val sprayDist = progress * (30f + k * 12f)
            val angle = 0.8f + (k - sprayCount / 2f) * 0.35f
            val sx = slashEnd.x + kotlin.math.cos(angle) * sprayDist
            val sy = slashEnd.y + kotlin.math.sin(angle) * sprayDist

            drawScope.drawCircle(
                color = Color(0xFFFF1744).copy(alpha = alpha * 0.85f),
                radius = (2.2f + (k % 3) * 0.8f) * (1f - progress * 0.3f),
                center = Offset(sx, sy)
            )
        }
    }
}

private data class GhoulPetal(
    val xNorm: Float,
    val yNorm: Float,
    val speed: Float,
    val swaySpeed: Float,
    val swayAmp: Float,
    val rotationSpeed: Float,
    val length: Float,
    val width: Float,
    val type: Int // 0: Red Spider Lily, 1: White Hair, 2: Golden Hope
)

private fun drawRinkakuKagune(
    drawScope: DrawScope,
    w: Float,
    h: Float,
    clock: Float,
    intensity: Float
) {
    // Tentacle 1: Large primary Rinkaku looping from bottom-left to top-center
    val p1Start = Offset(0f, h * 0.85f)
    val p1Ctrl1 = Offset(
        w * 0.25f + kotlin.math.sin(clock * 1.6f) * 35f,
        h * 0.35f + kotlin.math.cos(clock * 1.4f) * 25f
    )
    val p1Ctrl2 = Offset(
        w * 0.65f + kotlin.math.cos(clock * 1.8f) * 40f,
        h * 0.80f + kotlin.math.sin(clock * 1.5f) * 30f
    )
    val p1End = Offset(
        w * 0.95f,
        h * 0.45f + kotlin.math.sin(clock * 2.0f) * 20f
    )

    val path1 = Path().apply {
        moveTo(p1Start.x, p1Start.y)
        cubicTo(p1Ctrl1.x, p1Ctrl1.y, p1Ctrl2.x, p1Ctrl2.y, p1End.x, p1End.y)
    }

    // Outer Kagune Glow
    drawScope.drawPath(
        path = path1,
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0x33FF1744),
                Color(0x44D50000),
                Color(0x55FF1744)
            )
        ),
        style = Stroke(width = 14f * intensity)
    )

    // Inner Deep Crimson Body
    drawScope.drawPath(
        path = path1,
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0x99660011),
                Color(0xCCB71C1C),
                Color(0xDDFF1744),
                Color(0xCC880E4F)
            )
        ),
        style = Stroke(width = 6f * intensity)
    )

    // Glowing scarlet central nerve spine
    drawScope.drawPath(
        path = path1,
        color = Color(0xEEFF5252),
        style = Stroke(width = 1.8f * intensity)
    )

    // Tentacle 2: Secondary lower Rinkaku creeping across the bottom
    val p2Start = Offset(w * 0.1f, h * 0.98f)
    val p2Ctrl1 = Offset(
        w * 0.40f + kotlin.math.cos(clock * 1.3f) * 28f,
        h * 0.68f + kotlin.math.sin(clock * 1.5f) * 18f
    )
    val p2Ctrl2 = Offset(
        w * 0.75f + kotlin.math.sin(clock * 1.7f) * 32f,
        h * 0.92f + kotlin.math.cos(clock * 1.2f) * 22f
    )
    val p2End = Offset(w * 0.98f, h * 0.72f)

    val path2 = Path().apply {
        moveTo(p2Start.x, p2Start.y)
        cubicTo(p2Ctrl1.x, p2Ctrl1.y, p2Ctrl2.x, p2Ctrl2.y, p2End.x, p2End.y)
    }

    drawScope.drawPath(
        path = path2,
        color = Color(0x30FF1744),
        style = Stroke(width = 10f * intensity)
    )
    drawScope.drawPath(
        path = path2,
        brush = Brush.linearGradient(
            colors = listOf(Color(0x88880015), Color(0xAAFF1744), Color(0x66B71C1C))
        ),
        style = Stroke(width = 4.5f * intensity)
    )
}

private fun drawKakuganEyePulse(
    drawScope: DrawScope,
    eyeCenter: Offset,
    clock: Float,
    isSelected: Boolean
) {
    // Anatomical heartbeat rhythm: lub-dub pulse
    val period = 1.6f
    val t = (clock % period) / period
    val beatFactor = when {
        t < 0.15f -> kotlin.math.sin(t / 0.15f * Math.PI.toFloat()) // Primary pump "lub"
        t in 0.20f..0.35f -> kotlin.math.sin((t - 0.20f) / 0.15f * Math.PI.toFloat()) * 0.55f // Second pump "dub"
        else -> 0f // Diastole rest
    }

    val baseRadius = if (isSelected) 24f else 18f
    val pulseRadius = baseRadius * (1f + beatFactor * 0.35f)

    // Dark sclera halo
    drawScope.drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xDD0A0103),
                Color(0x99180208),
                Color.Transparent
            ),
            center = eyeCenter,
            radius = pulseRadius * 2.2f
        ),
        radius = pulseRadius * 2.2f,
        center = eyeCenter
    )

    // Pulsing Red Iris Core
    drawScope.drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFF5252),
                Color(0xFFFF1744),
                Color(0xFFB71C1C),
                Color(0x334A000A)
            ),
            center = eyeCenter,
            radius = pulseRadius
        ),
        radius = pulseRadius,
        center = eyeCenter
    )

    // Pure bright Kakugan pupil
    drawScope.drawCircle(
        color = Color(0xFFFFD5D9),
        radius = pulseRadius * 0.28f,
        center = eyeCenter
    )

    // Branching Kagune Capillaries / Veins pulsing synchronously with the heartbeat
    val veinAlpha = 0.35f + beatFactor * 0.50f
    val veinColor = Color(0xFFFF1744).copy(alpha = veinAlpha)

    val angles = listOf(130f, 165f, 205f, 235f, 270f)
    angles.forEachIndexed { i, deg ->
        val rad = Math.toRadians(deg.toDouble()).toFloat()
        val len = pulseRadius * (1.8f + (i % 3) * 0.6f + beatFactor * 0.5f)
        val endX = eyeCenter.x + kotlin.math.cos(rad) * len
        val endY = eyeCenter.y + kotlin.math.sin(rad) * len

        val midX = eyeCenter.x + kotlin.math.cos(rad - 0.15f) * (len * 0.55f)
        val midY = eyeCenter.y + kotlin.math.sin(rad - 0.15f) * (len * 0.55f)

        val veinPath = Path().apply {
            moveTo(eyeCenter.x, eyeCenter.y)
            quadraticBezierTo(midX, midY, endX, endY)
        }

        drawScope.drawPath(
            path = veinPath,
            color = veinColor,
            style = Stroke(width = if (i % 2 == 0) 1.2f else 0.8f)
        )
    }
}

private fun drawHaiseMemoryGlitch(
    drawScope: DrawScope,
    w: Float,
    h: Float,
    clock: Float
) {
    // Triggers a subtle horizontal VHS chromatic memory scan every 4 seconds for 0.4s
    val cycle = 4.2f
    val phase = clock % cycle
    if (phase < 0.45f) {
        val progress = phase / 0.45f
        val scanY = h * progress

        // Chromatic split scan line (Red & White offset)
        drawScope.drawLine(
            color = Color(0x33FF1744),
            start = Offset(0f, scanY - 2f),
            end = Offset(w, scanY - 2f),
            strokeWidth = 2.5f
        )
        drawScope.drawLine(
            color = Color(0x44FFFFFF),
            start = Offset(0f, scanY),
            end = Offset(w, scanY),
            strokeWidth = 1.5f
        )

        // Very faint film grain bars
        drawScope.drawRect(
            color = Color(0x18FFFFFF),
            topLeft = Offset(0f, scanY - 8f),
            size = Size(w, 16f)
        )
    }
}

private fun drawPetal(
    drawScope: DrawScope,
    center: Offset,
    length: Float,
    width: Float,
    rotDeg: Float,
    type: Int
) {
    val rad = Math.toRadians(rotDeg.toDouble()).toFloat()
    val cos = kotlin.math.cos(rad)
    val sin = kotlin.math.sin(rad)

    val tipX = center.x + cos * length
    val tipY = center.y + sin * length
    val baseX = center.x - cos * length
    val baseY = center.y - sin * length

    val perpX = -sin * width
    val perpY = cos * width

    val petalPath = Path().apply {
        moveTo(baseX, baseY)
        cubicTo(
            center.x + perpX, center.y + perpY,
            tipX + perpX * 0.5f, tipY + perpY * 0.5f,
            tipX, tipY
        )
        cubicTo(
            tipX - perpX * 0.5f, tipY - perpY * 0.5f,
            center.x - perpX, center.y - perpY,
            baseX, baseY
        )
        close()
    }

    when (type) {
        0 -> { // Red Spider Lily (Higanbana petal)
            drawScope.drawPath(
                path = petalPath,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFFF1744), Color(0xFFB71C1C)),
                    start = Offset(baseX, baseY),
                    end = Offset(tipX, tipY)
                )
            )
        }
        1 -> { // White Transformation Hair Flare
            drawScope.drawPath(
                path = petalPath,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xCCFFFFFF), Color(0x66E0E0E0)),
                    start = Offset(baseX, baseY),
                    end = Offset(tipX, tipY)
                )
            )
        }
        else -> { // Golden Hope Spark
            drawScope.drawCircle(
                color = Color(0xEEFFD700),
                radius = width * 0.8f,
                center = center
            )
        }
    }
}

/**
 * Fullscreen application-wide theme overlay for Ghoul Ken Kaneki theme.
 * Applies the complete suite of blood dripping, falling droplets, arterial slashes,
 * bottom blood pool, undulating kagune tentacles, kakugan vascular pulse, and higanbana petals
 * across the entire app interface when the theme is active.
 */
@Composable
fun GhoulFullscreenThemeOverlay(
    modifier: Modifier = Modifier
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

    // Fullscreen blood drops system (more drops for entire screen coverage)
    val fullscreenBloodDrops = remember {
        val bRng = Random(2007L)
        List(11) { idx ->
            BloodDropData(
                xNorm = 0.04f + (idx * 0.09f) + (bRng.nextFloat() - 0.5f) * 0.03f,
                speed = 0.30f + bRng.nextFloat() * 0.25f,
                phase = bRng.nextFloat(),
                radius = 3.0f + bRng.nextFloat() * 2.2f
            )
        }
    }

    // Fullscreen drifting spider lily petals and hair flares
    val fullscreenPetals = remember {
        val pRng = Random(888L)
        List(22) {
            GhoulPetal(
                xNorm = pRng.nextFloat(),
                yNorm = pRng.nextFloat(),
                speed = 0.06f + pRng.nextFloat() * 0.08f,
                swaySpeed = 1.0f + pRng.nextFloat() * 1.8f,
                swayAmp = 22f + pRng.nextFloat() * 28f,
                rotationSpeed = (pRng.nextFloat() - 0.5f) * 100f,
                length = 8f + pRng.nextFloat() * 9f,
                width = 2.4f + pRng.nextFloat() * 2.6f,
                type = when (pRng.nextInt(6)) {
                    0, 1, 2 -> 0 // Red Spider Lily
                    3, 4 -> 1    // White hair fragment
                    else -> 2    // Golden hope spark
                }
            )
        }
    }

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        // Bottom bar safety clearance height
        val contentH = h - 85f

        // 1. Ambient dark crimson & ink vignette around outer viewport
        val ambientVignette = Brush.radialGradient(
            colors = listOf(
                Color.Transparent,
                Color(0x15350009),
                Color(0x40150005)
            ),
            center = Offset(w * 0.5f, h * 0.45f),
            radius = w * 0.95f
        )
        drawRect(brush = ambientVignette)

        // 2. Hanging blood drip stalactites along the top edge / status bar
        drawTopBloodDrips(
            drawScope = this,
            w = w,
            h = contentH,
            clock = animClock,
            drops = fullscreenBloodDrops
        )

        // 3. Falling teardrop blood droplets with specular reflections (clearing bottom bar)
        drawFallingBloodDrops(
            drawScope = this,
            w = w,
            h = contentH,
            clock = animClock,
            drops = fullscreenBloodDrops
        )

        // 4. Periodic full-screen arterial blood slashes & splatters
        drawArterialBloodSlash(
            drawScope = this,
            w = w,
            h = contentH,
            clock = animClock
        )

        // 5. Fullscreen ambient writhing Rinkaku Kagune tentacles
        drawRinkakuKagune(
            drawScope = this,
            w = w,
            h = contentH,
            clock = animClock,
            intensity = 0.85f
        )

        // 6. Corner Kakugan Eye Heartbeat Pulse in top-right
        drawKakuganEyePulse(
            drawScope = this,
            eyeCenter = Offset(w * 0.90f, h * 0.08f),
            clock = animClock,
            isSelected = true
        )

        // 7. Sasaki Haise memory scanline glitch
        drawHaiseMemoryGlitch(
            drawScope = this,
            w = w,
            h = contentH,
            clock = animClock
        )

        // 8. Fullscreen drifting Higanbana petals and transformation sparks
        fullscreenPetals.forEach { p ->
            val y = ((p.yNorm + animClock * p.speed) % 1.0f) * contentH
            val sway = kotlin.math.sin(animClock * p.swaySpeed + p.xNorm * 10f) * p.swayAmp
            val x = ((p.xNorm * w + sway) % w + w) % w
            val rot = p.rotationSpeed * animClock

            drawPetal(
                drawScope = this,
                center = Offset(x, y),
                length = p.length,
                width = p.width,
                rotDeg = rot,
                type = p.type
            )
        }
    }
}


