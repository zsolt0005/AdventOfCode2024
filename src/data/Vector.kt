package data

import java.io.Serializable

data class Vector<T: Number>(val x: T, val y: T): Serializable
{
    override fun toString(): String = "X: $x, Y: $y"
}

operator fun Vector<Int>.plus(other: Vector<Int>): Vector<Int>
    = Vector(x + other.x, y + other.y)