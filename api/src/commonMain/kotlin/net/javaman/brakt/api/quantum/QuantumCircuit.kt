package net.javaman.brakt.api.quantum

data class QuantumCircuit(
    val numQubits: Int = 1,
    val numBits: Int = numQubits,
    private val qubits: List<Qubit> = List(numQubits) { Qubit(it) },
    private val bits: List<Bit> = List(numBits) { Bit(it) },
    private val circuit: List<QuantumOperation> = emptyList()
) {
    inline fun compose(block: QuantumCircuit.() -> Unit) = block()
}

interface QuantumOperation
