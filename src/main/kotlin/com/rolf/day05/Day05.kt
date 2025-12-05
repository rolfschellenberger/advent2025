package com.rolf.day05

import com.rolf.Day
import com.rolf.util.groupLines
import com.rolf.util.join
import com.rolf.util.size

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val (fresh, available) = groupLines(lines, "")
        val freshIdRanges = toRanges(fresh)
        val ingredients = available.map { it.toLong() }
        val freshIngredients = ingredients.filter { ingredient ->
            freshIdRanges.any { it.contains(ingredient) }
        }
        println(freshIngredients.size)
    }

    private fun toRanges(fresh: List<String>): List<LongRange> {
        return fresh.map {
            val (from, to) = it.split("-").map { it.toLong() }
            from..to
        }
    }

    override fun solve2(lines: List<String>) {
        val (fresh, _) = groupLines(lines, "")
        val freshIdRanges = toRanges(fresh).sortedBy { it.first }

        val mergedRanges = mutableListOf(freshIdRanges.first())
        for (range in freshIdRanges) {
            var overlapped = false
            for ((index, lastRange) in mergedRanges.withIndex()) {
                val union = lastRange.join(range)
                if (union != null) {
                    mergedRanges[index] = union
                    overlapped = true
                    break
                }
            }
            if (!overlapped) {
                mergedRanges.add(range)
            }
        }

        println(
            mergedRanges.sumOf {
                it.size()
            }
        )
    }
}
