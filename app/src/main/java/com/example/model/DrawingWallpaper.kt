package com.example.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/**
 * Single continuous stroke drawn on the canvas.
 */
data class DrawingStroke(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float
)

/**
 * Data representation of a user's custom drawing applied as wallpaper.
 */
data class CustomWallpaper(
    val strokes: List<DrawingStroke>,
    val canvasWidth: Float,
    val canvasHeight: Float,
    val opacity: Float = 0.85f,
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Serializes this wallpaper to a compact string for persistent storage.
     */
    fun serializeToString(): String {
        return try {
            val sb = StringBuilder()
            // Header: version=1;width;height;opacity;timestamp
            sb.append("v1;").append(canvasWidth).append(";")
                .append(canvasHeight).append(";")
                .append(opacity).append(";")
                .append(timestamp).append("\n")

            strokes.forEachIndexed { index, stroke ->
                if (index > 0) sb.append("|")
                sb.append(stroke.color.toArgb()).append(":")
                    .append(stroke.strokeWidth).append(":")
                stroke.points.forEachIndexed { pIdx, pt ->
                    if (pIdx > 0) sb.append(";")
                    sb.append(pt.x.toInt()).append(",").append(pt.y.toInt())
                }
            }
            sb.toString()
        } catch (e: Exception) {
            ""
        }
    }

    companion object {
        /**
         * Deserializes wallpaper from a string.
         */
        fun deserializeFromString(data: String): CustomWallpaper? {
            if (data.isBlank()) return null
            return try {
                val lines = data.split("\n", limit = 2)
                val headerParts = lines[0].split(";")
                if (headerParts.size < 4) return null

                val canvasWidth = headerParts[1].toFloatOrNull() ?: 720f
                val canvasHeight = headerParts[2].toFloatOrNull() ?: 500f
                val opacity = headerParts[3].toFloatOrNull() ?: 0.85f
                val timestamp = if (headerParts.size >= 5) headerParts[4].toLongOrNull() ?: 0L else 0L

                if (lines.size < 2 || lines[1].isBlank()) {
                    return CustomWallpaper(emptyList(), canvasWidth, canvasHeight, opacity, timestamp)
                }

                val strokeStrings = lines[1].split("|")
                val strokes = mutableListOf<DrawingStroke>()

                for (sStr in strokeStrings) {
                    if (sStr.isBlank()) continue
                    val parts = sStr.split(":", limit = 3)
                    if (parts.size < 3) continue
                    val colorArgb = parts[0].toIntOrNull() ?: 0xFFFFFFFF.toInt()
                    val strokeWidth = parts[1].toFloatOrNull() ?: 8f
                    val pointsStr = parts[2]

                    val points = mutableListOf<Offset>()
                    val pts = pointsStr.split(";")
                    for (ptStr in pts) {
                        val xy = ptStr.split(",")
                        if (xy.size == 2) {
                            val x = xy[0].toFloatOrNull()
                            val y = xy[1].toFloatOrNull()
                            if (x != null && y != null) {
                                points.add(Offset(x, y))
                            }
                        }
                    }
                    if (points.isNotEmpty()) {
                        strokes.add(
                            DrawingStroke(
                                points = points,
                                color = Color(colorArgb),
                                strokeWidth = strokeWidth
                            )
                        )
                    }
                }

                CustomWallpaper(
                    strokes = strokes,
                    canvasWidth = canvasWidth,
                    canvasHeight = canvasHeight,
                    opacity = opacity,
                    timestamp = timestamp
                )
            } catch (e: Exception) {
                null
            }
        }
    }
}
