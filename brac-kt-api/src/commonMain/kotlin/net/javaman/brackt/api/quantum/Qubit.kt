package net.javaman.brackt.api.quantum

import net.javaman.brackt.api.math.ComplexNumber
import kotlin.jvm.JvmStatic
import kotlin.math.sqrt

/**
 * A qubit that holds a quantum state with zero and one amplitudes
 */
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
