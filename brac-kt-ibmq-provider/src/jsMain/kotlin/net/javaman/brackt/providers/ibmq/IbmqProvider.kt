@file:OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@file:JsExport

package net.javaman.brackt.providers.ibmq

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import net.javaman.brackt.api.quantum.QuantumCircuit
import net.javaman.brackt.providers.ibmq.api.models.BackendsResponse
import net.javaman.brackt.providers.ibmq.api.models.NetworksResponse
import net.javaman.brackt.providers.ibmq.api.models.NewRequest
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentRequest
import net.javaman.brackt.providers.ibmq.api.models.VersionsRequest
import kotlin.time.Duration

fun IbmqProvider.logInAsync(apiToken: String) = GlobalScope.promise { logIn(apiToken) }

fun IbmqProvider.getApiTokenAsync() = GlobalScope.promise { getApiToken() }

fun IbmqProvider.selectNetworkAsync() = GlobalScope.promise { selectNetwork() }

@JsName("selectNetworkWithPickerAsync")
fun IbmqProvider.selectNetworkAsync(
    picker: IbmqProvider.(NetworksResponse) -> Unit
) = GlobalScope.promise { selectNetwork(picker) }

fun IbmqProvider.selectDeviceAsync(
    simulator: Boolean = true,
    minQubits: Int = 5
) = GlobalScope.promise { selectDevice(simulator, minQubits) }

@JsName("selectDeviceWithPickerAsync")
fun IbmqProvider.selectDeviceAsync(
    picker: IbmqProvider.(BackendsResponse) -> Unit
) = GlobalScope.promise { selectDevice(picker) }

fun IbmqProvider.getJobsAsync() = GlobalScope.promise { getJobs() }

fun IbmqProvider.getJobsLimitAsync() = GlobalScope.promise { getJobsLimit() }

@JsName("runExperimentWithRequestAsync")
fun IbmqProvider.runExperimentAsync(request: RunExperimentRequest) = GlobalScope.promise { runExperiment(request) }

fun IbmqProvider.runExperimentAsync(
    qc: QuantumCircuit,
    shots: Int = 1024,
    name: String = ""
) = GlobalScope.promise { runExperiment(qc, shots, name) }

fun IbmqProvider.updateExperimentAsync(
    codeId: String,
    request: VersionsRequest
) = GlobalScope.promise { updateExperiment(codeId, request) }

fun IbmqProvider.getExperimentAsync(codeId: String) = GlobalScope.promise { getExperiment(codeId) }

fun IbmqProvider.getExperimentsAsync() = GlobalScope.promise { getExperiments() }

fun IbmqProvider.newExperimentAsync(request: NewRequest) = GlobalScope.promise { newExperiment(request) }

fun IbmqProvider.getRunAsync(jobId: String) = GlobalScope.promise { getRun(jobId) }

fun IbmqProvider.getResultDownloadUrlAsync(jobId: String) = GlobalScope.promise { getResultDownloadUrl(jobId) }

fun IbmqProvider.getResultAsync(url: String) = GlobalScope.promise { getResult(url) }

fun IbmqProvider.andWaitAsync(
    timeoutDuration: Duration = IbmqProvider.JOB_TIMEOUT_DURATION
) = GlobalScope.promise { andWait(timeoutDuration) }

@JsName("runExperimentAndWaitWithRequestAsync")
fun IbmqProvider.runExperimentAndWaitAsync(
    request: RunExperimentRequest,
    timeoutDuration: Duration = IbmqProvider.JOB_TIMEOUT_DURATION
) = GlobalScope.promise { runExperimentAndWait(request, timeoutDuration) }

fun IbmqProvider.runExperimentAndWaitAsync(
    qc: QuantumCircuit,
    timeoutDuration: Duration = IbmqProvider.JOB_TIMEOUT_DURATION
) = GlobalScope.promise { runExperimentAndWait(qc, timeoutDuration) }
