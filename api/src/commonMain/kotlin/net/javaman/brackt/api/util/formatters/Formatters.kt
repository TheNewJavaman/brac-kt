package net.javaman.brackt.api.util.formatters

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.pow

const val INSTANT_YEAR_DIGITS = 4
const val INSTANT_MONTH_DIGITS = 2
const val INSTANT_DAY_DIGITS = 2
const val INSTANT_HOUR_DIGITS = 2
const val INSTANT_MINUTE_DIGITS = 2
const val INSTANT_SECOND_DIGITS = 2
const val INSTANT_NANOSECOND_DIGITS = 3
const val NANOSECOND_DECIMALS = 1_000_000_000
val INSTANT_NANOSECOND_DIVISOR = NANOSECOND_DECIMALS / 10f.pow(INSTANT_NANOSECOND_DIGITS).toInt()

/**
 * Pads or shortens a String as needed
 */
fun String.withLength(target: Int, direction: PadDirection, padChar: Char = ' ', prefix: String = "..."): String {
    return if (length <= target) when (direction) {
        PadDirection.LEFT -> padStart(target, padChar)
        PadDirection.RIGHT -> padEnd(target, padChar)
    } else prefix + substring(length - target + prefix.length)
}

enum class PadDirection {
    RIGHT,
    LEFT
}

/**
 * Pads [Number.toString] as needed
 */
fun Number.padTo(length: Int, padChar: Char = '0') = toString().padStart(length, padChar)

/**
 * Nicely formats an [Instant]
 */
fun Instant.pretty(): String {
    val timeZone = TimeZone.currentSystemDefault()
    val dateTime = toLocalDateTime(timeZone)
    return dateTime.year.padTo(INSTANT_YEAR_DIGITS) +
            "-${dateTime.monthNumber.padTo(INSTANT_MONTH_DIGITS)}" +
            "-${dateTime.dayOfMonth.padTo(INSTANT_DAY_DIGITS)}" +
            " ${dateTime.hour.padTo(INSTANT_HOUR_DIGITS)}" +
            ":${dateTime.minute.padTo(INSTANT_MINUTE_DIGITS)}" +
            ":${dateTime.second.padTo(INSTANT_SECOND_DIGITS)}" +
            ".${(dateTime.nanosecond / INSTANT_NANOSECOND_DIVISOR).padTo(INSTANT_NANOSECOND_DIGITS)}"
}
