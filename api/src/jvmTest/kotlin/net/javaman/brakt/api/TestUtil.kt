package net.javaman.brakt.api

import net.javaman.brakt.api.util.InjectionManager
import net.javaman.brakt.api.util.Logger
import net.javaman.brakt.api.util.PropertyManager

object TestUtil {
    @JvmStatic
    fun addInjections() {
        InjectionManager.add {
            one { PropertyManager() }
            many { Logger.fromKClass(it) }
        }
    }
}
