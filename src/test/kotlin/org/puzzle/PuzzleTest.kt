package org.puzzle

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.File

class PuzzleTest {
    @Test
    fun `put piece ok`() {
        val puzzle = Puzzle(3)
        val piece = Piece("""
            |x|x|
            | |x|""".trimIndent())

        val res: Boolean = puzzle.put(piece, 0, 0)

        assertThat(res).isTrue()
        assertThat(puzzle.toString()).isEqualToIgnoringCase("""
            |X|X| |
            | |X| |
            | | | |""".trimIndent())
    }

    @Test
    fun `put 2 piece ok`() {
        val puzzle = Puzzle(4)
        val piece1 = Piece("""
            |x|x| |
            | |x|x|""".trimIndent())
        val piece2 = Piece("""
            |x|x|
            | |x|""".trimIndent())


        puzzle.put(piece1, 0, 0)

        assertThat(puzzle.put(piece2, 2, 0)).isTrue()
        assertThat(puzzle.put(piece2, 2, 0)).isFalse()
        assertThat(puzzle.toString()).isEqualToIgnoringCase("""
            |X|X|X|X|
            | |X|X|X|
            | | | | |
            | | | | |""".trimIndent())
    }

    @Test
    fun `put piece KO`() {
        val puzzle = Puzzle(3)
        val piece = Piece("""
            |x|x|
            | |x|""".trimIndent())

        val res: Boolean = puzzle.put(piece, 2, 0)

        assertThat(res).isFalse()
        assertThat(puzzle.toString()).isEqualTo("""
            | | | |
            | | | |
            | | | |""".trimIndent())
    }

    @Test
    fun `put pieces (1)`() {
        val puzzle = Puzzle(3)
        puzzle.put(Piece("""
            |x|x|""".trimIndent()), 0, 0)
        assertThat(puzzle.toString()).isEqualToIgnoringCase("""
            |x|x| |
            | | | |
            | | | |""".trimIndent())
        puzzle.put(Piece("""
            | |x|
            |x|x|""".trimIndent()), 1, 0)
        assertThat(puzzle.toString()).isEqualToIgnoringCase("""
            |x|x|x|
            | |x|x|
            | | | |""".trimIndent())
        puzzle.put(Piece("""
            |x| |
            |x|x|""".trimIndent()), 0, 1)
        assertThat(puzzle.toString()).isEqualToIgnoringCase("""
            |x|x|x|
            |x|x|x|
            |x|x| |""".trimIndent())
    }

    @Test
    fun `solve 3 (1)`() {
        val puzzle = Puzzle(3)
        val p1 = Piece("""
            |x|x|x|""".trimIndent())
        assertThat(puzzle.solve(listOf(p1, p1, p1))).isTrue()
    }

    @Test
    fun `solve 3 (2)`() {
        val puzzle = Puzzle(3)
        val p1 = Piece("""
            |x|
            |x|
            |x|""".trimIndent())
        assertThat(puzzle.solve(listOf(p1, p1, p1))).isTrue()
    }

    @Test
    fun `solve 3 (3)`() {
        val puzzle = Puzzle(3)
        val p1 = Piece("""
            |x|
            |x|
            |x|""".trimIndent())
        val p2 = Piece("""
            |x|x|
            |x| |""".trimIndent())
        println(puzzle)
        puzzle.printSolution()
        assertThat(puzzle.solve(listOf(p1, p2, p2))).isTrue()
    }

    @Test
    fun `solve 4 (1)`() {
        val puzzle = Puzzle(4)
        val p1 = Piece("""
            | |x| |
            |x|x|X|
            | |x| |""".trimIndent())
        val p2 = Piece("""
            |x|x|
            |x| |""".trimIndent())
        val p3 = Piece("""
            |x|""".trimIndent())
        val p4 = Piece("""
            |x|x|
            |x|x|""".trimIndent())
        val result = puzzle.solve(listOf(p1, p2, p2, p3, p4))
        println(puzzle)
        puzzle.printSolution()
        assertThat(result).isTrue()
    }

    @Test
    fun `solve 5 (1)`() {
        val puzzle = Puzzle(5)
        val p1 = Piece("""
            | |x| |
            | |x|X|
            |x|x| |""".trimIndent())
        val p2 = Piece("""
            |x|x|
            |x| |""".trimIndent())
        val p3 = Piece("""
            |x|x|""".trimIndent())
        val p4 = Piece("""
            | |x|
            |x|x|
            | |x|""".trimIndent())
        val p5 = Piece("""
            | |x|
            | |x|
            |x|x|""".trimIndent())
        val p6 = Piece("""
            |x|
            |x|""".trimIndent())
        val p7 = Piece("""
            |x|""".trimIndent())
        val result = puzzle.solve(listOf(p1, p2, p2, p3, p4, p5, p6, p7, p7).shuffled())
        println(puzzle)
        puzzle.printSolution()
        assertThat(result).isTrue()
    }

    @Test
//    @Ignore("sometimes too long to solve")
    fun `solve 6 (1)`() {
        val puzzle = Puzzle(6)
        val p1 = Piece("""
            |x|""".trimIndent())
        val p2 = Piece("""
            |x|x|
            |x| |""".trimIndent())
        val p3 = Piece("""
            | | |x|
            |x|x|x|""".trimIndent())
        val result = puzzle.solve(listOf(p1, p1, p1, p1, p1, p2, p2, p2, p2, p2, p3, p3, p3, p3).shuffled())
        println(puzzle)
        puzzle.printSolution()
        assertThat(result).isTrue()
    }

    @Test
    fun `solve 6 (2)`() {
        val length = 6
        val puzzle = Puzzle(length)
        val result =
                puzzle.solve(listOf(
                        Piece("""
                | |x|x|
                |x|x| |""".trimIndent()),
                        Piece("""
                |x| | |
                |x|x|x|""".trimIndent()),
                        Piece("""
                |x|x| |
                | |x|x|""".trimIndent()),
                        Piece("""
                |x|x|x|
                |x| | |""".trimIndent()),
                        Piece("""
                |x|x|x|
                | |x| |""".trimIndent()),
                        Piece("""
                | |x| |
                |x|x|x|""".trimIndent()),
                        Piece("""
                |x|x|x|
                |x| | |""".trimIndent()),
                        Piece("""
                |x|x| |
                | |x|x|""".trimIndent()),
                        Piece("""
                |x|x|x|
                |x| | |""".trimIndent())
                ).shuffled())
        println(puzzle)
        puzzle.printSolution()
        assertThat(result).isTrue()
    }

    @Test
//    @Ignore
    fun `solve 6 holes`() {
        val length = 6
        val mask = Array(length) { Array(length) { Pattern.Plain } }
        mask[1][0] = Pattern.Hole
        mask[2][4] = Pattern.Hole
        val puzzle = Puzzle(length, mask)
        val result =
                puzzle.solve(listOf(
                        Piece("""
                | |x|x|
                |x|x| |""".trimIndent()),
                        Piece("""
                |x| | |
                |x|x|x|""".trimIndent()),
                        Piece("""
                |x|x| |
                | |x|x|""".trimIndent()),
                        Piece("""
                |x|x|x|
                |x| | |""".trimIndent()),
                        Piece("""
                |x|o|x|
                | |x| |""".trimIndent()),
                        Piece("""
                | |x| |
                |x|x|x|""".trimIndent()),
                        Piece("""
                |x|x|x|
                |x| | |""".trimIndent()),
                        Piece("""
                |o|x| |
                | |x|x|""".trimIndent()),
                        Piece("""
                |x|x|x|
                |x| | |""".trimIndent())
                ).shuffled())
        println(puzzle)
        puzzle.printSolution()
        assertThat(result).isTrue()
    }

    @Test
    fun `solve real cases`() {
        val length = 6
        var readPieces = false
        val pieces = arrayListOf<Piece>()
        File("src/test/kotlin/org/puzzle/realCases.txt").readText()
                .split("--".toRegex(RegexOption.MULTILINE))
                .drop(1)
                .filter { it.isNotEmpty() }
                .forEachIndexed { index, s ->
                    if (s.trim().startsWith("piece")) {
                        readPieces = true
                    } else if (s.trim().startsWith("case")) {
                        readPieces = false
                    }
                    if (readPieces) {
                        if (index % 2 == 0) {
                            println(s)
                        } else {
                            pieces.add(Piece(s.trimIndent()))
                        }
                    } else {
                        if (index % 2 == 0) {
                            println(s)
                        } else {
                            val puzzle = Puzzle(length, Puzzle.mask(s.trimIndent()))
                            val result = puzzle.solve(pieces.shuffled())
                            println(puzzle)
                            puzzle.printSolution()
                            assertThat(result).isTrue()
                        }
                    }
                }
    }
}
