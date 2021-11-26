package net.javaman.brakt.api.math

import net.javaman.brakt.api.util.max
import net.javaman.brakt.api.util.range

/**
 * 2D-matrix coordinates:
 * ```
 * [[(0, 0), (0, 1), (0, 2)]
 *  [(1, 0), (1, 1), (1, 2)]
 *  [(2, 0), (2, 1), (2, 2)]]
 * ```
 */
@Suppress("TooManyFunctions") // Many basic operators to implement
data class Matrix(
    val points: MutableSet<MatrixEntry> = HashSet()
) {
    fun isEmpty() = points.isEmpty()

    fun isValid() = if (isEmpty()) {
        true
    } else {
        points.map { it.coordinates.size }.groupBy { it }.size == 1
    }

    fun calcDepth() = if (isValid() && !isEmpty()) {
        points.maxOf { it.coordinates.size }
    } else {
        throw UnsupportedOperationException("Cannot calculate depth of empty Matrix")
    }

    fun calcDimensions() = if (isValid() && !isEmpty()) {
        (0 until calcDepth()).map { i -> points.maxOf { it.coordinates[i] + 1 } }
    } else {
        throw UnsupportedOperationException("Cannot calculate dimensions of empty Matrix")
    }

    fun calcSize(): Long {
        var size = 1L
        for (d in calcDimensions()) size *= d
        return size
    }

    fun areCoordinatesValid(c: List<Long>) = c.mapIndexed { i, l -> l < calcDimensions()[i] }.all { it }

    fun dotProduct(that: Matrix) = this * that

    operator fun contains(a: ComplexNumber) = when {
        a.isZero() && points.size < calcSize() -> true
        points.map { it.value }.contains(a) -> true
        else -> false
    }

    operator fun get(c: List<Long>) = if (areCoordinatesValid(c)) {
        points.firstOrNull { it.coordinates == c }?.value ?: ComplexNumber()
    } else {
        throw IndexOutOfBoundsException("Cannot find value with invalid coordinates")
    }

    operator fun set(c: List<Long>, a: ComplexNumber) {
        points.removeIf { it.coordinates == c }
        points.add(MatrixEntry(c, a))
    }

    operator fun plus(that: Matrix) = Matrix(
        calcDimensions().max(that.calcDimensions())
            .range()
            .map {
                val a = try {
                    this[it]
                } catch (_: IndexOutOfBoundsException) {
                    ComplexNumber()
                }
                val b = try {
                    that[it]
                } catch (_: IndexOutOfBoundsException) {
                    ComplexNumber()
                }
                MatrixEntry(it, a + b)
            }.toMutableSet()
    )

    operator fun minus(that: Matrix) = Matrix(
        calcDimensions().max(that.calcDimensions())
            .range()
            .map {
                val a = try {
                    this[it]
                } catch (_: IndexOutOfBoundsException) {
                    ComplexNumber()
                }
                val b = try {
                    that[it]
                } catch (_: IndexOutOfBoundsException) {
                    ComplexNumber()
                }
                MatrixEntry(it, a - b)
            }.toMutableSet()
    )

    @Suppress("ThrowsCount") // Necessary to check for many conditions
    operator fun times(that: Matrix): Matrix {
        if (calcDepth() < 0 || that.calcDepth() < 0) {
            throw UnsupportedOperationException("Cannot multiply empty Matrices")
        } else if (calcDepth() > 2 || that.calcDepth() > 2) {
            throw UnsupportedOperationException("Multiplying Matrices(depth>2) is not supported")
        }
        val thisLength = try {
            calcDimensions()[1]
        } catch (_: IndexOutOfBoundsException) {
            1
        }
        val thisHeight = calcDimensions()[0]
        val thatLength = try {
            that.calcDimensions()[1]
        } catch (_: IndexOutOfBoundsException) {
            1
        }
        val thatHeight = that.calcDimensions()[0]
        if (thisLength != thatHeight) {
            throw UnsupportedOperationException(
                "Matrix(length=$thisLength) * Matrix(height=$thatHeight) is not a valid operation"
            )
        }
        return Matrix(
            listOf(thisHeight, thatLength).range()
                .map {
                    var sum = ComplexNumber()
                    for (i in 0 until thisLength) {
                        sum += this[listOf(i, it[0])] * that[listOf(it[1], i)]
                    }
                    MatrixEntry(it, sum)
                }.toMutableSet()
        )
    }
}

data class MatrixEntry(
    val coordinates: List<Long>,
    val value: ComplexNumber
)
