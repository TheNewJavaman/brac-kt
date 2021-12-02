package net.javaman.brackt.api.util.assertions

/**
 * Assert conditions in pure Kotlin and a clean syntax
 */
fun assert(block: () -> Boolean) {
    if (!block()) throw AssertionException("Assertion failed")
}

class AssertionException(message: String) : Exception(message)
