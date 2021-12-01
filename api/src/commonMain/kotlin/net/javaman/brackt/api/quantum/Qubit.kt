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
    val zeroAmpl: net.javaman.brackt.api.math.ComplexNumber,
    val oneAmpl: net.javaman.brackt.api.math.ComplexNumber
) {
    companion object {
        @JvmStatic
        val ZERO = QubitState(
            net.javaman.brackt.api.math.ComplexNumber(1.0, 0.0),
            net.javaman.brackt.api.math.ComplexNumber(0.0, 0.0)
        )

        @JvmStatic
        val ONE = QubitState(
            net.javaman.brackt.api.math.ComplexNumber(0.0, 0.0),
            net.javaman.brackt.api.math.ComplexNumber(1.0, 0.0)
        )

        @JvmStatic
        val HADAMARD = QubitState(
            net.javaman.brackt.api.math.ComplexNumber(sqrt(2.0) / 2.0, 0.0),
            net.javaman.brackt.api.math.ComplexNumber(sqrt(2.0) / 2.0, 0.0)
        )
    }
}
