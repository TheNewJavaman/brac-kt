package net.javaman.brakt.api.quantum

sealed interface QuantumGate {
    data class Identity(val qubit: Int) : QuantumGate
    data class PauliX(val qubit: Int) : QuantumGate
    data class PauliY(val qubit: Int) : QuantumGate
    data class PauliZ(val qubit: Int) : QuantumGate
    data class Hadamard(val qubit: Int) : QuantumGate
    data class S(val qubit: Int) : QuantumGate
    data class SDagger(val qubit: Int) : QuantumGate
    data class T(val qubit: Int) : QuantumGate
    data class TDagger(val qubit: Int) : QuantumGate
    data class Phase(val qubit: Int, val phi: Double) : QuantumGate
    data class U(val qubit: Int, val theta: Double, val phi: Double, val lambda: Double) : QuantumGate
}
