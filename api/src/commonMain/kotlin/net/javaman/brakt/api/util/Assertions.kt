package net.javaman.brakt.api.util

fun assert(block: () -> Boolean) {
    if (!block()) throw AssertionException("Assertion failed")
}

class AssertionException(message: String) : Exception(message)
