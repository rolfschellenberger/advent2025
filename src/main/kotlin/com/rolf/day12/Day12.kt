package com.rolf.day12

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.groupLines
import com.rolf.util.splitLines
import java.util.regex.Pattern

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val elements = groupLines(lines, "")
        val presents = parsePresents(elements.take(elements.size - 1))
        val regions = parseRegions(elements.last())

        // Build all possible present shapes, since they can be flipped and rotated
        val allPresentShapes = presents.groupBy { it.id }

        var sum = 0
        for (region in regions) {
            // Check how many space the presents require
            val presentSpaceNeeded = region.presentCounts.mapIndexed { index, count ->
                count * allPresentShapes[index]!!.first().shape.count("#")
            }.sum()

            // If the region offers enough space, it's possible to arrange it (strangely!?!)
            if (presentSpaceNeeded < region.width * region.height) {
                sum++
            }
        }
        println(sum)
    }

    private fun parsePresents(groups: List<List<String>>): List<Present> {
        return groups.map { lines ->
            parsePresent(lines)
        }
    }

    private fun parsePresent(lines: List<String>): Present {
        val id = lines[0].replace(":", "").toInt()
        val shape = MatrixString.build(splitLines(lines.subList(1, lines.size)))
        return Present(id, shape)
    }

    private fun parseRegions(lines: List<String>): List<Region> {
        return lines.map { line -> parseRegion(line) }
    }

    private fun parseRegion(line: String): Region {
        val numbers = line.split(Pattern.compile("\\D+")).map { it.toInt() }
        return Region(
            numbers[0],
            numbers[1],
            numbers.subList(2, numbers.size),
        )
    }

    override fun solve2(lines: List<String>) {
    }
}

data class Present(
    val id: Int,
    val shape: MatrixString,
)

data class Region(
    val width: Int,
    val height: Int,
    val presentCounts: List<Int>,
)
