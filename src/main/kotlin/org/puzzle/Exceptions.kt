package org.puzzle

open class InvalidStructureException(message: String) : java.lang.Exception(message)
class NonAdjacentFramedInPieceException : InvalidStructureException("Try to build a piece with non adjacent frames")
class NegativeCoordinateFramedInPieceException : InvalidStructureException("Try to build a piece with non adjacent frames")
