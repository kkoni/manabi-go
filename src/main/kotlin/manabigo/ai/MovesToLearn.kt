package manabigo.ai

import manabigo.model.Board
import manabigo.model.Point
import manabigo.model.Stone

data class MovesToLearn(val moves: List<Point>, val player: Stone, val boardSize: Int) {
    init {
        if (boardSize < Board.minSize || Board.maxSize < boardSize) {
            throw IllegalArgumentException("invalid board size : $boardSize")
        }
    }
}
