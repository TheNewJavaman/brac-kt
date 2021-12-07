require('@js-joda/core');
const bracKtApi = require('@thenewjavaman/brac-kt-api').net.javaman.brackt.api;
const bracKtIbmqProvider = require('@thenewjavaman/brac-kt-ibmq-provider').net.javaman.brackt.providers.ibmq;

const n = 3;
const qc = new bracKtApi.quantum.QuantumCircuit("Example Superposition", 3);
for (let i = 0; i < n; i++) qc.h(i);
for (let i = 0; i < n; i++) qc.measure(i, i);

console.log(qc);
