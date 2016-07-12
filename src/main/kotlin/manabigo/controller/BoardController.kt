package manabigo.controller

import manabigo.model.Board
import manabigo.view.BoardSerializer
import manabigo.view.PointSerializer
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class BoardController {
    private val boardSerializer = BoardSerializer()
    private val pointSerializer = PointSerializer()

    @RequestMapping("/initialBoard", method = arrayOf(GET))
    fun getInitialBoard(@RequestParam size: Int): Map<String, Any> {
        return mapOf("board" to boardSerializer.serialize(Board.getInitialBoard(size)))
    }

    @RequestMapping("/move", method = arrayOf(POST))
    fun move(@RequestBody request: Map<String, Any>): Map<String, Any> {
        val board = boardSerializer.deserialize(request["board"] as Map<String, Any>)
        val move = pointSerializer.deserialize(request["move"] as Map<String, Any>)
        val result = board.applyMove(move)
        if (result == null) {
            return mapOf("success" to false)
        } else {
            return mapOf(
                    "success" to true,
                    "board" to boardSerializer.serialize(result))
        }
    }
}
