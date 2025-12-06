package com.rolf.day06

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.splitLines
import java.util.regex.Pattern

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val values = lines.map { line ->
            line.trim().split(Pattern.compile(" +"))
        }

        val numbers = values.take(values.size - 1).map { it.map { it.toLong() } }
        val operators = values.last()

        var sum = 0L
        for ((index, operator) in operators.withIndex()) {
            sum += calculate(operator, numbers, index)
        }
        println(sum)
    }

    private fun calculate(operator: String, numbers: List<List<Long>>, index: Int): Long {
        val group = mutableListOf<Long>()
        for (number in numbers) {
            group.add(number[index])
        }
        return calculate(operator, group)
    }

    override fun solve2(lines: List<String>) {
        val input = splitLines(lines)
        val width = input.maxOf { it.size }
        val matrix = MatrixString.buildDefault(width, lines.size, " ")
        for ((y, row) in input.withIndex()) {
            for ((x, number) in row.withIndex()) {
                matrix.set(x, y, number)
            }
        }

        var sum = 0L
        var operator = ""
        val numbers = mutableListOf<Long>()
        for (column in matrix.getColumns()) {
            if (column.all { it == " " }) {
                sum += calculate(operator, numbers)
                operator = ""
                numbers.clear()
            } else {
                if (column.last() != " ") {
                    operator = column.last()
                }

                val number = column.take(column.size - 1)
                    .filter { it.isNotBlank() }
                    .joinToString("")
                    .toLong()
                numbers.add(number)
            }
        }
        sum += calculate(operator, numbers)
        println(sum)
    }

    private fun calculate(operator: String, group: List<Long>): Long {
        return when (operator) {
            "+" -> group.sum()
            "*" -> group.reduce { a, b -> a * b }
            else -> throw IllegalArgumentException("Incorrect operator: $operator")
        }
    }
}
