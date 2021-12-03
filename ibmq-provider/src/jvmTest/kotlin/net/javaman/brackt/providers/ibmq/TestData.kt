package net.javaman.brackt.providers.ibmq

import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.providers.ibmq.api.models.CodeModel
import net.javaman.brackt.providers.ibmq.api.models.ExperimentBackendRequest
import net.javaman.brackt.providers.ibmq.api.models.ExperimentQasmRequest
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentRequest

object TestData {
    private val ibmqProvider: IbmqProvider by injection()

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
        CodeModel(
            qasm = QASM,
            userId = ibmqProvider.userId
        )
    }

    @JvmStatic
    val RUN_EXPERIMENT_REQUEST by lazy {
        RunExperimentRequest(
            backend = ExperimentBackendRequest(
                name = ibmqProvider.device.name
            ),
            codeId = ibmqProvider.code.idCode!!,
            qasms = listOf(
                ExperimentQasmRequest(
                    qasm = QASM
                )
            )
        )
    }
}
