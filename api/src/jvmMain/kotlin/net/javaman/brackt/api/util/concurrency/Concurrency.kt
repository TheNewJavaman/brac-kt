package net.javaman.brackt.api.util.concurrency

import kotlinx.coroutines.runBlocking

actual fun <T> runSync(block: suspend () -> T): T = runBlocking { block() }
