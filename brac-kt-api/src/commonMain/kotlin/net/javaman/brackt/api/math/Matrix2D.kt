package net.javaman.brackt.api.math

import kotlin.math.max

/**
 * A 2D matrix that holds only [ComplexNumber] entries. Any missing point entries are assumed to be 0.0+0.0i
 */
@Suppress("TooManyFunctions") // Many basic operators to implement
data class Matrix2D(
    val size: Matrix2DSize = Matrix2DSize(0, 0),
    val points: MutableSet<Matrix2DEntry> = mutableSetOf()
) {
    fun isPositionValid(p: Matrix2DPoint) = p.row < size.height && p.column < size.length

    fun transpose() = Matrix2D(
        Matrix2DSize(size.length, size.height),
        points.map { Matrix2DEntry(Matrix2DPoint(it.position.column, it.position.row), it.value) }.toMutableSet()
    )

    operator fun contains(a: ComplexNumber) = points.map { it.value }.contains(a)

    operator fun get(p: Matrix2DPoint): ComplexNumber {
        if (!isPositionValid(p)) throw IndexOutOfBoundsException("Cannot get value; invalid $p for $size")
        return points.firstOrNull { it.position == p }?.value ?: ComplexNumber()
    }

    operator fun set(p: Matrix2DPoint, a: ComplexNumber) {
        if (!isPositionValid(p)) throw IndexOutOfBoundsException("Cannot set value; invalid $p for $size")
        points.removeAll { it.position == p }
        points.add(Matrix2DEntry(p, a))
    }

    operator fun plus(that: Matrix2D): Matrix2D {
        val newSize = Matrix2DSize(max(size.height, that.size.height), max(size.length, that.size.length))
        val newPoints = mutableSetOf<Matrix2DEntry>()
        for (r in 0 until newSize.height) {
            for (c in 0 until newSize.length) {
                val p = Matrix2DPoint(r, c)
                val (a, b) = getOutOfBounds(p, that)
                newPoints.add(Matrix2DEntry(p, a + b))
            }
        }
        return Matrix2D(newSize, newPoints)
    }

    operator fun minus(that: Matrix2D): Matrix2D {
        val newSize = Matrix2DSize(max(size.height, that.size.height), max(size.length, that.size.length))
        val newPoints = mutableSetOf<Matrix2DEntry>()
        for (r in 0 until newSize.height) {
            for (c in 0 until newSize.length) {
                val p = Matrix2DPoint(r, c)
                val (a, b) = getOutOfBounds(p, that)
                newPoints.add(Matrix2DEntry(p, a - b))
            }
        }
        return Matrix2D(newSize, newPoints)
    }

    private fun getOutOfBounds(p: Matrix2DPoint, that: Matrix2D): Pair<ComplexNumber, ComplexNumber> {
        val a = try {
            this[p]
        } catch (_: IndexOutOfBoundsException) {
            ComplexNumber()
        }
        val b = try {
            that[p]
        } catch (_: IndexOutOfBoundsException) {
            ComplexNumber()
        }
        return Pair(a, b)
    }

    operator fun times(that: Matrix2D): Matrix2D {
        if (size.length != that.size.height) {
            throw UnsupportedOperationException("$size * ${that.size} is not a valid operation")
        }
        val newSize = Matrix2DSize(size.height, that.size.length)
        val newPoints = mutableSetOf<Matrix2DEntry>()
        for (r in 0 until newSize.height) {
            for (c in 0 until newSize.length) {
                var sum = ComplexNumber()
                for (o in 0 until size.length) {
                    sum += this[Matrix2DPoint(r, o)] * that[Matrix2DPoint(o, c)]
                }
                newPoints.add(Matrix2DEntry(Matrix2DPoint(r, c), sum))
            }
        }
        return Matrix2D(newSize, newPoints)
    }

    infix fun dotProduct(that: Matrix2D) = this * that
}

data class Matrix2DEntry(
    val position: Matrix2DPoint,
    val value: ComplexNumber
)

data class Matrix2DPoint(
    val row: Long,
    val column: Long
)

data class Matrix2DSize(
    val height: Long,
    val length: Long
)
