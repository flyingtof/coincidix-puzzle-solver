package org.puzzle

import java.lang.StringBuilder

class Piece {
    val frames: Set<Frame>

    constructor(frames: Iterable<Frame>) {
        var myFrames: Array<Frame> = emptyArray()
        frames.forEach {
            if (canAdd(it, myFrames)) {
                myFrames = myFrames.plus(it)
            }
        }
        verify(myFrames)
        this.frames = myFrames.toSet()
    }

    constructor(string: String) : this(fromString(string))

    val frameCount get() = frames.size


    fun rotate(rotationCount: Int): Piece {
        val rotatedFrames = this.frames.map { it.rotate(rotationCount) }
        return moveToFirstQuadrant(rotatedFrames)
    }

    fun turnaround(): Piece {
        val turnaroundFrames = this.frames.map { it.turnaround() }
        return moveToFirstQuadrant(turnaroundFrames)
    }

    private var _allOrientations: Collection<Piece> = emptyList()
    private var _allOrientationsComputed = false

    fun allOrientations(): Collection<Piece> {
        if (!_allOrientationsComputed){
            val turnaround = this.turnaround()
            _allOrientations = hashSetOf(this,
                    this.rotate(1), this.rotate(2), this.rotate(3),
                    turnaround,
                    turnaround.rotate(1), turnaround.rotate(2), turnaround.rotate(3))
            _allOrientationsComputed = true
        }
        return _allOrientations;
    }

    override fun toString(): String {
        val minX = frames.minBy { it.x }!!.x
        val minY = frames.minBy { it.y }!!.y
        val maxX = frames.maxBy { it.x }!!.x
        val maxY = frames.maxBy { it.y }!!.y
        val pixels = Array(maxX - minX + 1) { Array(maxY - minY + 1) { Pattern.Empty } }
        frames.forEach { frame -> pixels[frame.x - minX][frame.y - minY] = frame.pattern }
        val sb = StringBuilder()
        for (y in minY..maxY) {
            for (x in minX..maxX)
                sb.append("|").append(pixels[x][y])
            sb.append("|\n")
        }
        if (sb.lastIndex >=0){
            sb.deleteCharAt(sb.lastIndex)
        }
        return sb.toString()
    }

    private fun moveToFirstQuadrant(rotatedFrames: Iterable<Frame>): Piece {
        val minX = rotatedFrames.minBy { it.x }!!.x
        val minY = rotatedFrames.minBy { it.y }!!.y
        return Piece(rotatedFrames.map { it -> it.translate(-minX, -minY) })
    }

    override fun equals(other: Any?): Boolean {
        return other is Piece && this.frames == other.frames
    }

    override fun hashCode(): Int {
        return this.frames.hashCode()
    }

    private fun canAdd(frame: Frame, frames: Array<Frame>): Boolean {
        if (frames.isEmpty()) {
            return true
        }
        if (!isOnPositiveQuarter(frame)) {
            throw NegativeCoordinateFramedInPieceException()
        }
        return true
    }

    private fun isOnPositiveQuarter(frame: Frame) =
            frame.x >= 0 && frame.y >= 0

    private fun verify(frames: Array<Frame>) {
        if (frames.size <= 1) {
            return
        }
        if (!areAllAdjacent(frames)) {
            throw NonAdjacentFramedInPieceException()
        }
    }

    private fun areAllAdjacent(frames: Array<Frame>) =
            frames.fold(true) { allAdjacent, frame -> allAdjacent && isAdjacentToAnExistingFrame(frame, frames) }

    private fun isAdjacentToAnExistingFrame(frame: Frame, frames: Array<Frame>) =
            frames.any { it.isAdjacentWith(frame) }

    companion object {
        private fun fromString(string: String): Iterable<Frame> {
            var frames: Array<Frame> = emptyArray()
            string
                    .split("\n")
                    .map { it ->
                        it.trim('|')
                                .split('|')
                    }
                    .forEachIndexed { indexY, list ->
                        list.forEachIndexed { indexX, s ->
                            val pattern = Pattern.of(s)
                            if (pattern != Pattern.Empty){
                                frames = frames.plus(Frame(indexX, indexY, pattern))
                            }
                        }
                    }
            return frames.asIterable()
        }

    }
}
