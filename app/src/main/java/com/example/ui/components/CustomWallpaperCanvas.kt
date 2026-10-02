package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.model.CustomWallpaper

/**
 * Renders the user's custom drawing as a full-screen application background.
 * Scales coordinates dynamically to match any screen resolution or orientation.
 */
@Composable
fun CustomWallpaperCanvas(
    wallpaper: CustomWallpaper,
    modifier: Modifier = Modifier,
    dimOverlayColor: Color = Color(0x33000000)
) {
    if (wallpaper.strokes.isEmpty()) return

    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        val srcW = if (wallpaper.canvasWidth > 0f) wallpaper.canvasWidth else size.width
        val srcH = if (wallpaper.canvasHeight > 0f) wallpaper.canvasHeight else size.height

        val scaleX = size.width / srcW
        val scaleY = size.height / srcH
        val strokeScale = (scaleX + scaleY) / 2f

        for (stroke in wallpaper.strokes) {
            if (stroke.points.isEmpty()) continue

            val strokeWidth = (stroke.strokeWidth * strokeScale).coerceAtLeast(1.5f)
            val strokeColor = stroke.color.copy(
                alpha = (stroke.color.alpha * wallpaper.opacity).coerceIn(0f, 1f)
            )

            if (stroke.points.size == 1) {
                val pt = stroke.points[0]
                drawCircle(
                    color = strokeColor,
                    radius = strokeWidth / 2f,
                    center = Offset(pt.x * scaleX, pt.y * scaleY)
                )
            } else {
                val path = Path().apply {
                    val first = stroke.points[0]
                    moveTo(first.x * scaleX, first.y * scaleY)
                    for (i in 1 until stroke.points.size) {
                        val pt = stroke.points[i]
                        lineTo(pt.x * scaleX, pt.y * scaleY)
                    }
                }
                drawPath(
                    path = path,
                    color = strokeColor,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }

        // Subtle dark scrim to maintain comfortable readability of text and buttons
        if (dimOverlayColor.alpha > 0f) {
            drawRect(color = dimOverlayColor)
        }
    }
}
