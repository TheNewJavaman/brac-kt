@file:OptIn(ExperimentalJsExport::class)

package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
@Serializable
data class ResultResponse(
    @SerialName("backend_name")
    val backendName: String,
    @SerialName("backend_version")
    val backendVersion: String,
    val date: String, // An Instant with 5 decimals under the second
    val header: ResultHeader,
    @SerialName("job_id")
    val jobId: String,
    val metadata: ResultMetadata,
    @SerialName("qobj_id")
    val qobjId: String,
    val results: List<ResultResult>,
    val status: String,
    val success: Boolean,
    @SerialName("time_taken")
    val timeTaken: Double
)

@JsExport
@Serializable
data class ResultHeader(
    @SerialName("backend_name")
    val backendName: String,
    @SerialName("backend_version")
    val backendVersion: String,
)

@JsExport
@Serializable
data class ResultMetadata(
    @SerialName("max_gpu_memory_mb")
    val maxGpuMemoryMb: Int,
    @SerialName("max_memory_mb")
    val maxMemoryMb: Int,
    @SerialName("mpi_rank")
    val mpiRank: Int,
    @SerialName("num_mpi_processes")
    val numMpiProcesses: Int,
    @SerialName("omp_enabled")
    val ompEnabled: Boolean,
    @SerialName("parallel_experiments")
    val parallelExperiments: Int,
    @SerialName("time_taken")
    val timeTaken: Double
)

@JsExport
@Serializable
data class ResultResult(
    val data: ResultResultData,
    val header: ResultResultHeader,
    val metadata: ResultResultMetadata,
    @SerialName("seed_simulator")
    val seedSimulator: Long,
    val shots: Int,
    val status: String,
    val success: Boolean,
    @SerialName("time_taken")
    val timeTaken: Double
)

@JsExport
@Serializable
data class ResultResultData(
    val counts: Map<String, Int>
) {
    fun pretty(): String {
        val total = counts.values.sum()
        return "Qubit outcomes:\n" + counts.map {
            "    ${it.key}: ${it.value.toFloat() / total} (${it.value}/$total)"
        }.joinToString("\n")
    }
}

@JsExport
@Serializable
data class ResultResultHeader(
    @SerialName("clbit_labels")
    val clbitLabels: List<JsonArray>,
    @SerialName("creg_sizes")
    val cregSizes: List<JsonArray>,
    @SerialName("global_phase")
    val globalPhase: Double,
    @SerialName("memory_slots")
    val memorySlots: Int,
    val metadata: Map<String, String>,
    @SerialName("n_qubits")
    val nQubits: Int,
    val name: String? = null,
    @SerialName("qreg_sizes")
    val qregSizes: List<JsonArray>,
    @SerialName("qubit_labels")
    val qubitLabels: List<JsonArray>
)

@JsExport
@Serializable
data class ResultResultMetadata(
    @SerialName("active_input_qubits")
    val activeInputQubits: List<Int>,
    val device: String,
    val fusion: ResultResultFusion,
    @SerialName("input_qubit_map")
    val inputQubitMap: List<List<Int>>,
    @SerialName("measure_sampling")
    val measureSampling: Boolean,
    val method: String,
    val noise: String,
    @SerialName("num_clbits")
    val numClbits: Int,
    @SerialName("num_qubits")
    val numQubits: Int,
    @SerialName("parallel_shots")
    val parallelShots: Int,
    @SerialName("parallel_state_update")
    val parallelStateUpdate: Int,
    @SerialName("remapped_qubits")
    val remappedQubits: Boolean
)

@JsExport
@Serializable
data class ResultResultFusion(
    val enabled: Boolean
)
