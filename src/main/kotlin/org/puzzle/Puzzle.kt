package org.puzzle

import java.lang.System.currentTimeMillis

/*
pseudo code
tryWith(piece){
  for all positions in puzzle (1)
    for all piece's orientations (2)
      present piece on current position
      if piece can be placed
        place piece
        if puzzle is full
          terminate OK
        else
          tryWith(next piece)
          if not successful
            remove piece from current position
    end (2)
  end (1)
  terminate KO
}
*/

class Puzzle(
        /**
         * puzzle length
         */
        private val length: Int,
        /**
         * the result to obtain
         */
        private val mask: Array<Array<Pattern>> = Array(length) { Array(length) { Pattern.Plain } }) {

    /**
     * the actual result
     */
    private val grid: Array<Array<Pattern>> = Array(length) { Array(length) { Pattern.Empty } }
    /**
     * solution of the problem. A length x length matrix. If solution[i,j] = a Piece, the piece is at position i,j
     */
    private val solution: Array<Array<Piece?>> = Array(length) { Array(length) { null as Piece? } }
    /**
     * all positions to try
     */
    private val allPositions: Iterable<Pair<Int, Int>>
    /**
     * number of attempts performed to solve the puzzle
     */
    private var count: Long = 0

    init {
        this.allPositions = allPositions()
    }

    companion object {
        /**
         * Build a mask from a string
         */
        fun mask(s: String): Array<Array<Pattern>> {
            val nRows = s.split("\n").size
            val nCols = s.split("\n").map { it.split("|").size }.maxBy { it }!!.or(0)
            val mask = Array(nCols) { Array(nRows) { Pattern.Empty } }
            s.split("\n").forEachIndexed { rowIndex, row ->
                row.trim('|').split("|").forEachIndexed { colIndex, cell ->
                    mask[colIndex][rowIndex] = Pattern.of(cell)
                }
            }
            return mask
        }
    }

    /**
     * Solves the problem.
     * When returns true, the puzzle is solved
     */
    fun solve(pieces: Collection<Piece>): Boolean {
        count = 0
        val start = currentTimeMillis()
        val list = ArrayList(pieces)
        tryToAdd(list, 0)
        println("count = $count, ratio = ${count * 1000.0 / (currentTimeMillis() - start)} /sec, ${(currentTimeMillis() - start) * 1.0 / 1000} ")
        return isSolved()
    }

    /**
     * Put a piece on this puzzle at the given position
     */
    fun put(piece: Piece, x: Int, y: Int): Boolean {
        count += 1
        if (canPut(piece, x, y)) {
            // fill grid with given piece pattern
            piece.elements
                    .forEach { this.grid[it.x + x][it.y + y] = it.pattern }
            solution[x][y] = piece
            return true
        }
        return false
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (x in 0 until length) {
            for (y in 0 until length)
                sb.append("|").append(grid[y][x])
            sb.append("|\n")
        }
        sb.deleteCharAt(sb.lastIndex)
        return sb.toString()
    }

    fun printSolution() {
        this.solution.forEachIndexed { x, col ->
            col.forEachIndexed { y, cell ->
                if (cell != null) {
                    println("[$x,$y] => \n$cell")
                }
            }
        }
    }

    private fun tryToAdd(pieces: ArrayList<Piece>, index: Int): Boolean {
        if (index >= pieces.size) {
            return true
        }
        val piece = pieces[index]
        // when shuffling positions attempts, it seems that the resolution is quicker
        allPositions/*.shuffled()*/.forEach { position ->
            val (x, y) = position
            piece.allOrientations().forEach { pieceOnPositionToTry ->
                if (put(pieceOnPositionToTry, x, y)) {
                    if (isSolved()) {
                        return true
                    }
                    if (!tryToAdd(pieces, index + 1)) {
                        remove(pieceOnPositionToTry, x, y)
                    } else {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun isSolved(): Boolean {
        return this.grid.foldIndexed(true) { columnIndex, acc, column ->
            column.foldIndexed(acc) { rowIndex, acc2, b -> acc2 && b == mask[columnIndex][rowIndex] }
        }
    }

    private fun remove(piece: Piece, x: Int, y: Int) {
        piece.elements
                .forEach { this.grid[it.x + x][it.y + y] = Pattern.Empty }
        solution[x][y] = null
    }

    private fun canPut(piece: Piece, x: Int, y: Int): Boolean {
        piece.elements.forEach { element ->
            val translated = element.translate(x, y)
            val canPut =
                    isInsideGrid(translated.x, translated.y) &&
                            !hasElementAt(translated.x, translated.y) &&
                            matchRequiredPatterns(translated.x, translated.y, element)
            if (!canPut) {
                return false
            }
        }
        return true
    }

    private fun hasElementAt(x: Int, y: Int): Boolean {
        return this.grid[x][y] != Pattern.Empty
    }

    private fun matchRequiredPatterns(x: Int, y: Int, pieceElement: PieceElement): Boolean {
        return this.mask[x][y] == pieceElement.pattern
    }

    private fun isInsideGrid(x: Int, y: Int): Boolean {
        return x in 0 until length &&
                y in 0 until length
    }

    private fun allPositions(): Iterable<Pair<Int, Int>> {
        val allPositions = Array(length * length) { Pair(-1, -1) }
        var i = 0
        for (col in 0 until length) {
            for (line in 0 until length) {
                allPositions[i++] = Pair(col, line)
            }
        }
        return Iterable { allPositions.iterator() }
    }

}
