package net.javaman.brakt.api.quantum

import kotlin.test.Test

class QuantumCircuitTest {
    @Test
    fun workspace() {
        val qc = QuantumCircuit(4)
        qc.compose {}
    }
}