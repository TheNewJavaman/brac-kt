@file:Suppress("MagicNumber")

import net.javaman.brackt.api.BracKtApi
import net.javaman.brackt.api.quantum.QuantumCircuit
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.api.util.properties.PropertyManager
import net.javaman.brackt.providers.ibmq.IbmqProvider

object Application {
    init {
        BracKtApi.addInjections()
        IbmqProvider.addInjections()
    }

    private val propertyManager: PropertyManager by injection()
    private val ibmqProvider: IbmqProvider by injection()

    @JvmStatic
    fun main(args: Array<String>) {
        val n = 3
        val qc = QuantumCircuit(name = "Example Superposition", numQubits = 3) {
            repeat(n) { h(qubit = it) }
            repeat(n) { measure(qubit = it, bit = it) }
        }

        val apiToken: String = propertyManager["IBMQ_API_TOKEN"]
        ibmqProvider.logInSync(apiToken)
        ibmqProvider.selectNetworkSync()
        ibmqProvider.selectDeviceSync()
        ibmqProvider.runExperimentAndWaitSync(qc)
    }
}
