package net.javaman.brackt.providers.ibmq

import net.javaman.brackt.api.quantum.QuantumCircuit
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.providers.ibmq.api.models.Code
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentBackend
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentQasm
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentRequest
import kotlin.jvm.JvmStatic

object TestData {
    private val ibmqProvider: IbmqProviderImpl by injection()

    const val NETWORK = "ibm-q"
    const val GROUP = "open"
    const val PROJECT = "main"
    const val DEVICE = "ibmq_manila"

    @JvmStatic
    val QASM = """
        OPENQASM 2.0;
        include "qelib1.inc";
        qreg q[3];
        creg c[3];
        h q;
        measure q -> c;
        """.trimIndent()

    @JvmStatic
    val NEW_REQUEST by lazy {
        Code(
            qasm = QASM,
            userId = ibmqProvider.userId
        )
    }

    @JvmStatic
    val RUN_EXPERIMENT_REQUEST by lazy {
        RunExperimentRequest(
            backend = RunExperimentBackend(
                name = ibmqProvider.device.name
            ),
            codeId = ibmqProvider.code.idCode!!,
            qasms = listOf(
                RunExperimentQasm(
                    qasm = QASM
                )
            )
        )
    }

    @JvmStatic
    val QUANTUM_CIRCUIT = QuantumCircuit(numQubits = 3) {
        repeat(3) { h(it) }
        repeat(3) { measure(it, it) }
    }
}
