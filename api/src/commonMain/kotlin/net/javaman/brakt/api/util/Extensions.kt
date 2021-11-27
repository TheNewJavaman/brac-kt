package net.javaman.brakt.api.util

import java.util.*

fun Double.format(
    precision: Int = DOUBLE_FORMAT_PRECISION,
    locale: Locale = DEFAULT_LOCALE
) = String.format(locale, "%.${precision}f", this)
