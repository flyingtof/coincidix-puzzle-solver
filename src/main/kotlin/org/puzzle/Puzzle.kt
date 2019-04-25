package org.puzzle

import java.lang.StringBuilder
import java.lang.System.currentTimeMillis
import kotlin.collections.ArrayList

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
          if not successfull
            remove piece from current position
    end (2)
  end (1)
  terminate KO
}
*/

class Puzzle {
    private val grid: Array<Array<Pattern>>
    private val mask: Array<Array<Pattern>>
    private val length: Int
    val solution: Array<Array<Piece?>>
    private val allPositions: Iterable<Pair<Int, Int>>
    private var count: Long = 0

    constructor(length: Int, mask: Array<Array<Pattern>> = Array(length) { Array(length) { Pattern.Plain } }){
        this.length = length
        this.grid = Array(length) { Array(length) { Pattern.Empty } }
        this.solution = Array(length) { Array(length) { null as Piece? } }
        this.allPositions = allPositions()
        this.mask = mask
    }

    companion object {
        fun mask(s:String): Array<Array<Pattern>>{
            val nRows = s.split("\n").size
            val ncol = s.split("\n").map { it.split("|").size }.maxBy { it }!!.or(0)
            val mask  = Array(ncol) { Array(nRows) { Pattern.Empty } }
            s.split("\n").forEachIndexed{rowIndex, row->
                row.trim('|').split("|").forEachIndexed{colIndex, cell ->
                    mask[colIndex][rowIndex] = Pattern.of(cell)
                }
            }
            return mask
        }
    }

    fun solve(pieces: Collection<Piece>): Boolean {
        count = 0
        val start = currentTimeMillis()
        val list = ArrayList<Piece>(pieces)
        tryToAdd(list, 0)
        println("count = $count, ratio = ${count*1000.0/(currentTimeMillis()-start)} /sec, ${(currentTimeMillis()-start)*1.0/1000} ")
        return isSolved()
    }

    fun tryToAdd(pieces: ArrayList<Piece>, indice: Int): Boolean {
        if (indice >= pieces.size){
            return true
        }
        val piece = pieces[indice]
        allPositions.shuffled().forEach { position ->
            val (x, y) = position
            piece.allOrientations().forEach { pieceOnPositionToTry ->
                if (put(pieceOnPositionToTry, x, y)) {
                    if (isSolved()) {
                        return true
                    }
                    if (!tryToAdd(pieces, indice + 1)) {
                        remove(pieceOnPositionToTry, x, y)
                    } else {
                        return true
                    }
                }
            }
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

    fun printMask(): String {
        val sb = StringBuilder()
        this.mask.forEach { row ->
            row.forEach { cell-> sb.append(cell)
            }
            sb.append("\n")
        }
        return sb.toString()
    }

    private fun isSolved(): Boolean {
        return this.grid.foldIndexed(true) { columnIndex, acc, column ->
            column.foldIndexed(acc) { rowIndex, acc2, b -> acc2 && b == mask[columnIndex][rowIndex] } }
    }


    fun put(piece: Piece, x: Int, y: Int): Boolean {
        count += 1
        if (canPut(piece, x, y)) {
            piece.frames
                    .filter { it.pattern != Pattern.Empty  }
                    .forEach { this.grid[it.x + x][it.y + y] = it.pattern }
            solution[x][y] = piece
            return true
        }
        return false
    }

    private fun remove(piece: Piece, x: Int, y: Int) {
        piece.frames
                .forEach { this.grid[it.x + x][it.y + y] = Pattern.Empty }
        solution[x][y] = null
    }

    private fun canPut(piece: Piece, x: Int, y: Int): Boolean {
        piece.frames.forEach {frame->
            val xFrame = frame.x + x
            val yFrame = frame.y + y
            val canPut = isInsideGrid(xFrame, yFrame)&&
                    !hasFrameAt(xFrame, yFrame) &&
                    matchRequiredPatterns(xFrame, yFrame, frame)
            if (!canPut){
                return false
            }
        }
        return true
    }

    private fun hasFrameAt(x: Int, y: Int): Boolean {
        return this.grid[x][y] != Pattern.Empty
    }

    private fun matchRequiredPatterns(x: Int, y: Int, frame: Frame): Boolean {
        return this.mask[x][y] == frame.pattern
    }

    private fun isInsideGrid(x: Int, y: Int): Boolean {
        return x in 0..(length - 1) && y in 0..(length - 1)
    }

    private fun allPositions(): Iterable<Pair<Int, Int>> {
        var allPositions = emptyList<Pair<Int, Int>>()
        grid.forEachIndexed { columnIndex, column ->
            column.indices.forEach { lineIndex ->
                allPositions = allPositions.plus(Pair(columnIndex, lineIndex))
            }
        }
        return allPositions
    }

}
