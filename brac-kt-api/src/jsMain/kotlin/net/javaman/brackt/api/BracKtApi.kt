@file:OptIn(ExperimentalJsExport::class)

package net.javaman.brackt.api

// Due to a Kotlin/JS bug when compiling companion objects, we must redeclare this static method
@JsExport
fun addInjections() = BracKtApi.addInjections()
