package net.javaman.brackt.api

import net.javaman.brakt.api.util.injections.InjectionManager
import net.javaman.brakt.api.util.logger.Logger
import net.javaman.brakt.api.util.properties.PropertyManager

object TestUtil {
    @JvmStatic
    fun addInjections() {
        InjectionManager.add {
            one { PropertyManager() }
            many { Logger.fromKClass(it) }
        }
    }
}
