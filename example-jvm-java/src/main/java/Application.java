import net.javaman.brackt.api.BracKtApi;
import net.javaman.brackt.api.quantum.QuantumCircuit;
import net.javaman.brackt.providers.ibmq.IbmqProvider;
import net.javaman.brackt.providers.ibmq.IbmqProviderImpl;

public class Application {
    private static final IbmqProvider ibmqProvider = new IbmqProvider();

    static {
        BracKtApi.addInjections();
        IbmqProviderImpl.addInjections();
    }

    public static void main(String[] args) {
        // Construct the QuantumCircuit in Java
        int n = 3;
        QuantumCircuit qc = new QuantumCircuit("Example Superposition", n);
        for (int i = 0; i < n; i++) qc.h(i);
        for (int i = 0; i < n; i++) qc.measure(i, i);

        // Let Kotlin handle the coroutines
        String apiToken = System.getenv("IBMQ_API_TOKEN");
        ibmqProvider.logInSync(apiToken);
        ibmqProvider.selectNetworkSync();
        ibmqProvider.selectDeviceSync();
        ibmqProvider.runExperimentAndWaitSync(qc);
    }
}
