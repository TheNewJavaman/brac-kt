package net.javaman.brakt.api.quantum

data class QuantumOperation(
    val gate: QuantumGate,
    val qubits: List<Int>,
    val bits: List<Int>
) {
    override fun toString() = gate.toString(qubits, bits)
}

enum class QuantumGate {
    HADAMARD {
        override fun toString(qubits: List<Int>, bits: List<Int>) = "h(${qubits[0]})"
    };

    abstract fun toString(qubits: List<Int>, bits: List<Int>): String
}
