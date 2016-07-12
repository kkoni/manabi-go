package manabigo.view

import manabigo.model.Board

class BoardSerializer {
    private val stoneSerializer = StoneSerializer()
    private val moveResultSerializer = MoveResultSerializer()

    fun serialize(board: Board): Map<String, Any> {
        val stones: List<List<String>> = board.points.map {
            it.map {
                stoneSerializer.serialize(it.stone)
            }
        }
        if (board.lastMove == null) {
            return mapOf(
                    "stones" to stones,
                    "nextPlayer" to stoneSerializer.serialize(board.getNextPlayer()))
        } else {
            return mapOf(
                    "stones" to stones,
                    "nextPlayer" to stoneSerializer.serialize(board.getNextPlayer()),
                    "lastMove" to moveResultSerializer.serialize(board.lastMove))
        }
    }

    fun deserialize(map: Map<String, Any>): Board {
        if (map["stones"] !is List<*> || (map.containsKey("lastMove") && map["lastMove"] !is Map<*, *>)) {
            throw IllegalArgumentException("invalid map : $map")
        }
        val stones = (map["stones"] as List<List<String>>).map {
            it.map {
                stoneSerializer.deserialize(it)
            }
        }
        val lastMove = if (map.containsKey("lastMove"))
            moveResultSerializer.deserialize(map["lastMove"] as Map<String, Any>)
        else
            null
        return Board.create(stones, lastMove)
    }
}
