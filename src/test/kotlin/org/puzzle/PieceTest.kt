package org.puzzle

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PieceTest {

    @Test
    fun `can build with a simple frame(1)`() {
        val piece = Piece("|x|")
        assertThat(piece.frameCount).isEqualTo(1)
    }

    @Test
    fun `can build with a simple frame(2)`() {
        val piece = Piece("|x|o|")
        assertThat(piece.frameCount).isEqualTo(2)
    }

    @Test
    fun `can build with a simple frame(3)`() {
        val piece = Piece("""
            |x|o|x|
            | |x| |
            """".trimIndent())
        assertThat(piece.frameCount).isEqualTo(4)
    }

    @Test(expected = NonAdjacentFramedInPieceException::class)
    fun `can not build with no adjacent frames`() {
        Piece("""
            |X| |
            | |X|
        """.trimIndent())
    }

    @Test(expected = NonAdjacentFramedInPieceException::class)
    fun `can not build with no adjacent frames (2)`() {
        Piece("""
            |X| |X|
            | | |X|
        """.trimIndent())
    }

    @Test(expected = NegativeCoordinateFramedInPieceException::class)
    fun `can not build with frames not on positive x,positive y quadrant`() {
        Piece(listOf(Frame(0, 0), Frame(-1, 0)))
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
    fun `turnaround`() {
        val initial = Piece("""
            |x| |
            |x|x|
        """.trimIndent())
        val expected = Piece("""
            | |x|
            |x|x|
        """.trimIndent())
        assertThat(initial.turnaround()).isEqualTo(expected)
    }

    @Test
    fun `turnaround of a cross piece`() {
        val piece = Piece("""
            | |X| |
            |X|X|X|
            | |X| |
        """.trimIndent())
        assertThat(piece.turnaround()).isEqualTo(piece)
    }

    @Test
    fun `all orientations`(){
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
                .contains(piece.turnaround())
                .contains(piece.turnaround().rotate(1))
                .contains(piece.turnaround().rotate(2))
                .contains(piece.turnaround().rotate(3))
        assertThat(allOrientations).hasSize(8)
    }

    @Test
    fun `all orientations (2)`(){
        val piece = Piece("""
           | |X| |
           |X|X|X|
           | |X| |
       """.trimIndent())
        val allOrientations = piece.allOrientations()
        assertThat(allOrientations).hasSize(1)
    }

    @Test
    fun `toString 1`(){
        testToString("""
            |x|""".trimIndent())
    }

    @Test
    fun `toString 2`(){
        testToString("""
            |x|x|""".trimIndent())
    }

    @Test
    fun `toString 3`(){
        testToString("""
            |x|x|x|""".trimIndent())
    }

    @Test
    fun `toString 4`(){
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

