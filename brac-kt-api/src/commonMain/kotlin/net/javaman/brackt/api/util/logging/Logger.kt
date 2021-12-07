@file:OptIn(ExperimentalJsExport::class)

package net.javaman.brackt.api.util.logging

import kotlinx.datetime.Clock
import net.javaman.brackt.api.util.formatters.PadDirection
import net.javaman.brackt.api.util.formatters.pretty
import net.javaman.brackt.api.util.formatters.withLength
import net.javaman.brackt.api.util.reflection.getPlatformName
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import kotlin.jvm.JvmStatic
import kotlin.reflect.KClass

/**
 * Prints well-formatted messages to the console using pure Kotlin and a clean syntax. Useful for multiplatform projects
 */
@JsExport
class Logger(val className: String) {
    companion object {
        @JvmStatic
        var acceptableLevel: LoggingLevel = LoggingLevel.INFO

        const val CLASS_NAME_CHARS = 64

        @JvmStatic
        val LEVEL_CHARS = LoggingLevel.values().maxOf { it.name.length }
    }

    @JsName("LoggerByClass") constructor(kClass: KClass<*>) : this(kClass.getPlatformName())

    fun log(level: LoggingLevel, block: () -> Any) {
        if (acceptableLevel.severity <= level.severity) {
            val nowPretty = Clock.System.now().pretty()
            val classNamePretty = className.withLength(CLASS_NAME_CHARS, PadDirection.LEFT)
            val levelPretty = level.name.withLength(LEVEL_CHARS, PadDirection.RIGHT)
            val lines = block().toString().split('\n')
            println(lines.joinToString("\n") { "[$nowPretty] [$classNamePretty] [$levelPretty] $it" })
        }
    }

    fun error(block: () -> Any) = log(LoggingLevel.ERROR, block)
    fun warn(block: () -> Any) = log(LoggingLevel.WARN, block)
    fun info(block: () -> Any) = log(LoggingLevel.INFO, block)
    fun debug(block: () -> Any) = log(LoggingLevel.DEBUG, block)
    fun trace(block: () -> Any) = log(LoggingLevel.TRACE, block)
}

@Suppress("MagicNumber") // Explicit severity levels is more transparent than enum ordinals
enum class LoggingLevel(val severity: Int) {
    ERROR(4),
    WARN(3),
    INFO(2),
    DEBUG(1),
    TRACE(0)
}
