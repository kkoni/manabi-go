package manabigo.controller

import manabigo.ai.AiPlayer
import manabigo.ai.LearningQueue
import manabigo.view.BoardSerializer
import manabigo.view.MovesToLearnSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController

@RestController
class AiController @Autowired constructor(
        private val aiPlayer: AiPlayer,
        private val learningQueue: LearningQueue
) {
    private val boardSerializer = BoardSerializer()
    private val movesToLearnSerializer = MovesToLearnSerializer()

    @RequestMapping("/ai/move", method = arrayOf(POST))
    fun move(@RequestBody request: Map<String, Any>): Map<String, Any> {
        val board = boardSerializer.deserialize(request)
        val result = aiPlayer.play(board)
        if (result == null) {
            return mapOf("success" to false)
        } else {
            return mapOf("success" to true, "board" to boardSerializer.serialize(result))
        }
    }

    @RequestMapping("/ai/learn", method = arrayOf(POST))
    fun learn(@RequestBody request: Map<String, Any>): Unit {
        val movesToLearn = movesToLearnSerializer.deserialize(request)
        learningQueue.enqueue(movesToLearn)
    }
}
