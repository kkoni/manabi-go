package manabigo.ai.deeplearning

import manabigo.model.Board
import manabigo.model.Point
import manabigo.model.Stone
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

object INDArrayConverter {
    private val featureNum = 3

    fun convertBoardToFeatures(board: Board, player: Stone): INDArray {
        val boardSize = board.size
        val featureVector: FloatArray = (0..(boardSize-1)).flatMap { x ->
            (0..(boardSize-1)).flatMap { y ->
                getFeatures(board, x, y, player).toList()
            }
        }.toFloatArray()
        return Nd4j.create(featureVector)
    }

    private fun getFeatures(board: Board, x: Int, y: Int, player: Stone): FloatArray {
        val point: Point = board.points[x][y]
        val stone: Stone = point.stone
        val isPlayer = if (stone == player) 1.0f else 0.0f
        val isOpponent = if (stone != player && stone != Stone.EMPTY) 1.0f else 0.0f
        val isEmpty = if (stone == Stone.EMPTY) 1.0f else 0.0f
        return floatArrayOf(isPlayer, isOpponent, isEmpty)
    }

    fun convertMoveToLabels(move: Point, board: Board): INDArray {
        val boardSize = board.size
        val labelVector: FloatArray = (0..(boardSize-1)).flatMap { x ->
            (0..(boardSize-1)).map { y ->
                if (x == move.x && y == move.y) 1.0f else 0.0f
            }
        }.toFloatArray()
        return Nd4j.create(labelVector)
    }
}
