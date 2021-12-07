@file:OptIn(ExperimentalJsExport::class)

package net.javaman.brackt.api

import net.javaman.brackt.api.util.injections.InjectionAdder
import net.javaman.brackt.api.util.injections.InjectionManager
import net.javaman.brackt.api.util.logging.Logger
import net.javaman.brackt.api.util.properties.PropertyManager
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.jvm.JvmStatic

@JsExport
class BracKtApi private constructor() {
    companion object : InjectionAdder {
        /**
         * Add injections for this module
         */
        @JvmStatic
        override fun addInjections() = InjectionManager.add {
            many { Logger(it) }
            one { PropertyManager() }
        }
    }
}
