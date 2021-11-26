package net.javaman.brakt.api.math

import net.javaman.brakt.api.util.format

data class ComplexNumber(
    val real: Double = 0.0,
    val imaginary: Double = 0.0
) {
    fun isZero() = real == 0.0 && imaginary == 0.0

    operator fun plus(that: ComplexNumber) = ComplexNumber(real + that.real, imaginary + that.imaginary)

    operator fun minus(that: ComplexNumber) = ComplexNumber(real - that.real, imaginary - that.imaginary)

    operator fun times(that: ComplexNumber) = ComplexNumber(
        real * that.real - imaginary * that.imaginary,
        real * that.imaginary + that.real * imaginary
    )

    operator fun times(that: Matrix) = Matrix(
        that.points.map { MatrixEntry(it.coordinates, this * it.value) }.toMutableSet()
    )

    override fun toString() = "ComplexNumber(${real.format()}+${imaginary.format()}i)"
}
