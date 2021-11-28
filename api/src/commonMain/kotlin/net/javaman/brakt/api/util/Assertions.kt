@file:Suppress("MatchingDeclarationName") // This file is only for assertion-related code

package net.javaman.brakt.api.util

class AssertionException(message: String) : Exception(message)

infix fun Int.assertLessThan(that: Int) {
    if (this >= that) throw AssertionException("$this >= $that")
}
