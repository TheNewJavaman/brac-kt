import kotlinx.coroutines.runBlocking
import net.javaman.brackt.api.BracKtApi
import net.javaman.brackt.api.quantum.QuantumCircuit
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.api.util.properties.PropertyManager
import net.javaman.brackt.providers.ibmq.IbmqProvider

class BracKtIntegration private constructor() {
    companion object {
        private val propertyManager: PropertyManager by injection()
        private val ibmqProvider: IbmqProvider by injection()

        init {
            BracKtApi.addInjections()
            IbmqProvider.addInjections()
        }

        @JvmStatic
        fun callIbmq(qc: QuantumCircuit): Unit = runBlocking {
            val apiToken: String = propertyManager["IBMQ_API_TOKEN"]
            ibmqProvider.logIn(apiToken)
            ibmqProvider.selectNetwork()
            ibmqProvider.selectDevice()
            ibmqProvider.runExperimentAndWait(qc)
        }
    }
}
