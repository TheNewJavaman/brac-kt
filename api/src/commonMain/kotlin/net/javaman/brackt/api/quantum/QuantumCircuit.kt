package net.javaman.brackt.api.quantum

import net.javaman.brackt.api.util.assertions.assert
import kotlin.math.PI

/**
 * A named set of registers that hold [Qubit] and [Bit] lists
 *
 * To randomize three qubits:
 * ```kotlin
 * val qc = QuantumCircuit(name = "Basic Circuit", numQubits = 3, numBits = 3)
 * qc.compose {
 *     h(0)
 *     h(1)
 *     h(2)
 * }
 * ```
 *
 * Or use Kotlin's built-in functions:
 * ```kotlin
 * val qc = QuantumCircuit(name = "Basic Circuit", numQubits = 3, numBits = 3)
 * qc.compose {
 *     repeat(3) { h(it) }
 * }
 * ```
 */
@Suppress("TooManyFunctions") // Needs all QuantumGates
class QuantumCircuit constructor(
    val name: String? = null,
    val numQubits: Int,
    val numBits: Int = numQubits,
    //private val qubits: List<Qubit> = List(numQubits) { Qubit(it) },
    //private val bits: List<Bit> = List(numBits) { Bit(it) },
    private val circuit: MutableList<QuantumGate> = mutableListOf()
) {
    fun compose(block: QuantumCircuit.() -> Unit) = block()

    fun run(block: () -> Pair<QuantumMacro, QubitMap>) {
        val (macro, qubitMap) = block()
        macro(this, qubitMap)
    }

    fun i(qubit: Int) {
        assert { qubit < numQubits }
        circuit.add(QuantumGate.Identity(qubit))
    }

    fun x(qubit: Int) {
        assert { qubit < numQubits }
        circuit.add(QuantumGate.PauliX(qubit))
    }

    fun y(qubit: Int) {
        assert { qubit < numQubits }
        circuit.add(QuantumGate.PauliY(qubit))
    }

    fun z(qubit: Int) {
        assert { qubit < numQubits }
        circuit.add(QuantumGate.PauliZ(qubit))
    }

    fun h(qubit: Int) {
        assert { qubit < numQubits }
        circuit.add(QuantumGate.Hadamard(qubit))
    }

    fun s(qubit: Int) {
        assert { qubit < numQubits }
        circuit.add(QuantumGate.S(qubit))
    }

    fun sdg(qubit: Int) {
        assert { qubit < numQubits }
        circuit.add(QuantumGate.SDagger(qubit))
    }

    fun t(qubit: Int) {
        assert { qubit < numQubits }
        circuit.add(QuantumGate.T(qubit))
    }

    fun tdg(qubit: Int) {
        assert { qubit < numQubits }
        circuit.add(QuantumGate.TDagger(qubit))
    }

    fun p(qubit: Int, phi: Double) {
        assert {
            qubit < numQubits &&
                    -PI <= phi && phi <= PI
        }
        circuit.add(QuantumGate.Phase(qubit, phi))
    }

    fun u(qubit: Int, theta: Double, phi: Double, lambda: Double) {
        assert {
            qubit < numQubits &&
                    -PI <= theta && theta <= PI &&
                    -PI <= phi && phi <= PI &&
                    -PI <= lambda && lambda <= PI
        }
        circuit.add(QuantumGate.U(qubit, theta, phi, lambda))
    }

    fun cx(controlQubit: Int, targetQubit: Int) {
        assert {
            controlQubit < numQubits &&
                    targetQubit < numQubits &&
                    controlQubit != targetQubit
        }
        circuit.add(QuantumGate.ControlledX(controlQubit, targetQubit))
    }

    fun cz(qubit1: Int, qubit2: Int) {
        assert {
            qubit1 < numQubits &&
                    qubit2 < numQubits &&
                    qubit1 != qubit2
        }
        circuit.add(QuantumGate.ControlledZ(qubit1, qubit2))
    }

    fun measure(qubit: Int, bit: Int) {
        assert {
            qubit < numQubits &&
                    bit < numBits
        }
        circuit.add(QuantumGate.Measure(qubit, bit))
    }
}
