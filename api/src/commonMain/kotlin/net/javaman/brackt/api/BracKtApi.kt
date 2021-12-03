package net.javaman.brackt.api

import net.javaman.brackt.api.util.injections.InjectionManager
import net.javaman.brackt.api.util.logging.Logger
import net.javaman.brackt.api.util.properties.PropertyManager
import kotlin.jvm.JvmStatic

class BracKtApi private constructor() {
    companion object {
        /**
         * Add injections for this module
         */
        @JvmStatic
        fun addInjections() = InjectionManager.add {
            many { Logger(it) }
            one { PropertyManager() }
        }
    }
}
