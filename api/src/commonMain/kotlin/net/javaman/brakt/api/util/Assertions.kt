package net.javaman.brakt.api.util

import kotlin.jvm.JvmStatic

@JvmStatic
inline fun assert(block: () -> Boolean) {
    if (block()) throw AssertionException("Assertion failed")
}

class AssertionException(message: String) : Exception(message)
