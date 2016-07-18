package manabigo.ai.deeplearning

import manabigo.model.Board
import manabigo.model.Point
import manabigo.model.Position
import manabigo.model.Stone

class MoveProbabilities(
        private val probabilities: List<MoveProbability>,
        private val board: Board,
        private val player: Stone
) {
    fun getMostProbableMoves(n: Int): List<MoveProbability> {
        return probabilities.sortedByDescending { it.probability }.filter {
            board.isRegalMove(Point(it.position, player))
        }.take(n)
    }
}
