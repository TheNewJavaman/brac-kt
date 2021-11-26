package net.javaman.brakt.api.util

import java.util.*
import kotlin.math.max

fun Double.format(
    precision: Int = DOUBLE_FORMAT_PRECISION,
    locale: Locale = DEFAULT_LOCALE
) = String.format(locale, "%.${precision}f", this)

fun List<Long>.rangeFrom(that: List<Long>): List<List<Long>> {
    if (this.size != that.size) throw IndexOutOfBoundsException("Depths must be the same to range between List<Long>")
    if (this.isEmpty()) return listOf()
    val range = (this[0] until that[0]).map { listOf(it) }.toMutableList()
    for (d in this.subList(1, this.size).zip(that.subList(1, that.size))) {
        val temp = listOf<List<Long>>()
        range.clear()
        (d.first until d.second).forEach { i ->
            temp.forEach {
                range.add(it.plus(i))
            }
        }
    }
    return range
}

fun List<Long>.range() = this.rangeFrom(List(this.size) { 0 })

fun List<Long>.max(that: List<Long>) = this.zip(that).map { max(it.first, it.second) }
