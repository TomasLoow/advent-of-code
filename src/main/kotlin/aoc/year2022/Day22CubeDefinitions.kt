package aoc.year2022

import aoc.utils.geometry.Direction

private fun check(boolean:Boolean) = if (!boolean) throw AssertionError() else Unit

/**
 *    2211
 *    2211
 *    33
 *    33
 *  5544
 *  5544
 *  66
 *  66
 */
fun makeCubePt1(faces: Map<Int, Face>): Cube {
    val f1 = faces[1]!!
    val f2 = faces[2]!!
    val f4 = faces[4]!!
    val f3 = faces[3]!!
    val f5 = faces[5]!!
    val f6 = faces[6]!!
    val edges = mapOf<Edge, Edge>(
        f1.edges[Direction.DOWN]!! to f1.edges[Direction.UP]!!,
        f1.edges[Direction.LEFT]!! to f2.edges[Direction.RIGHT]!!,
        f1.edges[Direction.RIGHT]!! to f2.edges[Direction.LEFT]!!,
        f1.edges[Direction.UP]!! to f1.edges[Direction.DOWN]!!,

        f2.edges[Direction.DOWN]!! to f3.edges[Direction.UP]!!,
        f2.edges[Direction.LEFT]!! to f1.edges[Direction.RIGHT]!!,
        f2.edges[Direction.RIGHT]!! to f1.edges[Direction.LEFT]!!,
        f2.edges[Direction.UP]!! to f4.edges[Direction.DOWN]!!,

        f3.edges[Direction.DOWN]!! to f4.edges[Direction.UP]!!,
        f3.edges[Direction.LEFT]!! to f3.edges[Direction.RIGHT]!!,
        f3.edges[Direction.RIGHT]!! to f3.edges[Direction.LEFT]!!,
        f3.edges[Direction.UP]!! to f2.edges[Direction.DOWN]!!,

        f4.edges[Direction.DOWN]!! to f2.edges[Direction.UP]!!,
        f4.edges[Direction.LEFT]!! to f5.edges[Direction.RIGHT]!!,
        f4.edges[Direction.RIGHT]!! to f5.edges[Direction.LEFT]!!,
        f4.edges[Direction.UP]!! to f3.edges[Direction.DOWN]!!,

        f5.edges[Direction.DOWN]!! to f6.edges[Direction.UP]!!,
        f5.edges[Direction.LEFT]!! to f4.edges[Direction.RIGHT]!!,
        f5.edges[Direction.RIGHT]!! to f4.edges[Direction.LEFT]!!,
        f5.edges[Direction.UP]!! to f6.edges[Direction.DOWN]!!,

        f6.edges[Direction.DOWN]!! to f5.edges[Direction.UP]!!,
        f6.edges[Direction.LEFT]!! to f6.edges[Direction.RIGHT]!!,
        f6.edges[Direction.RIGHT]!! to f6.edges[Direction.LEFT]!!,
        f6.edges[Direction.UP]!! to f5.edges[Direction.DOWN]!!,
    )
    check(edges.size == 4 * 6)
    edges.forEach { (k, v) ->
        check(edges[v] == k)
        check(k.dirTowardsInternal == v.dirTowardsInternal.rotate180())
    }
    return Cube(faces, edges)
}

/**
 *    2211
 *    2211
 *    33
 *    33
 *  5544
 *  5544
 *  66
 *  66
 */
fun makeCubePt2(faces: Map<Int, Face>): Cube {
    val f1 = faces[1]!!
    val f2 = faces[2]!!
    val f4 = faces[4]!!
    val f3 = faces[3]!!
    val f5 = faces[5]!!
    val f6 = faces[6]!!
    val edges = mapOf<Edge, Edge>(
        f1.edges[Direction.DOWN]!! to f3.edges[Direction.RIGHT]!!,
        f1.edges[Direction.LEFT]!! to f2.edges[Direction.RIGHT]!!,
        f1.edges[Direction.RIGHT]!! to f4.edges[Direction.RIGHT]!!,
        f1.edges[Direction.UP]!! to f6.edges[Direction.DOWN]!!,

        f2.edges[Direction.RIGHT]!! to f1.edges[Direction.LEFT]!!,
        f2.edges[Direction.DOWN]!! to f3.edges[Direction.UP]!!,
        f2.edges[Direction.LEFT]!! to f5.edges[Direction.LEFT]!!,
        f2.edges[Direction.UP]!! to f6.edges[Direction.LEFT]!!,

        f3.edges[Direction.UP]!! to f2.edges[Direction.DOWN]!!,
        f3.edges[Direction.DOWN]!! to f4.edges[Direction.UP]!!,
        f3.edges[Direction.RIGHT]!! to f1.edges[Direction.DOWN]!!,
        f3.edges[Direction.LEFT]!! to f5.edges[Direction.UP]!!,

        f4.edges[Direction.LEFT]!! to f5.edges[Direction.RIGHT]!!,
        f4.edges[Direction.UP]!! to f3.edges[Direction.DOWN]!!,
        f4.edges[Direction.DOWN]!! to f6.edges[Direction.RIGHT]!!,
        f4.edges[Direction.RIGHT]!! to f1.edges[Direction.RIGHT]!!,

        f5.edges[Direction.DOWN]!! to f6.edges[Direction.UP]!!,
        f5.edges[Direction.LEFT]!! to f2.edges[Direction.LEFT]!!,
        f5.edges[Direction.RIGHT]!! to f4.edges[Direction.LEFT]!!,
        f5.edges[Direction.UP]!! to f3.edges[Direction.LEFT]!!,

        f6.edges[Direction.UP]!! to f5.edges[Direction.DOWN]!!,
        f6.edges[Direction.DOWN]!! to f1.edges[Direction.UP]!!,
        f6.edges[Direction.LEFT]!! to f2.edges[Direction.UP]!!,
        f6.edges[Direction.RIGHT]!! to f4.edges[Direction.DOWN]!!,
    )
    check(edges.size == 4 * 6)
    edges.forEach { (k, v) ->
        check(edges[v] == k)
    }
    return Cube(faces, edges)
}


/**
 *      11
 *      11
 *  223344
 *  223344
 *      5566
 *      5566
 */
fun makeTestCubePt1(faces: Map<Int, Face>): Cube {
    val edges = mapOf<Edge, Edge>(
        faces[1]!!.edges[Direction.UP]!! to faces[5]!!.edges[Direction.DOWN]!!,
        faces[1]!!.edges[Direction.DOWN]!! to faces[4]!!.edges[Direction.UP]!!,
        faces[1]!!.edges[Direction.RIGHT]!! to faces[1]!!.edges[Direction.LEFT]!!,
        faces[1]!!.edges[Direction.LEFT]!! to faces[1]!!.edges[Direction.RIGHT]!!,

        faces[2]!!.edges[Direction.LEFT]!! to faces[4]!!.edges[Direction.RIGHT]!!,
        faces[2]!!.edges[Direction.RIGHT]!! to faces[3]!!.edges[Direction.LEFT]!!,
        faces[2]!!.edges[Direction.UP]!! to faces[2]!!.edges[Direction.DOWN]!!,
        faces[2]!!.edges[Direction.DOWN]!! to faces[2]!!.edges[Direction.UP]!!,

        faces[3]!!.edges[Direction.UP]!! to faces[3]!!.edges[Direction.DOWN]!!,
        faces[3]!!.edges[Direction.LEFT]!! to faces[2]!!.edges[Direction.RIGHT]!!,
        faces[3]!!.edges[Direction.RIGHT]!! to faces[4]!!.edges[Direction.LEFT]!!,
        faces[3]!!.edges[Direction.DOWN]!! to faces[3]!!.edges[Direction.UP]!!,

        faces[4]!!.edges[Direction.UP]!! to faces[1]!!.edges[Direction.DOWN]!!,
        faces[4]!!.edges[Direction.LEFT]!! to faces[3]!!.edges[Direction.RIGHT]!!,
        faces[4]!!.edges[Direction.RIGHT]!! to faces[2]!!.edges[Direction.LEFT]!!,
        faces[4]!!.edges[Direction.DOWN]!! to faces[5]!!.edges[Direction.UP]!!,

        faces[5]!!.edges[Direction.UP]!! to faces[4]!!.edges[Direction.DOWN]!!,
        faces[5]!!.edges[Direction.LEFT]!! to faces[6]!!.edges[Direction.RIGHT]!!,
        faces[5]!!.edges[Direction.RIGHT]!! to faces[6]!!.edges[Direction.LEFT]!!,
        faces[5]!!.edges[Direction.DOWN]!! to faces[1]!!.edges[Direction.UP]!!,

        faces[6]!!.edges[Direction.UP]!! to faces[6]!!.edges[Direction.DOWN]!!,
        faces[6]!!.edges[Direction.LEFT]!! to faces[5]!!.edges[Direction.RIGHT]!!,
        faces[6]!!.edges[Direction.RIGHT]!! to faces[5]!!.edges[Direction.LEFT]!!,
        faces[6]!!.edges[Direction.DOWN]!! to faces[6]!!.edges[Direction.UP]!!,

        )
    check(edges.size == 4 * 6)
    edges.forEach { (k, v) ->
        check(edges[v] == k)
        check(k.dirTowardsInternal == v.dirTowardsInternal.rotate180())
    }
    return Cube(faces, edges)
}

fun makeTestCubePt2(faces: Map<Int, Face>): Cube {
    val edges = mapOf<Edge, Edge>(
        faces[1]!!.edges[Direction.DOWN]!! to faces[4]!!.edges[Direction.UP]!!,
        faces[1]!!.edges[Direction.LEFT]!! to faces[3]!!.edges[Direction.UP]!!,
        faces[1]!!.edges[Direction.RIGHT]!! to faces[6]!!.edges[Direction.RIGHT]!!,
        faces[1]!!.edges[Direction.UP]!! to faces[2]!!.edges[Direction.UP]!!,

        faces[2]!!.edges[Direction.DOWN]!! to faces[5]!!.edges[Direction.DOWN]!!,
        faces[2]!!.edges[Direction.LEFT]!! to faces[6]!!.edges[Direction.DOWN]!!,
        faces[2]!!.edges[Direction.RIGHT]!! to faces[3]!!.edges[Direction.LEFT]!!,
        faces[2]!!.edges[Direction.UP]!! to faces[1]!!.edges[Direction.UP]!!,

        faces[3]!!.edges[Direction.DOWN]!! to faces[5]!!.edges[Direction.LEFT]!!,
        faces[3]!!.edges[Direction.LEFT]!! to faces[2]!!.edges[Direction.RIGHT]!!,
        faces[3]!!.edges[Direction.RIGHT]!! to faces[4]!!.edges[Direction.LEFT]!!,
        faces[3]!!.edges[Direction.UP]!! to faces[1]!!.edges[Direction.LEFT]!!,

        faces[4]!!.edges[Direction.DOWN]!! to faces[5]!!.edges[Direction.UP]!!,
        faces[4]!!.edges[Direction.LEFT]!! to faces[3]!!.edges[Direction.RIGHT]!!,
        faces[4]!!.edges[Direction.RIGHT]!! to faces[6]!!.edges[Direction.UP]!!,
        faces[4]!!.edges[Direction.UP]!! to faces[1]!!.edges[Direction.DOWN]!!,

        faces[5]!!.edges[Direction.DOWN]!! to faces[2]!!.edges[Direction.DOWN]!!,
        faces[5]!!.edges[Direction.LEFT]!! to faces[3]!!.edges[Direction.DOWN]!!,
        faces[5]!!.edges[Direction.RIGHT]!! to faces[6]!!.edges[Direction.LEFT]!!,
        faces[5]!!.edges[Direction.UP]!! to faces[4]!!.edges[Direction.DOWN]!!,

        faces[6]!!.edges[Direction.DOWN]!! to faces[2]!!.edges[Direction.LEFT]!!,
        faces[6]!!.edges[Direction.LEFT]!! to faces[5]!!.edges[Direction.RIGHT]!!,
        faces[6]!!.edges[Direction.RIGHT]!! to faces[1]!!.edges[Direction.RIGHT]!!,
        faces[6]!!.edges[Direction.UP]!! to faces[4]!!.edges[Direction.RIGHT]!!,
    )
    check(edges.size == 4 * 6)
    edges.forEach { (k, v) ->
        check(edges[v] == k)
    }
    return Cube(faces, edges)
}