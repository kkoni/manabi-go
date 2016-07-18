package manabigo.ai.deeplearning

import manabigo.ai.AiPlayer
import manabigo.model.Board
import manabigo.model.Point
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PolicySigmaNetworkAiPlayer @Autowired constructor(
        private val networkRepository: PolicySigmaNetworkRepository
): AiPlayer {
    override fun play(board: Board): Board? {
        val mostProbableMoves = networkRepository.find(board.size).getMoveProbabilities(board).getMostProbableMoves(1)
        if (mostProbableMoves.isEmpty()) {
            return null
        } else {
            return board.applyMove(Point(mostProbableMoves[0].position, board.getNextPlayer()))
        }
    }
}
