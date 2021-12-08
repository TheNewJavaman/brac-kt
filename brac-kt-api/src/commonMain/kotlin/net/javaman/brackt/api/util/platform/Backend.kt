package net.javaman.brackt.api.util.platform

enum class Backend {
    JVM,
    JS_BROWSER,
    JS_NODE,
    NATIVE_LINUX,
    NATIVE_WINDOWS
}

expect val backend: Backend
