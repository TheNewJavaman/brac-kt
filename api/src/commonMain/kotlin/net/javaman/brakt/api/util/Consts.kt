package net.javaman.brakt.api.util

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

const val LOGGER_CLASS_NAME_CHARS = 48
val LOGGER_LEVEL_CHARS = LoggingLevel.values().maxOf { it.name.length }
