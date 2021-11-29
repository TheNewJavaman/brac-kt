package net.javaman.brakt.api.util.assertions

/**
 * Assert conditions pure Kotlin and a clean syntax
 */
fun assert(block: () -> Boolean) {
    if (!block()) throw AssertionException("Assertion failed")
}
