package manabigo.ai

import manabigo.model.*
import manabigo.model.Stone.EMPTY
import org.springframework.stereotype.Component
import java.util.*

@Component
class RandomAiPlayer: AiPlayer {
    private val random = Random()

    override fun play(board: Board): Board? {
        val playerStone = board.getNextPlayer()
        val emptyPoints: List<Point> = board.points.flatMap { it.filter { it.stone == EMPTY } }
        val captureSizeToMove: Map<Int, List<MoveResult>> = emptyPoints.map {
            board.getMoveResult(Point(Position(it.x, it.y), playerStone))
        }.filterNotNull().groupBy {
            it.capturedPositions.size
        }
        if (captureSizeToMove.isEmpty()) {
            return null
        }
        val maxCapturingMoves = captureSizeToMove.entries.sortedByDescending { it.key }.first().value
        val selectedMove = maxCapturingMoves[random.nextInt(maxCapturingMoves.size)]
        return board.applyMove(selectedMove.point)
    }
}
