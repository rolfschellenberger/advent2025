package com.rolf.util

import org.junit.Assert.*
import org.junit.Test

class BlockTest {

    @Test
    fun testConstructor() {
        val block = Block(0..10, 10..20)
        assertEquals(0..10, block.x)
        assertEquals(10..20, block.y)
    }

    @Test
    fun testContains() {
        val block = Block(0..10, 10..20)
        assertTrue(block.contains(Point(10, 10)))
        assertTrue(block.contains(Point(0, 10)))
        assertTrue(block.contains(Point(0, 20)))
        assertTrue(block.contains(Point(5, 15)))
        assertFalse(block.contains(Point(-1, 15)))
        assertFalse(block.contains(Point(11, 15)))
        assertFalse(block.contains(Point(5, 9)))
        assertFalse(block.contains(Point(5, 21)))
    }

    @Test
    fun testToPoints() {
        val block = Block(0..10, 10..20)
        assertEquals(121, block.toPoints().size)
    }

    @Test
    fun testOverlaps() {
        val block = Block(0..10, 10..20)
        assertTrue(block.overlaps(Block(5..15, 20..100)))
        assertTrue(block.overlaps(Block(5..15, 20..20)))
        assertFalse(block.overlaps(Block(100..200, 0..10)))
        assertFalse(block.overlaps(Block(11..200, 100..200)))
        assertFalse(block.overlaps(Block(10..20, 100..200)))
        assertFalse(block.overlaps(Block(100..200, 21..200)))
    }
}
