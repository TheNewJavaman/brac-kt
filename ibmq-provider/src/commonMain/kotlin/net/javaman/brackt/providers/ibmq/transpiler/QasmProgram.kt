package net.javaman.brackt.providers.ibmq.transpiler

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import net.javaman.brackt.providers.ibmq.api.models.QasmVersion
import kotlin.jvm.JvmStatic
import kotlin.random.Random

data class QasmProgram(
    val qasm: String,
    val version: QasmVersion,
    val idCode: String = randomIdCode(),
    val name: String = "Untitled Program",
    val description: String = "A QASM circuit",
    val creationDate: Instant = Clock.System.now(),
    val lastUpdateDate: Instant = Clock.System.now()
) {
    companion object {
        private const val ID_CODE_LENGTH = 24
        private const val HEXADECIMAL = "0123456789abcdef"

        @JvmStatic
        fun randomIdCode() = List(ID_CODE_LENGTH) { HEXADECIMAL[Random.nextInt(0, HEXADECIMAL.length)] }
            .toCharArray()
            .concatToString()
    }
}
