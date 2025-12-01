package com.rolf.day01

import com.rolf.Day
import kotlin.math.absoluteValue

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        var password = 0
        val safe = Safe(pointer = 50)
        val instructions = parseInstructions(lines)
        instructions.forEach { instruction ->
            when (instruction.direction) {
                "L" -> safe.left(instruction.amount)
                "R" -> safe.right(instruction.amount)
                else -> throw IllegalStateException("Unknown direction ${instruction.direction}")
            }
            if (safe.pointer == 0) password++
        }
        println(password)
    }

    override fun solve2(lines: List<String>) {
        val safe = Safe(pointer = 50)
        val instructions = parseInstructions(lines)
        instructions.forEach { instruction ->
            when (instruction.direction) {
                "L" -> safe.left(instruction.amount)
                "R" -> safe.right(instruction.amount)
                else -> throw IllegalStateException("Unknown direction ${instruction.direction}")
            }
        }
        println(safe.zero)
    }

    private fun parseInstructions(lines: List<String>): List<Instruction> {
        return lines.map { parseInstruction(it) }
    }

    private fun parseInstruction(line: String): Instruction {
        val direction = line.take(1)
        val amount = line.substring(1).toInt()
        return Instruction(direction, amount)
    }
}

data class Instruction(val direction: String, val amount: Int)

data class Safe(var pointer: Int = 0, val max: Int = 100) {
    var zero = 0

    fun left(amount: Int): Int {
        val newPointer = pointer - amount
        zero += newPointer.absoluteValue / max
        zero += if (newPointer <= 0 && pointer > 0) 1 else 0
        pointer = (pointer - amount).mod(max)
        return pointer
    }

    fun right(amount: Int): Int {
        val newPointer = pointer + amount
        zero += newPointer / max
        pointer = (pointer + amount).mod(max)
        return pointer
    }
}
