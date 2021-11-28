package net.javaman.brakt.api.quantum

import net.javaman.brakt.api.util.assertLessThan

class QuantumCircuit constructor(
    val name: String? = null,
    val numQubits: Int = 5,
    val numBits: Int = numQubits,
    private val qubits: List<Qubit> = List(numQubits) { Qubit(it) },
    private val bits: List<Bit> = List(numBits) { Bit(it) },
    private val circuit: MutableList<QuantumOperation> = mutableListOf()
) {
    inline fun compose(block: QuantumCircuit.() -> Unit) = block()

    fun h(qubit: Int) {
        qubit assertLessThan numQubits
        circuit.add(QuantumOperation(QuantumGate.HADAMARD, listOf(qubit), emptyList()))
    }
}
