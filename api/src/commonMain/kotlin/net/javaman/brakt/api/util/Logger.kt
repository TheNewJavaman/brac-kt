package net.javaman.brakt.api.util

import kotlinx.datetime.Clock
import kotlin.reflect.KClass

class Logger(val className: String) {
    companion object {
        var acceptableLevel: LoggingLevel = LoggingLevel.INFO
    }

    constructor(kClass: KClass<*>) : this(kClass.qualifiedName ?: "Anonymous")

    inline fun log(level: LoggingLevel, block: () -> Any) {
        if (acceptableLevel.severity <= level.severity) {
            val nowPretty = Clock.System.now().pretty()
            val classNamePretty = className.withLength(LOGGER_CLASS_NAME_CHARS, PadDirection.LEFT)
            val levelPretty = level.name.withLength(LOGGER_LEVEL_CHARS, PadDirection.RIGHT)
            val lines = block().toString().split('\n')
            println(lines.joinToString("\n") { "[$nowPretty] [$classNamePretty] [$levelPretty] $it" })
        }
    }

    inline fun error(block: () -> Any) = log(LoggingLevel.ERROR, block)
    inline fun warn(block: () -> Any) = log(LoggingLevel.WARN, block)
    inline fun info(block: () -> Any) = log(LoggingLevel.INFO, block)
    inline fun debug(block: () -> Any) = log(LoggingLevel.DEBUG, block)
    inline fun trace(block: () -> Any) = log(LoggingLevel.TRACE, block)
}

@Suppress("MagicNumber") // Explicit severity levels is more transparent than enum ordinals
enum class LoggingLevel(val severity: Int) {
    ERROR(4),
    WARN(3),
    INFO(2),
    DEBUG(1),
    TRACE(0)
}
