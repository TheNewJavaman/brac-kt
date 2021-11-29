package net.javaman.brakt.providers.ibmq

import net.javaman.brakt.api.util.InjectionManager
import net.javaman.brakt.api.util.PropertyManager

object TestUtil {
    @JvmStatic
    fun addInjections() {
        InjectionManager.addAll {
            dependency { PropertyManager() }
        }
    }
}
