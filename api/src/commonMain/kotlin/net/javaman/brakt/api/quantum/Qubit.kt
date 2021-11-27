package net.javaman.brakt.api.quantum

import net.javaman.brakt.api.math.ComplexNumber
import kotlin.math.sqrt

data class Qubit(
    val id: Int,
    val state: QubitState = QubitState.ZERO
)

data class QubitState(
    val zeroAmpl: ComplexNumber,
    val oneAmpl: ComplexNumber
) {
    companion object {
        @JvmStatic
        val ZERO = QubitState(
            ComplexNumber(1.0, 0.0),
            ComplexNumber(0.0, 0.0)
        )

        @JvmStatic
        val ONE = QubitState(
            ComplexNumber(0.0, 0.0),
            ComplexNumber(1.0, 0.0)
        )

        @JvmStatic
        val HADAMARD = QubitState(
            ComplexNumber(sqrt(2.0) / 2.0, 0.0),
            ComplexNumber(sqrt(2.0) / 2.0, 0.0)
        )
    }
}
