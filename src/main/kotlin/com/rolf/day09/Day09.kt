package com.rolf.day09

import com.rolf.Day
import com.rolf.util.Block
import com.rolf.util.Point
import kotlin.math.absoluteValue

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val points = parsePoints(lines)
        var largest = 0L
        for (p1 in points) {
            for (p2 in points) {
                val width = (p1.x - p2.x).absoluteValue + 1L
                val height = (p1.y - p2.y).absoluteValue + 1L
                val surface = width * height
                largest = maxOf(largest, surface)
            }
        }
        println(largest)
    }

    override fun solve2(lines: List<String>) {
        val points = parsePoints(lines)
        val borders = parseBorders(points)

        var largest = 0L
        for (p1 in points) {
            for (p2 in points) {
                if (p1 != p2) {
                    // Make the area 1 smaller, so the borders are removed (this is where borders can be)
                    val minX = minOf(p1.x, p2.x) + 1
                    val maxX = maxOf(p1.x, p2.x) - 1
                    val minY = minOf(p1.y, p2.y) + 1
                    val maxY = maxOf(p1.y, p2.y) - 1
                    // What if we take all areas between two points and check if there is any borderline hitting a point inside the area that is not its border?
                    if (hasNoBordersInside(Block(minX..maxX, minY..maxY), borders)) {
                        val width = (p1.x - p2.x).absoluteValue + 1L
                        val height = (p1.y - p2.y).absoluteValue + 1L
                        val surface = width * height
                        largest = maxOf(largest, surface)
                    }
                }
            }
        }
        println(largest)
    }

    private fun hasNoBordersInside(
        block: Block,
        borders: Set<Block>,
    ): Boolean {
        for (border in borders) {
            if (block.overlaps(border)) {
                return false
            }
        }
        return true
    }

    private fun parsePoints(lines: List<String>): List<Point> {
        return lines.map { line ->
            val (x, y) = line.split(",").map { it.toInt() }
            Point(x, y)
        }
    }

    private fun parseBorders(points: List<Point>): Set<Block> {
        val borders = mutableSetOf<Block>()
        for ((from, to) in points.zipWithNext() + listOf(points.last() to points.first())) {
            val minX = minOf(from.x, to.x)
            val maxX = maxOf(from.x, to.x)
            val minY = minOf(from.y, to.y)
            val maxY = maxOf(from.y, to.y)
            borders.add(
                Block(minX..maxX, minY..maxY)
            )
        }
        return borders
    }
}
