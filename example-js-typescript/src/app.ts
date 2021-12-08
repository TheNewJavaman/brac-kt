// Imports
require('@js-joda/core');
const bracKt = require('../../build/js/packages/brac-kt-ibmq-provider/kotlin/brac-kt-ibmq-provider').net.javaman.brackt

// Type definitions
const Logger = bracKt.api.util.logging.Logger;
const QuantumCircuit = bracKt.api.quantum.QuantumCircuit;
const IbmqProvider = bracKt.providers.ibmq.IbmqProvider;

const run = async () => {
    bracKt.api.addInjections();
    bracKt.providers.ibmq.addInjections();

    const n = 3;
    const qc = new QuantumCircuit("Example Superposition", 3);
    for (let i = 0; i < n; i++) qc.h(i);
    for (let i = 0; i < n; i++) qc.measure(i, i);

    const ibmqProvider = new IbmqProvider();
    await ibmqProvider.logInAsync(process.env.IBMQ_API_TOKEN);
    await ibmqProvider.selectNetworkAsync();
    await ibmqProvider.selectDeviceAsync();
    await ibmqProvider.runExperimentAndWaitAsync(qc);
};

run().then(() => {
    const logger = new Logger("app");
    logger.info(() => "Async run complete");
});
