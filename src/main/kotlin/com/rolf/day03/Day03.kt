package com.rolf.day03

import com.rolf.Day
import com.rolf.util.splitLine

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        println(
            lines.sumOf { toJoltage(it) }
        )
    }

    private fun toJoltage(line: String): Int {
        val numbers = splitLine(line).map { it.toInt() }
        // Find the largest number (except last)
        val withoutLast = numbers.subList(0, numbers.size - 1)
        val max = withoutLast.max()
        val index = withoutLast.indexOf(max)
        // Followed by a second-largest number
        val secondLargest = numbers.subList(index + 1, numbers.size).max()
        return "$max$secondLargest".toInt()
    }

    override fun solve2(lines: List<String>) {
        println(
            lines.sumOf { toJoltage2(it) }
        )
    }

    private fun toJoltage2(line: String): Long {
        val numbers = splitLine(line).map { it.toInt() }
        return findHighestNumber(numbers)
    }

    private fun findHighestNumber(numbers: List<Int>, length: Int = 12): Long {
        if (length == numbers.size) {
            return numbers.joinToString("").toLong()
        }

        // Get the highest number
        val withoutLast = numbers.subList(0, numbers.size - length + 1)
        val max = withoutLast.max()
        val index = withoutLast.indexOf(max)
        if (length == 1) {
            return max.toLong()
        }
        return "$max${findHighestNumber(numbers.subList(index + 1, numbers.size), length - 1)}".toLong()
    }
}
