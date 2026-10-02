package com.example.model

/**
 * List of unlockable achievements in NDCYCLE.
 *
 * @param id Unique persistent achievement key
 * @param title Achievement display name
 * @param description Explanation of what specific action triggered the achievement
 * @param iconEmoji Visual icon indicator
 * @param rewardXp Amount of XP experience awarded for unlocking
 */
enum class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val rewardXp: Int = 50
) {
    FIRST_LAUNCH(
        id = "first_launch",
        title = "Первый старт",
        description = "Запущен цикл автоматической смены текстур",
        iconEmoji = "🚀",
        rewardXp = 50
    ),
    STORAGE_GRANTED(
        id = "storage_granted",
        title = "Хранитель файлов",
        description = "Предоставлен доступ к памяти устройства",
        iconEmoji = "📁",
        rewardXp = 75
    ),
    NOTIFICATIONS_GRANTED(
        id = "notifications_granted",
        title = "На страже событий",
        description = "Включены системные уведомления для службы",
        iconEmoji = "🔔",
        rewardXp = 50
    ),
    THEME_PURCHASED(
        id = "theme_purchased",
        title = "Коллекционер",
        description = "Приобретена новая тема оформления в магазине",
        iconEmoji = "🛍️",
        rewardXp = 100
    ),
    THEME_CHANGED(
        id = "theme_changed",
        title = "Новый стиль",
        description = "Установлена альтернативная палитра интерфейса",
        iconEmoji = "🎨",
        rewardXp = 40
    ),
    CUSTOM_WALLPAPER(
        id = "custom_wallpaper",
        title = "Творец",
        description = "Создан и применён собственный рисунок как фон",
        iconEmoji = "🖌️",
        rewardXp = 150
    ),
    SWIPE_TESTED(
        id = "swipe_tested",
        title = "Тест жестов",
        description = "Проверена эмуляция микро-свайпа службы кликера",
        iconEmoji = "👆",
        rewardXp = 50
    ),
    CACHE_CLEANED(
        id = "cache_cleaned",
        title = "Чистый лист",
        description = "Выполнена очистка кеша и временных файлов",
        iconEmoji = "🧹",
        rewardXp = 60
    ),
    TIME_WARP(
        id = "time_warp",
        title = "Повелитель времени",
        description = "Настроен интервал смены паков текстур",
        iconEmoji = "⏱️",
        rewardXp = 40
    ),
    RAIN_MAKER(
        id = "rain_maker",
        title = "Вызыватель дождя",
        description = "Изменена вероятность появления дождливой погоды",
        iconEmoji = "🌧️",
        rewardXp = 50
    ),
    EASTER_EGG_NINE_TAPS(
        id = "easter_egg_nine_taps",
        title = "Секретный агент",
        description = "Найдена пасхалка разработчиков: 9 нажатий на бейдж версии",
        iconEmoji = "🕵️",
        rewardXp = 250
    ),
    TRACK_EXPLORER(
        id = "track_explorer",
        title = "Новая трасса",
        description = "Выбрана новая трасса для подмены текстур",
        iconEmoji = "🏎️",
        rewardXp = 50
    );

    // Backward-compatibility alias
    val rewardNd: Int get() = rewardXp
}

/**
 * User experience and level progression model.
 */
data class UserLevelInfo(
    val level: Int,
    val currentLevelXp: Int,
    val xpForNextLevel: Int,
    val totalXp: Int,
    val progress: Float,
    val rankTitle: String
)

/**
 * Computes level and tier based on cumulative XP earned.
 */
object LevelCalculator {
    const val XP_PER_LEVEL = 100

    fun calculate(totalXp: Int): UserLevelInfo {
        val safeXp = totalXp.coerceAtLeast(0)
        val level = (safeXp / XP_PER_LEVEL) + 1
        val currentLevelXp = safeXp % XP_PER_LEVEL
        val progress = (currentLevelXp.toFloat() / XP_PER_LEVEL.toFloat()).coerceIn(0f, 1f)

        val rankTitle = when (level) {
            1 -> "Новичок"
            2 -> "Энтузиаст"
            3 -> "Исследователь"
            4 -> "Специалист"
            5 -> "Инженер"
            6 -> "Мастер ND"
            7 -> "Кибер-гонщик"
            8 -> "Архитектор"
            9 -> "Грандмастер"
            else -> "Легенда"
        }

        return UserLevelInfo(
            level = level,
            currentLevelXp = currentLevelXp,
            xpForNextLevel = XP_PER_LEVEL,
            totalXp = safeXp,
            progress = progress,
            rankTitle = rankTitle
        )
    }
}

/**
 * Transient achievement notification event shown in the top toaster.
 */
data class AchievementPopup(
    val achievement: Achievement,
    val timestamp: Long = System.currentTimeMillis()
)
