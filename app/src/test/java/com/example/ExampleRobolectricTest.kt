package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("NDCYCLE", appName)
  }

  @Test
  fun `engine state coordinates update correctly`() {
    val state = com.example.model.EngineState(
      startX = 1169,
      startY = 25,
      endX = 1194,
      endY = 25
    )
    assertEquals(1169, state.startX)
    assertEquals(25, state.startY)
    assertEquals(1194, state.endX)
    assertEquals(25, state.endY)

    com.example.service.NDCycleService.updateGestureCoordinates(500, 300, 600, 350)
    val updated = com.example.service.NDCycleService.engineState.value
    assertEquals(500, updated.startX)
    assertEquals(300, updated.startY)
    assertEquals(600, updated.endX)
    assertEquals(350, updated.endY)
  }

  @Test
  fun `auto save persists settings to SharedPreferences`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = com.example.ui.MainViewModel(app)

    viewModel.setCycleInterval(42)
    viewModel.setRainProbability(77)
    viewModel.setRandomMode(true)

    val prefs = app.getSharedPreferences("ndcycle_app_prefs", Context.MODE_PRIVATE)
    assertEquals(42, prefs.getInt("key_cycle_interval", 0))
    assertEquals(77, prefs.getInt("key_rain_probability", 0))
    assertEquals(true, prefs.getBoolean("key_random_mode", false))
  }

  @Test
  fun `urban pink theme purchase with Nd 48 unlocks and sets active theme`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val prefs = app.getSharedPreferences("ndcycle_app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putInt("key_nd_balance", 100).apply()

    val viewModel = com.example.ui.MainViewModel(app)
    viewModel.purchaseTheme(com.example.ui.theme.AppThemeMode.URBAN_PINK, cost = 48)

    assertEquals(com.example.ui.theme.AppThemeMode.URBAN_PINK, viewModel.appTheme.value)
    assertEquals(52, prefs.getInt("key_nd_balance", 0))
    org.junit.Assert.assertTrue(viewModel.unlockedThemes.value.contains("URBAN_PINK"))
  }

  @Test
  fun `crimson spark theme purchase with Nd 33 unlocks and sets active theme`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val prefs = app.getSharedPreferences("ndcycle_app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putInt("key_nd_balance", 100).apply()

    val viewModel = com.example.ui.MainViewModel(app)
    viewModel.purchaseTheme(com.example.ui.theme.AppThemeMode.CRIMSON_SPARK, cost = 33)

    assertEquals(com.example.ui.theme.AppThemeMode.CRIMSON_SPARK, viewModel.appTheme.value)
    assertEquals(67, prefs.getInt("key_nd_balance", 0))
    org.junit.Assert.assertTrue(viewModel.unlockedThemes.value.contains("CRIMSON_SPARK"))
  }

  @Test
  fun `minecraft blocks theme purchase with Nd 64 unlocks and sets active theme`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val prefs = app.getSharedPreferences("ndcycle_app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putInt("key_nd_balance", 100).apply()

    val viewModel = com.example.ui.MainViewModel(app)
    viewModel.purchaseTheme(com.example.ui.theme.AppThemeMode.MINECRAFT_BLOCKS, cost = 64)

    assertEquals(com.example.ui.theme.AppThemeMode.MINECRAFT_BLOCKS, viewModel.appTheme.value)
    assertEquals(36, prefs.getInt("key_nd_balance", 0))
    org.junit.Assert.assertTrue(viewModel.unlockedThemes.value.contains("MINECRAFT_BLOCKS"))
  }

  @Test
  fun `wodener worc theme purchase with 86 Nd spends balance and unlocks theme`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val prefs = app.getSharedPreferences("ndcycle_app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putInt("key_nd_balance", 120).apply()

    val viewModel = com.example.ui.MainViewModel(app)
    viewModel.purchaseTheme(com.example.ui.theme.AppThemeMode.WODENER_WORC, cost = 86)

    assertEquals(com.example.ui.theme.AppThemeMode.WODENER_WORC, viewModel.appTheme.value)
    assertEquals(34, prefs.getInt("key_nd_balance", 0))
    org.junit.Assert.assertTrue(viewModel.unlockedThemes.value.contains("WODENER_WORC"))
  }

  @Test
  fun `lasurite crystal theme purchase with 13 Nd spends balance and unlocks theme`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val prefs = app.getSharedPreferences("ndcycle_app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putInt("key_nd_balance", 50).apply()

    val viewModel = com.example.ui.MainViewModel(app)
    viewModel.purchaseTheme(com.example.ui.theme.AppThemeMode.LASURITE_CRYSTAL, cost = 13)

    assertEquals(com.example.ui.theme.AppThemeMode.LASURITE_CRYSTAL, viewModel.appTheme.value)
    assertEquals(37, prefs.getInt("key_nd_balance", 0))
    org.junit.Assert.assertTrue(viewModel.unlockedThemes.value.contains("LASURITE_CRYSTAL"))

    val palette = com.example.ui.theme.AppThemeMode.LASURITE_CRYSTAL.palette
    assertEquals("Lasurite Crystal", palette.displayName)
    assertEquals(5, palette.accentSwatchList.size)
  }

  @Test
  fun `inferno street theme purchase with 10 Nd spends balance and unlocks theme`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val prefs = app.getSharedPreferences("ndcycle_app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putInt("key_nd_balance", 50).apply()

    val viewModel = com.example.ui.MainViewModel(app)
    viewModel.purchaseTheme(com.example.ui.theme.AppThemeMode.INFERNO_STREET, cost = 10)

    assertEquals(com.example.ui.theme.AppThemeMode.INFERNO_STREET, viewModel.appTheme.value)
    assertEquals(40, prefs.getInt("key_nd_balance", 0))
    org.junit.Assert.assertTrue(viewModel.unlockedThemes.value.contains("INFERNO_STREET"))

    val palette = com.example.ui.theme.AppThemeMode.INFERNO_STREET.palette
    assertEquals("Inferno Street", palette.displayName)
    assertEquals(5, palette.accentSwatchList.size)
  }

  @Test
  fun `cryptic frost theme purchase with 92 Nd spends balance and unlocks theme`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val prefs = app.getSharedPreferences("ndcycle_app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putInt("key_nd_balance", 100).apply()

    val viewModel = com.example.ui.MainViewModel(app)
    viewModel.purchaseTheme(com.example.ui.theme.AppThemeMode.CRYPTIC_FROST, cost = 92)

    assertEquals(com.example.ui.theme.AppThemeMode.CRYPTIC_FROST, viewModel.appTheme.value)
    assertEquals(8, prefs.getInt("key_nd_balance", 0))
    org.junit.Assert.assertTrue(viewModel.unlockedThemes.value.contains("CRYPTIC_FROST"))

    val palette = com.example.ui.theme.AppThemeMode.CRYPTIC_FROST.palette
    assertEquals("Cryptic Frost", palette.displayName)
    assertEquals(5, palette.accentSwatchList.size)
  }

  @Test
  fun `ghoul ken kaneki theme purchase with 107 Nd spends balance and unlocks theme`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val prefs = app.getSharedPreferences("ndcycle_app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putInt("key_nd_balance", 200).apply()

    val viewModel = com.example.ui.MainViewModel(app)
    viewModel.purchaseTheme(com.example.ui.theme.AppThemeMode.GHOUL_KEN_KANEKI, cost = 107)

    assertEquals(com.example.ui.theme.AppThemeMode.GHOUL_KEN_KANEKI, viewModel.appTheme.value)
    assertEquals(93, prefs.getInt("key_nd_balance", 0))
    org.junit.Assert.assertTrue(viewModel.unlockedThemes.value.contains("GHOUL_KEN_KANEKI"))

    val palette = com.example.ui.theme.AppThemeMode.GHOUL_KEN_KANEKI.palette
    assertEquals("Ghoul Ken Kaneki", palette.displayName)
    assertEquals(5, palette.accentSwatchList.size)
  }

  @Test
  fun `purchase and verification of 5 new shop themes without effects`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val prefs = app.getSharedPreferences("ndcycle_app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putInt("key_nd_balance", 2500).apply()

    val viewModel = com.example.ui.MainViewModel(app)

    // 1. Neon Cyberpunk (Nd 450)
    viewModel.purchaseTheme(com.example.ui.theme.AppThemeMode.NEON_CYBERPUNK, cost = 450)
    assertEquals(com.example.ui.theme.AppThemeMode.NEON_CYBERPUNK, viewModel.appTheme.value)
    org.junit.Assert.assertTrue(viewModel.unlockedThemes.value.contains("NEON_CYBERPUNK"))
    val p1 = com.example.ui.theme.AppThemeMode.NEON_CYBERPUNK.palette
    assertEquals("Neon Cyberpunk", p1.displayName)
    assertEquals(5, p1.accentSwatchList.size)

    // 2. Cyber Sunset (Nd 123)
    viewModel.purchaseTheme(com.example.ui.theme.AppThemeMode.CYBER_SUNSET, cost = 123)
    assertEquals(com.example.ui.theme.AppThemeMode.CYBER_SUNSET, viewModel.appTheme.value)
    org.junit.Assert.assertTrue(viewModel.unlockedThemes.value.contains("CYBER_SUNSET"))
    val p2 = com.example.ui.theme.AppThemeMode.CYBER_SUNSET.palette
    assertEquals("Cyber Sunset", p2.displayName)
    assertEquals(5, p2.accentSwatchList.size)

    // 3. Digital Dawn (Nd 892)
    viewModel.purchaseTheme(com.example.ui.theme.AppThemeMode.DIGITAL_DAWN, cost = 892)
    assertEquals(com.example.ui.theme.AppThemeMode.DIGITAL_DAWN, viewModel.appTheme.value)
    org.junit.Assert.assertTrue(viewModel.unlockedThemes.value.contains("DIGITAL_DAWN"))
    val p3 = com.example.ui.theme.AppThemeMode.DIGITAL_DAWN.palette
    assertEquals("Digital Dawn", p3.displayName)
    assertEquals(5, p3.accentSwatchList.size)

    // 4. Neon Pulse (Nd 35)
    viewModel.purchaseTheme(com.example.ui.theme.AppThemeMode.NEON_PULSE, cost = 35)
    assertEquals(com.example.ui.theme.AppThemeMode.NEON_PULSE, viewModel.appTheme.value)
    org.junit.Assert.assertTrue(viewModel.unlockedThemes.value.contains("NEON_PULSE"))
    val p4 = com.example.ui.theme.AppThemeMode.NEON_PULSE.palette
    assertEquals("Neon Pulse", p4.displayName)
    assertEquals(5, p4.accentSwatchList.size)

    // 5. Cyber Mint (Nd 612)
    viewModel.purchaseTheme(com.example.ui.theme.AppThemeMode.CYBER_MINT, cost = 612)
    assertEquals(com.example.ui.theme.AppThemeMode.CYBER_MINT, viewModel.appTheme.value)
    org.junit.Assert.assertTrue(viewModel.unlockedThemes.value.contains("CYBER_MINT"))
    val p5 = com.example.ui.theme.AppThemeMode.CYBER_MINT.palette
    assertEquals("Cyber Mint", p5.displayName)
    assertEquals(5, p5.accentSwatchList.size)
  }

  @Test
  fun `purchase and verification of 5 new shop themes with simple effects`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val prefs = app.getSharedPreferences("ndcycle_app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putInt("key_nd_balance", 3500).apply()

    val viewModel = com.example.ui.MainViewModel(app)

    // 1. Electric Charge (Nd 78)
    viewModel.purchaseTheme(com.example.ui.theme.AppThemeMode.ELECTRIC_CHARGE, cost = 78)
    assertEquals(com.example.ui.theme.AppThemeMode.ELECTRIC_CHARGE, viewModel.appTheme.value)
    org.junit.Assert.assertTrue(viewModel.unlockedThemes.value.contains("ELECTRIC_CHARGE"))
    val p1 = com.example.ui.theme.AppThemeMode.ELECTRIC_CHARGE.palette
    assertEquals("Electric Charge", p1.displayName)
    assertEquals(5, p1.accentSwatchList.size)

    // 2. Neon Spectrum (Nd 934)
    viewModel.purchaseTheme(com.example.ui.theme.AppThemeMode.NEON_SPECTRUM, cost = 934)
    assertEquals(com.example.ui.theme.AppThemeMode.NEON_SPECTRUM, viewModel.appTheme.value)
    org.junit.Assert.assertTrue(viewModel.unlockedThemes.value.contains("NEON_SPECTRUM"))
    val p2 = com.example.ui.theme.AppThemeMode.NEON_SPECTRUM.palette
    assertEquals("Neon Spectrum", p2.displayName)
    assertEquals(5, p2.accentSwatchList.size)

    // 3. Toxic Burst (Nd 205)
    viewModel.purchaseTheme(com.example.ui.theme.AppThemeMode.TOXIC_BURST, cost = 205)
    assertEquals(com.example.ui.theme.AppThemeMode.TOXIC_BURST, viewModel.appTheme.value)
    org.junit.Assert.assertTrue(viewModel.unlockedThemes.value.contains("TOXIC_BURST"))
    val p3 = com.example.ui.theme.AppThemeMode.TOXIC_BURST.palette
    assertEquals("Toxic Burst", p3.displayName)
    assertEquals(5, p3.accentSwatchList.size)

    // 4. Cyber Peach (Nd 511)
    viewModel.purchaseTheme(com.example.ui.theme.AppThemeMode.CYBER_PEACH, cost = 511)
    assertEquals(com.example.ui.theme.AppThemeMode.CYBER_PEACH, viewModel.appTheme.value)
    org.junit.Assert.assertTrue(viewModel.unlockedThemes.value.contains("CYBER_PEACH"))
    val p4 = com.example.ui.theme.AppThemeMode.CYBER_PEACH.palette
    assertEquals("Cyber Peach", p4.displayName)
    assertEquals(5, p4.accentSwatchList.size)

    // 5. Neon Emerald (Nd 88)
    viewModel.purchaseTheme(com.example.ui.theme.AppThemeMode.NEON_EMERALD, cost = 88)
    assertEquals(com.example.ui.theme.AppThemeMode.NEON_EMERALD, viewModel.appTheme.value)
    org.junit.Assert.assertTrue(viewModel.unlockedThemes.value.contains("NEON_EMERALD"))
    val p5 = com.example.ui.theme.AppThemeMode.NEON_EMERALD.palette
    assertEquals("Neon Emerald", p5.displayName)
    assertEquals(5, p5.accentSwatchList.size)
  }

  @Test
  fun `purchase and verification of 10 new pastel shop themes without effects`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val prefs = app.getSharedPreferences("ndcycle_app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putInt("key_nd_balance", 10000).apply()

    val viewModel = com.example.ui.MainViewModel(app)

    val themesToTest = listOf(
        Pair(com.example.ui.theme.AppThemeMode.PASTEL_TENDERNESS, 340),
        Pair(com.example.ui.theme.AppThemeMode.VANILLA_CREAM, 765),
        Pair(com.example.ui.theme.AppThemeMode.LAVENDER_MIST, 199),
        Pair(com.example.ui.theme.AppThemeMode.MARSHMALLOW_DREAM, 555),
        Pair(com.example.ui.theme.AppThemeMode.SOFT_BEIGE, 2),
        Pair(com.example.ui.theme.AppThemeMode.APRICOT_MOUSSE, 910),
        Pair(com.example.ui.theme.AppThemeMode.COTTON_SILK, 422),
        Pair(com.example.ui.theme.AppThemeMode.ASH_MARSHMALLOW, 688),
        Pair(com.example.ui.theme.AppThemeMode.SMOKY_MINT, 315),
        Pair(com.example.ui.theme.AppThemeMode.POWDERY_PEACH, 801)
    )

    themesToTest.forEach { (theme, cost) ->
        viewModel.purchaseTheme(theme, cost = cost)
        assertEquals(theme, viewModel.appTheme.value)
        org.junit.Assert.assertTrue(viewModel.unlockedThemes.value.contains(theme.name))
        assertEquals(5, theme.palette.accentSwatchList.size)
    }
  }

  @Test
  fun `version badge 9 taps bonus adds 250 Nd to balance once and sets one-time flag`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val prefs = app.getSharedPreferences("ndcycle_app_prefs", Context.MODE_PRIVATE)
    prefs.edit()
      .putInt("key_nd_balance", 50)
      .putBoolean("key_version_bonus_claimed", false)
      .apply()

    val viewModel = com.example.ui.MainViewModel(app)
    val firstClaim = viewModel.claimVersionBonus(250)

    assertEquals(true, firstClaim)
    assertEquals(300, prefs.getInt("key_nd_balance", 0))
    assertEquals(300, viewModel.engineState.value.ndBalance)
    assertEquals(true, prefs.getBoolean("key_version_bonus_claimed", false))
    assertEquals(true, viewModel.isVersionBonusClaimed.value)

    // Second claim attempt should fail and not add additional Nd
    val secondClaim = viewModel.claimVersionBonus(250)
    assertEquals(false, secondClaim)
    assertEquals(300, prefs.getInt("key_nd_balance", 0))
  }

  @Test
  fun `main navigation tabs configured correctly without cases`() {
    val tabs = com.example.ui.screens.MainTab.values()
    assertEquals(8, tabs.size)
    val titles = tabs.map { it.title }
    org.junit.Assert.assertTrue(titles.contains("Панель"))
    org.junit.Assert.assertTrue(titles.contains("Трассы"))
    org.junit.Assert.assertTrue(titles.contains("Магазин"))
    org.junit.Assert.assertTrue(titles.contains("Настройки"))
    org.junit.Assert.assertTrue(titles.contains("Темы"))
    org.junit.Assert.assertTrue(titles.contains("Эксперименты"))
    org.junit.Assert.assertTrue(titles.contains("Терминал"))
    org.junit.Assert.assertTrue(titles.contains("Редактор"))
    org.junit.Assert.assertFalse(titles.contains("Кейсы"))
  }

  @Test
  fun `splash screen initializes with progress and status text`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = com.example.ui.MainViewModel(app)

    org.junit.Assert.assertTrue(viewModel.splashStatusText.value.startsWith("Инициализация"))
    org.junit.Assert.assertTrue(viewModel.splashProgress.value >= 0f)

    viewModel.completeSplashImmediately()
    assertEquals(true, viewModel.isSplashComplete.value)
    assertEquals(1.0f, viewModel.splashProgress.value, 0.001f)
    org.junit.Assert.assertTrue(viewModel.splashStatusText.value.contains("Инициализация"))
  }

  @Test
  fun `splash screen duration is exactly 5 seconds`() {
    assertEquals(5000L, com.example.ui.MainViewModel.SPLASH_DURATION_MS)
  }

  @Test
  fun `layout switcher controls position defaults to bottom and updates correctly`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val prefs = app.getSharedPreferences("ndcycle_app_prefs", Context.MODE_PRIVATE)
    prefs.edit().remove("key_controls_position").apply()

    val viewModel = com.example.ui.MainViewModel(app)
    assertEquals(com.example.model.ControlsPosition.BOTTOM, viewModel.controlsPosition.value)

    viewModel.setControlsPosition(com.example.model.ControlsPosition.TOP)
    assertEquals(com.example.model.ControlsPosition.TOP, viewModel.controlsPosition.value)
    assertEquals("TOP", prefs.getString("key_controls_position", null))

    viewModel.setControlsPosition(com.example.model.ControlsPosition.DOCKED_BOTTOM)
    assertEquals(com.example.model.ControlsPosition.DOCKED_BOTTOM, viewModel.controlsPosition.value)
    assertEquals("DOCKED_BOTTOM", prefs.getString("key_controls_position", null))
  }

  @Test
  fun `custom drawing wallpaper can be applied and cleared correctly`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val prefs = app.getSharedPreferences("ndcycle_app_prefs", Context.MODE_PRIVATE)
    prefs.edit().remove("key_custom_wallpaper").apply()

    val viewModel = com.example.ui.MainViewModel(app)
    org.junit.Assert.assertNull(viewModel.customDrawingWallpaper.value)

    val strokes = listOf(
      com.example.model.DrawingStroke(
        points = listOf(
          androidx.compose.ui.geometry.Offset(10f, 20f),
          androidx.compose.ui.geometry.Offset(30f, 40f)
        ),
        color = androidx.compose.ui.graphics.Color.Cyan,
        strokeWidth = 8f
      )
    )

    val applied = viewModel.applyDrawingAsBackground(strokes, 720f, 480f, 0.9f)
    org.junit.Assert.assertTrue(applied)

    val wallpaper = viewModel.customDrawingWallpaper.value
    org.junit.Assert.assertNotNull(wallpaper)
    assertEquals(1, wallpaper!!.strokes.size)
    assertEquals(720f, wallpaper.canvasWidth, 0.01f)
    assertEquals(480f, wallpaper.canvasHeight, 0.01f)
    assertEquals(0.9f, wallpaper.opacity, 0.01f)

    // Verify persistence in shared preferences
    val serialized = prefs.getString("key_custom_wallpaper", null)
    org.junit.Assert.assertNotNull(serialized)

    // Verify clear
    viewModel.clearCustomDrawingBackground()
    org.junit.Assert.assertNull(viewModel.customDrawingWallpaper.value)
    org.junit.Assert.assertNull(prefs.getString("key_custom_wallpaper", null))
  }

  @Test
  fun `custom wallpaper serialization and deserialization preserves points and properties`() {
    val strokes = listOf(
      com.example.model.DrawingStroke(
        points = listOf(
          androidx.compose.ui.geometry.Offset(100f, 150f),
          androidx.compose.ui.geometry.Offset(200f, 250f)
        ),
        color = androidx.compose.ui.graphics.Color(0xFFD0BCFF),
        strokeWidth = 12f
      )
    )
    val original = com.example.model.CustomWallpaper(
      strokes = strokes,
      canvasWidth = 1080f,
      canvasHeight = 1920f,
      opacity = 0.75f
    )

    val str = original.serializeToString()
    val restored = com.example.model.CustomWallpaper.deserializeFromString(str)

    org.junit.Assert.assertNotNull(restored)
    assertEquals(1, restored!!.strokes.size)
    assertEquals(1080f, restored.canvasWidth, 0.01f)
    assertEquals(1920f, restored.canvasHeight, 0.01f)
    assertEquals(0.75f, restored.opacity, 0.01f)
    assertEquals(2, restored.strokes[0].points.size)
    assertEquals(100f, restored.strokes[0].points[0].x, 0.01f)
    assertEquals(150f, restored.strokes[0].points[0].y, 0.01f)
  }

  @Test
  fun `textures ini content matches PPSSPP quick hash template`() {
    val expected = """[options]
version = 1
hash = quick

[hashes]
"""
    assertEquals(expected, com.example.data.TrackDictionary.TEXTURES_INI_CONTENT)
  }

  @Test
  fun `storage permissions array contains standard runtime permissions`() {
    val perms = com.example.util.PermissionHelper.getStoragePermissions()
    org.junit.Assert.assertTrue(perms.isNotEmpty())
    val hasMediaOrStorage = perms.any {
      it == android.Manifest.permission.READ_MEDIA_IMAGES ||
      it == android.Manifest.permission.READ_EXTERNAL_STORAGE
    }
    org.junit.Assert.assertTrue(hasMediaOrStorage)
  }

  @Test
  fun `unlocking achievement adds to unlocked set and shows popup`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val prefs = app.getSharedPreferences("ndcycle_app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putInt("key_nd_balance", 100).putStringSet("key_unlocked_achievements", emptySet()).apply()

    val viewModel = com.example.ui.MainViewModel(app)
    viewModel.unlockAchievement(com.example.model.Achievement.TRACK_EXPLORER)

    // Verify unlocked in ViewModel and SharedPreferences
    org.junit.Assert.assertTrue(viewModel.unlockedAchievements.value.contains(com.example.model.Achievement.TRACK_EXPLORER.id))
    org.junit.Assert.assertTrue(prefs.getStringSet("key_unlocked_achievements", emptySet())!!.contains(com.example.model.Achievement.TRACK_EXPLORER.id))

    // Verify popup event is created and contains correct action description
    val popup = viewModel.achievementPopup.value
    org.junit.Assert.assertNotNull(popup)
    assertEquals(com.example.model.Achievement.TRACK_EXPLORER, popup!!.achievement)
    assertEquals("Выбрана новая трасса для подмены текстур", popup.achievement.description)
    assertEquals("Новая трасса", popup.achievement.title)
  }

  @Test
  fun `unlocking duplicate achievement does not duplicate entries`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val prefs = app.getSharedPreferences("ndcycle_app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putStringSet("key_unlocked_achievements", emptySet()).apply()

    val viewModel = com.example.ui.MainViewModel(app)
    viewModel.unlockAchievement(com.example.model.Achievement.TIME_WARP)
    assertEquals(1, viewModel.unlockedAchievements.value.size)

    // Second unlock attempt should be ignored
    viewModel.unlockAchievement(com.example.model.Achievement.TIME_WARP)
    assertEquals(1, viewModel.unlockedAchievements.value.size)
  }

  @Test
  fun `user actions trigger corresponding achievements`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val prefs = app.getSharedPreferences("ndcycle_app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putInt("key_nd_balance", 500).putStringSet("key_unlocked_achievements", emptySet()).apply()

    val viewModel = com.example.ui.MainViewModel(app)

    // Action 1: setTrack triggers TRACK_EXPLORER
    viewModel.setTrack("track_fuji")
    org.junit.Assert.assertTrue(viewModel.unlockedAchievements.value.contains(com.example.model.Achievement.TRACK_EXPLORER.id))

    // Action 2: setCycleInterval triggers TIME_WARP
    viewModel.setCycleInterval(45)
    org.junit.Assert.assertTrue(viewModel.unlockedAchievements.value.contains(com.example.model.Achievement.TIME_WARP.id))

    // Action 3: setRainProbability triggers RAIN_MAKER
    viewModel.setRainProbability(35)
    org.junit.Assert.assertTrue(viewModel.unlockedAchievements.value.contains(com.example.model.Achievement.RAIN_MAKER.id))

    // Action 4: testSwipeNow triggers SWIPE_TESTED
    viewModel.testSwipeNow()
    org.junit.Assert.assertTrue(viewModel.unlockedAchievements.value.contains(com.example.model.Achievement.SWIPE_TESTED.id))
  }

  @Test
  fun `level calculator computes correct level progress and rank for XP`() {
    val level0 = com.example.model.LevelCalculator.calculate(0)
    assertEquals(1, level0.level)
    assertEquals(0, level0.currentLevelXp)
    assertEquals(0.0f, level0.progress, 0.001f)
    assertEquals("Новичок", level0.rankTitle)

    val level150 = com.example.model.LevelCalculator.calculate(150)
    assertEquals(2, level150.level)
    assertEquals(50, level150.currentLevelXp)
    assertEquals(0.5f, level150.progress, 0.001f)
    assertEquals("Энтузиаст", level150.rankTitle)

    val level320 = com.example.model.LevelCalculator.calculate(320)
    assertEquals(4, level320.level)
    assertEquals(20, level320.currentLevelXp)
    assertEquals("Специалист", level320.rankTitle)

    val levelMax = com.example.model.LevelCalculator.calculate(1050)
    assertEquals(11, levelMax.level)
    assertEquals("Легенда", levelMax.rankTitle)
  }

  @Test
  fun `all achievements award positive XP experience`() {
    com.example.model.Achievement.values().forEach { achievement ->
      org.junit.Assert.assertTrue("Achievement ${achievement.id} must award positive XP", achievement.rewardXp > 0)
    }
    val totalAvailableXp = com.example.model.Achievement.values().sumOf { it.rewardXp }
    org.junit.Assert.assertTrue("Total available XP should be substantial", totalAvailableXp >= 900)
  }

  @Test
  fun `dixel mine theme remains locked and is not unlocked by achievements`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = com.example.ui.MainViewModel(app)

    org.junit.Assert.assertFalse(viewModel.unlockedThemes.value.contains("MINECRAFT_BLOCKS"))

    viewModel.unlockAchievement(com.example.model.Achievement.FIRST_LAUNCH)
    viewModel.unlockAchievement(com.example.model.Achievement.STORAGE_GRANTED)
    viewModel.unlockAchievement(com.example.model.Achievement.TIME_WARP)

    // Dixel Mine must remain locked and not bound to achievements
    org.junit.Assert.assertFalse(viewModel.unlockedThemes.value.contains("MINECRAFT_BLOCKS"))
  }

  @Test
  fun `dixel mine progress is blocked and remains 0 without storage permission`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    com.example.util.PermissionHelper.storagePermissionOverrideForTesting = false
    try {
      val viewModel = com.example.ui.MainViewModel(app)

      org.junit.Assert.assertFalse("Storage permission should be false", com.example.util.PermissionHelper.hasStoragePermission(app))

      // Attempt to mark packs completed without storage permission
      com.example.data.TrackDictionary.INITIAL_NORMAL_PACKS.forEach { pack ->
        viewModel.markPackCompleted(pack)
      }

      // Must be completely blocked: 0 completed packs
      assertEquals(0, viewModel.completedPacks.value.size)
      org.junit.Assert.assertFalse(viewModel.unlockedThemes.value.contains("MINECRAFT_BLOCKS"))
    } finally {
      com.example.util.PermissionHelper.storagePermissionOverrideForTesting = null
    }
  }

  @Test
  fun `dixel mine progress is blocked when ppsspp process is not running`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    com.example.util.PermissionHelper.storagePermissionOverrideForTesting = true
    com.example.util.PermissionHelper.emulatorProcessRunningOverrideForTesting = false
    try {
      val viewModel = com.example.ui.MainViewModel(app)

      org.junit.Assert.assertFalse("PPSSPP process should be not running", com.example.util.PermissionHelper.isPpssppProcessRunning(app))

      // Attempt to mark packs completed without org.ppsspp.ppsspp running
      com.example.data.TrackDictionary.INITIAL_NORMAL_PACKS.forEach { pack ->
        viewModel.markPackCompleted(pack)
      }

      // Bypass attempt must be strictly blocked: 0 completed packs
      assertEquals(0, viewModel.completedPacks.value.size)
      org.junit.Assert.assertFalse(viewModel.unlockedThemes.value.contains("MINECRAFT_BLOCKS"))
    } finally {
      com.example.util.PermissionHelper.storagePermissionOverrideForTesting = null
      com.example.util.PermissionHelper.emulatorProcessRunningOverrideForTesting = null
    }
  }

  @Test
  fun `dixel mine unlocks automatically when all 24 packs are completed with storage permission and ppsspp running`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    com.example.util.PermissionHelper.storagePermissionOverrideForTesting = true
    com.example.util.PermissionHelper.emulatorProcessRunningOverrideForTesting = true
    try {
      org.junit.Assert.assertTrue("Storage permission must be true", com.example.util.PermissionHelper.hasStoragePermission(app))
      org.junit.Assert.assertTrue("PPSSPP process must be running", com.example.util.PermissionHelper.isPpssppProcessRunning(app))

      val viewModel = com.example.ui.MainViewModel(app)

      org.junit.Assert.assertFalse(viewModel.unlockedThemes.value.contains("MINECRAFT_BLOCKS"))
      assertEquals(24, com.example.data.TrackDictionary.INITIAL_NORMAL_PACKS.size)

      // Complete 23 packs: should still be locked
      com.example.data.TrackDictionary.INITIAL_NORMAL_PACKS.dropLast(1).forEach { pack ->
        viewModel.markPackCompleted(pack)
      }
      assertEquals(23, viewModel.completedPacks.value.size)
      org.junit.Assert.assertFalse(viewModel.unlockedThemes.value.contains("MINECRAFT_BLOCKS"))

      // Complete the 24th pack: should unlock automatically
      val lastPack = com.example.data.TrackDictionary.INITIAL_NORMAL_PACKS.last()
      viewModel.markPackCompleted(lastPack)

      assertEquals(24, viewModel.completedPacks.value.size)
      org.junit.Assert.assertTrue("Dixel Mine must be unlocked after 24 packs", viewModel.unlockedThemes.value.contains("MINECRAFT_BLOCKS"))
      org.junit.Assert.assertTrue(viewModel.isThemeUnlocked(com.example.ui.theme.AppThemeMode.MINECRAFT_BLOCKS))

      // Can be chosen as active theme
      viewModel.setAppTheme(com.example.ui.theme.AppThemeMode.MINECRAFT_BLOCKS)
      assertEquals(com.example.ui.theme.AppThemeMode.MINECRAFT_BLOCKS, viewModel.appTheme.value)
    } finally {
      com.example.util.PermissionHelper.storagePermissionOverrideForTesting = null
      com.example.util.PermissionHelper.emulatorProcessRunningOverrideForTesting = null
    }
  }

  @Test
  fun `idle screen with gran turismo icon blocks pack progress and currency farming`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    com.example.util.PermissionHelper.storagePermissionOverrideForTesting = true
    com.example.util.PermissionHelper.emulatorProcessRunningOverrideForTesting = true
    com.example.util.GtScreenDetector.isIdleScreenOverrideForTesting = true
    try {
      val initialBalance = com.example.service.NDCycleService.getNdBalance(app)

      // Start evaluator check or simulate state update
      val result = kotlinx.coroutines.runBlocking {
        com.example.util.GtScreenDetector.evaluateScreen(app)
      }
      org.junit.Assert.assertTrue("isGtIconScreen must be true", result.isGtIconScreen)
      org.junit.Assert.assertTrue("isIdleNoGameplay must be true", result.isIdleNoGameplay)

      // Synchronize engine state with detected idle result
      com.example.service.NDCycleService.updateIdleAndGtState(
        isIdle = result.isIdleNoGameplay,
        isGtIcon = result.isGtIconScreen
      )

      val viewModel = com.example.ui.MainViewModel(app)

      // Attempt to mark packs completed while idle on GT icon screen
      com.example.data.TrackDictionary.INITIAL_NORMAL_PACKS.forEach { pack ->
        viewModel.markPackCompleted(pack)
      }

      // Progress must be strictly blocked: 0 completed packs
      assertEquals(0, viewModel.completedPacks.value.size)
      org.junit.Assert.assertFalse(viewModel.unlockedThemes.value.contains("MINECRAFT_BLOCKS"))

      // Attempt to increment currency while idle on GT icon screen
      com.example.service.NDCycleService.incrementNdBalance(app)
      com.example.service.NDCycleService.addNdBalance(app, 10, "Farming attempt")

      // Balance must NOT increase
      val finalBalance = com.example.service.NDCycleService.getNdBalance(app)
      assertEquals(initialBalance, finalBalance)
    } finally {
      com.example.util.PermissionHelper.storagePermissionOverrideForTesting = null
      com.example.util.PermissionHelper.emulatorProcessRunningOverrideForTesting = null
      com.example.util.GtScreenDetector.isIdleScreenOverrideForTesting = null
      com.example.service.NDCycleService.updateIdleAndGtState(isIdle = false, isGtIcon = false)
    }
  }

  @Test
  fun `active gran turismo gameplay allows pack completion and currency farming`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    com.example.util.PermissionHelper.storagePermissionOverrideForTesting = true
    com.example.util.PermissionHelper.emulatorProcessRunningOverrideForTesting = true
    com.example.util.GtScreenDetector.isIdleScreenOverrideForTesting = false
    try {
      val initialBalance = com.example.service.NDCycleService.getNdBalance(app)

      val result = kotlinx.coroutines.runBlocking {
        com.example.util.GtScreenDetector.evaluateScreen(app)
      }
      org.junit.Assert.assertFalse("isGtIconScreen must be false during gameplay", result.isGtIconScreen)
      org.junit.Assert.assertFalse("isIdleNoGameplay must be false during gameplay", result.isIdleNoGameplay)

      com.example.service.NDCycleService.updateIdleAndGtState(
        isIdle = false,
        isGtIcon = false
      )

      val viewModel = com.example.ui.MainViewModel(app)

      val firstPack = com.example.data.TrackDictionary.INITIAL_NORMAL_PACKS.first()
      viewModel.markPackCompleted(firstPack)

      assertEquals(1, viewModel.completedPacks.value.size)

      com.example.service.NDCycleService.incrementNdBalance(app)
      val newBalance = com.example.service.NDCycleService.getNdBalance(app)
      assertEquals(initialBalance + 1, newBalance)
    } finally {
      com.example.util.PermissionHelper.storagePermissionOverrideForTesting = null
      com.example.util.PermissionHelper.emulatorProcessRunningOverrideForTesting = null
      com.example.util.GtScreenDetector.isIdleScreenOverrideForTesting = null
    }
  }

  @Test
  fun `game paused or settings menu screen blocks pack progress and currency farming`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    com.example.util.PermissionHelper.storagePermissionOverrideForTesting = true
    com.example.util.PermissionHelper.emulatorProcessRunningOverrideForTesting = true
    com.example.util.GtScreenDetector.isGamePausedOverrideForTesting = true
    try {
      val initialBalance = com.example.service.NDCycleService.getNdBalance(app)

      val result = kotlinx.coroutines.runBlocking {
        com.example.util.GtScreenDetector.evaluateScreen(app)
      }
      org.junit.Assert.assertTrue("isGamePaused must be true", result.isGamePaused)
      org.junit.Assert.assertTrue("isIdleNoGameplay must be true during pause", result.isIdleNoGameplay)

      com.example.service.NDCycleService.updateIdleAndGtState(
        isIdle = result.isIdleNoGameplay,
        isGtIcon = false,
        isGamePaused = result.isGamePaused
      )

      val viewModel = com.example.ui.MainViewModel(app)
      org.junit.Assert.assertTrue("isGameplayBlocked must be true during pause", viewModel.engineState.value.isGameplayBlocked)
      org.junit.Assert.assertTrue("isGamePausedDetected must be true", viewModel.engineState.value.isGamePausedDetected)

      // Attempt to mark packs completed during pause / settings menu
      com.example.data.TrackDictionary.INITIAL_NORMAL_PACKS.forEach { pack ->
        viewModel.markPackCompleted(pack)
      }

      // Progress must strictly stand still: 0 completed packs
      assertEquals(0, viewModel.completedPacks.value.size)
      org.junit.Assert.assertFalse(viewModel.unlockedThemes.value.contains("MINECRAFT_BLOCKS"))

      // Attempt to increment currency during pause
      com.example.service.NDCycleService.incrementNdBalance(app)
      com.example.service.NDCycleService.addNdBalance(app, 25, "Pause farming attempt")

      // Balance must NOT increase while game is paused
      val pausedBalance = com.example.service.NDCycleService.getNdBalance(app)
      assertEquals(initialBalance, pausedBalance)

      // Now unpause the game and resume active gameplay
      com.example.util.GtScreenDetector.isGamePausedOverrideForTesting = false
      com.example.util.GtScreenDetector.isIdleScreenOverrideForTesting = false
      com.example.service.NDCycleService.updateIdleAndGtState(
        isIdle = false,
        isGtIcon = false,
        isGamePaused = false
      )

      org.junit.Assert.assertFalse("isGameplayBlocked must be false after unpausing", viewModel.engineState.value.isGameplayBlocked)

      // Progress and currency must work normally during active gameplay
      val testPack = com.example.data.TrackDictionary.INITIAL_NORMAL_PACKS.first()
      viewModel.markPackCompleted(testPack)
      assertEquals(1, viewModel.completedPacks.value.size)

      com.example.service.NDCycleService.incrementNdBalance(app)
      assertEquals(initialBalance + 1, com.example.service.NDCycleService.getNdBalance(app))
    } finally {
      com.example.util.PermissionHelper.storagePermissionOverrideForTesting = null
      com.example.util.PermissionHelper.emulatorProcessRunningOverrideForTesting = null
      com.example.util.GtScreenDetector.isGamePausedOverrideForTesting = null
      com.example.util.GtScreenDetector.isIdleScreenOverrideForTesting = null
      com.example.service.NDCycleService.updateIdleAndGtState(isIdle = false, isGtIcon = false, isGamePaused = false)
    }
  }

  @Test
  fun `emulator running in ppsspp main menu without active race strictly blocks progress and currency`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    com.example.util.PermissionHelper.storagePermissionOverrideForTesting = true
    com.example.util.PermissionHelper.emulatorProcessRunningOverrideForTesting = true
    com.example.util.GtScreenDetector.isMainMenuOverrideForTesting = true
    try {
      val initialBalance = com.example.service.NDCycleService.getNdBalance(app)

      val result = kotlinx.coroutines.runBlocking {
        com.example.util.GtScreenDetector.evaluateScreen(app)
      }
      org.junit.Assert.assertTrue("isMainMenu must be true", result.isMainMenu)
      org.junit.Assert.assertFalse("isRealGameplayConfirmed must be false in main menu", result.isRealGameplayConfirmed)

      com.example.service.NDCycleService.updateIdleAndGtState(
        isIdle = result.isIdleNoGameplay,
        isGtIcon = false,
        isGamePaused = false,
        isMainMenu = result.isMainMenu,
        isRealGameplayConfirmed = result.isRealGameplayConfirmed
      )

      val viewModel = com.example.ui.MainViewModel(app)
      org.junit.Assert.assertTrue("isGameplayBlocked must be true in main menu", viewModel.engineState.value.isGameplayBlocked)
      org.junit.Assert.assertFalse("isRealGameplayConfirmed must be false", viewModel.engineState.value.isRealGameplayConfirmed)

      // Even though emulator process is running, progress MUST stand still because user is merely in PPSSPP menu
      com.example.data.TrackDictionary.INITIAL_NORMAL_PACKS.forEach { pack ->
        viewModel.markPackCompleted(pack)
      }

      assertEquals(0, viewModel.completedPacks.value.size)
      org.junit.Assert.assertFalse(viewModel.unlockedThemes.value.contains("MINECRAFT_BLOCKS"))

      // Currency increment must also be blocked
      com.example.service.NDCycleService.incrementNdBalance(app)
      com.example.service.NDCycleService.addNdBalance(app, 50, "Menu farming attempt")
      assertEquals(initialBalance, com.example.service.NDCycleService.getNdBalance(app))
    } finally {
      com.example.util.PermissionHelper.storagePermissionOverrideForTesting = null
      com.example.util.PermissionHelper.emulatorProcessRunningOverrideForTesting = null
      com.example.util.GtScreenDetector.isMainMenuOverrideForTesting = null
      com.example.service.NDCycleService.updateIdleAndGtState(isIdle = false, isGtIcon = false, isGamePaused = false, isMainMenu = false, isRealGameplayConfirmed = true)
    }
  }
}
