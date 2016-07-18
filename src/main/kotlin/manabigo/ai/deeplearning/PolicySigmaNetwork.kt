package manabigo.ai.deeplearning

import manabigo.model.Board
import manabigo.model.Point
import manabigo.model.Position
import manabigo.model.Stone
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.nd4j.linalg.dataset.DataSet

class PolicySigmaNetwork(val model: MultiLayerNetwork, val boardSize: Int) {
    fun learn(board: Board, move: Point): Unit {
        if (board.size != boardSize) {
            throw IllegalArgumentException("different board size : ${board.size}")
        }

        if (board.getNextPlayer() != move.stone) {
            throw IllegalArgumentException("stone is different from the next player : ${move.stone}")
        }

        val dataSet: DataSet = DataSet(
                INDArrayConverter.convertBoardToFeatures(board, move.stone),
                INDArrayConverter.convertMoveToLabels(move, board))
        model.fit(dataSet)
    }

    fun getMoveProbabilities(board: Board): MoveProbabilities {
        val player = board.getNextPlayer()
        val probArray = model.output(INDArrayConverter.convertBoardToFeatures(board, player), false)
        val probabilities: List<MoveProbability> = (0..(boardSize-1)).flatMap { x ->
            (0..(boardSize-1)).map { y ->
                MoveProbability(Position(x, y), probArray.getFloat(x * boardSize + y))
            }
        }
        return MoveProbabilities(probabilities, board, player)
    }
}
