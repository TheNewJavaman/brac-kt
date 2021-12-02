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
        val hPair = QuantumMacro(numQubits = 2) { qubitMap ->
            h(qubitMap[0])
            h(qubitMap[1])
        }
        qc.compose {
            run { hPair onQubits listOf(0, 1) }
            run { hPair onQubits listOf(2, 3) }
        }
    }
}
