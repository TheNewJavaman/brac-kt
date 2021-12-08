@file:Suppress("MagicNumber")

import kotlinx.coroutines.runBlocking
import net.javaman.brackt.api.BracKtApi
import net.javaman.brackt.api.quantum.QuantumCircuit
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.api.util.properties.PropertyManager
import net.javaman.brackt.providers.ibmq.IbmqProviderImpl

object Application {
    // Dependencies are managed by InjectionManager
    private val propertyManager: PropertyManager by injection()
    private val ibmqProvider: IbmqProviderImpl by injection()

    init {
        BracKtApi.addInjections()
        IbmqProviderImpl.addInjections()
    }

    @JvmStatic
    fun main(args: Array<String>): Unit = runBlocking { // runBlocking uses Kotlin's (speedy) coroutines
        // Create a basic quantum circuit: three qubits in superposition, then measured
        val n = 3
        val qc = QuantumCircuit(name = "Example Superposition", numQubits = 3) {
            repeat(n) { h(qubit = it) }
            repeat(n) { measure(qubit = it, bit = it) }
        }

        // Run the circuit on an IBM Quantum device
        // By default, brac-kt will choose a simulator with 5 or more qubits with the shortest queue
        // Add your IBM API token as environment variable IBMQ_API_TOKEN
        val apiToken: String = propertyManager["IBMQ_API_TOKEN"]
        ibmqProvider.logIn(apiToken)
        ibmqProvider.selectNetwork()
        ibmqProvider.selectDevice()
        ibmqProvider.runExperimentAndWait(qc)
    }
}
