package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.model.Achievement
import com.example.model.LevelCalculator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TrackDictionary
import com.example.ui.MainViewModel
import com.example.util.PermissionHelper
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.ImmersiveBackground
import com.example.ui.theme.ImmersiveCardBorder
import com.example.ui.theme.ImmersiveCardBorderSubtle
import com.example.ui.theme.ImmersiveLilac
import com.example.ui.theme.ImmersiveLilacContainer
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.StoneTimberPalette
import com.example.ui.theme.UrbanPinkPalette
import com.example.ui.theme.CrimsonSparkPalette
import com.example.ui.theme.MinecraftBlocksPalette
import com.example.ui.theme.WodenerWorcPalette
import com.example.ui.theme.LasuriteCrystalPalette
import com.example.ui.theme.InfernoStreetPalette
import com.example.ui.theme.CrypticFrostPalette
import com.example.ui.theme.GhoulKenKanekiPalette
import com.example.ui.theme.NeonCyberpunkPalette
import com.example.ui.theme.CyberSunsetPalette
import com.example.ui.theme.DigitalDawnPalette
import com.example.ui.theme.NeonPulsePalette
import com.example.ui.theme.CyberMintPalette
import com.example.ui.theme.ElectricChargePalette
import com.example.ui.theme.NeonSpectrumPalette
import com.example.ui.theme.ToxicBurstPalette
import com.example.ui.theme.CyberPeachPalette
import com.example.ui.theme.NeonEmeraldPalette
import com.example.ui.theme.PastelTendernessPalette
import com.example.ui.theme.VanillaCreamPalette
import com.example.ui.theme.LavenderMistPalette
import com.example.ui.theme.MarshmallowDreamPalette
import com.example.ui.theme.SoftBeigePalette
import com.example.ui.theme.ApricotMoussePalette
import com.example.ui.theme.CottonSilkPalette
import com.example.ui.theme.AshMarshmallowPalette
import com.example.ui.theme.SmokyMintPalette
import com.example.ui.theme.PowderyPeachPalette
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun QuestsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = androidx.compose.ui.platform.LocalContext.current
    val engineState by viewModel.engineState.collectAsState()
    val currentTheme by viewModel.appTheme.collectAsState()
    val unlockedThemes by viewModel.unlockedThemes.collectAsState()
    val unlockedAchievements by viewModel.unlockedAchievements.collectAsState()
    val completedPacks by viewModel.completedPacks.collectAsState()
    val permissions by viewModel.permissions.collectAsState()
    val hasStorage = permissions.hasStorage
    val completedNormalPacksCount = completedPacks.count { TrackDictionary.INITIAL_NORMAL_PACKS.contains(it) }
    val effectiveCompletedNormalPacksCount = if (hasStorage) completedNormalPacksCount else 0
    val totalNormalPacksCount = TrackDictionary.INITIAL_NORMAL_PACKS.size

    var subTab by rememberSaveable { mutableIntStateOf(0) }
    val totalAchievements = Achievement.values().size
    val unlockedCount = unlockedAchievements.size
    val earnedAchievementXp = Achievement.values().filter { unlockedAchievements.contains(it.id) }.sumOf { it.rewardXp }
    val totalAchievementXp = Achievement.values().sumOf { it.rewardXp }
    val userLevel = LevelCalculator.calculate(earnedAchievementXp)

    val isStoneTimberUnlocked = unlockedThemes.contains(AppThemeMode.STONE_TIMBER.name)
    val isUrbanPinkUnlocked = unlockedThemes.contains(AppThemeMode.URBAN_PINK.name)
    val isCrimsonSparkUnlocked = unlockedThemes.contains(AppThemeMode.CRIMSON_SPARK.name)
    val isMinecraftBlocksUnlocked = unlockedThemes.contains(AppThemeMode.MINECRAFT_BLOCKS.name)
    val isWodenerWorcUnlocked = unlockedThemes.contains(AppThemeMode.WODENER_WORC.name)
    val isLasuriteCrystalUnlocked = unlockedThemes.contains(AppThemeMode.LASURITE_CRYSTAL.name)
    val isInfernoStreetUnlocked = unlockedThemes.contains(AppThemeMode.INFERNO_STREET.name)
    val isCrypticFrostUnlocked = unlockedThemes.contains(AppThemeMode.CRYPTIC_FROST.name)
    val isGhoulKenKanekiUnlocked = unlockedThemes.contains(AppThemeMode.GHOUL_KEN_KANEKI.name)
    val isNeonCyberpunkUnlocked = unlockedThemes.contains(AppThemeMode.NEON_CYBERPUNK.name)
    val isCyberSunsetUnlocked = unlockedThemes.contains(AppThemeMode.CYBER_SUNSET.name)
    val isDigitalDawnUnlocked = unlockedThemes.contains(AppThemeMode.DIGITAL_DAWN.name)
    val isNeonPulseUnlocked = unlockedThemes.contains(AppThemeMode.NEON_PULSE.name)
    val isCyberMintUnlocked = unlockedThemes.contains(AppThemeMode.CYBER_MINT.name)
    val isElectricChargeUnlocked = unlockedThemes.contains(AppThemeMode.ELECTRIC_CHARGE.name)
    val isNeonSpectrumUnlocked = unlockedThemes.contains(AppThemeMode.NEON_SPECTRUM.name)
    val isToxicBurstUnlocked = unlockedThemes.contains(AppThemeMode.TOXIC_BURST.name)
    val isCyberPeachUnlocked = unlockedThemes.contains(AppThemeMode.CYBER_PEACH.name)
    val isNeonEmeraldUnlocked = unlockedThemes.contains(AppThemeMode.NEON_EMERALD.name)
    val isPastelTendernessUnlocked = unlockedThemes.contains(AppThemeMode.PASTEL_TENDERNESS.name)
    val isVanillaCreamUnlocked = unlockedThemes.contains(AppThemeMode.VANILLA_CREAM.name)
    val isLavenderMistUnlocked = unlockedThemes.contains(AppThemeMode.LAVENDER_MIST.name)
    val isMarshmallowDreamUnlocked = unlockedThemes.contains(AppThemeMode.MARSHMALLOW_DREAM.name)
    val isSoftBeigeUnlocked = unlockedThemes.contains(AppThemeMode.SOFT_BEIGE.name)
    val isApricotMousseUnlocked = unlockedThemes.contains(AppThemeMode.APRICOT_MOUSSE.name)
    val isCottonSilkUnlocked = unlockedThemes.contains(AppThemeMode.COTTON_SILK.name)
    val isAshMarshmallowUnlocked = unlockedThemes.contains(AppThemeMode.ASH_MARSHMALLOW.name)
    val isSmokyMintUnlocked = unlockedThemes.contains(AppThemeMode.SMOKY_MINT.name)
    val isPowderyPeachUnlocked = unlockedThemes.contains(AppThemeMode.POWDERY_PEACH.name)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ImmersiveBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .testTag("shop_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Shop Header Card with Balance
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 680.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
            border = BorderStroke(1.dp, ImmersiveCardBorderSubtle)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(ImmersiveLilacContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Магазин",
                            tint = ImmersiveLilac,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "МАГАЗИН ТЕМ",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Эксклюзивные палитры оформления",
                            fontSize = 11.5.sp,
                            color = TextSecondary
                        )
                    }
                }

                Text(
                    text = "Nd ${engineState.ndBalance}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = ImmersiveLilac
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tab Switcher: "Темы оформления" & "Достижения"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 680.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(ImmersiveSurface)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { subTab = 0 }
                    .testTag("shop_tab_themes"),
                color = if (subTab == 0) ImmersiveLilac.copy(alpha = 0.22f) else Color.Transparent,
                border = if (subTab == 0) BorderStroke(1.dp, ImmersiveLilac.copy(alpha = 0.6f)) else null,
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = if (subTab == 0) ImmersiveLilac else TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Темы",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (subTab == 0) ImmersiveLilac else TextSecondary
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { subTab = 1 }
                    .testTag("shop_tab_achievements"),
                color = if (subTab == 1) Color(0xFFFFD700).copy(alpha = 0.22f) else Color.Transparent,
                border = if (subTab == 1) BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.6f)) else null,
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = if (subTab == 1) Color(0xFFFFD700) else TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Достижения (Ур. ${userLevel.level} • $unlockedCount/$totalAchievements)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (subTab == 1) Color(0xFFFFD700) else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (subTab == 0) {
            // Theme Showcase Section
            Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 680.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = ImmersiveLilac,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "ЭКСКЛЮЗИВНЫЕ ТЕМЫ",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = ImmersiveLilac,
                    letterSpacing = 1.sp
                )
            }

            // Dixel Mine отображается заблокированной с иконкой замка и прогресс-баром пока не пройдены все 24 пака
            if (!isMinecraftBlocksUnlocked) {
                ThemeCard(
                    palette = MinecraftBlocksPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = {
                        val isEmulatorRunning = PermissionHelper.isPpssppProcessRunning(context)
                        val isIdleScreen = engineState.isIdleScreenDetected || engineState.isGtIconScreenDetected
                        val isGamePaused = engineState.isGamePausedDetected
                        viewModel.notifyThemeLocked(
                            completedCount = effectiveCompletedNormalPacksCount,
                            totalRequired = totalNormalPacksCount,
                            hasStorage = hasStorage,
                            isEmulatorRunning = isEmulatorRunning,
                            isIdleScreen = isIdleScreen,
                            isGamePaused = isGamePaused
                        )
                    },
                    onBuy = null,
                    priceText = "",
                    isAchievementTheme = false,
                    hideColorDescription = true,
                    completedPacksCount = effectiveCompletedNormalPacksCount,
                    totalRequiredPacks = totalNormalPacksCount,
                    hasStoragePermission = hasStorage
                )
            }

            if (!isStoneTimberUnlocked) {
                ThemeCard(
                    palette = StoneTimberPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.STONE_TIMBER, cost = 24)
                    },
                    priceText = "Nd 24"
                )
            }

            if (!isUrbanPinkUnlocked) {
                ThemeCard(
                    palette = UrbanPinkPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.URBAN_PINK, cost = 48)
                    },
                    priceText = "Nd 48"
                )
            }

            if (!isCrimsonSparkUnlocked) {
                ThemeCard(
                    palette = CrimsonSparkPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.CRIMSON_SPARK, cost = 33)
                    },
                    priceText = "Nd 33"
                )
            }

            if (!isWodenerWorcUnlocked) {
                ThemeCard(
                    palette = WodenerWorcPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.WODENER_WORC, cost = 86)
                    },
                    priceText = "Nd 86"
                )
            }

            if (!isLasuriteCrystalUnlocked) {
                ThemeCard(
                    palette = LasuriteCrystalPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.LASURITE_CRYSTAL, cost = 13)
                    },
                    priceText = "Nd 13"
                )
            }

            if (!isInfernoStreetUnlocked) {
                ThemeCard(
                    palette = InfernoStreetPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.INFERNO_STREET, cost = 10)
                    },
                    priceText = "Nd 10"
                )
            }

            if (!isCrypticFrostUnlocked) {
                ThemeCard(
                    palette = CrypticFrostPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.CRYPTIC_FROST, cost = 92)
                    },
                    priceText = "Nd 92"
                )
            }

            if (!isGhoulKenKanekiUnlocked) {
                ThemeCard(
                    palette = GhoulKenKanekiPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.GHOUL_KEN_KANEKI, cost = 107)
                    },
                    priceText = "Nd 107"
                )
            }

            if (!isNeonCyberpunkUnlocked) {
                ThemeCard(
                    palette = NeonCyberpunkPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.NEON_CYBERPUNK, cost = 450)
                    },
                    priceText = "Nd 450"
                )
            }

            if (!isCyberSunsetUnlocked) {
                ThemeCard(
                    palette = CyberSunsetPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.CYBER_SUNSET, cost = 123)
                    },
                    priceText = "Nd 123"
                )
            }

            if (!isDigitalDawnUnlocked) {
                ThemeCard(
                    palette = DigitalDawnPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.DIGITAL_DAWN, cost = 892)
                    },
                    priceText = "Nd 892"
                )
            }

            if (!isNeonPulseUnlocked) {
                ThemeCard(
                    palette = NeonPulsePalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.NEON_PULSE, cost = 35)
                    },
                    priceText = "Nd 35"
                )
            }

            if (!isCyberMintUnlocked) {
                ThemeCard(
                    palette = CyberMintPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.CYBER_MINT, cost = 612)
                    },
                    priceText = "Nd 612"
                )
            }

            if (!isElectricChargeUnlocked) {
                ThemeCard(
                    palette = ElectricChargePalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.ELECTRIC_CHARGE, cost = 78)
                    },
                    priceText = "Nd 78"
                )
            }

            if (!isNeonSpectrumUnlocked) {
                ThemeCard(
                    palette = NeonSpectrumPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.NEON_SPECTRUM, cost = 934)
                    },
                    priceText = "Nd 934"
                )
            }

            if (!isToxicBurstUnlocked) {
                ThemeCard(
                    palette = ToxicBurstPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.TOXIC_BURST, cost = 205)
                    },
                    priceText = "Nd 205"
                )
            }

            if (!isCyberPeachUnlocked) {
                ThemeCard(
                    palette = CyberPeachPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.CYBER_PEACH, cost = 511)
                    },
                    priceText = "Nd 511"
                )
            }

            if (!isNeonEmeraldUnlocked) {
                ThemeCard(
                    palette = NeonEmeraldPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.NEON_EMERALD, cost = 88)
                    },
                    priceText = "Nd 88"
                )
            }

            if (!isPastelTendernessUnlocked) {
                ThemeCard(
                    palette = PastelTendernessPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.PASTEL_TENDERNESS, cost = 340)
                    },
                    priceText = "Nd 340"
                )
            }

            if (!isVanillaCreamUnlocked) {
                ThemeCard(
                    palette = VanillaCreamPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.VANILLA_CREAM, cost = 765)
                    },
                    priceText = "Nd 765"
                )
            }

            if (!isLavenderMistUnlocked) {
                ThemeCard(
                    palette = LavenderMistPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.LAVENDER_MIST, cost = 199)
                    },
                    priceText = "Nd 199"
                )
            }

            if (!isMarshmallowDreamUnlocked) {
                ThemeCard(
                    palette = MarshmallowDreamPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.MARSHMALLOW_DREAM, cost = 555)
                    },
                    priceText = "Nd 555"
                )
            }

            if (!isSoftBeigeUnlocked) {
                ThemeCard(
                    palette = SoftBeigePalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.SOFT_BEIGE, cost = 2)
                    },
                    priceText = "Nd 2"
                )
            }

            if (!isApricotMousseUnlocked) {
                ThemeCard(
                    palette = ApricotMoussePalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.APRICOT_MOUSSE, cost = 910)
                    },
                    priceText = "Nd 910"
                )
            }

            if (!isCottonSilkUnlocked) {
                ThemeCard(
                    palette = CottonSilkPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.COTTON_SILK, cost = 422)
                    },
                    priceText = "Nd 422"
                )
            }

            if (!isAshMarshmallowUnlocked) {
                ThemeCard(
                    palette = AshMarshmallowPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.ASH_MARSHMALLOW, cost = 688)
                    },
                    priceText = "Nd 688"
                )
            }

            if (!isSmokyMintUnlocked) {
                ThemeCard(
                    palette = SmokyMintPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.SMOKY_MINT, cost = 315)
                    },
                    priceText = "Nd 315"
                )
            }

            if (!isPowderyPeachUnlocked) {
                ThemeCard(
                    palette = PowderyPeachPalette,
                    isSelected = false,
                    isLocked = true,
                    onSelect = { },
                    onBuy = {
                        viewModel.purchaseTheme(AppThemeMode.POWDERY_PEACH, cost = 801)
                    },
                    priceText = "Nd 801"
                )
            }

            if (isStoneTimberUnlocked && isUrbanPinkUnlocked && isCrimsonSparkUnlocked && isWodenerWorcUnlocked && isLasuriteCrystalUnlocked && isInfernoStreetUnlocked && isCrypticFrostUnlocked && isGhoulKenKanekiUnlocked && isNeonCyberpunkUnlocked && isCyberSunsetUnlocked && isDigitalDawnUnlocked && isNeonPulseUnlocked && isCyberMintUnlocked && isElectricChargeUnlocked && isNeonSpectrumUnlocked && isToxicBurstUnlocked && isCyberPeachUnlocked && isNeonEmeraldUnlocked && isPastelTendernessUnlocked && isVanillaCreamUnlocked && isLavenderMistUnlocked && isMarshmallowDreamUnlocked && isSoftBeigeUnlocked && isApricotMousseUnlocked && isCottonSilkUnlocked && isAshMarshmallowUnlocked && isSmokyMintUnlocked && isPowderyPeachUnlocked) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = ImmersiveSurface,
                    border = BorderStroke(1.dp, ImmersiveCardBorderSubtle)
                ) {
                    Text(
                        text = "Все эксклюзивные темы в магазине успешно приобретены!",
                        fontSize = 12.5.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    } else {
        // Achievements Section (Система достижений)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp)
                    .testTag("achievements_list"),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Summary Progress Card with Level and XP
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
                    border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Level Hex/Circle Badge
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFFFD700).copy(alpha = 0.2f),
                                    border = BorderStroke(1.2.dp, Color(0xFFFFD700).copy(alpha = 0.7f))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "УРОВЕНЬ",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            color = Color(0xFFFFD700).copy(alpha = 0.8f),
                                            letterSpacing = 0.8.sp
                                        )
                                        Text(
                                            text = "${userLevel.level}",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.Monospace,
                                            color = Color(0xFFFFD700)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = userLevel.rankTitle.uppercase(),
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontFamily = FontFamily.Monospace,
                                        color = Color(0xFFFFD700),
                                        letterSpacing = 0.8.sp
                                    )
                                    Text(
                                        text = "$unlockedCount из $totalAchievements ачивок открыто",
                                        fontSize = 11.5.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            // Total Cumulative XP Pill
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFFFD700).copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(text = "⚡", fontSize = 12.sp)
                                    Text(
                                        text = "${userLevel.totalXp} XP",
                                        color = Color(0xFFFFD700),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }

                        // Level Progress Bar
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${userLevel.currentLevelXp} / ${userLevel.xpForNextLevel} XP до Уровня ${userLevel.level + 1}",
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "${(userLevel.progress * 100).toInt()}%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFD700),
                                    fontFamily = FontFamily.Monospace
                                )
                            }

                            LinearProgressIndicator(
                                progress = { userLevel.progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = Color(0xFFFFD700),
                                trackColor = Color(0xFF262638)
                            )
                        }
                    }
                }

                Text(
                    text = "СПИСОК ВСЕХ АЧИВОК",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFFFFD700),
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 6.dp, bottom = 2.dp)
                )

                Achievement.values().forEach { achievement ->
                    val isUnlocked = unlockedAchievements.contains(achievement.id)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("achievement_card_${achievement.id}"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUnlocked) Color(0xFF191824) else ImmersiveSurface
                        ),
                        border = BorderStroke(
                            width = if (isUnlocked) 1.2.dp else 1.dp,
                            color = if (isUnlocked) Color(0xFFFFD700).copy(alpha = 0.55f) else ImmersiveCardBorderSubtle
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Icon Badge
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isUnlocked) {
                                            Color(0xFFFFD700).copy(alpha = 0.2f)
                                        } else {
                                            Color.White.copy(alpha = 0.05f)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isUnlocked) achievement.iconEmoji else "🔒",
                                    fontSize = if (isUnlocked) 22.sp else 18.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = achievement.title,
                                    color = if (isUnlocked) TextPrimary else TextSecondary,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = achievement.description,
                                    color = if (isUnlocked) TextSecondary else TextTertiary,
                                    fontSize = 11.5.sp,
                                    lineHeight = 15.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Status / Reward Badge
                            if (isUnlocked) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFFFD700).copy(alpha = 0.15f),
                                    border = BorderStroke(0.8.dp, Color(0xFFFFD700).copy(alpha = 0.6f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Получено",
                                            tint = Color(0xFFFFD700),
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = "+${achievement.rewardXp} XP",
                                            color = Color(0xFFFFD700),
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.White.copy(alpha = 0.05f),
                                    border = BorderStroke(0.8.dp, ImmersiveCardBorderSubtle)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = "Заблокировано",
                                            tint = TextTertiary,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = "+${achievement.rewardXp} XP",
                                            color = TextTertiary,
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Medium,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
