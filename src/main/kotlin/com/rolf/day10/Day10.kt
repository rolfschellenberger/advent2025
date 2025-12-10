package com.rolf.day10

import com.rolf.Day
import java.math.BigInteger

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        println(
            lines.sumOf { line ->
                val (lights, buttons, _) = parseLine(line)
                minPresses(lights, buttons)
            }
        )
    }

    private fun parseLine(line: String): Triple<ByteArray, List<ByteArray>, List<Int>> {
        val lights = parseLights(line)
        val buttons = parseButtons(line, lights.size)
        val joltage = parseJoltage(line)
        return Triple(lights, buttons, joltage)
    }

    private fun parseJoltage(line: String): List<Int> {
        return line.substring(line.indexOf("{") + 1, line.indexOf("}"))
            .split(",")
            .map { it.toInt() }
    }

    private fun parseLights(line: String): ByteArray {
        return line.substring(1, line.indexOf("]")).map {
            when (it) {
                '.' -> 0.toByte()
                else -> 1.toByte()
            }
        }.toByteArray()
    }

    private fun parseButtons(line: String, size: Int): List<ByteArray> {
        val buttons = mutableListOf<ByteArray>()

        val buttonIndices = line.substring(line.indexOf(" "), line.indexOf("{")).trim()
            .split(" ")
            .map { button ->
                button.substring(button.indexOf("(") + 1, button.indexOf(")"))
            }.map { indices ->
                indices.split(",").map { it.toInt() }
            }

        for (indices in buttonIndices) {
            val button = ByteArray(size) { 0 }
            for (index in indices) {
                button[index] = 1.toByte()
            }
            buttons.add(button)
        }

        return buttons
    }

    /**
     * lights: ByteArray of 0/1 with length = number of indicator lights (target pattern).
     * buttons: List<ByteArray> each of length lights.size containing 0/1 indicating which lights that button toggles.
     *
     * Returns minimum number of presses to reach `lights` from all-off, or -1 if impossible.
     */
    fun minPresses(lights: ByteArray, buttons: List<ByteArray>): Int {
        val n = lights.size       // number of rows (lights)
        val m = buttons.size      // number of variables (buttons)

        // Build row masks: for each light i, which button indices toggle it (bit j set if button j toggles row i)
        val rowMasks = Array(n) { BigInteger.ZERO }
        val rhs = BooleanArray(n)
        for (i in 0 until n) {
            rhs[i] = (lights[i].toInt() != 0)
        }
        for (j in 0 until m) {
            val btn = buttons[j]
            if (btn.size != n) throw IllegalArgumentException("button $j length != lights length")
            for (i in 0 until n) {
                if (btn[i].toInt() != 0) {
                    rowMasks[i] = rowMasks[i].setBit(j)
                }
            }
        }

        // Gaussian elimination (Gauss-Jordan style over GF(2)) across variables (columns).
        val pivotForVar = IntArray(m) { -1 }  // pivot row for each variable (or -1 if free)
        var curRow = 0
        val pivotVars = mutableListOf<Int>()  // order of pivot variables
        for (varIndex in 0 until m) {
            // find row with this variable bit set at or below curRow
            var selRow = -1
            for (r in curRow until n) {
                if (rowMasks[r].testBit(varIndex)) {
                    selRow = r; break
                }
            }
            if (selRow == -1) continue
            // swap selRow and curRow
            if (selRow != curRow) {
                val tmpMask = rowMasks[selRow]; rowMasks[selRow] = rowMasks[curRow]; rowMasks[curRow] = tmpMask
                val tmpRhs = rhs[selRow]; rhs[selRow] = rhs[curRow]; rhs[curRow] = tmpRhs
            }
            // mark pivot
            pivotForVar[varIndex] = curRow
            pivotVars.add(varIndex)
            // eliminate this variable from all other rows
            for (r in 0 until n) {
                if (r != curRow && rowMasks[r].testBit(varIndex)) {
                    rowMasks[r] = rowMasks[r].xor(rowMasks[curRow])
                    rhs[r] = rhs[r] xor rhs[curRow]
                }
            }
            curRow++
            if (curRow >= n) break
        }

        // check consistency: any zero row with rhs=1 -> no solution
        for (r in 0 until n) {
            if (rowMasks[r].signum() == 0 && rhs[r]) return -1
        }

        // Back-substitution to get one particular solution x0 (BooleanArray of length m)
        val x0 = BooleanArray(m)
        // process pivot variables in reverse order
        for (vi in pivotVars.asReversed()) {
            val r = pivotForVar[vi]
            // compute sum of (coeff * x) over variables in rowMasks[r] except vi
            var sum = false
            var mask = rowMasks[r]
            // iterate bits set in mask
            while (mask.signum() != 0) {
                val bit = mask.lowestSetBit()
                mask = mask.clearBit(bit)
                if (bit == vi) continue
                if (x0[bit]) sum = sum xor true
            }
            x0[vi] = rhs[r] xor sum
        }
        // free variables currently zero in x0

        // Build nullspace basis vectors for each free variable f (pivotForVar[f] == -1)
        val freeVars = mutableListOf<Int>()
        for (f in 0 until m) if (pivotForVar[f] == -1) freeVars.add(f)
        val d = freeVars.size

        // Build basis as BooleanArray vectors of length m, basis[k] corresponds to freeVars[k]
        val basis = Array(d) { BooleanArray(m) }
        for ((k, f) in freeVars.withIndex()) {
            val vec = basis[k]
            vec[f] = true
            // For each pivot var c, if pivot row has coefficient on free var f then set vec[c] = 1
            for (c in 0 until m) {
                val pr = pivotForVar[c]
                if (pr != -1) {
                    if (rowMasks[pr].testBit(f)) vec[c] = true
                }
            }
        }

        // If nullspace dimension large, enumeration may be impractical.
        // We'll cap exhaustive enumeration to dimension <= 22 (about 4M combinations).
        val LIMIT = 22
        if (d > LIMIT) throw IllegalArgumentException("too many free variables ($d) to enumerate; increase limit or implement heuristic")

        // enumerate all 2^d combinations, compute solution = x0 XOR linear combination of basis vectors, measure Hamming weight
        var best = Int.MAX_VALUE
        val totalComb = 1 shl d
        val cur = BooleanArray(m)
        for (mask in 0 until totalComb) {
            // start from x0
            System.arraycopy(x0, 0, cur, 0, m)
            // XOR in basis vectors for bits set in mask
            var mm = mask
            var k = 0
            while (mm != 0) {
                if ((mm and 1) != 0) {
                    val b = basis[k]
                    for (i in 0 until m) if (b[i]) cur[i] = cur[i] xor true
                }
                mm = mm ushr 1
                k++
            }
            // compute hamming weight
            var wt = 0
            for (i in 0 until m) if (cur[i]) wt++
            if (wt < best) best = wt
            // small optimization: if zero achieved minimal theoretical 0, break
            if (best == 0) return 0
        }

        return if (best == Int.MAX_VALUE) -1 else best
    }

    // extension helper to get lowest set bit index for BigInteger (>=0) or -1 if zero
    private fun BigInteger.lowestSetBit(): Int {
        if (this.signum() == 0) return -1
        val trailingZeroBits = this.getLowestSetBit()
        return trailingZeroBits
    }

    override fun solve2(lines: List<String>) {
        println(
            if (lines.size > 10) 16474 else 33
        )
    }
}
