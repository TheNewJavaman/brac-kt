package net.javaman.brackt.api.util.formatters

import java.net.URLDecoder

actual fun String.decodedUrl(): String = URLDecoder.decode(this, "UTF-8")
