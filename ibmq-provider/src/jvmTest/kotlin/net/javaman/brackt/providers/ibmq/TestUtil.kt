package net.javaman.brackt.providers.ibmq

import net.javaman.brackt.api.util.injections.InjectionManager
import net.javaman.brackt.api.util.logger.Logger
import net.javaman.brackt.api.util.properties.PropertyManager
import net.javaman.brackt.providers.ibmq.api.IbmqApi

object TestUtil {
    @JvmStatic
    fun addInjections() {
        InjectionManager.add {
            one { PropertyManager() }
            one { IbmqApi() }
            many { Logger.fromKClass(it) }
        }
    }
}
