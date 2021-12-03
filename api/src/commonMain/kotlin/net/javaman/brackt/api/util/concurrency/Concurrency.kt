package net.javaman.brackt.api.util.concurrency

expect fun <T> runSync(block: suspend () -> T): T
