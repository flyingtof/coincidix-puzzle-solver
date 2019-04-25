package org.puzzle

enum class Pattern {
    Empty,
    Plain,
    Hole;

    companion object {
        fun of(x: String): Pattern {
            return when(x){
                "0","o","O"-> Hole
                "x","X"->Plain
                else->Empty
            }
        }
    }

    override fun toString(): String {
        return when(this){
            Empty -> " "
            Plain -> "X"
            Hole -> "O"
        }
    }
}
