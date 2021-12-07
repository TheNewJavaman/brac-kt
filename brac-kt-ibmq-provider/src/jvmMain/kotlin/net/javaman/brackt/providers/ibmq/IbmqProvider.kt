package net.javaman.brackt.providers.ibmq

import kotlinx.coroutines.runBlocking
import net.javaman.brackt.api.quantum.QuantumCircuit
import net.javaman.brackt.providers.ibmq.api.models.BackendsResponse
import net.javaman.brackt.providers.ibmq.api.models.NetworksResponse
import net.javaman.brackt.providers.ibmq.api.models.NewRequest
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentRequest
import net.javaman.brackt.providers.ibmq.api.models.VersionsRequest
import kotlin.time.Duration

fun IbmqProvider.logInSync(apiToken: String) = runBlocking { logIn(apiToken) }

fun IbmqProvider.getApiTokenSync() = runBlocking { getApiToken() }

fun IbmqProvider.selectNetworkSync() = runBlocking { selectNetwork() }

fun IbmqProvider.selectNetworkSync(
    picker: IbmqProvider.(NetworksResponse) -> Unit
) = runBlocking { selectNetwork(picker) }

@JvmOverloads
fun IbmqProvider.selectDeviceSync(
    simulator: Boolean = true,
    minQubits: Int = 5
) = runBlocking { selectDevice(simulator, minQubits) }

fun IbmqProvider.selectDeviceSync(
    picker: IbmqProvider.(BackendsResponse) -> Unit
) = runBlocking { selectDevice(picker) }

fun IbmqProvider.getJobsSync() = runBlocking { getJobs() }

fun IbmqProvider.getJobsLimitSync() = runBlocking { getJobsLimit() }

fun IbmqProvider.runExperimentSync(request: RunExperimentRequest) = runBlocking { runExperiment(request) }

@JvmOverloads
fun IbmqProvider.runExperimentSync(
    qc: QuantumCircuit,
    shots: Int = 1024,
    name: String = ""
) = runBlocking { runExperiment(qc, shots, name) }

fun IbmqProvider.updateExperimentSync(
    codeId: String,
    request: VersionsRequest
) = runBlocking { updateExperiment(codeId, request) }

fun IbmqProvider.getExperimentSync(codeId: String) = runBlocking { getExperiment(codeId) }

fun IbmqProvider.getExperimentsSync() = runBlocking { getExperiments() }

fun IbmqProvider.newExperimentSync(request: NewRequest) = runBlocking { newExperiment(request) }

fun IbmqProvider.getRunSync(jobId: String) = runBlocking { getRun(jobId) }

fun IbmqProvider.getResultDownloadUrlSync(jobId: String) = runBlocking { getResultDownloadUrl(jobId) }

fun IbmqProvider.getResultSync(url: String) = runBlocking { getResult(url) }

fun IbmqProvider.andWaitSync(
    timeoutDuration: Duration = IbmqProvider.JOB_TIMEOUT_DURATION
) = runBlocking { andWait(timeoutDuration) }

fun IbmqProvider.andWaitSync() = runBlocking { andWait() }

fun IbmqProvider.runExperimentAndWaitSync(
    request: RunExperimentRequest,
    timeoutDuration: Duration = IbmqProvider.JOB_TIMEOUT_DURATION
) = runBlocking { runExperimentAndWait(request, timeoutDuration) }

fun IbmqProvider.runExperimentAndWaitSync(request: RunExperimentRequest) = runBlocking { runExperimentAndWait(request) }

fun IbmqProvider.runExperimentAndWaitSync(
    qc: QuantumCircuit,
    timeoutDuration: Duration = IbmqProvider.JOB_TIMEOUT_DURATION
) = runBlocking { runExperimentAndWait(qc, timeoutDuration) }

fun IbmqProvider.runExperimentAndWaitSync(qc: QuantumCircuit) = runBlocking { runExperimentAndWait(qc) }
