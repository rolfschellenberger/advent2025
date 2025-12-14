package com.rolf.day11

import com.rolf.Day

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val graph = buildGraph(lines)
        cache.clear()
        println(
            count(graph, "you", "out")
        )
    }

    private fun buildGraph(lines: List<String>): MutableMap<String, Set<String>> {
        val graph = mutableMapOf<String, Set<String>>()
        for (line in lines) {
            val split = line.replace(":", "").split(" ")
            val from = split.first()
            val to = split.subList(1, split.size).toSet()
            graph[from] = to
        }
        return graph
    }

    private val cache: MutableMap<Pair<String, String>, Long> = mutableMapOf()

    private fun count(
        graph: MutableMap<String, Set<String>>,
        from: String,
        to: String,
    ): Long {
        if (from == to) {
            return 1
        }

        if (cache.containsKey(from to to)) {
            return cache.getValue(from to to)
        }

        var sum = 0L
        for (next in graph.getOrDefault(from, emptySet())) {
            val count = count(graph, next, to)
            cache[next to to] = count
            sum += count
        }
        return sum
    }

    override fun solve2(lines: List<String>) {
        val graph = buildGraph(lines)
        cache.clear()
        println(
            count(graph, "svr", "fft") * count(graph, "fft", "dac") * count(graph, "dac", "out") +
                    count(graph, "svr", "dac") * count(graph, "dac", "fft") * count(graph, "fft", "out")
        )
    }
}
