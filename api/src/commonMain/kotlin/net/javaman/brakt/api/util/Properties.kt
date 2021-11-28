package net.javaman.brakt.api.util

import kotlin.jvm.JvmStatic

@JvmStatic
expect inline fun <reified T> getProperty(key: String): T
