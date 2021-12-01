package net.javaman.brackt.api.util

import net.javaman.brackt.api.TestUtil
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.api.util.logger.Logger
import org.junit.jupiter.api.Test

class LoggerTest {
    private val logger: Logger by injection()

    init {
        TestUtil.addInjections()
    }

    @Test
    fun loggerInjection_ok() {
        logger.info {
            "If you can see this, all is well! " +
                    "Otherwise, a ${NullPointerException::class.simpleName} should have been thrown"
        }
    }
}
