package com.rolf.day04

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
        var sum = 0
        val rolls = matrix.find("@")
        for (roll in rolls) {
            if (getNeighbourRolls(roll, matrix) < 4) {
                sum++
            }
        }
        println(sum)
    }

    override fun solve2(lines: List<String>) {
        val matrix = MatrixString.build(splitLines(lines))
        val copy = matrix.copy()

        var removed = true
        while (removed) {
            removed = false
            val rolls = matrix.find("@")
            for (roll in rolls) {
                if (getNeighbourRolls(roll, matrix) < 4) {
                    matrix.set(roll, ".")
                    removed = true
                }
            }
        }
        println(
            copy.count("@") - matrix.count("@")
        )
    }

    private fun getNeighbourRolls(point: Point, matrix: MatrixString): Int {
        return matrix.getNeighbours(point)
            .map { matrix.get(it) }
            .filter { it == "@" }
            .size
    }
}
