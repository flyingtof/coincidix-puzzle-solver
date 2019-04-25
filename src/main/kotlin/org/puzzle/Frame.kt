package org.puzzle

data class Frame(val x: Int, val y: Int, val pattern: Pattern = Pattern.Plain) {
    fun isAdjacentWith(frame: Frame): Boolean {
        return (this.x == frame.x || this.y == frame.y)
                && (Math.abs(this.x - frame.x) == 1 || Math.abs(this.y - frame.y) == 1)
    }

    fun rotate(): Frame {

        // cos(-90)  -sin(-90)  =>  0  1
        // sin(-90)   cos(-90)     -1  0
        return Frame(y, -x, pattern)
    }

    fun rotate(n: Int): Frame {
        var res = this
        for (i in 0..n-1) {
            res = res.rotate()
        }
        return res
    }

    fun translate(tx: Int, ty: Int): Frame {
        return Frame(x + tx, y + ty, pattern)
    }

    fun turnaround(): Frame {
        return Frame(-x,y, pattern)
    }
}
