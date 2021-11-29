package net.javaman.brakt.api.quantum

import net.javaman.brakt.api.util.assertions.AssertionException
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
}
