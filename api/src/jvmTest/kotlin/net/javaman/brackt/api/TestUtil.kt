package net.javaman.brackt.api

import net.javaman.brackt.api.util.injections.InjectionManager
import net.javaman.brackt.api.util.logging.Logger
import net.javaman.brackt.api.util.properties.PropertyManager

object TestUtil {
    @JvmStatic
    fun addInjections() {
        InjectionManager.add {
            one { PropertyManager() }
            many { Logger(it) }
        }
    }
}
