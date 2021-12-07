package net.javaman.brackt.api.quantum

/**
 * All necessary quantum operations are derived from this class
 *
 * Custom providers can implement this interface and write extensions for [QuantumCircuit]
 */
interface QuantumGate {
    class Identity(val qubit: Int) : QuantumGate
    class PauliX(val qubit: Int) : QuantumGate
    class PauliY(val qubit: Int) : QuantumGate
    class PauliZ(val qubit: Int) : QuantumGate
    class Hadamard(val qubit: Int) : QuantumGate
    class S(val qubit: Int) : QuantumGate
    class SDagger(val qubit: Int) : QuantumGate
    class T(val qubit: Int) : QuantumGate
    class TDagger(val qubit: Int) : QuantumGate
    class Phase(val qubit: Int, val phi: Double) : QuantumGate
    class U(val qubit: Int, val theta: Double, val phi: Double, val lambda: Double) : QuantumGate
    class ControlledX(val controlQubit: Int, val targetQubit: Int) : QuantumGate
    class ControlledZ(val qubit1: Int, val qubit2: Int) : QuantumGate
    class Measure(val qubit: Int, val bit: Int) : QuantumGate
}
