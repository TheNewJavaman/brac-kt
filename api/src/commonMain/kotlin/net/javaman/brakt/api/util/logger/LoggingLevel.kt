package net.javaman.brakt.api.util.logger

@Suppress("MagicNumber") // Explicit severity levels is more transparent than enum ordinals
enum class LoggingLevel(val severity: Int) {
    ERROR(4),
    WARN(3),
    INFO(2),
    DEBUG(1),
    TRACE(0)
}
