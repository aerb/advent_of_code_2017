import kotlin.math.abs
import kotlin.test.assertEquals

data class Coord(val x: Int, val y: Int) {
    val distance = abs(x) + abs(y)
    fun move(dX: Int = 0, dY: Int = 0): Coord = Coord(x + dX, y + dY)
}

fun findSquare(predicate: (Int, Coord, Int) -> Boolean): Pair<Coord, Int> {
    val been = HashMap<Coord, Int>()

    var now = Coord(0, 0)
    been += now to 1
    var ring = 0
    var square = 1
    while(true) {
        val moves = listOf(
            now.move(dX = +1),
            now.move(dY = +1),
            now.move(dX = -1),
            now.move(dY = -1)
        )
        val next = moves.firstOrNull { abs(it.x) <= ring && abs(it.y) <= ring && it !in been }
        if(next != null) {
            now = next

            val adjacent = listOf(
                now.move(dX = +1),
                now.move(dY = +1),
                now.move(dX = -1),
                now.move(dY = -1),
                now.move(dX = +1, dY = +1),
                now.move(dX = +1, dY = -1),
                now.move(dX = -1, dY = +1),
                now.move(dX = -1, dY = -1)
            )

            val value = adjacent.filter { it in been }.sumBy { been[it]!! }
            been += now to value
            square ++
            if(predicate(square, now, value)) return now to value
        } else {
            ring ++
        }
    }
}

fun distanceTo(squareToFind: Int): Coord {
    return findSquare { i, _, _ ->
        i == squareToFind
    }.first
}

fun valueAbove(value: Int): Int {
    return findSquare { _, _, v ->
        v > value
    }.second
}

fun main(args: Array<String>) {
    assertEquals(3, distanceTo(12).distance)
    assertEquals(2, distanceTo(23).distance)
    assertEquals(31, distanceTo(1024).distance)
    println(distanceTo(361527).distance)

    assertEquals(10, valueAbove(5))
    assertEquals(25, valueAbove(23))
    assertEquals(304, valueAbove(300))
    println(valueAbove(361527))
}