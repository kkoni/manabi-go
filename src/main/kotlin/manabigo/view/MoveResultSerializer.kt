package manabigo.view

import manabigo.model.MoveResult

class MoveResultSerializer {
    private val pointSerializer = PointSerializer()
    private val positionSerializer = PositionSerializer()

    fun serialize(moveResult: MoveResult): Map<String, Any> {
        return mapOf(
            "point" to pointSerializer.serialize(moveResult.point),
            "capturedPositions" to moveResult.capturedPositions.map { positionSerializer.serialize(it) }
        )
    }

    fun deserialize(map: Map<String, Any>): MoveResult {
        if(map["point"] !is Map<*, *> || map["capturedPositions"] !is List<*>) {
            throw IllegalArgumentException("invalid map : $map")
        }
        val point = pointSerializer.deserialize(map["point"] as Map<String, Any>)
        val capturedPositions = (map["capturedPositions"] as List<Map<String, Any>>).map {
            positionSerializer.deserialize(it)
        }.toSet()
        return MoveResult(point, capturedPositions)
    }
}
