package org.puzzle

open class InvalidStructureException(message: String) : java.lang.Exception(message)
class NonAdjacentElementInPieceException : InvalidStructureException("Try to build a piece with non adjacent elements")
class NegativeCoordinateElementInPieceException : InvalidStructureException("Try to build a piece with non adjacent elements")
