package net.javaman.brackt.api.quantum

import net.javaman.brakt.api.util.assertions.assert
import kotlin.math.PI

/**
 * A named set of registers that hold [Qubit] and [Bit] lists
 */
@Suppress("TooManyFunctions") // Needs all QuantumGates
class QuantumCircuit constructor(
    val name: String? = null,
    val numQubits: Int = 5,
    val numBits: Int = numQubits,
    //private val qubits: List<Qubit> = List(numQubits) { Qubit(it) },
    //private val bits: List<Bit> = List(numBits) { Bit(it) },
    private val circuit: MutableList<QuantumGate> = mutableListOf()
) {
    fun compose(block: QuantumCircuit.() -> Unit) = block()

    fun i(qubit: Int) {
        assert { qubit <= numQubits }
        circuit.add(QuantumGate.Identity(qubit))
    }

    fun x(qubit: Int) {
        assert { qubit <= numQubits }
        circuit.add(QuantumGate.PauliX(qubit))
    }

    fun y(qubit: Int) {
        assert { qubit <= numQubits }
        circuit.add(QuantumGate.PauliY(qubit))
    }

    fun z(qubit: Int) {
        assert { qubit <= numQubits }
        circuit.add(QuantumGate.PauliZ(qubit))
    }

    fun h(qubit: Int) {
        assert { qubit <= numQubits }
        circuit.add(QuantumGate.Hadamard(qubit))
    }

    fun s(qubit: Int) {
        assert { qubit <= numQubits }
        circuit.add(QuantumGate.S(qubit))
    }

    fun sdg(qubit: Int) {
        assert { qubit <= numQubits }
        circuit.add(QuantumGate.SDagger(qubit))
    }

    fun t(qubit: Int) {
        assert { qubit <= numQubits }
        circuit.add(QuantumGate.T(qubit))
    }

    fun tdg(qubit: Int) {
        assert { qubit <= numQubits }
        circuit.add(QuantumGate.TDagger(qubit))
    }

    fun p(qubit: Int, phi: Double) {
        assert {
            qubit <= numQubits &&
                    -PI <= phi && phi <= PI
        }
        circuit.add(QuantumGate.Phase(qubit, phi))
    }

    fun u(qubit: Int, theta: Double, phi: Double, lambda: Double) {
        assert {
            qubit <= numQubits &&
                    -PI <= theta && theta <= PI &&
                    -PI <= phi && phi <= PI &&
                    -PI <= lambda && lambda <= PI
        }
        circuit.add(QuantumGate.U(qubit, theta, phi, lambda))
    }
}
