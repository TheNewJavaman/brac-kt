package net.javaman.brackt.api.math

/**
 * A number composed of real and imaginary parts
 */
data class ComplexNumber(
    val real: Double = 0.0,
    val imaginary: Double = 0.0
) {
    operator fun plus(that: net.javaman.brackt.api.math.ComplexNumber) =
        net.javaman.brackt.api.math.ComplexNumber(real + that.real, imaginary + that.imaginary)

    operator fun minus(that: net.javaman.brackt.api.math.ComplexNumber) =
        net.javaman.brackt.api.math.ComplexNumber(real - that.real, imaginary - that.imaginary)

    operator fun times(that: net.javaman.brackt.api.math.ComplexNumber) = net.javaman.brackt.api.math.ComplexNumber(
        real * that.real - imaginary * that.imaginary,
        real * that.imaginary + that.real * imaginary
    )

    operator fun times(that: net.javaman.brackt.api.math.Matrix2D) = net.javaman.brackt.api.math.Matrix2D(
        that.size,
        that.points.map { net.javaman.brackt.api.math.Matrix2DEntry(it.position, this * it.value) }.toMutableSet()
    )

    override fun toString() = "$real+${imaginary}i"
}
