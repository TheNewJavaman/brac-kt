package net.javaman.brackt.providers.ibmq

import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.api.util.properties.PropertyManager
import net.javaman.brackt.providers.ibmq.api.models.ExperimentBackendRequest
import net.javaman.brackt.providers.ibmq.api.models.ExperimentQasmRequest
import net.javaman.brackt.providers.ibmq.api.models.LogInWithTokenRequest
import net.javaman.brackt.providers.ibmq.api.models.QasmVersion
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentRequest
import net.javaman.brackt.providers.ibmq.api.models.VersionsRequest

object TestData {
    private val propertyManager: PropertyManager by injection()

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
        h q[0];
        h q[1];
        h q[2];
        measure q -> c;
        """.trimIndent()

    @JvmStatic
    val LOG_IN_WITH_TOKEN_REQUEST = LogInWithTokenRequest(
        apiToken = propertyManager.getProperty("IBMQ_API_TOKEN")
    )

    @JvmStatic
    val EXPERIMENT_QASM_REQUEST = ExperimentQasmRequest(
        qasm = QASM
    )

    @JvmStatic
    val EXPERIMENT_BACKEND_REQUEST = ExperimentBackendRequest(
        name = DEVICE
    )

    @JvmStatic
    val RUN_EXPERIMENT_REQUEST = RunExperimentRequest(
        qasms = listOf(EXPERIMENT_QASM_REQUEST),
        backend = EXPERIMENT_BACKEND_REQUEST,
        codeId = propertyManager.getProperty("IBMQ_CODE_ID")
    )

    @JvmStatic
    val VERSIONS_REQUEST = VersionsRequest(
        idCode = propertyManager.getProperty("IBMQ_ID_CODE"),
        name = "Untitled circuit",
        qasm = QASM,
        codeType = QasmVersion.QASM2,
        userId = "KNOWN AT RUNTIME"
    )
}
