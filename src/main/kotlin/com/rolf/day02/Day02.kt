package com.rolf.day02

import com.rolf.Day
import com.rolf.util.isOdd
import com.rolf.util.splitLine

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val ranges = parseRanges(lines.first())
        var sum = 0L
        for (range in ranges) {
            for (value in range) {
                if (isInvalid(value)) {
                    sum += value
                }
            }
        }
        println(sum)
    }

    private fun parseRanges(line: String): List<LongRange> {
        val ranges = splitLine(line, ",")
        return ranges.map { parseRange(it) }
    }

    private fun parseRange(range: String): LongRange {
        val (from, to) = splitLine(range, "-")
        return LongRange(from.toLong(), to.toLong())
    }

    private fun isInvalid(value: Long): Boolean {
        val string = value.toString()
        if (string.length.isOdd()) return false
        val (left, right) = string.chunked(string.length / 2)
        return left == right
    }

    override fun solve2(lines: List<String>) {
        val ranges = parseRanges(lines.first())
        var sum = 0L
        for (range in ranges) {
            for (value in range) {
                if (isInvalid2(value)) {
                    sum += value
                }
            }
        }
        println(sum)
    }

    private fun isInvalid2(value: Long): Boolean {
        val string = value.toString()
        for (i in 1..(string.length / 2)) {
            val parts = string.chunked(i).toSet()
            if (parts.size == 1) return true
        }
        return false
    }
}
