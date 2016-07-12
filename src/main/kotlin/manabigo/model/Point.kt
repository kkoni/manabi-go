package manabigo.model

data class Point(val position: Position, val stone: Stone) {
    val x = position.x
    val y = position.y
}
