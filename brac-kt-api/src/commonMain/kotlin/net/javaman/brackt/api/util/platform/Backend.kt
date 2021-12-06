package net.javaman.brackt.api.util.platform

enum class Backend {
    JVM,
    JS_BROWSER,
    JS_NODE
}

expect val backend: Backend
