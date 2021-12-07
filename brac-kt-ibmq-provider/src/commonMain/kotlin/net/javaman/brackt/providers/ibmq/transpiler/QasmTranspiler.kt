@file:OptIn(ExperimentalJsExport::class)

package net.javaman.brackt.providers.ibmq.transpiler

import net.javaman.brackt.api.quantum.QuantumCircuit
import net.javaman.brackt.api.quantum.QuantumGate
import net.javaman.brackt.api.quantum.QuantumMacro
import net.javaman.brackt.api.quantum.QubitMap
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.math.max

@Suppress("ComplexMethod") // The conditionals are simple, there are just a lot of them
@JsExport
fun QuantumCircuit.toQasm(): String = (listOf(
    "OPENQASM 2.0",
    "include \"qelib1.inc\"",
    "qreg q[${this.numQubits}]",
    "creg c[${this.numBits}]"
) + circuit.map {
    when (it) {
        is QuantumGate.Identity -> ""
        is QuantumGate.PauliX -> "x q[${it.qubit}]"
        is QuantumGate.PauliY -> "y q[${it.qubit}]"
        is QuantumGate.PauliZ -> "z q[${it.qubit}]"
        is QuantumGate.Hadamard -> "h q[${it.qubit}]"
        is QuantumGate.S -> "s q[${it.qubit}]"
        is QuantumGate.SDagger -> "sdg q[${it.qubit}]"
        is QuantumGate.T -> "t q[${it.qubit}]"
        is QuantumGate.TDagger -> "tdg q[${it.qubit}]"
        is QuantumGate.Phase -> "p(${it.phi}) q[${it.qubit}]"
        is QuantumGate.U -> "u(${it.theta},${it.phi},${it.lambda}) q[${it.qubit}]"
        is QuantumGate.ControlledX -> "cx q[${it.controlQubit}],q[${it.targetQubit}]"
        is QuantumGate.ControlledZ -> QuantumCircuit(numQubits = max(it.qubit1, it.qubit2)) {
            runMacro { QuantumMacro.cz onQubits listOf(it.qubit1, it.qubit2) }
        }.toQasm()
        is QuantumGate.Measure -> "measure q[${it.qubit}] -> c[${it.bit}]"
        else -> throw UnsupportedOperationException("Unsupported gate (${it::class.simpleName})")
    }
}).joinToString(";") + ";"
