package com.rolf.day07

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.Point
import com.rolf.util.splitLines

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val matrix = MatrixString.build(splitLines(lines))
        val start = matrix.find("S").first()
        val startPoints = mutableSetOf(start)
        val splitPoints = mutableSetOf<Point>()
        while (startPoints.isNotEmpty()) {
            val from = startPoints.first()
            startPoints.remove(from)

            val splitPoint = findSplitPoint(matrix, from)
            if (splitPoint != null) {
                splitPoints.add(splitPoint)
                val left = matrix.getLeft(splitPoint)
                if (left != null) {
                    startPoints.add(left)
                }
                val right = matrix.getRight(splitPoint)
                if (right != null) {
                    startPoints.add(right)
                }
            }
        }
        println(splitPoints.size)
    }

    private fun findSplitPoint(matrix: MatrixString, from: Point): Point? {
        var location: Point? = from
        while (location != null) {
            val value = matrix.get(location)
            if (value == "^") return location
            location = matrix.getDown(location)
        }
        return null
    }

    override fun solve2(lines: List<String>) {
        val matrix = MatrixString.build(splitLines(lines))
        val start = matrix.find("S").first()
        println(
            findPaths(start, matrix)
        )
    }

    private fun findPaths(start: Point, matrix: MatrixString, cache: MutableMap<Point, Long> = mutableMapOf()): Long {
        // Reached the bottom
        if (start.y >= matrix.height() - 1) {
            return 1
        }

        val cached = cache[start]
        if (cached != null) {
            return cached
        }

        val value = matrix.get(start)
        if (value == "^") {
            var paths = 0L
            val left = matrix.getLeft(start)
            if (left != null) {
                paths += findPaths(left, matrix, cache)
            }
            val right = matrix.getRight(start)
            if (right != null) {
                paths += findPaths(right, matrix, cache)
            }
            cache[start] = paths
            return paths
        } else {
            val next = matrix.getDown(start)!!
            val paths = findPaths(next, matrix, cache)
            cache[start] = paths
            return paths
        }
    }
}
