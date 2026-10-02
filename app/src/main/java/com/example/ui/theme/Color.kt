package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppThemePalette(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val description: String,
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val background: Color,
    val surface: Color,
    val surfaceDark: Color,
    val surfaceVariant: Color,
    val surfaceContainer: Color,
    val cardBorder: Color,
    val cardBorderSubtle: Color,
    val fieldBg: Color,
    val codeBg: Color,
    val mintDark: Color,
    val amber: Color,
    val amberWarm: Color,
    val error: Color,
    val errorBg: Color,
    val errorBorder: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val accentSwatchList: List<Color>,
    val isLocked: Boolean = false
)

val CyberLilacPalette = AppThemePalette(
    id = "CYBER_LILAC",
    displayName = "Cyber Lilac",
    subtitle = "Лавандовый неон • Фирменный стиль",
    description = "Глубокий графитовый фон с неоново-сиреневыми и мятными элементами",
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFD0BCFF),
    secondary = Color(0xFFB6FFB4),
    onSecondary = Color(0xFF183B1A),
    background = Color(0xFF0F1012),
    surface = Color(0xFF1D1B20),
    surfaceDark = Color(0xFF161419),
    surfaceVariant = Color(0xFF252329),
    surfaceContainer = Color(0xFF2D2933),
    cardBorder = Color(0xFF49454F),
    cardBorderSubtle = Color(0xFF353439),
    fieldBg = Color(0xFF1C1B1F),
    codeBg = Color(0xFF111014),
    mintDark = Color(0xFF183B1A),
    amber = Color(0xFFFFD8E4),
    amberWarm = Color(0xFFFFB74D),
    error = Color(0xFFF2B8B5),
    errorBg = Color(0xFF410E0B),
    errorBorder = Color(0xFF8C1D18),
    textPrimary = Color(0xFFE6E1E5),
    textSecondary = Color(0xFFCAC4D0),
    textTertiary = Color(0xFF938F99),
    accentSwatchList = listOf(Color(0xFFD0BCFF), Color(0xFF4F378B), Color(0xFFB6FFB4), Color(0xFFFFB74D), Color(0xFF1D1B20))
)

val RacingAmberPalette = AppThemePalette(
    id = "RACING_AMBER",
    displayName = "Racing Amber",
    subtitle = "Гоночный янтарь • Тёплый динамичный",
    description = "Контрастный тёмно-шоколадный фон с огненно-янтарными и золотыми акцентами",
    primary = Color(0xFFFFB74D),
    onPrimary = Color(0xFF451B00),
    primaryContainer = Color(0xFF6D3800),
    onPrimaryContainer = Color(0xFFFFE0B2),
    secondary = Color(0xFF80DEEA),
    onSecondary = Color(0xFF00363A),
    background = Color(0xFF120E0A),
    surface = Color(0xFF1E1813),
    surfaceDark = Color(0xFF17120E),
    surfaceVariant = Color(0xFF2C221B),
    surfaceContainer = Color(0xFF362B22),
    cardBorder = Color(0xFF5A4335),
    cardBorderSubtle = Color(0xFF3E2F26),
    fieldBg = Color(0xFF1B1511),
    codeBg = Color(0xFF110D0A),
    mintDark = Color(0xFF00363A),
    amber = Color(0xFFFFE0B2),
    amberWarm = Color(0xFFFF9800),
    error = Color(0xFFFF8A80),
    errorBg = Color(0xFF44100D),
    errorBorder = Color(0xFF8D1C17),
    textPrimary = Color(0xFFFBE9E7),
    textSecondary = Color(0xFFD7CCC8),
    textTertiary = Color(0xFFA1887F),
    accentSwatchList = listOf(Color(0xFFFFB74D), Color(0xFF6D3800), Color(0xFF80DEEA), Color(0xFFFF9800), Color(0xFF1E1813))
)

val EmeraldMatrixPalette = AppThemePalette(
    id = "EMERALD_MATRIX",
    displayName = "Emerald Matrix",
    subtitle = "Изумрудная матрица • Кибер-терминал",
    description = "Высокотехнологичный стиль терминала с ультра-яркими изумрудными акцентами",
    primary = Color(0xFF50FA7B),
    onPrimary = Color(0xFF003B13),
    primaryContainer = Color(0xFF005C23),
    onPrimaryContainer = Color(0xFFA8FFB2),
    secondary = Color(0xFF8BE9FD),
    onSecondary = Color(0xFF003741),
    background = Color(0xFF09120D),
    surface = Color(0xFF121E16),
    surfaceDark = Color(0xFF0E1812),
    surfaceVariant = Color(0xFF1A2A20),
    surfaceContainer = Color(0xFF22362A),
    cardBorder = Color(0xFF3B5644),
    cardBorderSubtle = Color(0xFF283B2E),
    fieldBg = Color(0xFF101B13),
    codeBg = Color(0xFF080F0A),
    mintDark = Color(0xFF003741),
    amber = Color(0xFFFFB86C),
    amberWarm = Color(0xFFFFD166),
    error = Color(0xFFFF6E6E),
    errorBg = Color(0xFF3D0C0C),
    errorBorder = Color(0xFF801A1A),
    textPrimary = Color(0xFFE8F5E9),
    textSecondary = Color(0xFFC8E6C9),
    textTertiary = Color(0xFF81C784),
    accentSwatchList = listOf(Color(0xFF50FA7B), Color(0xFF005C23), Color(0xFF8BE9FD), Color(0xFFFFB86C), Color(0xFF121E16))
)

val PureLightPalette = AppThemePalette(
    id = "PURE_LIGHT",
    displayName = "Pure Light",
    subtitle = "Светлый минимализм • Высокая чёткость",
    description = "Чистый светлый фон с контрастными глубоко-фиолетовыми и бирюзовыми акцентами",
    primary = Color(0xFF6750A4),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    secondary = Color(0xFF00897B),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFF6F7F9),
    surface = Color(0xFFFFFFFF),
    surfaceDark = Color(0xFFEDEDF2),
    surfaceVariant = Color(0xFFE7E7EC),
    surfaceContainer = Color(0xFFECECF0),
    cardBorder = Color(0xFFD6D6DF),
    cardBorderSubtle = Color(0xFFE4E4EB),
    fieldBg = Color(0xFFF0F1F5),
    codeBg = Color(0xFFE8E9EE),
    mintDark = Color(0xFF004D40),
    amber = Color(0xFFFF8F00),
    amberWarm = Color(0xFFE65100),
    error = Color(0xFFB3261E),
    errorBg = Color(0xFFF9DEDC),
    errorBorder = Color(0xFFB3261E),
    textPrimary = Color(0xFF1B1B1F),
    textSecondary = Color(0xFF48464C),
    textTertiary = Color(0xFF78757E),
    accentSwatchList = listOf(Color(0xFF6750A4), Color(0xFFEADDFF), Color(0xFF00897B), Color(0xFFFF8F00), Color(0xFFFFFFFF))
)

val CarbonGTRedPalette = AppThemePalette(
    id = "CARBON_GT_RED",
    displayName = "Carbon GT Red",
    subtitle = "Матовый чёрный • GT Red & Cyber Blue",
    description = "Глубокий матовый чёрный фон с насыщенным красным GT Red, серебристым металлом и технологичным синим",
    primary = Color(0xFFFF334B),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF5A000F),
    onPrimaryContainer = Color(0xFFFFD9DD),
    secondary = Color(0xFF38B6FF),
    onSecondary = Color(0xFF00223A),
    background = Color(0xFF0B0B0C),
    surface = Color(0xFF151517),
    surfaceDark = Color(0xFF101012),
    surfaceVariant = Color(0xFF202024),
    surfaceContainer = Color(0xFF28282D),
    cardBorder = Color(0xFF424248),
    cardBorderSubtle = Color(0xFF2B2B30),
    fieldBg = Color(0xFF131315),
    codeBg = Color(0xFF0A0A0B),
    mintDark = Color(0xFF00223A),
    amber = Color(0xFFC0C7D5),
    amberWarm = Color(0xFFFF5252),
    error = Color(0xFFFF5252),
    errorBg = Color(0xFF450007),
    errorBorder = Color(0xFF8F0013),
    textPrimary = Color(0xFFF2F2F5),
    textSecondary = Color(0xFFB4B4BC),
    textTertiary = Color(0xFF7E7E88),
    accentSwatchList = listOf(Color(0xFFFF334B), Color(0xFF38B6FF), Color(0xFFB4B4BC), Color(0xFF5A000F), Color(0xFF151517))
)

val StoneTimberPalette = AppThemePalette(
    id = "STONE_TIMBER",
    displayName = "Stone & Timber",
    subtitle = "Серый камень • Древесина, Синий & Оранж",
    description = "Архитектурная схема: светло-серые стены, глубокие тени, кобальтовый синий, натуральное дерево и оранжевая подсветка",
    primary = Color(0xFF2979FF),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF0D3E8C),
    onPrimaryContainer = Color(0xFFD0E1FD),
    secondary = Color(0xFFFF9100),
    onSecondary = Color(0xFF3E1E00),
    background = Color(0xFF121418),
    surface = Color(0xFF1E2128),
    surfaceDark = Color(0xFF15171D),
    surfaceVariant = Color(0xFF2C303B),
    surfaceContainer = Color(0xFF383D4A),
    cardBorder = Color(0xFF5A6273),
    cardBorderSubtle = Color(0xFF3E4452),
    fieldBg = Color(0xFF181A20),
    codeBg = Color(0xFF0F1014),
    mintDark = Color(0xFF003822),
    amber = Color(0xFF9C6644),
    amberWarm = Color(0xFFFF9100),
    error = Color(0xFFFF5252),
    errorBg = Color(0xFF420C10),
    errorBorder = Color(0xFF8F0013),
    textPrimary = Color(0xFFF0F2F5),
    textSecondary = Color(0xFFB0B7C3),
    textTertiary = Color(0xFF788194),
    accentSwatchList = listOf(Color(0xFF8E95A5), Color(0xFF1B1D22), Color(0xFF2979FF), Color(0xFF9C6644), Color(0xFFFF9100)),
    isLocked = true
)

val UrbanPinkPalette = AppThemePalette(
    id = "URBAN_PINK",
    displayName = "Urban Pink",
    subtitle = "Яркий розовый • Тёмный бетон, Серый & Пыльная роза",
    description = "Урбанистическая неоновая палитра: глубокий тёмный бетон, контрастный розовый акцент, пыльно-розовые элементы и мягкие серые тона",
    primary = Color(0xFFFF2A85),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF5A0B2C),
    onPrimaryContainer = Color(0xFFFFD8E7),
    secondary = Color(0xFFD48BA3),
    onSecondary = Color(0xFF3B1323),
    background = Color(0xFF111215),
    surface = Color(0xFF1A1C22),
    surfaceDark = Color(0xFF131418),
    surfaceVariant = Color(0xFF252832),
    surfaceContainer = Color(0xFF303440),
    cardBorder = Color(0xFF555966),
    cardBorderSubtle = Color(0xFF393C46),
    fieldBg = Color(0xFF17191F),
    codeBg = Color(0xFF0E0F12),
    mintDark = Color(0xFF28131E),
    amber = Color(0xFFB87D92),
    amberWarm = Color(0xFFFF5B9A),
    error = Color(0xFFFF5252),
    errorBg = Color(0xFF450D15),
    errorBorder = Color(0xFF8F0018),
    textPrimary = Color(0xFFF6F6F9),
    textSecondary = Color(0xFFB4B9C4),
    textTertiary = Color(0xFF7C8290),
    accentSwatchList = listOf(Color(0xFFFF2A85), Color(0xFFD48BA3), Color(0xFF888D9A), Color(0xFF252832), Color(0xFFF6F6F9)),
    isLocked = true
)

val CrimsonSparkPalette = AppThemePalette(
    id = "CRIMSON_SPARK",
    displayName = "Crimson Spark",
    subtitle = "Сочный красный • Чёрные блоки, Серый & Жёлтая искра",
    description = "Энергичная контрастная палитра: сочный базовый красный фон, глубокие чёрные блоки интерфейса, тёмно-серые границы и яркая жёлтая искра",
    primary = Color(0xFFFFD600),
    onPrimary = Color(0xFF0D0D10),
    primaryContainer = Color(0xFF6B0014),
    onPrimaryContainer = Color(0xFFFFEA79),
    secondary = Color(0xFFFF2A42),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFF38030B),
    surface = Color(0xFF0D0D10),
    surfaceDark = Color(0xFF070709),
    surfaceVariant = Color(0xFF1B1D22),
    surfaceContainer = Color(0xFF262830),
    cardBorder = Color(0xFF424652),
    cardBorderSubtle = Color(0xFF2A2C34),
    fieldBg = Color(0xFF101115),
    codeBg = Color(0xFF08080A),
    mintDark = Color(0xFF003822),
    amber = Color(0xFFFFD600),
    amberWarm = Color(0xFFFFAB00),
    error = Color(0xFFFF4D4D),
    errorBg = Color(0xFF4D000A),
    errorBorder = Color(0xFF990014),
    textPrimary = Color(0xFFFBFBFC),
    textSecondary = Color(0xFFB0B4C0),
    textTertiary = Color(0xFF7A7E8D),
    accentSwatchList = listOf(Color(0xFFD30E2A), Color(0xFF0D0D10), Color(0xFF262830), Color(0xFF8E95A5), Color(0xFFFFD600)),
    isLocked = true
)

val MinecraftBlocksPalette = AppThemePalette(
    id = "MINECRAFT_BLOCKS",
    displayName = "Dixel Mine",
    subtitle = "Призмарин, Адский нарост, Золото, Медь & Аметист",
    description = "Культовая пиксельная палитра: бирюзовый призмарин, глубокий бордовый нарост, сияющий золотой блок, тёплая медь и кристаллический аметист",
    primary = Color(0xFFFCD836),
    onPrimary = Color(0xFF1B1B1F),
    primaryContainer = Color(0xFF4C0A10),
    onPrimaryContainer = Color(0xFFFFE082),
    secondary = Color(0xFF5AB6A8),
    onSecondary = Color(0xFF0F2623),
    background = Color(0xFF0D1716),
    surface = Color(0xFF1C1115),
    surfaceDark = Color(0xFF110A0D),
    surfaceVariant = Color(0xFF2E191D),
    surfaceContainer = Color(0xFF26182B),
    cardBorder = Color(0xFF5A3E38),
    cardBorderSubtle = Color(0xFF352528),
    fieldBg = Color(0xFF140D10),
    codeBg = Color(0xFF0A0709),
    mintDark = Color(0xFF133630),
    amber = Color(0xFFD47C59),
    amberWarm = Color(0xFFFCD836),
    error = Color(0xFFFF4558),
    errorBg = Color(0xFF4A0A10),
    errorBorder = Color(0xFF8F0F1D),
    textPrimary = Color(0xFFF0FAF8),
    textSecondary = Color(0xFFB5C8C4),
    textTertiary = Color(0xFF7A8F8B),
    accentSwatchList = listOf(Color(0xFF5AB6A8), Color(0xFF6B1118), Color(0xFFFCD836), Color(0xFFD47C59), Color(0xFF9A5CC0)),
    isLocked = true
)

val WodenerWorcPalette = AppThemePalette(
    id = "WODENER_WORC",
    displayName = "Wodener Worc",
    subtitle = "Древесина, Доски, Гранит, Кирпич & Призмарин",
    description = "Аутентичная природная палитра: тёмно-коричневая древесина, соломенные доски, полированный розовый гранит, глиняный кирпич и призмариновые решетчатые кирпичи",
    primary = Color(0xFF1B6A66), // Призмариновые кирпичи (глубокий бирюзово-зеленый / темный циан)
    onPrimary = Color(0xFFE8FAF7),
    primaryContainer = Color(0xFF0E3836),
    onPrimaryContainer = Color(0xFF7FE6DE),
    secondary = Color(0xFFD69C54), // Доски (соломенный / светло-желтый)
    onSecondary = Color(0xFF2B1804),
    background = Color(0xFF140E0B), // Глубокая темная древесно-землистая основа
    surface = Color(0xFF211712), // Теплая древесная поверхность
    surfaceDark = Color(0xFF170F0C),
    surfaceVariant = Color(0xFF31221B),
    surfaceContainer = Color(0xFF3D2B22),
    cardBorder = Color(0xFF63412C), // Глиняно-древесный контур
    cardBorderSubtle = Color(0xFF3A261A),
    fieldBg = Color(0xFF19110D),
    codeBg = Color(0xFF100A08),
    mintDark = Color(0xFF0B2B29),
    amber = Color(0xFFD69C54), // Соломенный оттенок
    amberWarm = Color(0xFFBE8A7B), // Розовато-бежевый гранит
    error = Color(0xFFFF5449),
    errorBg = Color(0xFF410002),
    errorBorder = Color(0xFF93000A),
    textPrimary = Color(0xFFF8F0E5), // Теплый пергаментный светлый
    textSecondary = Color(0xFFCFBFAB),
    textTertiary = Color(0xFF968774),
    accentSwatchList = listOf(
        Color(0xFF382012), // Древесина (Wood)
        Color(0xFFD69C54), // Доски (Planks)
        Color(0xFFBE8A7B), // Полированный гранит (Polished Granite)
        Color(0xFF984931), // Кирпич (Bricks)
        Color(0xFF1B6A66)  // Призмариновые кирпичи (Prismarine Bricks)
    ),
    isLocked = true
)

val LasuriteCrystalPalette = AppThemePalette(
    id = "LASURITE_CRYSTAL",
    displayName = "Lasurite Crystal",
    subtitle = "Асфальт, Кристалл, Уголь, Металл & Ультрамарин",
    description = "Кристально-минеральная палитра: матовый асфальт, сверкающий бирюзовый кристалл, глубокий уголь, серебристый металл и чистый ультрамарин",
    primary = Color(0xFF00D2D3), // Кристалл (яркий насыщенный бирюзово-голубой / морская волна)
    onPrimary = Color(0xFF041B1C),
    primaryContainer = Color(0xFF063A3B),
    onPrimaryContainer = Color(0xFF67F6F7),
    secondary = Color(0xFF1B44E8), // Ультрамарин (насыщенный ярко-синий цвет)
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFF121417), // Уголь (темно-угольный, почти черный)
    surface = Color(0xFF1E2227), // Асфальт (глухой темно-серый с ровной матовой поверхностью)
    surfaceDark = Color(0xFF14171A),
    surfaceVariant = Color(0xFF282D33),
    surfaceContainer = Color(0xFF333942),
    cardBorder = Color(0xFF3D4652),
    cardBorderSubtle = Color(0xFF242930),
    fieldBg = Color(0xFF16191D),
    codeBg = Color(0xFF0F1113),
    mintDark = Color(0xFF072425),
    amber = Color(0xFFCBD5E1), // Металл (бледно-серый / светло-серебристый)
    amberWarm = Color(0xFF94A3B8),
    error = Color(0xFFFF5449),
    errorBg = Color(0xFF410002),
    errorBorder = Color(0xFF93000A),
    textPrimary = Color(0xFFF1F5F9), // Серебристый чистый текст
    textSecondary = Color(0xFF94A3B8),
    textTertiary = Color(0xFF64748B),
    accentSwatchList = listOf(
        Color(0xFF33383E), // Асфальт (Asphalt)
        Color(0xFF00D2D3), // Кристалл (Crystal)
        Color(0xFF121417), // Уголь (Charcoal)
        Color(0xFFCBD5E1), // Металл (Metal)
        Color(0xFF1B44E8)  // Ультрамарин (Ultramarine)
    ),
    isLocked = true
)

val InfernoStreetPalette = AppThemePalette(
    id = "INFERNO_STREET",
    displayName = "Inferno Street",
    subtitle = "Терракота, Лаванда, Джунгли, Мшистый & Резной камень",
    description = "Уличная фактурная палитра: терракотовый кирпич со светлыми швами, пастельная лаванда, песочная древесина джунглей, мшистый серо-зеленый и резной угольный камень",
    primary = Color(0xFFC84E38), // Терракотовый кирпич (терракотово-красный / кирпичный)
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF48120B),
    onPrimaryContainer = Color(0xFFFFB4A5),
    secondary = Color(0xFFC49AC4), // Лаванда (нежный пастельный розовато-лиловый оттенок)
    onSecondary = Color(0xFF2A152A),
    background = Color(0xFF141517), // Резной камень (темный серо-угольный)
    surface = Color(0xFF1F2226), // Матовая поверхность резного камня
    surfaceDark = Color(0xFF16181B),
    surfaceVariant = Color(0xFF2B2E33),
    surfaceContainer = Color(0xFF353940),
    cardBorder = Color(0xFF5A443B), // Терракотово-каменный контур
    cardBorderSubtle = Color(0xFF332924),
    fieldBg = Color(0xFF191B1E),
    codeBg = Color(0xFF101214),
    mintDark = Color(0xFF1A2619),
    amber = Color(0xFFA87F4A), // Древесина джунглей (желтовато-коричневый / песочно-древесный)
    amberWarm = Color(0xFF5E7955), // Мшистый камень (серо-зеленый)
    error = Color(0xFFFF5449),
    errorBg = Color(0xFF410002),
    errorBorder = Color(0xFF93000A),
    textPrimary = Color(0xFFF6EFEA), // Теплый светлый каменный
    textSecondary = Color(0xFFC5B8B0),
    textTertiary = Color(0xFF8C7F77),
    accentSwatchList = listOf(
        Color(0xFFC84E38), // Терракотовый кирпич (Terracotta Bricks)
        Color(0xFFC49AC4), // Лаванда (Lavender)
        Color(0xFFA87F4A), // Древесина джунглей (Jungle Wood)
        Color(0xFF5E7955), // Мшистый камень (Mossy Stone)
        Color(0xFF2E3136)  // Резной камень (Carved Stone)
    ),
    isLocked = true
)

val CrypticFrostPalette = AppThemePalette(
    id = "CRYPTIC_FROST",
    displayName = "Cryptic Frost",
    subtitle = "Лёд, Мухомор, Камень Края, Стекло & Оранжевая керамика",
    description = "Ледяная морозная палитра: светло-синий лед с морозными разводами, насыщенный мухоморный красный с белыми пятнышками, бежевый камень Края, полупрозрачное стекло и оранжевая керамика",
    primary = Color(0xFF6AD6F5), // Лёд (светло-синий ледяной голубой)
    onPrimary = Color(0xFF04202B),
    primaryContainer = Color(0xFF0F3B4F),
    onPrimaryContainer = Color(0xFFBCEBFC),
    secondary = Color(0xFFFF6D00), // Оранжевая керамика (яркий однородный оранжевый цвет)
    onSecondary = Color(0xFF2C1000),
    background = Color(0xFF0C1217), // Глубокая темная морозная основа
    surface = Color(0xFF141E26), // Матовая поверхность с морозным оттенком
    surfaceDark = Color(0xFF0D151C),
    surfaceVariant = Color(0xFF1B2934),
    surfaceContainer = Color(0xFF223644),
    cardBorder = Color(0xFF4C7B9E), // Ледяной морозный контур
    cardBorderSubtle = Color(0xFF29465B),
    fieldBg = Color(0xFF111A22),
    codeBg = Color(0xFF090E13),
    mintDark = Color(0xFF092933),
    amber = Color(0xFFDFDC9B), // Камень Края (бледно-желтоватый / бежево-кремовый)
    amberWarm = Color(0xFFFF7A1A), // Оранжевая керамика
    error = Color(0xFFE52E2E), // Мухомор (насыщенный красный с белыми крапинами)
    errorBg = Color(0xFF420B0E),
    errorBorder = Color(0xFF8F1418),
    textPrimary = Color(0xFFF0F7FB), // Ледяной чистый текст
    textSecondary = Color(0xFFB2CAD8),
    textTertiary = Color(0xFF708C9D),
    accentSwatchList = listOf(
        Color(0xFF6AD6F5), // Лёд (Ice)
        Color(0xFFE52E2E), // Мухомор (Mushroom)
        Color(0xFFDFDC9B), // Камень Края (End Stone)
        Color(0xFFE6F2F8), // Стекло / Матовый блок (Glass & Matte)
        Color(0xFFFF6D00)  // Оранжевая керамика (Orange Ceramic)
    ),
    isLocked = true
)

val GhoulKenKanekiPalette = AppThemePalette(
    id = "GHOUL_KEN_KANEKI",
    displayName = "Ghoul Ken Kaneki",
    subtitle = "Белый, Чёрный, Серый, Красный Какуган & Золотой",
    description = "Трагический путь полугуля: белоснежное перерождение, чернильная тьма альтер-эго, серый монохром Сасаки Хайсе, алый пульс какугу и золотое сияние обретенного счастья",
    primary = Color(0xFFFF1744), // Красный (Какуган / цвет хищной природы)
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF4A000E),
    onPrimaryContainer = Color(0xFFFFB3B8),
    secondary = Color(0xFFFFD700), // Золотой (символ надежды и счастья)
    onSecondary = Color(0xFF261D00),
    background = Color(0xFF09090C), // Глубокая чернильная основа
    surface = Color(0xFF131318), // Тёмная поверхность альтер-эго
    surfaceDark = Color(0xFF0B0B0E),
    surfaceVariant = Color(0xFF1D1E24),
    surfaceContainer = Color(0xFF262730),
    cardBorder = Color(0xFF70121C), // Алый какуган контур
    cardBorderSubtle = Color(0xFF33161C),
    fieldBg = Color(0xFF0E0E12),
    codeBg = Color(0xFF070709),
    mintDark = Color(0xFF00291D),
    amber = Color(0xFFFFD700), // Золотой
    amberWarm = Color(0xFFFFB300),
    error = Color(0xFFFF1744), // Красный какуган
    errorBg = Color(0xFF3B0008),
    errorBorder = Color(0xFF8B0000),
    textPrimary = Color(0xFFF7F8FA), // Белый чистый текст перерождения
    textSecondary = Color(0xFFA0A6B4), // Серый Сасаки Хайсе
    textTertiary = Color(0xFF656C7A),
    accentSwatchList = listOf(
        Color(0xFFFFFFFF), // Белый — перерождение
        Color(0xFF0F0F14), // Черный — альтер-эго
        Color(0xFF6B7280), // Серый — Сасаки Хайсе
        Color(0xFFFF1744), // Красный — какугу / ярость
        Color(0xFFFFD700)  // Золотой — надежда и счастье
    ),
    isLocked = true
)

val NeonCyberpunkPalette = AppThemePalette(
    id = "NEON_CYBERPUNK",
    displayName = "Neon Cyberpunk",
    subtitle = "Салатовый, фуксия, электрик, ультрафиолет & кислотно-желтый",
    description = "Энергия ночного мегаполиса: кислотный неон, контрастная фуксия и электрическое свечение в ультрафиолете",
    primary = Color(0xFF39FF14), // Салатовый
    onPrimary = Color(0xFF002204),
    primaryContainer = Color(0xFF133B0E),
    onPrimaryContainer = Color(0xFF8CFF78),
    secondary = Color(0xFFFF007F), // Фуксия
    onSecondary = Color(0xFF3B0019),
    background = Color(0xFF0C0717), // Ультрафиолетовая темная основа
    surface = Color(0xFF150C26), // Ультрафиолетовая поверхность
    surfaceDark = Color(0xFF0E071A),
    surfaceVariant = Color(0xFF22153D),
    surfaceContainer = Color(0xFF2E1C52),
    cardBorder = Color(0xFF00E5FF), // Электрик контур
    cardBorderSubtle = Color(0xFF3D1D6D),
    fieldBg = Color(0xFF190F2E),
    codeBg = Color(0xFF0E071A),
    mintDark = Color(0xFF0B2412),
    amber = Color(0xFFCCFF00), // Кислотно-желтый
    amberWarm = Color(0xFFFAFF00),
    error = Color(0xFFFF007F), // Фуксия
    errorBg = Color(0xFF3A001C),
    errorBorder = Color(0xFF8F0045),
    textPrimary = Color(0xFFF7F8FA),
    textSecondary = Color(0xFFB8A9D9),
    textTertiary = Color(0xFF7D6C99),
    accentSwatchList = listOf(
        Color(0xFF39FF14), // Салатовый
        Color(0xFFFF007F), // Фуксия
        Color(0xFF00E5FF), // Электрик
        Color(0xFF7B1FA2), // Ультрафиолет
        Color(0xFFCCFF00)  // Кислотно-желтый
    ),
    isLocked = true
)

val CyberSunsetPalette = AppThemePalette(
    id = "CYBER_SUNSET",
    displayName = "Cyber Sunset",
    subtitle = "Неоново-розовый, бирюзовый, токсично-зеленый, фиолетовый & ярко-оранжевый",
    description = "Кибернетический закат: неоновое розово-оранжевое зарево над бирюзовым горизонтом и фиолетовой дымкой",
    primary = Color(0xFFFF2A85), // Неоново-розовый
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF5E002C),
    onPrimaryContainer = Color(0xFFFFB0D2),
    secondary = Color(0xFF00F5D4), // Бирюзовый
    onSecondary = Color(0xFF003830),
    background = Color(0xFF10091D), // Фиолетовый фон
    surface = Color(0xFF1A0F2E), // Фиолетовая поверхность
    surfaceDark = Color(0xFF120A21),
    surfaceVariant = Color(0xFF281845),
    surfaceContainer = Color(0xFF35205C),
    cardBorder = Color(0xFFFF7700), // Ярко-оранжевый контур
    cardBorderSubtle = Color(0xFF4A1E5C),
    fieldBg = Color(0xFF1E1136),
    codeBg = Color(0xFF110821),
    mintDark = Color(0xFF003830),
    amber = Color(0xFFFF7700), // Ярко-оранжевый
    amberWarm = Color(0xFFFF9433),
    error = Color(0xFFFF2A85),
    errorBg = Color(0xFF380017),
    errorBorder = Color(0xFF8A003D),
    textPrimary = Color(0xFFF9F7FD),
    textSecondary = Color(0xFFBAA7D9),
    textTertiary = Color(0xFF7E6F94),
    accentSwatchList = listOf(
        Color(0xFFFF2A85), // Неоново-розовый
        Color(0xFF00F5D4), // Бирюзовый
        Color(0xFF00FF66), // Токсично-зеленый
        Color(0xFF7928CA), // Фиолетовый
        Color(0xFFFF7700)  // Ярко-оранжевый
    ),
    isLocked = true
)

val DigitalDawnPalette = AppThemePalette(
    id = "DIGITAL_DAWN",
    displayName = "Digital Dawn",
    subtitle = "Циан, маджента, лимонный, лавандовый & темно-синий",
    description = "Цифровой рассвет нового поколения: сияющий циан и маджента на глубоком темно-синем полотне с лавандовыми акцентами",
    primary = Color(0xFF00F0FF), // Циан
    onPrimary = Color(0xFF003438),
    primaryContainer = Color(0xFF004D54),
    onPrimaryContainer = Color(0xFF80F7FF),
    secondary = Color(0xFFFF00A0), // Маджента
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFF090E1A), // Темно-синий
    surface = Color(0xFF10192E),
    surfaceDark = Color(0xFF0B1121),
    surfaceVariant = Color(0xFF192545),
    surfaceContainer = Color(0xFF22325C),
    cardBorder = Color(0xFFB388FF), // Лавандовый
    cardBorderSubtle = Color(0xFF1F2F59),
    fieldBg = Color(0xFF131D36),
    codeBg = Color(0xFF0A0F1F),
    mintDark = Color(0xFF002A30),
    amber = Color(0xFFFFF01F), // Лимонный
    amberWarm = Color(0xFFFFE600),
    error = Color(0xFFFF00A0),
    errorBg = Color(0xFF380023),
    errorBorder = Color(0xFF7A004D),
    textPrimary = Color(0xFFF0F5FF),
    textSecondary = Color(0xFFA1B4D6),
    textTertiary = Color(0xFF637396),
    accentSwatchList = listOf(
        Color(0xFF00F0FF), // Циан
        Color(0xFFFF00A0), // Маджента
        Color(0xFFFFF01F), // Лимонный
        Color(0xFFB388FF), // Лавандовый
        Color(0xFF0D1B2A)  // Темно-синий
    ),
    isLocked = true
)

val NeonPulsePalette = AppThemePalette(
    id = "NEON_PULSE",
    displayName = "Neon Pulse",
    subtitle = "Кибер-красный, неоновый голубой, ярко-зеленый, пурпурный & солнечно-желтый",
    description = "Высокочастотный пульс неона: агрессивный кибер-красный, неоновый голубой и насыщенный пурпур",
    primary = Color(0xFFFF003C), // Кибер-красный
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF540012),
    onPrimaryContainer = Color(0xFFFF859B),
    secondary = Color(0xFF00E5FF), // Неоновый голубой
    onSecondary = Color(0xFF00353B),
    background = Color(0xFF0E0B12), // Тёмно-пурпурная база
    surface = Color(0xFF191322),
    surfaceDark = Color(0xFF120D1A),
    surfaceVariant = Color(0xFF261C33),
    surfaceContainer = Color(0xFF332545),
    cardBorder = Color(0xFFBF00FF), // Пурпурный контур
    cardBorderSubtle = Color(0xFF421E5C),
    fieldBg = Color(0xFF1C1526),
    codeBg = Color(0xFF100C16),
    mintDark = Color(0xFF002E13),
    amber = Color(0xFFFFD700), // Солнечно-желтый
    amberWarm = Color(0xFFFFEA00),
    error = Color(0xFFFF003C),
    errorBg = Color(0xFF38000C),
    errorBorder = Color(0xFF8F0022),
    textPrimary = Color(0xFFF9F7FA),
    textSecondary = Color(0xFFBCAFD0),
    textTertiary = Color(0xFF786B8C),
    accentSwatchList = listOf(
        Color(0xFFFF003C), // Кибер-красный
        Color(0xFF00E5FF), // Неоновый голубой
        Color(0xFF00FF66), // Ярко-зеленый
        Color(0xFFBF00FF), // Пурпурный
        Color(0xFFFFD700)  // Солнечно-желтый
    ),
    isLocked = true
)

val CyberMintPalette = AppThemePalette(
    id = "CYBER_MINT",
    displayName = "Cyber Mint",
    subtitle = "Мятный, коралловый, ультрамарин, неоновый лайм & фиолетово-розовый",
    description = "Свежая кибернетическая волна: искрящийся мятный, теплый коралл и глубокий ультрамарин",
    primary = Color(0xFF00F5D4), // Мятный
    onPrimary = Color(0xFF003830),
    primaryContainer = Color(0xFF005247),
    onPrimaryContainer = Color(0xFF80FAEA),
    secondary = Color(0xFFFF6F61), // Коралловый
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFF070B16), // Ультрамариновая темная основа
    surface = Color(0xFF0F172C),
    surfaceDark = Color(0xFF0A1020),
    surfaceVariant = Color(0xFF172342),
    surfaceContainer = Color(0xFF1F2F59),
    cardBorder = Color(0xFF1E30F3), // Ультрамарин
    cardBorderSubtle = Color(0xFF1B2C63),
    fieldBg = Color(0xFF121B33),
    codeBg = Color(0xFF080D1C),
    mintDark = Color(0xFF00362D),
    amber = Color(0xFFCCFF00), // Неоновый лайм
    amberWarm = Color(0xFFE040FB), // Фиолетово-розовый
    error = Color(0xFFFF6F61),
    errorBg = Color(0xFF38120F),
    errorBorder = Color(0xFF8A2F27),
    textPrimary = Color(0xFFF0FDFB),
    textSecondary = Color(0xFFA1B7D1),
    textTertiary = Color(0xFF657894),
    accentSwatchList = listOf(
        Color(0xFF00F5D4), // Мятный
        Color(0xFFFF6F61), // Коралловый
        Color(0xFF1E30F3), // Ультрамарин
        Color(0xFFCCFF00), // Неоновый лайм
        Color(0xFFE040FB)  // Фиолетово-розовый
    ),
    isLocked = true
)

val ElectricChargePalette = AppThemePalette(
    id = "ELECTRIC_CHARGE",
    displayName = "Electric Charge",
    subtitle = "Электрический синий, кислотный апельсин, розовый неон, индиго & салатовый",
    description = "Высоковольтный заряд энергии: электрический синий и кислотный апельсин на ультраглубоком индиго с едкими салатовыми акцентами",
    primary = Color(0xFF00E5FF), // Электрический синий
    onPrimary = Color(0xFF00363D),
    primaryContainer = Color(0xFF004E57),
    onPrimaryContainer = Color(0xFF80F2FF),
    secondary = Color(0xFFFF6D00), // Кислотный апельсин
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFF080720), // Глубокий индиго
    surface = Color(0xFF110F33),
    surfaceDark = Color(0xFF0A0924),
    surfaceVariant = Color(0xFF1C194D),
    surfaceContainer = Color(0xFF262263),
    cardBorder = Color(0xFF76FF03), // Едкий салатовый
    cardBorderSubtle = Color(0xFF262057),
    fieldBg = Color(0xFF141238),
    codeBg = Color(0xFF090822),
    mintDark = Color(0xFF0B2B1E),
    amber = Color(0xFFFF6D00),
    amberWarm = Color(0xFFFF9100),
    error = Color(0xFFFF1774), // Розовый неон
    errorBg = Color(0xFF3B0017),
    errorBorder = Color(0xFF8C0037),
    textPrimary = Color(0xFFF5F5FF),
    textSecondary = Color(0xFFADB0DC),
    textTertiary = Color(0xFF6B6E9C),
    accentSwatchList = listOf(
        Color(0xFF00E5FF), // Электрический синий
        Color(0xFFFF6D00), // Кислотный апельсин
        Color(0xFFFF1774), // Розовый неон
        Color(0xFF1A164F), // Глубокий индиго
        Color(0xFF76FF03)  // Едкий салатовый
    ),
    isLocked = true
)

val NeonSpectrumPalette = AppThemePalette(
    id = "NEON_SPECTRUM",
    displayName = "Neon Spectrum",
    subtitle = "Лазурный, маджентовый неон, неоновый лимон, ультрафиолет & ярко-мятный",
    description = "Полноспектральное неоновое свечение: лазурь и маджента на ультрафиолетовом полотне с лимонно-мятными искрами",
    primary = Color(0xFF00BFFF), // Лазурный
    onPrimary = Color(0xFF003347),
    primaryContainer = Color(0xFF004E6B),
    onPrimaryContainer = Color(0xFF8AE0FF),
    secondary = Color(0xFFFF007F), // Маджентовый неон
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFF0E0520), // Ультрафиолетовый фон
    surface = Color(0xFF190C36),
    surfaceDark = Color(0xFF110727),
    surfaceVariant = Color(0xFF271552),
    surfaceContainer = Color(0xFF361E6E),
    cardBorder = Color(0xFF00FFB2), // Ярко-мятный контур
    cardBorderSubtle = Color(0xFF45196E),
    fieldBg = Color(0xFF1D0E3D),
    codeBg = Color(0xFF0E0520),
    mintDark = Color(0xFF003827),
    amber = Color(0xFFF4FF52), // Неоновый лимон
    amberWarm = Color(0xFFFFEE55),
    error = Color(0xFFFF007F),
    errorBg = Color(0xFF3B001D),
    errorBorder = Color(0xFF8A0044),
    textPrimary = Color(0xFFFAF7FF),
    textSecondary = Color(0xFFBCABDB),
    textTertiary = Color(0xFF766599),
    accentSwatchList = listOf(
        Color(0xFF00BFFF), // Лазурный
        Color(0xFFFF007F), // Маджентовый неон
        Color(0xFFF4FF52), // Неоновый лимон
        Color(0xFF651FFF), // Ультрафиолетовый
        Color(0xFF00FFB2)  // Ярко-мятный
    ),
    isLocked = true
)

val ToxicBurstPalette = AppThemePalette(
    id = "TOXIC_BURST",
    displayName = "Toxic Burst",
    subtitle = "Ядовито-зеленый, горячий розовый, неоново-голубой, лиловый & оранжевый ультра",
    description = "Взрыв токсичного неона: ядовито-зеленый и неоново-голубой на лиловом контрасте с ультра-оранжевыми всплесками",
    primary = Color(0xFF00FF40), // Ядовито-зеленый
    onPrimary = Color(0xFF00380B),
    primaryContainer = Color(0xFF005212),
    onPrimaryContainer = Color(0xFF7DFF9F),
    secondary = Color(0xFFFF1493), // Горячий розовый
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFF0C0514), // Лиловый глубокий фон
    surface = Color(0xFF180A26),
    surfaceDark = Color(0xFF10061B),
    surfaceVariant = Color(0xFF27123D),
    surfaceContainer = Color(0xFF371B54),
    cardBorder = Color(0xFF00F0FF), // Неоново-голубой
    cardBorderSubtle = Color(0xFF45195E),
    fieldBg = Color(0xFF1D0C30),
    codeBg = Color(0xFF0E0517),
    mintDark = Color(0xFF003816),
    amber = Color(0xFFFF5500), // Оранжевый ультра
    amberWarm = Color(0xFFFF7724),
    error = Color(0xFFFF1493),
    errorBg = Color(0xFF38001F),
    errorBorder = Color(0xFF8A004C),
    textPrimary = Color(0xFFF9F7FC),
    textSecondary = Color(0xFFC0ADD6),
    textTertiary = Color(0xFF7E6999),
    accentSwatchList = listOf(
        Color(0xFF00FF40), // Ядовито-зеленый
        Color(0xFFFF1493), // Горячий розовый
        Color(0xFF00F0FF), // Неоново-голубой
        Color(0xFFBA55D3), // Лиловый
        Color(0xFFFF5500)  // Оранжевый ультра
    ),
    isLocked = true
)

val CyberPeachPalette = AppThemePalette(
    id = "CYBER_PEACH",
    displayName = "Cyber Peach",
    subtitle = "Бирюзово-неоновый, кислотный персик, фиолетовый неон, мятный лайм & ярко-красный",
    description = "Сочный кибернетический персик: тёплые неоновые тона в окружении бирюзы, фиолета и свежего мятного лайма",
    primary = Color(0xFFFF7E67), // Кислотный персик
    onPrimary = Color(0xFF420B00),
    primaryContainer = Color(0xFF6B1D0E),
    onPrimaryContainer = Color(0xFFFFC0B5),
    secondary = Color(0xFF00F5D4), // Бирюзово-неоновый
    onSecondary = Color(0xFF003830),
    background = Color(0xFF10071C), // Фиолетовый неон фон
    surface = Color(0xFF1C0D30),
    surfaceDark = Color(0xFF130821),
    surfaceVariant = Color(0xFF2C164B),
    surfaceContainer = Color(0xFF3B1F63),
    cardBorder = Color(0xFF9D4EDD), // Фиолетовый неон контур
    cardBorderSubtle = Color(0xFF4A1A6B),
    fieldBg = Color(0xFF200F36),
    codeBg = Color(0xFF11071F),
    mintDark = Color(0xFF003830),
    amber = Color(0xFF76FF03), // Мятный лайм
    amberWarm = Color(0xFFA3FF47),
    error = Color(0xFFFF0D38), // Ярко-красный
    errorBg = Color(0xFF3B000C),
    errorBorder = Color(0xFF8F001E),
    textPrimary = Color(0xFFFFF7F5),
    textSecondary = Color(0xFFD4B0A7),
    textTertiary = Color(0xFF916C63),
    accentSwatchList = listOf(
        Color(0xFF00F5D4), // Бирюзово-неоновый
        Color(0xFFFF7E67), // Кислотный персик
        Color(0xFF9D4EDD), // Фиолетовый неон
        Color(0xFF76FF03), // Мятный лайм
        Color(0xFFFF0D38)  // Ярко-красный
    ),
    isLocked = true
)

val NeonEmeraldPalette = AppThemePalette(
    id = "NEON_EMERALD",
    displayName = "Neon Emerald",
    subtitle = "Неоновый изумруд, ультра-розовый, солнечно-лимонный, кибер-синий & неоновый фиолетовый",
    description = "Драгоценное кибернетическое сияние: чистый изумруд и ультра-розовые акценты на тёмно-синей кристаллической основе",
    primary = Color(0xFF00E676), // Неоновый изумруд
    onPrimary = Color(0xFF003317),
    primaryContainer = Color(0xFF005226),
    onPrimaryContainer = Color(0xFF80FFBA),
    secondary = Color(0xFFFF007F), // Ультра-розовый
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFF060D14), // Кибер-сине-изумрудный фон
    surface = Color(0xFF0C1B26),
    surfaceDark = Color(0xFF08131C),
    surfaceVariant = Color(0xFF142938),
    surfaceContainer = Color(0xFF1C374A),
    cardBorder = Color(0xFF00B0FF), // Кибер-синий
    cardBorderSubtle = Color(0xFF113247),
    fieldBg = Color(0xFF0F202D),
    codeBg = Color(0xFF071017),
    mintDark = Color(0xFF00381C),
    amber = Color(0xFFFFEE00), // Солнечно-лимонный
    amberWarm = Color(0xFFFFFA55),
    error = Color(0xFFFF007F),
    errorBg = Color(0xFF3B001D),
    errorBorder = Color(0xFF8A0044),
    textPrimary = Color(0xFFF2FBF7),
    textSecondary = Color(0xFFA1C6B7),
    textTertiary = Color(0xFF5D8474),
    accentSwatchList = listOf(
        Color(0xFF00E676), // Неоновый изумруд
        Color(0xFFFF007F), // Ультра-розовый
        Color(0xFFFFEE00), // Солнечно-лимонный
        Color(0xFF00B0FF), // Кибер-синий
        Color(0xFF7C4DFF)  // Неоновый фиолетовый
    ),
    isLocked = true
)

val PastelTendernessPalette = AppThemePalette(
    id = "PASTEL_TENDERNESS",
    displayName = "Pastel Tenderness",
    subtitle = "Пудрово-розовый, мятный, персиковый, лавандовый & небесно-голубой",
    description = "Нежная пастельная симфония: бархатный пудрово-розовый, прохладная мята и небесно-голубые блики",
    primary = Color(0xFFF8BBD0), // Пудрово-розовый
    onPrimary = Color(0xFF4A1024),
    primaryContainer = Color(0xFF6B213B),
    onPrimaryContainer = Color(0xFFFFD8E4),
    secondary = Color(0xFFA7F3D0), // Мятный
    onSecondary = Color(0xFF003822),
    background = Color(0xFF140F16),
    surface = Color(0xFF1E1722),
    surfaceDark = Color(0xFF161019),
    surfaceVariant = Color(0xFF2B2030),
    surfaceContainer = Color(0xFF382A3F),
    cardBorder = Color(0xFFD1C4E9), // Лавандовый
    cardBorderSubtle = Color(0xFF3B2A45),
    fieldBg = Color(0xFF221A26),
    codeBg = Color(0xFF130D17),
    mintDark = Color(0xFF0C241B),
    amber = Color(0xFFFFCCBC), // Персиковый
    amberWarm = Color(0xFFFFAB91),
    error = Color(0xFFF48FB1),
    errorBg = Color(0xFF3B0B1A),
    errorBorder = Color(0xFF8C2044),
    textPrimary = Color(0xFFFDF8F9),
    textSecondary = Color(0xFFD9CAD3),
    textTertiary = Color(0xFF94818D),
    accentSwatchList = listOf(
        Color(0xFFF8BBD0), // Пудрово-розовый
        Color(0xFFA7F3D0), // Мятный
        Color(0xFFFFCCBC), // Персиковый
        Color(0xFFD1C4E9), // Лавандовый
        Color(0xFFB3E5FC)  // Небесно-голубой
    ),
    isLocked = true
)

val VanillaCreamPalette = AppThemePalette(
    id = "VANILLA_CREAM",
    displayName = "Vanilla Cream",
    subtitle = "Ванильный, фисташковый, кремовый, светло-желтый & пепельно-розовый",
    description = "Теплый сливочно-ванильный десерт: фисташковые акценты и шелковистая кремовая текстура",
    primary = Color(0xFFFEEAA7), // Ванильный
    onPrimary = Color(0xFF3E3100),
    primaryContainer = Color(0xFF5B4905),
    onPrimaryContainer = Color(0xFFFFF3CD),
    secondary = Color(0xFFC8E6C9), // Фисташковый
    onSecondary = Color(0xFF163819),
    background = Color(0xFF141310),
    surface = Color(0xFF1F1D17),
    surfaceDark = Color(0xFF161510),
    surfaceVariant = Color(0xFF2C2921),
    surfaceContainer = Color(0xFF3B372D),
    cardBorder = Color(0xFFD8B4B8), // Пепельно-розовый
    cardBorderSubtle = Color(0xFF383228),
    fieldBg = Color(0xFF22201A),
    codeBg = Color(0xFF14130F),
    mintDark = Color(0xFF162B18),
    amber = Color(0xFFFFF59D), // Светло-желтый
    amberWarm = Color(0xFFFFF176),
    error = Color(0xFFE57373),
    errorBg = Color(0xFF381010),
    errorBorder = Color(0xFF822020),
    textPrimary = Color(0xFFFAF7F0),
    textSecondary = Color(0xFFD6CFC0),
    textTertiary = Color(0xFF8F8877),
    accentSwatchList = listOf(
        Color(0xFFFEEAA7), // Ванильный
        Color(0xFFC8E6C9), // Фисташковый
        Color(0xFFFFF9E6), // Кремовый
        Color(0xFFFFF59D), // Светло-желтый
        Color(0xFFD8B4B8)  // Пепельно-розовый
    ),
    isLocked = true
)

val LavenderMistPalette = AppThemePalette(
    id = "LAVENDER_MIST",
    displayName = "Lavender Mist",
    subtitle = "Лавандовый туман, светло-мятный, пыльная роза, бледно-лимонный & жемчужный",
    description = "Утренний лавандовый туман: мягкие переливы пыльной розы и прохладной мяты",
    primary = Color(0xFFCE93D8), // Лавандовый туман
    onPrimary = Color(0xFF3B1047),
    primaryContainer = Color(0xFF572066),
    onPrimaryContainer = Color(0xFFF3D5FA),
    secondary = Color(0xFFB2DFDB), // Светло-мятный
    onSecondary = Color(0xFF003833),
    background = Color(0xFF120E17),
    surface = Color(0xFF1C1624),
    surfaceDark = Color(0xFF140F1A),
    surfaceVariant = Color(0xFF2A2036),
    surfaceContainer = Color(0xFF372A47),
    cardBorder = Color(0xFFD8A4B8), // Пыльная роза
    cardBorderSubtle = Color(0xFF352642),
    fieldBg = Color(0xFF1F1929),
    codeBg = Color(0xFF110C17),
    mintDark = Color(0xFF092925),
    amber = Color(0xFFFFF9C4), // Бледно-лимонный
    amberWarm = Color(0xFFFFF59D),
    error = Color(0xFFE57373),
    errorBg = Color(0xFF381010),
    errorBorder = Color(0xFF822020),
    textPrimary = Color(0xFFF9F5FB),
    textSecondary = Color(0xFFD0C3DB),
    textTertiary = Color(0xFF887994),
    accentSwatchList = listOf(
        Color(0xFFCE93D8), // Лавандовый туман
        Color(0xFFB2DFDB), // Светло-мятный
        Color(0xFFD8A4B8), // Пыльная роза
        Color(0xFFFFF9C4), // Бледно-лимонный
        Color(0xFFF3E5F5)  // Жемчужный
    ),
    isLocked = true
)

val MarshmallowDreamPalette = AppThemePalette(
    id = "MARSHMALLOW_DREAM",
    displayName = "Marshmallow Dream",
    subtitle = "Зефирный, персиковый крем, небесный, светло-сиреневый & фисташковый крем",
    description = "Зефирный сон: воздушная сладость персикового крема и легкая сиреневая дымка",
    primary = Color(0xFFFCE4EC), // Зефирный
    onPrimary = Color(0xFF4A1025),
    primaryContainer = Color(0xFF6B1D39),
    onPrimaryContainer = Color(0xFFFFD9E6),
    secondary = Color(0xFFB3E5FC), // Небесный
    onSecondary = Color(0xFF00344D),
    background = Color(0xFF140F15),
    surface = Color(0xFF1F1721),
    surfaceDark = Color(0xFF161017),
    surfaceVariant = Color(0xFF2C212E),
    surfaceContainer = Color(0xFF3D2D40),
    cardBorder = Color(0xFFE1BEE7), // Светло-сиреневый
    cardBorderSubtle = Color(0xFF38263B),
    fieldBg = Color(0xFF221A24),
    codeBg = Color(0xFF130D14),
    mintDark = Color(0xFF142918),
    amber = Color(0xFFFFE0B2), // Персиковый крем
    amberWarm = Color(0xFFFFCC80),
    error = Color(0xFFF48FB1),
    errorBg = Color(0xFF3B0B1A),
    errorBorder = Color(0xFF8C2044),
    textPrimary = Color(0xFFFAF5F8),
    textSecondary = Color(0xFFD6C6D2),
    textTertiary = Color(0xFF8F7B8A),
    accentSwatchList = listOf(
        Color(0xFFFCE4EC), // Зефирный
        Color(0xFFFFE0B2), // Персиковый крем
        Color(0xFFB3E5FC), // Небесный
        Color(0xFFE1BEE7), // Светло-сиреневый
        Color(0xFFDCEDC8)  // Фисташковый крем
    ),
    isLocked = true
)

val SoftBeigePalette = AppThemePalette(
    id = "SOFT_BEIGE",
    displayName = "Soft Beige",
    subtitle = "Кремово-бежевый, размытый мятный, приглушенный розовый, дымчато-голубой & ванильный",
    description = "Утонченный мягкий беж: спокойная гармония пудровых и дымчато-голубых тонов",
    primary = Color(0xFFF5EBE0), // Кремово-бежевый
    onPrimary = Color(0xFF3B2E21),
    primaryContainer = Color(0xFF564332),
    onPrimaryContainer = Color(0xFFFFEEDD),
    secondary = Color(0xFFC5E1A5), // Размытый мятный
    onSecondary = Color(0xFF1A3805),
    background = Color(0xFF141210),
    surface = Color(0xFF1E1A16),
    surfaceDark = Color(0xFF15120F),
    surfaceVariant = Color(0xFF2C2621),
    surfaceContainer = Color(0xFF3B332C),
    cardBorder = Color(0xFFB0BEC5), // Дымчато-голубой
    cardBorderSubtle = Color(0xFF383029),
    fieldBg = Color(0xFF211D18),
    codeBg = Color(0xFF13110E),
    mintDark = Color(0xFF172B10),
    amber = Color(0xFFE8AEB7), // Приглушенный розовый
    amberWarm = Color(0xFFFFF8E1), // Ванильный
    error = Color(0xFFE57373),
    errorBg = Color(0xFF381010),
    errorBorder = Color(0xFF822020),
    textPrimary = Color(0xFFFAF7F2),
    textSecondary = Color(0xFFD6CBC0),
    textTertiary = Color(0xFF8C8074),
    accentSwatchList = listOf(
        Color(0xFFF5EBE0), // Кремово-бежевый
        Color(0xFFC5E1A5), // Размытый мятный
        Color(0xFFE8AEB7), // Приглушенный розовый
        Color(0xFFB0BEC5), // Дымчато-голубой
        Color(0xFFFFF8E1)  // Ванильный
    ),
    isLocked = true
)

val ApricotMoussePalette = AppThemePalette(
    id = "APRICOT_MOUSSE",
    displayName = "Apricot Mousse",
    subtitle = "Нежно-абрикосовый, сиреневый мусс, зефирно-розовый, светлый хаки & бледно-бирюзовый",
    description = "Абрикосовый мусс: сочные фруктовые нотки с мягким сиреневым и бирюзовым шлейфом",
    primary = Color(0xFFFFCC80), // Нежно-абрикосовый
    onPrimary = Color(0xFF472A00),
    primaryContainer = Color(0xFF663E00),
    onPrimaryContainer = Color(0xFFFFE3B8),
    secondary = Color(0xFFD1C4E9), // Сиреневый мусс
    onSecondary = Color(0xFF261845),
    background = Color(0xFF14100E),
    surface = Color(0xFF1E1814),
    surfaceDark = Color(0xFF16110D),
    surfaceVariant = Color(0xFF2C221D),
    surfaceContainer = Color(0xFF3B2E27),
    cardBorder = Color(0xFFB2EBF2), // Бледно-бирюзовый
    cardBorderSubtle = Color(0xFF3D2A20),
    fieldBg = Color(0xFF211A15),
    codeBg = Color(0xFF130F0D),
    mintDark = Color(0xFF09292E),
    amber = Color(0xFFF8BBD0), // Зефирно-розовый
    amberWarm = Color(0xFFC7C3A8), // Светлый хаки
    error = Color(0xFFFF8A80),
    errorBg = Color(0xFF3B0E0A),
    errorBorder = Color(0xFF8F241B),
    textPrimary = Color(0xFFFAF6F2),
    textSecondary = Color(0xFFD9CAC0),
    textTertiary = Color(0xFF917F74),
    accentSwatchList = listOf(
        Color(0xFFFFCC80), // Нежно-абрикосовый
        Color(0xFFD1C4E9), // Сиреневый мусс
        Color(0xFFF8BBD0), // Зефирно-розовый
        Color(0xFFC7C3A8), // Светлый хаки
        Color(0xFFB2EBF2)  // Бледно-бирюзовый
    ),
    isLocked = true
)

val CottonSilkPalette = AppThemePalette(
    id = "COTTON_SILK",
    displayName = "Cotton Silk",
    subtitle = "Хлопковый, лавандовый крем, мятный шелк, розовая пастель & кремово-желтый",
    description = "Прикосновение хлопкового шелка: невесомый лавандовый крем и прохладная мятная текстура",
    primary = Color(0xFFEDE7F6), // Хлопковый
    onPrimary = Color(0xFF2E243D),
    primaryContainer = Color(0xFF47395C),
    onPrimaryContainer = Color(0xFFF5EEFF),
    secondary = Color(0xFFC8E6C9), // Мятный шелк
    onSecondary = Color(0xFF143818),
    background = Color(0xFF121117),
    surface = Color(0xFF1B1A24),
    surfaceDark = Color(0xFF13121A),
    surfaceVariant = Color(0xFF282636),
    surfaceContainer = Color(0xFF37344A),
    cardBorder = Color(0xFFFCE4EC), // Розовая пастель
    cardBorderSubtle = Color(0xFF312E42),
    fieldBg = Color(0xFF1E1D29),
    codeBg = Color(0xFF111017),
    mintDark = Color(0xFF112915),
    amber = Color(0xFFFFFDE7), // Кремово-желтый
    amberWarm = Color(0xFFFFF9C4),
    error = Color(0xFFE57373),
    errorBg = Color(0xFF381010),
    errorBorder = Color(0xFF822020),
    textPrimary = Color(0xFFF9F7FC),
    textSecondary = Color(0xFFCAC5D6),
    textTertiary = Color(0xFF827D91),
    accentSwatchList = listOf(
        Color(0xFFEDE7F6), // Хлопковый
        Color(0xFFE8EAF6), // Лавандовый крем
        Color(0xFFC8E6C9), // Мятный шелк
        Color(0xFFFCE4EC), // Розовая пастель
        Color(0xFFFFFDE7)  // Кремово-желтый
    ),
    isLocked = true
)

val AshMarshmallowPalette = AppThemePalette(
    id = "ASH_MARSHMALLOW",
    displayName = "Ash Marshmallow",
    subtitle = "Пепельно-кремовый, небесно-пастельный, фисташковый мусс, пудрово-лиловый & персиковый зефир",
    description = "Пепельный маршмеллоу: туманная пастельная прохлада с фисташковым муссом и персиковым оттенком",
    primary = Color(0xFFD7CCC8), // Пепельно-кремовый
    onPrimary = Color(0xFF382F2C),
    primaryContainer = Color(0xFF524541),
    onPrimaryContainer = Color(0xFFF2E9E6),
    secondary = Color(0xFFBBDEFB), // Небесно-пастельный
    onSecondary = Color(0xFF003352),
    background = Color(0xFF131112),
    surface = Color(0xFF1D191B),
    surfaceDark = Color(0xFF141113),
    surfaceVariant = Color(0xFF2B2528),
    surfaceContainer = Color(0xFF3B3337),
    cardBorder = Color(0xFFD1C4E9), // Пудрово-лиловый
    cardBorderSubtle = Color(0xFF362E33),
    fieldBg = Color(0xFF201B1E),
    codeBg = Color(0xFF110F11),
    mintDark = Color(0xFF152917),
    amber = Color(0xFFFFE0B2), // Персиковый зефир
    amberWarm = Color(0xFFDCEDC8), // Фисташковый мусс
    error = Color(0xFFEF9A9A),
    errorBg = Color(0xFF381212),
    errorBorder = Color(0xFF822A2A),
    textPrimary = Color(0xFFF8F5F6),
    textSecondary = Color(0xFFCEC4C8),
    textTertiary = Color(0xFF877C81),
    accentSwatchList = listOf(
        Color(0xFFD7CCC8), // Пепельно-кремовый
        Color(0xFFBBDEFB), // Небесно-пастельный
        Color(0xFFDCEDC8), // Фисташковый мусс
        Color(0xFFD1C4E9), // Пудрово-лиловый
        Color(0xFFFFE0B2)  // Персиковый зефир
    ),
    isLocked = true
)

val SmokyMintPalette = AppThemePalette(
    id = "SMOKY_MINT",
    displayName = "Smoky Mint",
    subtitle = "Мягкий беж, размытый мятный, бледно-розовый, дымчато-лавандовый & ванильный крем",
    description = "Дымчатая мята: освежающий мятный ветерок в объятиях бежевого шёлка и лавандовой вуали",
    primary = Color(0xFFB2DFDB), // Размытый мятный
    onPrimary = Color(0xFF003833),
    primaryContainer = Color(0xFF00524B),
    onPrimaryContainer = Color(0xFFD0F5F1),
    secondary = Color(0xFFEFEBE9), // Мягкий беж
    onSecondary = Color(0xFF383331),
    background = Color(0xFF101313),
    surface = Color(0xFF171D1D),
    surfaceDark = Color(0xFF101414),
    surfaceVariant = Color(0xFF222B2B),
    surfaceContainer = Color(0xFF303C3C),
    cardBorder = Color(0xFFC5CAE9), // Дымчато-лавандовый
    cardBorderSubtle = Color(0xFF263333),
    fieldBg = Color(0xFF1A2121),
    codeBg = Color(0xFF0E1212),
    mintDark = Color(0xFF003833),
    amber = Color(0xFFF8BBD0), // Бледно-розовый
    amberWarm = Color(0xFFFFF8E1), // Ванильный крем
    error = Color(0xFFEF9A9A),
    errorBg = Color(0xFF381212),
    errorBorder = Color(0xFF822A2A),
    textPrimary = Color(0xFFF2F8F7),
    textSecondary = Color(0xFFB8C9C7),
    textTertiary = Color(0xFF738583),
    accentSwatchList = listOf(
        Color(0xFFEFEBE9), // Мягкий беж
        Color(0xFFB2DFDB), // Размытый мятный
        Color(0xFFF8BBD0), // Бледно-розовый
        Color(0xFFC5CAE9), // Дымчато-лавандовый
        Color(0xFFFFF8E1)  // Ванильный крем
    ),
    isLocked = true
)

val PowderyPeachPalette = AppThemePalette(
    id = "POWDERY_PEACH",
    displayName = "Powdery Peach",
    subtitle = "Пастельно-бирюзовый, кремовый персик, светло-лавандовый, пыльный мятный & пудровый беж",
    description = "Пудровый персик: матовая пастельная гармония нежного персика, лаванды и пыльной мяты",
    primary = Color(0xFFFFCCBC), // Кремовый персик
    onPrimary = Color(0xFF472218),
    primaryContainer = Color(0xFF663428),
    onPrimaryContainer = Color(0xFFFFDFD6),
    secondary = Color(0xFFB2DFDB), // Пастельно-бирюзовый
    onSecondary = Color(0xFF003833),
    background = Color(0xFF141010),
    surface = Color(0xFF1F1817),
    surfaceDark = Color(0xFF161110),
    surfaceVariant = Color(0xFF2C2322),
    surfaceContainer = Color(0xFF3D302E),
    cardBorder = Color(0xFFE1BEE7), // Светло-лавандовый
    cardBorderSubtle = Color(0xFF3B2724),
    fieldBg = Color(0xFF221B19),
    codeBg = Color(0xFF130E0E),
    mintDark = Color(0xFF0B2925),
    amber = Color(0xFFC8E6C9), // Пыльный мятный
    amberWarm = Color(0xFFEFEBE9), // Пудровый беж
    error = Color(0xFFFF8A80),
    errorBg = Color(0xFF3B0E0A),
    errorBorder = Color(0xFF8F241B),
    textPrimary = Color(0xFFFAF5F4),
    textSecondary = Color(0xFFD6C6C2),
    textTertiary = Color(0xFF8C7975),
    accentSwatchList = listOf(
        Color(0xFFB2DFDB), // Пастельно-бирюзовый
        Color(0xFFFFCCBC), // Кремовый персик
        Color(0xFFE1BEE7), // Светло-лавандовый
        Color(0xFFC8E6C9), // Пыльный мятный
        Color(0xFFEFEBE9)  // Пудровый беж
    ),
    isLocked = true
)

enum class AppThemeMode(val palette: AppThemePalette) {
    CYBER_LILAC(CyberLilacPalette),
    RACING_AMBER(RacingAmberPalette),
    EMERALD_MATRIX(EmeraldMatrixPalette),
    PURE_LIGHT(PureLightPalette),
    CARBON_GT_RED(CarbonGTRedPalette),
    STONE_TIMBER(StoneTimberPalette),
    URBAN_PINK(UrbanPinkPalette),
    CRIMSON_SPARK(CrimsonSparkPalette),
    MINECRAFT_BLOCKS(MinecraftBlocksPalette),
    WODENER_WORC(WodenerWorcPalette),
    LASURITE_CRYSTAL(LasuriteCrystalPalette),
    INFERNO_STREET(InfernoStreetPalette),
    CRYPTIC_FROST(CrypticFrostPalette),
    GHOUL_KEN_KANEKI(GhoulKenKanekiPalette),
    NEON_CYBERPUNK(NeonCyberpunkPalette),
    CYBER_SUNSET(CyberSunsetPalette),
    DIGITAL_DAWN(DigitalDawnPalette),
    NEON_PULSE(NeonPulsePalette),
    CYBER_MINT(CyberMintPalette),
    ELECTRIC_CHARGE(ElectricChargePalette),
    NEON_SPECTRUM(NeonSpectrumPalette),
    TOXIC_BURST(ToxicBurstPalette),
    CYBER_PEACH(CyberPeachPalette),
    NEON_EMERALD(NeonEmeraldPalette),
    PASTEL_TENDERNESS(PastelTendernessPalette),
    VANILLA_CREAM(VanillaCreamPalette),
    LAVENDER_MIST(LavenderMistPalette),
    MARSHMALLOW_DREAM(MarshmallowDreamPalette),
    SOFT_BEIGE(SoftBeigePalette),
    APRICOT_MOUSSE(ApricotMoussePalette),
    COTTON_SILK(CottonSilkPalette),
    ASH_MARSHMALLOW(AshMarshmallowPalette),
    SMOKY_MINT(SmokyMintPalette),
    POWDERY_PEACH(PowderyPeachPalette)
}

val LocalAppThemeColors = compositionLocalOf { CyberLilacPalette }
val LocalAppThemeMode = compositionLocalOf { AppThemeMode.CYBER_LILAC }

val ImmersiveBackground: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.background

val ImmersiveSurface: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.surface

val ImmersiveSurfaceDark: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.surfaceDark

val ImmersiveSurfaceVariant: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.surfaceVariant

val ImmersiveSurfaceContainer: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.surfaceContainer

val ImmersiveCardBorder: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.cardBorder

val ImmersiveCardBorderSubtle: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.cardBorderSubtle

val ImmersiveFieldBg: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.fieldBg

val ImmersiveCodeBg: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.codeBg

val ImmersiveLilac: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.primary

val ImmersiveOnLilac: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.onPrimary

val ImmersiveLilacContainer: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.primaryContainer

val ImmersiveMint: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.secondary

val ImmersiveMintDark: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.mintDark

val ImmersiveAmber: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.amber

val ImmersiveAmberWarm: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.amberWarm

val ImmersiveError: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.error

val ImmersiveErrorBg: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.errorBg

val ImmersiveErrorBorder: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.errorBorder

val TextPrimary: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.textPrimary

val TextSecondary: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.textSecondary

val TextTertiary: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalAppThemeColors.current.textTertiary

// Backward compatible aliases
val CarbonBackground: Color @Composable @ReadOnlyComposable get() = ImmersiveBackground
val CarbonSurface: Color @Composable @ReadOnlyComposable get() = ImmersiveSurface
val CarbonSurfaceVariant: Color @Composable @ReadOnlyComposable get() = ImmersiveSurfaceVariant
val CarbonCardBorder: Color @Composable @ReadOnlyComposable get() = ImmersiveCardBorder
val NeonCyan: Color @Composable @ReadOnlyComposable get() = ImmersiveLilac
val NeonCyanVariant: Color @Composable @ReadOnlyComposable get() = ImmersiveLilacContainer
val RacingOrange: Color @Composable @ReadOnlyComposable get() = ImmersiveLilac
val RacingAmber: Color @Composable @ReadOnlyComposable get() = ImmersiveAmberWarm
val AcidGreen: Color @Composable @ReadOnlyComposable get() = ImmersiveMint
val DangerRed: Color @Composable @ReadOnlyComposable get() = ImmersiveError
