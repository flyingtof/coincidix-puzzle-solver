package org.puzzle

/**
 * A single Piece of the puzzle.
 * It is made of several elements
 */
class Piece {
    val elements: Set<PieceElement>

    constructor(elements: Iterable<PieceElement>) {
        var myPieceElements: Array<PieceElement> = emptyArray()
        elements.forEach {
            if (canAdd(it, myPieceElements)) {
                myPieceElements += it
            }
        }
        verify(myPieceElements)
        this.elements = myPieceElements.toSet()
    }

    constructor(string: String) : this(fromString(string))

    val elementsCount get() = elements.size


    fun rotate(rotationCount: Int): Piece {
        val rotatedElements = this.elements.map { it.rotate(rotationCount) }
        return moveToFirstQuadrant(rotatedElements)
    }

    fun invert(): Piece {
        val inverted = this.elements.map { it.invert() }
        return moveToFirstQuadrant(inverted)
    }

    private var _allOrientations: Collection<Piece> = emptyList()
    private var _allOrientationsComputed = false

    fun allOrientations(): Collection<Piece> {
        if (!_allOrientationsComputed) {
            val inverted = this.invert()
            // use a Set here to remove duplicated pieces
            _allOrientations = hashSetOf(this,
                    this.rotate(1), this.rotate(2), this.rotate(3),
                    inverted,
                    inverted.rotate(1), inverted.rotate(2), inverted.rotate(3))
            _allOrientationsComputed = true
        }
        return _allOrientations
    }

    override fun toString(): String {
        val minX = elements.minBy { it.x }!!.x
        val minY = elements.minBy { it.y }!!.y
        val maxX = elements.maxBy { it.x }!!.x
        val maxY = elements.maxBy { it.y }!!.y
        val patterns = Array(maxX - minX + 1) { Array(maxY - minY + 1) { Pattern.Empty } }
        elements.forEach { element -> patterns[element.x - minX][element.y - minY] = element.pattern }
        val sb = StringBuilder()
        for (y in minY..maxY) {
            for (x in minX..maxX)
                sb.append("|").append(patterns[x][y])
            sb.append("|\n")
        }
        if (sb.lastIndex >= 0) {
            sb.deleteCharAt(sb.lastIndex)
        }
        return sb.toString()
    }

    private fun moveToFirstQuadrant(elements: Iterable<PieceElement>): Piece {
        val minX = elements.minBy { it.x }!!.x
        val minY = elements.minBy { it.y }!!.y
        return Piece(elements.map { element -> element.translate(-minX, -minY) })
    }

    override fun equals(other: Any?): Boolean {
        return other is Piece && this.elements == other.elements
    }

    override fun hashCode(): Int {
        return this.elements.hashCode()
    }

    private fun canAdd(pieceElement: PieceElement, pieceElements: Array<PieceElement>): Boolean {
        if (pieceElements.isEmpty()) {
            return true
        }
        if (!isOnPositiveQuarter(pieceElement)) {
            throw NegativeCoordinateElementInPieceException()
        }
        return true
    }

    private fun isOnPositiveQuarter(pieceElement: PieceElement) =
            pieceElement.x >= 0 && pieceElement.y >= 0

    private fun verify(pieceElements: Array<PieceElement>) {
        if (pieceElements.size <= 1) {
            return
        }
        if (!areAllAdjacent(pieceElements)) {
            throw NonAdjacentElementInPieceException()
        }
    }

    private fun areAllAdjacent(pieceElements: Array<PieceElement>) =
            pieceElements.fold(true) { allAdjacent, element -> allAdjacent && isAdjacentToAnExistingElement(element, pieceElements) }

    private fun isAdjacentToAnExistingElement(pieceElement: PieceElement, pieceElements: Array<PieceElement>) =
            pieceElements.any { it.isAdjacentWith(pieceElement) }

    companion object {
        /**
         * build a Piece from a textual representation
         * E.g.:
         * | |x|x|
         * |x|o| |
         * When the letter is: <ul>
         * <li>'x': it is plain</li>
         * <li>'o': there is a hole</li>
         * <li>' ': it's empty</li>
         * </ul>
         * The character '|' is for better visualisation
         *
         */
        private fun fromString(string: String): Iterable<PieceElement> {
            var elements: Array<PieceElement> = emptyArray()
            string
                    .split("\n")
                    .map {
                        it.trim('|')
                                .split('|')
                    }
                    .forEachIndexed { indexY, list ->
                        list.forEachIndexed { indexX, s ->
                            val pattern = Pattern.of(s)
                            if (pattern != Pattern.Empty) {
                                elements += PieceElement(indexX, indexY, pattern)
                            }
                        }
                    }
            return elements.asIterable()
        }

    }
}
