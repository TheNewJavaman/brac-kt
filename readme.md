[![](https://img.shields.io/github/workflow/status/TheNewJavaman/brac-kt/Code%20Linting%20(Detekt)/main?label=Code%20Linting&cacheSeconds=1&logo=github)](https://github.com/TheNewJavaman/brac-kt/actions/workflows/linting.yml)
[![](https://img.shields.io/github/workflow/status/TheNewJavaman/brac-kt/Tests%20(Kotlin%20Multiplatform)/main?label=Tests&cacheSeconds=1&logo=github)](https://github.com/TheNewJavaman/brac-kt/actions/workflows/tests.yml)
[![](https://img.shields.io/github/workflow/status/TheNewJavaman/brac-kt/Publish%20Docs%20(Dokka)/main?label=Docs&cacheSeconds=1&logo=github)](https://javaman.net/brac-kt)
[![](https://img.shields.io/jitpack/v/github/TheNewJavaman/brac-kt?color=passing&label=Release&cacheSeconds=1&logo=data:image/png;base64,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)](https://jitpack.io/#net.javaman.brac-kt/brac-kt)

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
<i>Named after bra-ket quantum notation (with "kt" for Kotlin), pronounced "bracket"</i>
</div>
</td>
</tr>
</table>

## Installation

Import the root project into IntelliJ. Current version supports JDK 11 Temurin with Gradle 7.3

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
        // Add your IBM API token as environment variable IBMQ_API_TOKEN within IntelliJ
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
...
[...] [INFO ] Qubit outcomes:
[...] [INFO ]     0x0: 0.12890625 (132/1024)
[...] [INFO ]     0x1: 0.12792969 (131/1024)
[...] [INFO ]     0x2: 0.13769531 (141/1024)
[...] [INFO ]     0x3: 0.12695312 (130/1024)
[...] [INFO ]     0x4: 0.1171875 (120/1024)
[...] [INFO ]     0x5: 0.12109375 (124/1024)
[...] [INFO ]     0x6: 0.109375 (112/1024)
[...] [INFO ]     0x7: 0.13085938 (134/1024)
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
