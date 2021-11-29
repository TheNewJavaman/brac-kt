package net.javaman.brakt.api.quantum

import kotlin.jvm.JvmStatic

/**
 * A bit that holds a binary state
 */
data class Bit(
    val id: Int,
    val state: BitState = BitState.ZERO
)

data class BitState(
    val value: Int
) {
    companion object {
        @JvmStatic
        val ZERO = BitState(0)

        @JvmStatic
        val ONE = BitState(1)
    }
}
