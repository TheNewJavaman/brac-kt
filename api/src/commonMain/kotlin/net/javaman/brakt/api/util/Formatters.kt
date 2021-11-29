package net.javaman.brakt.api.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.pow

fun String.withLength(target: Int, direction: PadDirection, padChar: Char = ' ', prefix: String = "..."): String {
    return if (length <= target) when (direction) {
        PadDirection.LEFT -> padStart(target - length, padChar)
        PadDirection.RIGHT -> padEnd(target - length, padChar)
    } else prefix + substring(length - target + prefix.length)
}

enum class PadDirection {
    RIGHT,
    LEFT
}

fun Number.padTo(length: Int, padChar: Char = '0') = toString().padStart(length, padChar)

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
