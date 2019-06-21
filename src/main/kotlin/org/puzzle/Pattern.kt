package org.puzzle

/**
 * characterization of a element of a Piece.
 * Is it empty (no material), has a hole, or is plain (no hole)
 */
enum class Pattern {
    Empty,
    Plain,
    Hole;

    companion object {
        /**
         * build from a textual representation
         */
        fun of(x: String): Pattern {
            return when (x) {
                "0", "o", "O" -> Hole
                "x", "X" -> Plain
                else -> Empty
            }
        }
    }

    /**
     * useful for textual representation
     */
    override fun toString(): String {
        return when (this) {
            Empty -> " "
            Plain -> "X"
            Hole -> "O"
        }
    }
}
