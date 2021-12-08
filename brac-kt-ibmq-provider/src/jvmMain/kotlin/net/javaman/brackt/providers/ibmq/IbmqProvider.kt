package net.javaman.brackt.providers.ibmq

import kotlinx.coroutines.runBlocking
import net.javaman.brackt.api.quantum.QuantumCircuit
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.providers.ibmq.IbmqProviderImpl.Companion.JOB_TIMEOUT_DURATION
import net.javaman.brackt.providers.ibmq.api.models.BackendsResponse
import net.javaman.brackt.providers.ibmq.api.models.NetworksResponse
import net.javaman.brackt.providers.ibmq.api.models.NewRequest
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentRequest
import net.javaman.brackt.providers.ibmq.api.models.VersionsRequest
import kotlin.time.Duration

@Suppress("TooManyFunctions")
actual class IbmqProvider {
    private val ibmqProviderImpl: IbmqProviderImpl by injection()

    companion object {
        @JvmStatic
        fun addInjections() = IbmqProviderImpl.addInjections()
    }

    fun logInSync(apiToken: String) = runBlocking { ibmqProviderImpl.logIn(apiToken) }

    fun getApiTokenSync() = runBlocking { ibmqProviderImpl.getApiToken() }

    fun selectNetworkSync() = runBlocking { ibmqProviderImpl.selectNetwork() }

    fun selectNetworkSync(
        picker: (NetworksResponse) -> Unit
    ) = runBlocking { ibmqProviderImpl.selectNetwork(picker) }

    @JvmOverloads
    fun selectDeviceSync(
        simulator: Boolean = true,
        minQubits: Int = 5
    ) = runBlocking { ibmqProviderImpl.selectDevice(simulator, minQubits) }

    fun selectDeviceSync(
        picker: (BackendsResponse) -> Unit
    ) = runBlocking { ibmqProviderImpl.selectDevice(picker) }

    fun getJobsSync() = runBlocking { ibmqProviderImpl.getJobs() }

    fun getJobsLimitSync() = runBlocking { ibmqProviderImpl.getJobsLimit() }

    fun runExperimentSync(request: RunExperimentRequest) = runBlocking { ibmqProviderImpl.runExperiment(request) }

    @JvmOverloads
    fun runExperimentSync(
        qc: QuantumCircuit,
        shots: Int = 1024,
        name: String = ""
    ) = runBlocking { ibmqProviderImpl.runExperiment(qc, shots, name) }

    fun updateExperimentSync(
        codeId: String,
        request: VersionsRequest
    ) = runBlocking { ibmqProviderImpl.updateExperiment(codeId, request) }

    fun getExperimentSync(codeId: String) = runBlocking { ibmqProviderImpl.getExperiment(codeId) }

    fun getExperimentsSync() = runBlocking { ibmqProviderImpl.getExperiments() }

    fun newExperimentSync(request: NewRequest) = runBlocking { ibmqProviderImpl.newExperiment(request) }

    fun getRunSync(jobId: String) = runBlocking { ibmqProviderImpl.getRun(jobId) }

    fun getResultDownloadUrlSync(jobId: String) = runBlocking { ibmqProviderImpl.getResultDownloadUrl(jobId) }

    fun getResultSync(url: String) = runBlocking { ibmqProviderImpl.getResult(url) }

    fun andWaitSync(
        timeoutDuration: Duration = JOB_TIMEOUT_DURATION
    ) = runBlocking { ibmqProviderImpl.andWait(timeoutDuration) }

    fun andWaitSync() = runBlocking { ibmqProviderImpl.andWait() }

    fun runExperimentAndWaitSync(
        request: RunExperimentRequest,
        timeoutDuration: Duration = JOB_TIMEOUT_DURATION
    ) = runBlocking { ibmqProviderImpl.runExperimentAndWait(request, timeoutDuration) }

    fun runExperimentAndWaitSync(
        request: RunExperimentRequest
    ) = runBlocking { ibmqProviderImpl.runExperimentAndWait(request) }

    fun runExperimentAndWaitSync(
        qc: QuantumCircuit,
        timeoutDuration: Duration = JOB_TIMEOUT_DURATION
    ) = runBlocking { ibmqProviderImpl.runExperimentAndWait(qc, timeoutDuration) }

    fun runExperimentAndWaitSync(qc: QuantumCircuit) = runBlocking { ibmqProviderImpl.runExperimentAndWait(qc) }
}
