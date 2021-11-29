package net.javaman.brakt.api.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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
    return dateTime.year.padTo(4) +
            "-${dateTime.monthNumber.padTo(2)}" +
            "-${dateTime.dayOfMonth.padTo(2)}" +
            " ${dateTime.hour.padTo(2)}" +
            ":${dateTime.minute.padTo(2)}" +
            ":${dateTime.second.padTo(2)}" +
            ".${(dateTime.nanosecond / 1_000_000).padTo(3)}"
}
