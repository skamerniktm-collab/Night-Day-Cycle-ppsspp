package com.example.model

/**
 * Structural placement options for the dashboard control panel.
 */
enum class ControlsPosition(
    val title: String,
    val subtitle: String,
    val badge: String
) {
    TOP(
        title = "Панель вверху",
        subtitle = "Кнопки управления (Старт/Стоп/Трасса) отображаются в верхней части экрана перед телеметрией",
        badge = "TOP BAR"
    ),
    BOTTOM(
        title = "Панель внизу",
        subtitle = "Классический порядок: сначала телеметрия и статус двигателя, затем кнопки управления",
        badge = "BELOW STATUS"
    ),
    DOCKED_BOTTOM(
        title = "Закреплена снизу",
        subtitle = "Панель управления зафиксирована у нижней границы экрана поверх контента",
        badge = "DOCKED"
    ),
    DRAWING(
        title = "Режим рисования",
        subtitle = "Интерактивный холст для свободного рисования пальцем и создания фона приложения",
        badge = "CANVAS DRAW"
    )
}
