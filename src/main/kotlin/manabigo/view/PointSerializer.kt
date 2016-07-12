package manabigo.view

import manabigo.model.Point
import manabigo.model.Position

class PointSerializer {
    private val stoneSerializer = StoneSerializer()

    fun serialize(point: Point): Map<String, Any> {
        return mapOf("x" to point.x, "y" to point.y, "s" to stoneSerializer.serialize(point.stone))
    }

    fun deserialize(map: Map<String, Any>): Point {
        if (map["x"] !is Int || map["y"] !is Int || map["s"] !is String) {
            throw IllegalArgumentException("invalid map : $map")
        }
        return Point(Position(map["x"] as Int, map["y"] as Int), stoneSerializer.deserialize(map["s"] as String))
    }
}
