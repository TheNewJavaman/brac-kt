package net.javaman.brakt.api.util.injections

import kotlin.reflect.KClass

interface FromKClassObject<T> {
    fun fromKClass(kClass: KClass<*>): T
}
