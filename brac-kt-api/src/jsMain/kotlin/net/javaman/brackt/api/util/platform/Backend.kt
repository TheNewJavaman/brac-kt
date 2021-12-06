package net.javaman.brackt.api.util.platform

actual val backend = when (js("typeof process === 'object'") as Boolean) {
    true -> Backend.JS_NODE
    false -> Backend.JS_BROWSER
}
