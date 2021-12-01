package net.javaman.brakt.api.util.logger

import kotlinx.datetime.Clock
import net.javaman.brakt.api.util.formatters.PadDirection
import net.javaman.brakt.api.util.formatters.pretty
import net.javaman.brakt.api.util.formatters.withLength
import net.javaman.brakt.api.util.injections.FromKClassObject
import kotlin.jvm.JvmStatic
import kotlin.reflect.KClass

/**
 * Prints well-formatted messages to the console using pure Kotlin and a clean syntax. Useful for multiplatform projects
 */
class Logger(val className: String) {
    companion object : FromKClassObject<Logger> {
        @JvmStatic
        var acceptableLevel: LoggingLevel = LoggingLevel.INFO

        @JvmStatic
        override fun fromKClass(kClass: KClass<*>) = Logger(kClass)

        const val CLASS_NAME_CHARS = 48

        @JvmStatic
        val LEVEL_CHARS = LoggingLevel.values().maxOf { it.name.length }
    }

    constructor(kClass: KClass<*>) : this(kClass.qualifiedName ?: "Anonymous")

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
