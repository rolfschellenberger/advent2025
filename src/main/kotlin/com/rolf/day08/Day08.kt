package com.rolf.day08

import com.rolf.Day
import com.rolf.util.Location
import com.rolf.util.splitLine

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val locations = parseLocations(lines)
        val distances = findDistances(locations)

        // Start adding them to groups of circuits
        val groups = mutableListOf<MutableSet<Location>>()

        val distanceTotal = distances.size
        val take = if (distanceTotal < 1000) 10 else 1000
        while (distances.size > distanceTotal - take) {
            moveToGroups(distances, groups)
        }

        val numbers = groups.map { it.size }
        println(
            numbers.sortedDescending()
                .take(3)
                .reduce { a, b -> a * b }
        )
    }

    private fun parseLocations(lines: List<String>): List<Location> {
        return lines.map { line ->
            val (x, y, z) = splitLine(line, ",").map { it.toInt() }
            Location(x, y, z)
        }
    }

    private fun findDistances(locations: List<Location>): MutableList<Pair<Pair<Location, Location>, Double>> {
        val seen = mutableSetOf<Pair<Location, Location>>()
        val distances = mutableListOf<Pair<Pair<Location, Location>, Double>>()

        for (location in locations) {
            for (otherLocation in locations) {
                if (location != otherLocation) {
                    if (seen.add(Pair(location, otherLocation)) &&
                        seen.add(Pair(otherLocation, location))
                    ) {
                        val distance = location.straightLineDistance(otherLocation)
                        distances.add((location to otherLocation) to distance)
                    }
                }
            }
        }
        return distances.sortedBy { (_, distance) ->
            distance
        }.toMutableList()
    }

    private fun moveToGroups(
        distances: MutableList<Pair<Pair<Location, Location>, Double>>,
        groups: MutableList<MutableSet<Location>>,
    ): Pair<Pair<Location, Location>, Double> {
        val next = distances.removeFirst()
        val loc1 = next.first.first
        val loc2 = next.first.second

        val containingGroups = groups.filter {
            it.contains(loc1) || it.contains(loc2)
        }
        if (containingGroups.isEmpty()) {
            groups.add(mutableSetOf(loc1, loc2))
        } else if (containingGroups.size == 1) {
            containingGroups[0].add(loc1)
            containingGroups[0].add(loc2)
        } else {
            groups.removeAll(containingGroups)
            val newLocations = containingGroups.flatten() + loc1 + loc2
            groups.add(newLocations.toMutableSet())
        }
        return next
    }

    override fun solve2(lines: List<String>) {
        val locations = parseLocations(lines)
        val distances = findDistances(locations)

        // Start adding them to groups of circuits
        val groups = mutableListOf<MutableSet<Location>>()

        while (true) {
            val next = moveToGroups(distances, groups)
            val loc1 = next.first.first
            val loc2 = next.first.second

            // Stop?
            if (groups.size == 1 && groups.flatten().toSet().size == locations.size) {
                println(loc1.x.toLong() * loc2.x)
                break
            }
        }
    }
}
