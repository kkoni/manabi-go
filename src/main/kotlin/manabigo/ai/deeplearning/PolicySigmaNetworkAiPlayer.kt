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
    private val networks: MutableMap<Int, PolicySigmaNetwork> = mutableMapOf()

    init {
        networks[9] = networkRepository.find(9)
    }

    override fun play(board: Board): Board? {
        val mostProbableMoves = getNetwork(board.size).getMoveProbabilities(board).getMostProbableMoves(1)
        if (mostProbableMoves.isEmpty()) {
            return null
        } else {
            return board.applyMove(Point(mostProbableMoves[0].position, board.getNextPlayer()))
        }
    }

    private fun getNetwork(boardSize: Int): PolicySigmaNetwork {
        return synchronized(networks) {
            networks[boardSize] ?: {
                val newNetwork = networkRepository.find(boardSize)
                networks[boardSize] = newNetwork
                newNetwork
            }()
        }
    }

    fun updateNetwork(network: PolicySigmaNetwork): Unit {
        synchronized(networks) {
            networks[network.boardSize] = network
        }
    }
}

