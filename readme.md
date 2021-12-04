[![Code Linting (Detekt)](https://github.com/TheNewJavaman/brac-kt/actions/workflows/linting.yml/badge.svg)](https://github.com/TheNewJavaman/brac-kt/actions/workflows/linting.yml) [![Publish Docs (Dokka)](https://github.com/TheNewJavaman/brac-kt/actions/workflows/docs.yml/badge.svg)](https://github.com/TheNewJavaman/brac-kt/actions/workflows/docs.yml) [![Tests (Kotlin Multiplatform)](https://github.com/TheNewJavaman/brac-kt/actions/workflows/tests.yml/badge.svg)](https://github.com/TheNewJavaman/brac-kt/actions/workflows/tests.yml)

<table>
<tr>
<td><img src="resources/full-outline-downsized.png" alt="branding" width=1750/></td>
<td>
<h1>brac-kt</h1>

A Kotlin/Multiplatform interface for quantum computing
</td>  
</tr>
<tr>
<td colspan=2>
<div align="center">
<i>Named after bra-ket quantum notation (with "kt" for Kotlin)</i>
</div>
</td>
</tr>
</table>

## Installation

Import the root project into IntelliJ. Current version supports JDK 11 Corretto with Gradle 7.3

## Examples

Check out the `example` subproject for a full, reproducible example. Here's the gist:

```kotlin
object Application {
    // Dependencies are managed by InjectionManager
    private val propertyManager: PropertyManager by injection()
    private val ibmqProvider: IbmqProvider by injection()

    init {
        BracKtApi.addInjections()
        IbmqProvider.addInjections()
    }

    // Run the program
    @JvmStatic
    fun main(args: Array<String>) {
        // Create a basic quantum circuit: three qubits in superposition, then measured
        val n = 3
        val qc = QuantumCircuit(name = "Example Superposition", numQubits = 3) {
            repeat(n) { h(qubit = it) }
            repeat(n) { measure(qubit = it, bit = it) }
        }

        // Run the circuit on an IBM Quantum device
        // By default, brac-kt will choose a simulator with 5 or more qubits with the shortest queue
        // Add your IBM API token as environment variable "IBMQ_API_TOKEN" within IntelliJ
        val apiToken: String = propertyManager["IBMQ_API_TOKEN"]
        ibmqProvider.logIn(apiToken)
        ibmqProvider.selectNetwork()
        ibmqProvider.selectDevice()
        ibmqProvider.runExperiment(qc).andWait()
    }
}
```

The example code will output:

```
[2021-12-04 00:04:09.060] [...avaman.brackt.providers.ibmq.api.models.RunExperimentResponse] [INFO ] Qubit outcomes:
[2021-12-04 00:04:09.060] [...avaman.brackt.providers.ibmq.api.models.RunExperimentResponse] [INFO ]     0x0: 0.12890625 (132/1024)
[2021-12-04 00:04:09.060] [...avaman.brackt.providers.ibmq.api.models.RunExperimentResponse] [INFO ]     0x1: 0.12792969 (131/1024)
[2021-12-04 00:04:09.060] [...avaman.brackt.providers.ibmq.api.models.RunExperimentResponse] [INFO ]     0x2: 0.13769531 (141/1024)
[2021-12-04 00:04:09.060] [...avaman.brackt.providers.ibmq.api.models.RunExperimentResponse] [INFO ]     0x3: 0.12695312 (130/1024)
[2021-12-04 00:04:09.060] [...avaman.brackt.providers.ibmq.api.models.RunExperimentResponse] [INFO ]     0x4: 0.1171875 (120/1024)
[2021-12-04 00:04:09.060] [...avaman.brackt.providers.ibmq.api.models.RunExperimentResponse] [INFO ]     0x5: 0.12109375 (124/1024)
[2021-12-04 00:04:09.060] [...avaman.brackt.providers.ibmq.api.models.RunExperimentResponse] [INFO ]     0x6: 0.109375 (112/1024)
[2021-12-04 00:04:09.060] [...avaman.brackt.providers.ibmq.api.models.RunExperimentResponse] [INFO ]     0x7: 0.13085938 (134/1024)
```

## Goals

What am I trying to accomplish?

- Replace standard Python libraries with a pure Kotlin implementation
    - Python has many limitations:
        - Restricted to one runtime (Python)
        - Not type-safe, null-safe, etc.
        - Slow
        - Not very well adopted outside of data science
    - Kotlin has many improvements over Python:
        - Multiplatform projects (like brac-kt) can compile to the JVM, JS, or native libs; can interop with each
          backend
        - Type-safe, null-safe, etc.
        - Very fast, depends on the backend implementation
        - Each backend has its own plethora of use cases:
            - JVM: Desktop applications, servers
            - JS: Websites, servers
            - Native: Desktop applications, workstation tasks (think scientific research)
    - Reduce dependency on a single framework
- Replace fractured online quantum circuit builders
    - Each graphical circuit builder is specific to a certain company's hardware
    - Hard to learn/high entry barrier
    - Very uncommon to write programs this way because it's slow
- Lower the entry barrier for quantum
    - It's too hard to get started
    - Online resources, including free textbooks, have made it easier, but it's still extremely hard to learn
    - Provide more algorithm explanations and pre-built circuits for beginners
- If I have time, work on a _very_ basic simulator
    - Just a proof-of-concept that the hardware-agnostic circuit builder is easy to implement for specific platforms

Summary:

1. Fix the fractured quantum ecosystem with a multi-runtime, hardware-agnostic circuit builder
2. Make it easier for people to write quantum software

## Plans

<table>
<tr>
<th>Project</th>
<th>Details</th>
</tr>
<tr>
<td>Quantum circuit builder</td>
<td>

- Enum for all basic gates
    - Single-qubit gates are complete
    - Entanglement/CNOT, measurement operations needed

</td>
<tr>
<td>IBM Q Provider</td>
<td>

- Continue documenting API
    - Read through more Qiskit code
    - Use Chrome devtools more in the online circuit builder
    - Finish adding read-only endpoints
- Write QASM compiler from quantum circuit builder
    - Reverse-engineer QASM-related endpoints

</td>
</tr>
<tr>
<td>First-class support for JS backend</td>
<td>

- Support composing quantum circuits in JavaScript or TypeScript
    - Ideally, write a GUI in React to build circuits
    - Release to npm, test in Node.js
- Compile unified API module for JS backend
    - Implement `PropertyManager` for JS

</td>
</tr>
<tr>
<td>Basic simulator</td>
<td>

- Try to simulate basic quantum circuits on a Kotlin server
    - How? I have no clue how the math works
- Optimize heavily using a profiler
- Will this be a commercial product? If so, I need to buy IntelliJ Ultimate
- Kotlin/Native or Kotlin/JVM?
    - If, somehow, this application can be CUDA-accelerated, look into Kotlin/Native interop with libs/DLLs
    - Otherwise, Kotlin/JVM is just as easy (not in a math sense, though)

</td>
</tr>
</table>

## Inspired By

- [Qiskit](https://github.com/Qiskit)
- [ssuukk/Qotlin](https://github.com/ssuukk/Qotlin)
- [Antimonit/Quantum](https://github.com/Antimonit/Quantum)
