package org.puzzle

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PieceTest {

    @Test
    fun `can build with a simple element(1)`() {
        val piece = Piece("|x|")
        assertThat(piece.elementsCount).isEqualTo(1)
    }

    @Test
    fun `can build with a simple element(2)`() {
        val piece = Piece("|x|o|")
        assertThat(piece.elementsCount).isEqualTo(2)
    }

    @Test
    fun `can build with a simple element(3)`() {
        val piece = Piece("""
            |x|o|x|
            | |x| |
            """".trimIndent())
        assertThat(piece.elementsCount).isEqualTo(4)
    }

    @Test(expected = NonAdjacentElementInPieceException::class)
    fun `can not build with no adjacent elements`() {
        Piece("""
            |X| |
            | |X|
        """.trimIndent())
    }

    @Test(expected = NonAdjacentElementInPieceException::class)
    fun `can not build with no adjacent elements(2)`() {
        Piece("""
            |X| |X|
            | | |X|
        """.trimIndent())
    }

    @Test(expected = NegativeCoordinateElementInPieceException::class)
    fun `can not build with elements not on positive x,positive y quadrant`() {
        Piece(listOf(PieceElement(0, 0), PieceElement(-1, 0)))
    }

    @Test
    fun `rotation 1 quarter`() {
        val initial = Piece("""
            |x| |
            |x|x|
        """.trimIndent())
        val expected = Piece("""
            | |x|
            |x|x|
        """.trimIndent())
        assertThat(initial.rotate(1)).isEqualTo(expected)
    }

    @Test
    fun `rotation 1 quarter(2)`() {
        val initial = Piece("""
            |x| | |
            |x|x|x|
        """.trimIndent())
        val expected = Piece("""
            | |x|
            | |x|
            |x|x|
        """.trimIndent())
        assertThat(initial.rotate(1)).isEqualTo(expected)
    }

    @Test
    fun `rotation 2 quarter`() {
        val initial = Piece("""
            |x| |
            |x|x|
        """.trimIndent())
        val expected = Piece("""
            |x|x|
            | |x|
        """.trimIndent())
        assertThat(initial.rotate(2)).isEqualTo(expected)
    }

    @Test
    fun `rotation 3 quarter`() {
        val initial = Piece("""
            |x| |
            |x|x|
        """.trimIndent())
        val expected = Piece("""
            |x|x|
            |x| |
        """.trimIndent())
        assertThat(initial.rotate(3)).isEqualTo(expected)
    }

    @Test
    fun `rotation 4 quarter`() {
        val initial = Piece("""
            |x| |
            |x|x|
        """.trimIndent())
        val expected = Piece("""
            |x| |
            |x|x|
        """.trimIndent())
        assertThat(initial.rotate(4)).isEqualTo(expected)
    }

    @Test
    fun `rotation of a cross piece`() {
        val piece = Piece("""
            | |X| |
            |X|X|X|
            | |X| |
        """.trimIndent())
        assertThat(piece.rotate(1)).isEqualTo(piece)
        assertThat(piece.rotate(2)).isEqualTo(piece)
        assertThat(piece.rotate(3)).isEqualTo(piece)
        assertThat(piece.rotate(4)).isEqualTo(piece)
    }

    @Test
    fun invert() {
        val initial = Piece("""
            |x| |
            |x|x|
        """.trimIndent())
        val expected = Piece("""
            | |x|
            |x|x|
        """.trimIndent())
        assertThat(initial.invert()).isEqualTo(expected)
    }

    @Test
    fun `invert of a cross piece`() {
        val piece = Piece("""
            | |X| |
            |X|X|X|
            | |X| |
        """.trimIndent())
        assertThat(piece.invert()).isEqualTo(piece)
    }

    @Test
    fun `all orientations`() {
        val piece = Piece("""
           |X| | |
           |X|X|X|
       """.trimIndent())
        val allOrientations = piece.allOrientations()
        assertThat(allOrientations)
                .contains(piece)
                .contains(piece.rotate(1))
                .contains(piece.rotate(2))
                .contains(piece.rotate(3))
                .contains(piece.invert())
                .contains(piece.invert().rotate(1))
                .contains(piece.invert().rotate(2))
                .contains(piece.invert().rotate(3))
        assertThat(allOrientations).hasSize(8)
    }

    @Test
    fun `all orientations (2)`() {
        val piece = Piece("""
           | |X| |
           |X|X|X|
           | |X| |
       """.trimIndent())
        val allOrientations = piece.allOrientations()
        assertThat(allOrientations).hasSize(1)
    }

    @Test
    fun `toString 1`() {
        testToString("""
            |x|""".trimIndent())
    }

    @Test
    fun `toString 2`() {
        testToString("""
            |x|x|""".trimIndent())
    }

    @Test
    fun `toString 3`() {
        testToString("""
            |x|x|x|""".trimIndent())
    }

    @Test
    fun `toString 4`() {
        testToString("""
            |x|
            |x|
            |x|
            """.trimIndent())
    }

    private fun testToString(s: String) {
        assertThat(s).isEqualToIgnoringCase(Piece(s).toString())
    }
}

