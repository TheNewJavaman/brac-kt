@file:OptIn(ExperimentalJsExport::class)

package net.javaman.brackt.api.util.assertions

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * Assert conditions in pure Kotlin and a clean syntax
 */
@JsExport
fun assert(block: () -> Boolean) {
    if (!block()) throw AssertionException("Assertion failed")
}

@JsExport
class AssertionException(message: String) : Exception(message)
