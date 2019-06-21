package org.puzzle

import kotlin.math.abs

/**
 * An element of a Puzzle Piece.
 * It has a position, relative to the Piece origin and a pattern (Plain or With Hole)
 */
data class PieceElement(
        val x: Int,
        val y: Int,
        val pattern: Pattern = Pattern.Plain) {

    fun isAdjacentWith(pieceElement: PieceElement): Boolean {
        return (this.x == pieceElement.x || this.y == pieceElement.y)
                && (abs(this.x - pieceElement.x) == 1
                || abs(this.y - pieceElement.y) == 1)
    }

    fun rotate(n: Int): PieceElement {
        var res = this
        for (i in 0 until n) {
            res = res.rotate()
        }
        return res
    }

    fun translate(tx: Int, ty: Int): PieceElement {
        return PieceElement(x + tx, y + ty, pattern)
    }

    fun invert(): PieceElement {
        return PieceElement(-x, y, pattern)
    }

    private fun rotate(): PieceElement {
        return PieceElement(y, -x, pattern)
    }

}
