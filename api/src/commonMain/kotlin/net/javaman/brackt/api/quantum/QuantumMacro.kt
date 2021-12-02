package net.javaman.brackt.api.quantum

import net.javaman.brackt.api.util.assertions.assert

/**
 * Allows creating reusable pieces of [QuantumCircuit]s.
 *
 * To randomize qubits in pairs:
 * ```kotlin
 * val qc = QuantumCircuit(numQubits = 4)
 * val hPair = QuantumMacro(numQubits = 2) { qubitMap ->
 *     h(qubitMap[0])
 *     h(qubitMap[1])
 * }
 * qc.compose {
 *     run { hPair onQubits listOf(0, 1) }
 *     run { hPair onQubits listOf(2, 3) }
 * }
 * ```
 */
class QuantumMacro private constructor(val block: QuantumCircuit.(qubitMap: QubitMap) -> Unit) {
    constructor(numQubits: Int, macro: QuantumCircuit.(qubitMap: QubitMap) -> Unit) : this({ qubitMap ->
        assert { numQubits == qubitMap.size }
        macro(qubitMap)
    })

    operator fun invoke(qc: QuantumCircuit, qubitMap: QubitMap) {
        block(qc, qubitMap)
    }

    infix fun onQubits(qubitMap: QubitMap) = Pair(this, qubitMap)
}

typealias QubitMap = List<Int>
