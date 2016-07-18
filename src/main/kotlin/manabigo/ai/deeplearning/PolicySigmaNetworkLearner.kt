package manabigo.ai.deeplearning

import manabigo.ai.MovesToLearn
import manabigo.model.Board
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PolicySigmaNetworkLearner @Autowired constructor(
        private val networkRepository: PolicySigmaNetworkRepository
) {
    private val logger = LoggerFactory.getLogger(PolicySigmaNetworkLearner::class.java)

    fun learnMoves(movesToLearn: MovesToLearn): Unit {
        logger.info("start learning")
        val network = networkRepository.find(movesToLearn.boardSize).clone()
        var board = Board.getInitialBoard(movesToLearn.boardSize)
        movesToLearn.moves.forEach { move ->
            val nextBoard = board.applyMove(move)
            if (nextBoard == null) {
                logger.info("stop learning because of invalid move: invalidMove=$move, movesToLearn=$movesToLearn")
                return
            }
            if (move.stone == movesToLearn.player) {
                network.learn(board, move)
            }
            board = nextBoard
        }
        networkRepository.store(network)
        logger.info("finish learning : movesToLearn=$movesToLearn")
    }
}
