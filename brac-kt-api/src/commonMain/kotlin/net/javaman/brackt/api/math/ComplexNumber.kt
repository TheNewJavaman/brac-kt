package net.javaman.brackt.api.math

import kotlin.js.JsName

/**
 * A number composed of real and imaginary parts
 */
data class ComplexNumber(
    val real: Double = 0.0,
    val imaginary: Double = 0.0
) {
    operator fun plus(that: ComplexNumber) =
        ComplexNumber(real + that.real, imaginary + that.imaginary)

    operator fun minus(that: ComplexNumber) =
        ComplexNumber(real - that.real, imaginary - that.imaginary)

    operator fun times(that: ComplexNumber) = ComplexNumber(
        real * that.real - imaginary * that.imaginary,
        real * that.imaginary + that.real * imaginary
    )

    @JsName("timesMatrix")
    operator fun times(that: Matrix2D) = Matrix2D(
        that.size,
        that.points.map { Matrix2DEntry(it.position, this * it.value) }.toMutableSet()
    )

    override fun toString() = "$real+${imaginary}i"
}
