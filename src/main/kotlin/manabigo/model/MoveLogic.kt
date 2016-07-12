package manabigo.model

import manabigo.model.Stone.BLACK
import manabigo.model.Stone.EMPTY

object MoveLogic {
    fun getMoveResult(move: Point, board: Board): MoveResult? {
        if (move.stone == EMPTY || board.size <= move.x || board.size <= move.y) {
            throw IllegalArgumentException("invalid move : move=$move board=$board")
        }

        if (isInvalidMove(move, board)) {
            return null
        }

        val stonePlacedBoard: List<List<Point>> = board.points.map {
            it.map {
                if(it.x == move.x && it.y == move.y) {
                    move
                } else {
                    it
                }
            }
        }

        val capturedPoints = getAdjacentPoints(move, stonePlacedBoard).map {
            if (it.stone != EMPTY && it.stone != move.stone) {
                getCapturedPoints(it, stonePlacedBoard)
            } else {
                emptySet()
            }
        }.reduce { set1, set2 ->  set1.plus(set2) }
        if (capturedPoints.isNotEmpty()) {
            return MoveResult(move, capturedPoints.map { it.position }.toSet())
        } else if (getCapturedPoints(move, stonePlacedBoard).isNotEmpty()) {
            return null
        } else {
            return MoveResult(move, emptySet())
        }
    }

    private fun isInvalidMove(move: Point, board: Board): Boolean {
        val firstMoveIsNotByBlack = board.lastMove == null && move.stone != BLACK
        val successiveMoveBySamePlayer = board.lastMove != null && board.lastMove.stone == move.stone
        val stoneAlreadyExists = board.points[move.x][move.y].stone != EMPTY
        val invalidKo = board.lastMove != null &&
                board.lastMove.capturedPositions.size == 1 &&
                board.lastMove.capturedPositions.any { it == move.position }
        return firstMoveIsNotByBlack || successiveMoveBySamePlayer || stoneAlreadyExists || invalidKo
    }

    private fun getAdjacentPoints(point: Point, board: List<List<Point>>): List<Point> {
        return listOf(
                getLeftPoint(point, board),
                getUpperPoint(point, board),
                getRightPoint(point, board),
                getLowerPoint(point, board)).filterNotNull()
    }

    private fun getLeftPoint(point: Point, board: List<List<Point>>): Point? {
        return if(point.x == 0) null else board[point.x-1][point.y]
    }

    private fun getUpperPoint(point: Point, board: List<List<Point>>): Point? {
        return if(point.y == 0) null else board[point.x][point.y-1]
    }

    private fun getRightPoint(point: Point, board: List<List<Point>>): Point? {
        return if(point.x == board.size-1) null else board[point.x+1][point.y]
    }

    private fun getLowerPoint(point: Point, board: List<List<Point>>): Point? {
        return if(point.y == board.size-1) null else board[point.x][point.y+1]
    }

    private fun getCapturedPoints(startPoint: Point, board: List<List<Point>>): Set<Point> {
        if (startPoint.stone == EMPTY) {
            throw IllegalArgumentException("Can not search from an empty point")
        }
        val visitedPlayer: MutableSet<Point> = mutableSetOf()
        val visitedOpponent: MutableSet<Point> = mutableSetOf()
        if (getCapturedPointsRecur(startPoint.stone, startPoint, board, visitedPlayer, visitedOpponent)) {
            return visitedPlayer
        } else {
            return emptySet()
        }
    }

    private fun getCapturedPointsRecur(
            player: Stone,
            point: Point,
            allPoints: List<List<Point>>,
            visitedSelf: MutableSet<Point>,
            visitedOpponent: MutableSet<Point>
    ): Boolean {
        if (visitedSelf.contains(point) || visitedOpponent.contains(point)) {
            return true
        }
        if (point.stone == EMPTY) {
            return false
        } else if (point.stone == player) {
            visitedSelf.add(point)
            for (adjacentPoint in getAdjacentPoints(point, allPoints)) {
                if (!getCapturedPointsRecur(player, adjacentPoint, allPoints, visitedSelf, visitedOpponent)) {
                    return false
                }
            }
            return true
        } else {
            visitedOpponent.add(point)
            return true
        }
    }
}
