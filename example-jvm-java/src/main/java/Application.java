import net.javaman.brackt.api.BracKtApi;
import net.javaman.brackt.api.quantum.QuantumCircuit;
import net.javaman.brackt.providers.ibmq.IbmqProvider;
import net.javaman.brackt.providers.ibmq.IbmqProviderPlatformKt;

public class Application {
    private static final IbmqProvider ibmqProvider = new IbmqProvider();

    static {
        BracKtApi.addInjections();
        IbmqProvider.addInjections();
    }

    public static void main(String[] args) {
        // Construct the QuantumCircuit in Java
        int n = 3;
        QuantumCircuit qc = new QuantumCircuit("Example Superposition", n);
        for (int i = 0; i < n; i++) qc.h(i);
        for (int i = 0; i < n; i++) qc.measure(i, i);

        // Let Kotlin handle the coroutines
        String apiToken = System.getenv("IBMQ_API_TOKEN");
        IbmqProviderPlatformKt.logInSync(ibmqProvider, apiToken);
        IbmqProviderPlatformKt.selectNetworkSync(ibmqProvider);
        IbmqProviderPlatformKt.selectDeviceSync(ibmqProvider);
        IbmqProviderPlatformKt.runExperimentAndWaitSync(ibmqProvider, qc);
    }
}
