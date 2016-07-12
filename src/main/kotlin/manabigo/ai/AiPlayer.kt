package manabigo.ai

import manabigo.model.Board

interface AiPlayer {
    fun play(board: Board): Board?
}
