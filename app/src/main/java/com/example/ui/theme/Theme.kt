package com.example.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.example.ui.components.CrypticFrostAmbientAuraOverlay
import com.example.ui.components.CrypticFrostParticlesOverlay
import com.example.ui.components.GhoulFullscreenThemeOverlay
import com.example.ui.components.MinecraftBlockParticlesOverlay
import com.example.ui.components.WodenerParticlesOverlay
import com.example.ui.components.ElectricChargeEffect
import com.example.ui.components.NeonSpectrumEffect
import com.example.ui.components.ToxicBurstEffect
import com.example.ui.components.CyberPeachEffect
import com.example.ui.components.NeonEmeraldEffect

@Composable
fun NDCycleTheme(
    themeMode: AppThemeMode = AppThemeMode.CYBER_LILAC,
    content: @Composable () -> Unit
) {
    val palette = themeMode.palette
    val colorScheme = if (themeMode == AppThemeMode.PURE_LIGHT) {
        lightColorScheme(
            primary = palette.primary,
            onPrimary = palette.onPrimary,
            primaryContainer = palette.primaryContainer,
            onPrimaryContainer = palette.onPrimaryContainer,
            secondary = palette.secondary,
            onSecondary = palette.onSecondary,
            secondaryContainer = palette.surfaceContainer,
            onSecondaryContainer = palette.secondary,
            tertiary = palette.amber,
            onTertiary = palette.surface,
            background = palette.background,
            onBackground = palette.textPrimary,
            surface = palette.surface,
            onSurface = palette.textPrimary,
            surfaceVariant = palette.surfaceVariant,
            onSurfaceVariant = palette.textSecondary,
            outline = palette.cardBorder,
            outlineVariant = palette.cardBorderSubtle,
            error = palette.error,
            onError = palette.errorBg,
            errorContainer = palette.errorBg,
            onErrorContainer = palette.error
        )
    } else {
        darkColorScheme(
            primary = palette.primary,
            onPrimary = palette.onPrimary,
            primaryContainer = palette.primaryContainer,
            onPrimaryContainer = palette.onPrimaryContainer,
            secondary = palette.secondary,
            onSecondary = palette.onSecondary,
            secondaryContainer = palette.surfaceContainer,
            onSecondaryContainer = palette.secondary,
            tertiary = palette.amber,
            onTertiary = palette.surface,
            background = palette.background,
            onBackground = palette.textPrimary,
            surface = palette.surface,
            onSurface = palette.textPrimary,
            surfaceVariant = palette.surfaceVariant,
            onSurfaceVariant = palette.textSecondary,
            outline = palette.cardBorder,
            outlineVariant = palette.cardBorderSubtle,
            error = palette.error,
            onError = palette.errorBg,
            errorContainer = palette.errorBg,
            onErrorContainer = palette.error
        )
    }

    CompositionLocalProvider(
        LocalAppThemeColors provides palette,
        LocalAppThemeMode provides themeMode
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = {
                Box(modifier = Modifier.fillMaxSize()) {
                    content()
                    if (themeMode == AppThemeMode.MINECRAFT_BLOCKS) {
                        MinecraftBlockParticlesOverlay(
                            modifier = Modifier.fillMaxSize(),
                            particleCount = 14,
                            alphaMultiplier = 0.35f
                        )
                    } else if (themeMode == AppThemeMode.WODENER_WORC) {
                        WodenerParticlesOverlay(
                            modifier = Modifier.fillMaxSize(),
                            particleCount = 16,
                            alphaMultiplier = 0.32f
                        )
                    } else if (themeMode == AppThemeMode.CRYPTIC_FROST) {
                        CrypticFrostAmbientAuraOverlay(
                            modifier = Modifier.fillMaxSize()
                        )
                        CrypticFrostParticlesOverlay(
                            modifier = Modifier.fillMaxSize(),
                            particleCount = 20,
                            alphaMultiplier = 0.42f
                        )
                    } else if (themeMode == AppThemeMode.GHOUL_KEN_KANEKI) {
                        GhoulFullscreenThemeOverlay(
                            modifier = Modifier.fillMaxSize()
                        )
                    } else if (themeMode == AppThemeMode.ELECTRIC_CHARGE) {
                        ElectricChargeEffect(
                            modifier = Modifier.fillMaxSize()
                        )
                    } else if (themeMode == AppThemeMode.NEON_SPECTRUM) {
                        NeonSpectrumEffect(
                            modifier = Modifier.fillMaxSize()
                        )
                    } else if (themeMode == AppThemeMode.TOXIC_BURST) {
                        ToxicBurstEffect(
                            modifier = Modifier.fillMaxSize()
                        )
                    } else if (themeMode == AppThemeMode.CYBER_PEACH) {
                        CyberPeachEffect(
                            modifier = Modifier.fillMaxSize()
                        )
                    } else if (themeMode == AppThemeMode.NEON_EMERALD) {
                        NeonEmeraldEffect(
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        )
    }
}
