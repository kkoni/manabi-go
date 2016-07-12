package manabigo.view

import manabigo.model.Position

class PositionSerializer {
    fun serialize(position: Position): Map<String, Any> {
        return mapOf("x" to position.x, "y" to position.y)
    }

    fun deserialize(map: Map<String, Any>): Position {
        if (map["x"] !is Int || map["y"] !is Int) {
            throw IllegalArgumentException("invalid map : $map")
        }
        return Position(map["x"] as Int, map["y"] as Int)
    }
}
