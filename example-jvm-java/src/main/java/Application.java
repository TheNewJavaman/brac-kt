import net.javaman.brackt.api.quantum.QuantumCircuit;

public class Application {
    public static void main(String[] args) {
        // Construct the QuantumCircuit in Java
        int n = 3;
        QuantumCircuit qc = new QuantumCircuit("Example Superposition", n);
        for (int i = 0; i < n; i++) qc.h(i);
        for (int i = 0; i < n; i++) qc.measure(i, i);

        // Let Kotlin handle the coroutines
        BracKtIntegration.callIbmq(qc);
    }
}
