# bra-kt

A Kotlin/Multiplatform interface for quantum computing

## Installation

Import the root project into IntelliJ. Current supported version is JDK 17.0.1 Corretto with Gradle 7.3

## Plans

- Finish internal QuantumCircuit representation
    - Should be pretty basic
- Integrate with IBM Q Experience API
    - Needs an API client; look into pure Kotlin libraries (Ktor?)
- Support JS backend in addition to JVM
    - Easy once everything else is done
    - Imagine composing/simulating a quantum circuit within a JS/React app!

## Inspired By

- [Qiskit](https://github.com/Qiskit)
- [ssuukk/Qotlin](https://github.com/ssuukk/Qotlin)
- [Antimonit/Quantum](https://github.com/Antimonit/Quantum)
