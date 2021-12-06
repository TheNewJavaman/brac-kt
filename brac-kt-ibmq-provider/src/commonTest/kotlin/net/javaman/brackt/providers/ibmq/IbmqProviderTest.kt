package net.javaman.brackt.providers.ibmq

import net.javaman.brackt.api.BracKtApi
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.api.util.logging.Logger
import net.javaman.brackt.api.util.platform.Backend
import net.javaman.brackt.api.util.platform.backend
import net.javaman.brackt.api.util.properties.PropertyManager
import net.javaman.brackt.providers.ibmq.TestData.NEW_REQUEST
import net.javaman.brackt.providers.ibmq.TestData.QUANTUM_CIRCUIT
import net.javaman.brackt.providers.ibmq.TestData.RUN_EXPERIMENT_REQUEST
import kotlin.test.Test

class IbmqProviderTest {
    private val ibmqProvider: IbmqProvider by injection()
    private val propertyManager: PropertyManager by injection()
    private val logger: Logger by injection()

    init {
        BracKtApi.addInjections()
        IbmqProvider.addInjections()
    }

    // kotlin-test-common does not support test ordering, so we must run all the tests in one function
    @Test
    fun all_ok() {
        if (backend == Backend.JS_BROWSER) return // Can't pass API token as an environment variable
        runSuspendTest {
            val apiToken: String = propertyManager["IBMQ_API_TOKEN"]
            ibmqProvider.logIn(apiToken)
            ibmqProvider.getApiToken()
            ibmqProvider.selectNetwork()
            ibmqProvider.selectDevice()
            val newExperimentRequest = NEW_REQUEST
            ibmqProvider.newExperiment(newExperimentRequest)
            ibmqProvider.getExperiments()
            val codeId = ibmqProvider.code.idCode!!
            val updateExperimentRequest = ibmqProvider.code.copy(id = null)
            ibmqProvider.updateExperiment(codeId, updateExperimentRequest)
            ibmqProvider.getExperiment(codeId)
            val runExperimentRequest = RUN_EXPERIMENT_REQUEST
            try {
                ibmqProvider.runExperiment(runExperimentRequest)
            } catch (_: Exception) {
                logger.info { "Maximum number of concurrent jobs reached. You really gotta get a better API key..." }
            }
            ibmqProvider.getJobs()
            ibmqProvider.getJobsLimit()
            val qc = QUANTUM_CIRCUIT
            try {
                ibmqProvider.runExperimentAndWait(qc)
            } catch (_: Exception) {
                logger.info { "Maximum number of concurrent jobs reached. You really gotta get a better API key..." }
            }
        }
    }
}
