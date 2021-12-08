#include <cstdlib>
// Linux:
//#include "../../brac-kt-ibmq-provider/build/bin/linuxX64/debugShared/brac_kt_ibmq_provider_api.h"
// Windows:
#include "../../brac-kt-ibmq-provider/build/bin/mingwX64/debugShared/brac_kt_ibmq_provider_api.h"

#define api kotlin.root.net.javaman.brackt.api
#define ibmq_provider kotlin.root.net.javaman.brackt.providers.ibmq
using BracKt = brac_kt_ibmq_provider_ExportedSymbols;
using QuantumCircuit = brac_kt_ibmq_provider_kref_net_javaman_brackt_api_quantum_QuantumCircuit;
using IbmqProvider = brac_kt_ibmq_provider_kref_net_javaman_brackt_providers_ibmq_IbmqProvider;

int main(int argc, char **argv) {
    BracKt *bracKt = brac_kt_ibmq_provider_symbols();
    bracKt->api.addInjections();
    bracKt->ibmq_provider.addInjections();

    int n = 3;
    QuantumCircuit qc = bracKt->api.quantum.QuantumCircuit.QuantumCircuit_("Example Superposition", n, n);
    for (int i = 0; i < n; i++) bracKt->api.quantum.QuantumCircuit.h(qc, i);
    for (int i = 0; i < n; i++) bracKt->api.quantum.QuantumCircuit.measure(qc, i, i);

    IbmqProvider ibmqProvider = bracKt->ibmq_provider.IbmqProvider.IbmqProvider();
    bracKt->ibmq_provider.IbmqProvider.logInSync(ibmqProvider, getenv("IBMQ_API_TOKEN"));
    bracKt->ibmq_provider.IbmqProvider.selectNetworkSync(ibmqProvider);
    bracKt->ibmq_provider.IbmqProvider.selectDeviceSync_(ibmqProvider, true, n);
    bracKt->ibmq_provider.IbmqProvider.runExperimentAndWaitSync(ibmqProvider, qc);
}
