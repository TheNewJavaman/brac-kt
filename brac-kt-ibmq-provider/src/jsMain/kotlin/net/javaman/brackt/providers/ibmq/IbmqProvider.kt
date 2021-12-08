@file:OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)

package net.javaman.brackt.providers.ibmq

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import net.javaman.brackt.api.quantum.QuantumCircuit
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.providers.ibmq.api.models.BackendsResponse
import net.javaman.brackt.providers.ibmq.api.models.NetworksResponse
import net.javaman.brackt.providers.ibmq.api.models.NewRequest
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentRequest
import net.javaman.brackt.providers.ibmq.api.models.VersionsRequest
import kotlin.time.Duration

@Suppress("TooManyFunctions")
@JsExport
actual class IbmqProvider {
    private val ibmqProviderImpl: IbmqProviderImpl by injection()

    fun logInAsync(apiToken: String) = GlobalScope.promise { ibmqProviderImpl.logIn(apiToken) }

    fun getApiTokenAsync() = GlobalScope.promise { ibmqProviderImpl.getApiToken() }

    fun selectNetworkAsync() = GlobalScope.promise { ibmqProviderImpl.selectNetwork() }

    @JsName("selectNetworkWithPickerAsync")
    fun selectNetworkAsync(
        picker: (NetworksResponse) -> Unit
    ) = GlobalScope.promise { ibmqProviderImpl.selectNetwork(picker) }

    fun selectDeviceAsync(
        simulator: Boolean = true,
        minQubits: Int = 5
    ) = GlobalScope.promise { ibmqProviderImpl.selectDevice(simulator, minQubits) }

    @JsName("selectDeviceWithPickerAsync")
    fun selectDeviceAsync(
        picker: (BackendsResponse) -> Unit
    ) = GlobalScope.promise { ibmqProviderImpl.selectDevice(picker) }

    fun getJobsAsync() = GlobalScope.promise { ibmqProviderImpl.getJobs() }

    fun getJobsLimitAsync() = GlobalScope.promise { ibmqProviderImpl.getJobsLimit() }

    @JsName("runExperimentWithRequestAsync")
    fun runExperimentAsync(
        request: RunExperimentRequest
    ) = GlobalScope.promise { ibmqProviderImpl.runExperiment(request) }

    fun runExperimentAsync(
        qc: QuantumCircuit
    ) = GlobalScope.promise { ibmqProviderImpl.runExperiment(qc) }

    @JsName("runExperimentAsyncWithShots")
    fun runExperimentAsync(
        qc: QuantumCircuit,
        shots: Int = 1024
    ) = GlobalScope.promise { ibmqProviderImpl.runExperiment(qc, shots) }

    @JsName("runExperimentAsyncWithShotsAndName")
    fun runExperimentAsync(
        qc: QuantumCircuit,
        shots: Int = 1024,
        name: String = ""
    ) = GlobalScope.promise { ibmqProviderImpl.runExperiment(qc, shots, name) }

    fun updateExperimentAsync(
        codeId: String,
        request: VersionsRequest
    ) = GlobalScope.promise { ibmqProviderImpl.updateExperiment(codeId, request) }

    fun getExperimentAsync(codeId: String) = GlobalScope.promise { ibmqProviderImpl.getExperiment(codeId) }

    fun getExperimentsAsync() = GlobalScope.promise { ibmqProviderImpl.getExperiments() }

    fun newExperimentAsync(request: NewRequest) = GlobalScope.promise { ibmqProviderImpl.newExperiment(request) }

    fun getRunAsync(jobId: String) = GlobalScope.promise { ibmqProviderImpl.getRun(jobId) }

    fun getResultDownloadUrlAsync(jobId: String) = GlobalScope.promise { ibmqProviderImpl.getResultDownloadUrl(jobId) }

    fun getResultAsync(url: String) = GlobalScope.promise { ibmqProviderImpl.getResult(url) }

    fun andWaitAsync() = GlobalScope.promise { ibmqProviderImpl.andWait() }

    @JsName("andWaitWithTimeoutAsync")
    fun andWaitAsync(timeoutDuration: Duration) = GlobalScope.promise { ibmqProviderImpl.andWait(timeoutDuration) }

    fun runExperimentAndWaitAsync(
        qc: QuantumCircuit
    ) = GlobalScope.promise { ibmqProviderImpl.runExperimentAndWait(qc) }

    @JsName("runExperimentAndWaitWithTimeoutAsync")
    fun runExperimentAndWaitAsync(
        qc: QuantumCircuit,
        timeoutDuration: Duration
    ) = GlobalScope.promise { ibmqProviderImpl.runExperimentAndWait(qc, timeoutDuration) }

    @JsName("runExperimentAndWaitWithRequestAsync")
    fun runExperimentAndWaitAsync(
        request: RunExperimentRequest
    ) = GlobalScope.promise { ibmqProviderImpl.runExperimentAndWait(request) }

    @JsName("runExperimentAndWaitWithRequestAndTimeoutAsync")
    fun runExperimentAndWaitAsync(
        request: RunExperimentRequest,
        timeoutDuration: Duration
    ) = GlobalScope.promise { ibmqProviderImpl.runExperimentAndWait(request, timeoutDuration) }
}
