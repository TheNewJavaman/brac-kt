package net.javaman.brackt.api.quantum

import net.javaman.brackt.api.util.assertions.AssertionException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class QuantumCircuitTest {
    @Test
    fun compose_ok() {
        val qc = QuantumCircuit(name = "Basic Circuit", numQubits = 3, numBits = 3)
        qc.compose {
            h(0)
            h(1)
        }
    }

    @Test
    fun compose_qubitOutOfBounds() {
        val qc = QuantumCircuit(numQubits = 1)
        assertThrows<AssertionException> {
            qc.compose {
                h(1)
            }
        }
    }

    @Test
    fun compose_withMacro() {
        val qc = QuantumCircuit(numQubits = 4)
        val hPairMacro = QuantumMacro(numQubits = 2) { qubitMap ->
            h(qubitMap[0])
            h(qubitMap[1])
        }
        qc.compose {
            runMacro { hPairMacro onQubits listOf(0, 1) }
            runMacro { hPairMacro onQubits listOf(2, 3) }
        }
    }

    @Test
    fun compose_threeQubitSuperposition() {
        val n = 3
        val qc = QuantumCircuit(name = "Superposition (N=$n)", numQubits = n)
        qc.compose {
            repeat(n) { h(qubit = it) }
            repeat(n) { measure(qubit = it, bit = it) }
        }
    }

    @Test
    fun compose_doubleSwap() {
        val n = 2
        val qc = QuantumCircuit(name = "Swap (N=$n)", numQubits = n)
        val swapMacro = QuantumMacro(numQubits = 2) { qubitMap ->
            cx(qubitMap[0], qubitMap[1])
            cx(qubitMap[1], qubitMap[0])
            cx(qubitMap[0], qubitMap[1])
        }
        qc.compose {
            runMacro { swapMacro onQubits listOf(0, 1) }
            runMacro { swapMacro onQubits listOf(0, 1) }
            repeat(n) { measure(qubit = it, bit = it) }
        }
    }

    @Test
    fun compose_grover() {
        val n = 2
        val qc = QuantumCircuit(name = "Grover's Algorithm (NSolutions=4, Winner=4)", numQubits = n)
        qc.compose {
            // Initialize all qubits in superposition
            repeat(n) { h(it) }
            cz(0, 1)
            repeat(n) { h(it) }
            repeat(n) { z(it) }
            cz(0, 1)
            repeat(n) { h(it) }
        }
    }
}
