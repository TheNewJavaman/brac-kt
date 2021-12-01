package net.javaman.brackt.providers.ibmq

import net.javaman.brackt.api.util.injections.InjectionManager
import net.javaman.brackt.api.util.properties.PropertyManager
import net.javaman.brackt.providers.ibmq.client.IbmqApi

object TestUtil {
    @JvmStatic
    fun addInjections() {
        InjectionManager.add {
            one { PropertyManager() }
            one { IbmqApi() }
        }
    }
}
