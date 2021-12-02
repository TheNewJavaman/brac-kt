package net.javaman.brackt.api.quantum.prebuilt

import net.javaman.brackt.api.quantum.QuantumMacro

val swap = QuantumMacro(numQubits = 2) { qubitMap ->
    cx(qubitMap[0], qubitMap[1])
    cx(qubitMap[1], qubitMap[0])
    cx(qubitMap[0], qubitMap[1])
}
