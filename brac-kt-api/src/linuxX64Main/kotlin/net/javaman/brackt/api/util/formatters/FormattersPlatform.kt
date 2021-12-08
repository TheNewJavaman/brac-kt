package net.javaman.brackt.api.util.formatters

import kotlinx.cinterop.toKString
import libcurl.curl_easy_init
import libcurl.curl_easy_unescape

actual fun String.decodedUrl(): String {
    val curl = curl_easy_init()
    val strPtr = curl_easy_unescape(curl, this, this.length, null)
    return strPtr?.toKString()!!
}
