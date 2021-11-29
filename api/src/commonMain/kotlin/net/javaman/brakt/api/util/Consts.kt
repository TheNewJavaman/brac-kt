package net.javaman.brakt.api.util

import net.javaman.brakt.api.util.logger.LoggingLevel
import kotlin.jvm.JvmStatic
import kotlin.math.pow

object Consts {
    const val INSTANT_YEAR_DIGITS = 4
    const val INSTANT_MONTH_DIGITS = 2
    const val INSTANT_DAY_DIGITS = 2
    const val INSTANT_HOUR_DIGITS = 2
    const val INSTANT_MINUTE_DIGITS = 2
    const val INSTANT_SECOND_DIGITS = 2
    const val INSTANT_NANOSECOND_DIGITS = 3
    const val NANOSECOND_DECIMALS = 1_000_000_000

    @JvmStatic
    val INSTANT_NANOSECOND_DIVISOR = NANOSECOND_DECIMALS / 10f.pow(INSTANT_NANOSECOND_DIGITS).toInt()

    const val LOGGER_CLASS_NAME_CHARS = 48

    @JvmStatic
    val LOGGER_LEVEL_CHARS = LoggingLevel.values().maxOf { it.name.length }
}


