package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EngineState
import com.example.ui.MainViewModel
import com.example.ui.components.MinecraftBlockParticlesOverlay
import com.example.ui.components.MinecraftCardShimmerOverlay
import com.example.ui.components.WodenerBlockSwatch
import com.example.ui.components.WodenerBlockType
import com.example.ui.components.WodenerCardShimmerOverlay
import com.example.ui.components.WodenerParticlesOverlay
import com.example.ui.components.CrypticFrostBlockSwatch
import com.example.ui.components.CrypticFrostBlockType
import com.example.ui.components.CrypticFrostCardShimmerOverlay
import com.example.ui.components.CrypticFrostParticlesOverlay
import com.example.ui.components.GhoulBlockSwatch
import com.example.ui.components.GhoulBlockType
import com.example.ui.components.GhoulCardBespokeOverlay
import com.example.ui.components.rememberGhoulCardBorderBrush
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.AppThemePalette
import com.example.ui.theme.MinecraftBlocksPalette
import com.example.ui.theme.ImmersiveCardBorder
import com.example.ui.theme.ImmersiveCardBorderSubtle
import com.example.ui.theme.ImmersiveLilac
import com.example.ui.theme.ImmersiveLilacContainer
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun ThemesScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentTheme by viewModel.appTheme.collectAsState()
    val engineState by viewModel.engineState.collectAsState()
    val unlockedThemes by viewModel.unlockedThemes.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .widthIn(max = 720.dp)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Compact Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("themes_header_card"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
            border = BorderStroke(1.dp, ImmersiveCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ImmersiveLilacContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = null,
                            tint = ImmersiveLilac,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ОФОРМЛЕНИЕ И ТЕМЫ",
                            color = TextPrimary,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "${AppThemeMode.values().size} стилей визуализации интерфейса",
                            color = TextTertiary,
                            fontSize = 11.sp
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ImmersiveLilac.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, ImmersiveLilac.copy(alpha = 0.35f))
                    ) {
                        Text(
                            text = currentTheme.palette.displayName,
                            color = ImmersiveLilac,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        Text(
            text = "ДОСТУПНЫЕ ТЕМАТИЧЕСКИЕ ПАКЕТЫ",
            color = TextTertiary,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.2.sp,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )

        // Compact Theme Selection Cards (Includes standard and acquired shop themes, including unlocked Dixel Mine)
        AppThemeMode.values().filter { themeMode ->
            !themeMode.palette.isLocked || unlockedThemes.contains(themeMode.name)
        }.forEach { themeMode ->
            val isSelected = currentTheme == themeMode

            ThemeCard(
                palette = themeMode.palette,
                isSelected = isSelected,
                isLocked = false,
                onSelect = {
                    viewModel.setAppTheme(themeMode)
                },
                hideColorDescription = false
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun ThemeCard(
    palette: AppThemePalette,
    isSelected: Boolean,
    isLocked: Boolean,
    onSelect: () -> Unit,
    onBuy: (() -> Unit)? = null,
    priceText: String = "Nd 0",
    isAchievementTheme: Boolean = false,
    hideColorDescription: Boolean = false,
    completedPacksCount: Int = 0,
    totalRequiredPacks: Int = 24,
    hasStoragePermission: Boolean = true
) {
    val isMinecraftTheme = palette.id == "MINECRAFT_BLOCKS"
    val isWodenerTheme = palette.id == "WODENER_WORC"
    val isCrypticFrostTheme = palette.id == "CRYPTIC_FROST"
    val isGhoulTheme = palette.id == "GHOUL_KEN_KANEKI"

    val cardBorder = if (isMinecraftTheme && (isSelected || isLocked)) {
        BorderStroke(
            width = if (isSelected) 1.8.dp else 1.2.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF5AB6A8), // Prismarine
                    Color(0xFFFCD836), // Gold
                    Color(0xFF9A5CC0), // Amethyst
                    Color(0xFF55FFFF)  // Diamond
                )
            )
        )
    } else if (isWodenerTheme && (isSelected || isLocked)) {
        BorderStroke(
            width = if (isSelected) 1.8.dp else 1.2.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF1B6A66), // Prismarine Bricks
                    Color(0xFFD69C54), // Planks
                    Color(0xFFBE8A7B), // Granite
                    Color(0xFF984931), // Bricks
                    Color(0xFF1B6A66)  // Loop back
                )
            )
        )
    } else if (isCrypticFrostTheme && (isSelected || isLocked)) {
        BorderStroke(
            width = if (isSelected) 1.8.dp else 1.2.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF6AD6F5), // Лёд
                    Color(0xFFE52E2E), // Мухомор
                    Color(0xFFDFDC9B), // Камень Края
                    Color(0xFFE6F2F8), // Стекло / Матовый
                    Color(0xFFFF6D00)  // Оранжевая керамика
                )
            )
        )
    } else if (isGhoulTheme && (isSelected || isLocked)) {
        BorderStroke(
            width = if (isSelected) 1.8.dp else 1.2.dp,
            brush = rememberGhoulCardBorderBrush(isSelected = isSelected)
        )
    } else {
        BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) palette.primary else if (isLocked) palette.primary.copy(alpha = 0.5f) else ImmersiveCardBorderSubtle
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isLocked) Modifier.clickable { onBuy?.invoke() ?: onSelect() }
                else if (!isSelected) Modifier.clickable(onClick = onSelect)
                else Modifier
            )
            .testTag("theme_card_${palette.id.lowercase()}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) palette.surface else if (isLocked) palette.surface.copy(alpha = 0.85f) else ImmersiveSurface
        ),
        border = cardBorder
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (isMinecraftTheme) {
                MinecraftBlockParticlesOverlay(
                    modifier = Modifier.matchParentSize(),
                    particleCount = 14,
                    alphaMultiplier = 0.7f
                )
                MinecraftCardShimmerOverlay(
                    modifier = Modifier.matchParentSize()
                )
            } else if (isWodenerTheme) {
                WodenerParticlesOverlay(
                    modifier = Modifier.matchParentSize(),
                    particleCount = 16,
                    alphaMultiplier = 0.65f
                )
                WodenerCardShimmerOverlay(
                    modifier = Modifier.matchParentSize()
                )
            } else if (isCrypticFrostTheme) {
                CrypticFrostParticlesOverlay(
                    modifier = Modifier.matchParentSize(),
                    particleCount = 14,
                    alphaMultiplier = 0.65f
                )
                CrypticFrostCardShimmerOverlay(
                    modifier = Modifier.matchParentSize()
                )
            } else if (isGhoulTheme) {
                GhoulCardBespokeOverlay(
                    modifier = Modifier.matchParentSize(),
                    isSelected = isSelected
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Header: Dot + Title + Status Chip / Buy Button / Select Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(if (isMinecraftTheme) RoundedCornerShape(2.5.dp) else CircleShape)
                                .background(palette.primary)
                                .then(
                                    if (isMinecraftTheme) Modifier.border(0.8.dp, Color(0xFFC79E08), RoundedCornerShape(2.dp))
                                    else Modifier
                                )
                        )
                        Text(
                            text = palette.displayName,
                            color = if (isSelected) palette.primary else TextPrimary,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (isMinecraftTheme) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF5AB6A8).copy(alpha = 0.2f),
                                border = BorderStroke(0.6.dp, Color(0xFF5AB6A8).copy(alpha = 0.6f))
                            ) {
                                Text(
                                    text = "✨ CUBIC",
                                    color = Color(0xFFFCD836),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        } else if (isWodenerTheme) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF1B6A66).copy(alpha = 0.22f),
                                border = BorderStroke(0.6.dp, Color(0xFF1B6A66).copy(alpha = 0.7f))
                            ) {
                                Text(
                                    text = "🪵 CRAFT",
                                    color = Color(0xFFD69C54),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        } else if (palette.id == "LASURITE_CRYSTAL") {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF00D2D3).copy(alpha = 0.15f),
                                border = BorderStroke(0.6.dp, Color(0xFF00D2D3).copy(alpha = 0.6f))
                            ) {
                                Text(
                                    text = "💎 CRYSTAL",
                                    color = Color(0xFF00D2D3),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        } else if (palette.id == "INFERNO_STREET") {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFC84E38).copy(alpha = 0.18f),
                                border = BorderStroke(0.6.dp, Color(0xFFC84E38).copy(alpha = 0.65f))
                            ) {
                                Text(
                                    text = "🔥 STREET",
                                    color = Color(0xFFC84E38),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        } else if (isCrypticFrostTheme) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF6AD6F5).copy(alpha = 0.18f),
                                border = BorderStroke(0.6.dp, Color(0xFF6AD6F5).copy(alpha = 0.65f))
                            ) {
                                Text(
                                    text = "❄️ FROST",
                                    color = Color(0xFF6AD6F5),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        } else if (isGhoulTheme) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFFF1744).copy(alpha = 0.18f),
                                border = BorderStroke(0.6.dp, Color(0xFFFF1744).copy(alpha = 0.65f))
                            ) {
                                Text(
                                    text = "🩸 GHOUL",
                                    color = Color(0xFFFF1744),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }

                    if (isLocked) {
                        if (isAchievementTheme || palette.id == "MINECRAFT_BLOCKS" || onBuy == null) {
                            // Значок замка вместо кнопки «Купить»
                            Surface(
                                onClick = onSelect,
                                shape = RoundedCornerShape(8.dp),
                                color = if (hasStoragePermission) palette.primary.copy(alpha = 0.18f) else Color(0xFFB84549).copy(alpha = 0.14f),
                                border = BorderStroke(1.dp, if (hasStoragePermission) palette.primary.copy(alpha = 0.7f) else Color(0xFFB84549).copy(alpha = 0.45f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Заблокировано",
                                        tint = if (hasStoragePermission) palette.primary else Color(0xFFC75450),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    if (isMinecraftTheme) {
                                        Text(
                                            text = if (hasStoragePermission) "$completedPacksCount/$totalRequiredPacks" else "0/$totalRequiredPacks",
                                            color = if (hasStoragePermission) palette.primary else Color(0xFFC75450),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                        } else {
                            Surface(
                                onClick = { onBuy?.invoke() ?: onSelect() },
                                shape = RoundedCornerShape(8.dp),
                                color = palette.primary.copy(alpha = 0.18f),
                                border = BorderStroke(1.dp, palette.primary.copy(alpha = 0.7f))
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "Купить",
                                        color = TextPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "•",
                                        color = TextTertiary,
                                        fontSize = 9.sp
                                    )
                                    Text(
                                        text = "Nd",
                                        color = palette.primary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Text(
                                        text = priceText.replace("Nd", "").trim(),
                                        color = TextPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    } else if (isSelected) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = palette.primary.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, palette.primary)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = palette.primary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "АКТИВНА",
                                    color = palette.primary,
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    } else {
                        OutlinedButton(
                            onClick = onSelect,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(28.dp),
                            contentPadding = PaddingValues(horizontal = 9.dp, vertical = 2.dp),
                            border = BorderStroke(1.dp, ImmersiveCardBorderSubtle)
                        ) {
                            Text(
                                text = "Выбрать",
                                fontSize = 10.5.sp,
                                color = palette.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Прогресс-бар для темы Dixel Mine в заблокированном состоянии
                if (isLocked && isMinecraftTheme) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (hasStoragePermission) Color.Black.copy(alpha = 0.35f)
                                else Color(0xFF240D10).copy(alpha = 0.5f)
                            )
                            .border(
                                width = 0.8.dp,
                                color = if (hasStoragePermission) palette.primary.copy(alpha = 0.35f) else Color(0xFFB84549).copy(alpha = 0.4f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = if (hasStoragePermission) palette.primary else Color(0xFFC75450),
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "Пройдено паков:",
                                    color = if (hasStoragePermission) TextSecondary else TextSecondary.copy(alpha = 0.85f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Text(
                                text = if (hasStoragePermission) "$completedPacksCount из $totalRequiredPacks" else "0 из $totalRequiredPacks",
                                color = if (hasStoragePermission) palette.primary else Color(0xFFC75450),
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        LinearProgressIndicator(
                            progress = {
                                if (hasStoragePermission) {
                                    (completedPacksCount.toFloat() / totalRequiredPacks.toFloat()).coerceIn(0f, 1f)
                                } else {
                                    1f // Спокойная приглушённо-красная полоса при отсутствии доступа
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (hasStoragePermission) palette.primary else Color(0xFFB84549).copy(alpha = 0.75f),
                            trackColor = if (hasStoragePermission) palette.primary.copy(alpha = 0.2f) else Color(0xFF421518).copy(alpha = 0.35f)
                        )
                    }
                }

                if (!hideColorDescription && palette.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = palette.description,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(if (hideColorDescription) 4.dp else 10.dp))

                // Palette Swatches Row - плашка с цветами
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (!hideColorDescription) {
                            Text(
                                text = "Палитра:",
                                color = TextTertiary,
                                fontSize = 9.5.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        if (isWodenerTheme) {
                            listOf(
                                WodenerBlockType.WOOD,
                                WodenerBlockType.PLANKS,
                                WodenerBlockType.POLISHED_GRANITE,
                                WodenerBlockType.BRICKS,
                                WodenerBlockType.PRISMARINE_BRICKS
                            ).forEach { blockType ->
                                WodenerBlockSwatch(type = blockType)
                            }
                        } else if (isCrypticFrostTheme) {
                            listOf(
                                CrypticFrostBlockType.ICE,
                                CrypticFrostBlockType.MUSHROOM,
                                CrypticFrostBlockType.END_STONE,
                                CrypticFrostBlockType.GLASS_MATTE,
                                CrypticFrostBlockType.ORANGE_CERAMIC
                            ).forEach { blockType ->
                                CrypticFrostBlockSwatch(type = blockType)
                            }
                        } else if (isGhoulTheme) {
                            listOf(
                                GhoulBlockType.WHITE_REBIRTH,
                                GhoulBlockType.BLACK_ALTER_EGO,
                                GhoulBlockType.GREY_HAISE,
                                GhoulBlockType.RED_KAKUGU,
                                GhoulBlockType.GOLDEN_HOPE
                            ).forEach { blockType ->
                                GhoulBlockSwatch(type = blockType)
                            }
                        } else {
                            palette.accentSwatchList.forEach { color ->
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(color)
                                        .border(0.8.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(5.dp))
                                )
                            }
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = palette.background,
                        border = BorderStroke(0.8.dp, palette.cardBorderSubtle)
                    ) {
                        Text(
                            text = palette.id,
                            color = palette.primary.copy(alpha = 0.8f),
                            fontSize = 8.5.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
