import net.javaman.brackt.api.BracKtApi;
import net.javaman.brackt.api.quantum.QuantumCircuit;
import net.javaman.brackt.providers.ibmq.IbmqProvider;

public class Application {
    static {
        BracKtApi.addInjections();
        IbmqProvider.addInjections();
    }

    private static final IbmqProvider ibmqProvider = new IbmqProvider();

    public static void main(String[] args) {
        int n = 3;
        QuantumCircuit qc = new QuantumCircuit("Example Superposition", n);
        for (int i = 0; i < n; i++) qc.h(i);
        for (int i = 0; i < n; i++) qc.measure(i, i);

        String apiToken = System.getenv("IBMQ_API_TOKEN");
        ibmqProvider.logInSync(apiToken);
        ibmqProvider.selectNetworkSync();
        ibmqProvider.selectDeviceSync();
        ibmqProvider.runExperimentAndWaitSync(qc);
    }
}
