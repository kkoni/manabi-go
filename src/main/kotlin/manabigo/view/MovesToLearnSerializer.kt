package manabigo.view

import manabigo.ai.MovesToLearn
import manabigo.model.Point
import manabigo.model.Stone

class MovesToLearnSerializer {
    private val stoneSerializer = StoneSerializer()
    private val pointSerializer = PointSerializer()

    fun deserialize(map: Map<String, Any>): MovesToLearn {
        if (map["moves"] !is List<*> || map["player"] !is String || map["boardSize"] !is Int) {
            throw IllegalArgumentException("invalid map : $map")
        }

        val moves: List<Point> = (map["moves"] as List<Map<String, Any>>).map { pointSerializer.deserialize(it) }
        val player: Stone = stoneSerializer.deserialize(map["player"] as String)
        val boardSize: Int = map["boardSize"] as Int
        return MovesToLearn(moves, player, boardSize)
    }
}
