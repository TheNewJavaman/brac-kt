package net.javaman.brackt.api.util.formatters

external fun decodeURIComponent(encodedURI: String): String

actual fun String.decodedUrl(): String = decodeURIComponent(this)
