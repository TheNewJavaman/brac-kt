package net.javaman.brackt.providers.ibmq

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

actual fun runSuspendTest(block: suspend () -> Unit) {
    CoroutineScope(Dispatchers.Default).promise { block() }
}
