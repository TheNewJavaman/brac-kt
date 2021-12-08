package net.javaman.brackt.providers.ibmq

import kotlinx.coroutines.runBlocking

actual fun runSuspendTest(block: suspend () -> Unit) = runBlocking { block() }
