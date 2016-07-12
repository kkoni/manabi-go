package manabigo.model

data class Position(val x: Int, val y: Int) {
    init {
        if (x < 0) {
            throw IllegalArgumentException("x is negative : $x")
        }
        if (y < 0) {
            throw IllegalArgumentException("y is negative : $y")
        }
    }
}
