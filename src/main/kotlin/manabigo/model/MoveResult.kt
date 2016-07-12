package manabigo.model

data class MoveResult(val point: Point, val capturedPositions: Set<Position>) {
    val x = point.position.x
    val y = point.position.y
    val stone = point.stone
}
