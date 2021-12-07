@file:OptIn(ExperimentalJsExport::class)

package net.javaman.brackt.api.quantum

import net.javaman.brackt.api.util.assertions.assert
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.jvm.JvmStatic

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
 *     runMacro { hPair onQubits listOf(0, 1) }
 *     runMacro { hPair onQubits listOf(2, 3) }
 * }
 * ```
 */
@JsExport
class QuantumMacro(val numQubits: Int, val macro: QuantumCircuit.(qubitMap: QubitMap) -> Unit) {
    var safeMacro: QuantumCircuit.(qubitMap: QubitMap) -> Unit

    companion object {
        @JvmStatic
        val swap = QuantumMacro(numQubits = 2) { qubitMap ->
            cx(qubitMap[0], qubitMap[1])
            cx(qubitMap[1], qubitMap[0])
            cx(qubitMap[0], qubitMap[1])
        }

        @JvmStatic
        val cz = QuantumMacro(numQubits = 2) { qubitMap ->
            h(qubitMap[0])
            cx(qubitMap[1], qubitMap[0])
            h(qubitMap[0])
        }
    }

    init {
        val thisMacro = this
        safeMacro = { qubitMap ->
            assert { thisMacro.numQubits == qubitMap.size }
            macro(qubitMap)
        }
    }

    operator fun invoke(qc: QuantumCircuit, qubitMap: QubitMap) {
        safeMacro(qc, qubitMap)
    }

    infix fun onQubits(qubitMap: QubitMap) = Pair(this, qubitMap)
}

typealias QubitMap = List<Int>
