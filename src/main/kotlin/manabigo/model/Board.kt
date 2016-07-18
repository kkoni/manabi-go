package manabigo.model

import manabigo.model.Stone.*

class Board(val points: List<List<Point>>, val lastMove: MoveResult?) {
    init {
        if (!validatePoints(points)) {
            throw IllegalArgumentException("invalid points : $points")
        }
        if (!validateLastMove(lastMove, points)) {
            throw IllegalArgumentException("invalid lastMove : lastMove=$lastMove points=$points")
        }
    }

    val size: Int = points.size

    fun getMoveResult(move: Point): MoveResult? {
        return MoveLogic.getMoveResult(move, this)
    }

    fun applyMove(move: Point): Board? {
        val moveResult = getMoveResult(move)
        if (moveResult == null) {
            return null
        } else {
            val nextPoints = points.map {
                it.map {
                    if (it.x == moveResult.x && it.y == moveResult.y) {
                        moveResult.point
                    } else if(moveResult.capturedPositions.contains(it.position)) {
                        Point(it.position, EMPTY)
                    } else {
                        it
                    }
                }
            }
            return Board(nextPoints, moveResult)
        }
    }

    fun isRegalMove(move: Point): Boolean {
        return getMoveResult(move) != null
    }

    fun getNextPlayer(): Stone {
        if (lastMove == null || lastMove.stone == WHITE) {
            return BLACK
        } else {
            return WHITE
        }
    }

    companion object {
        fun getInitialBoard(size: Int): Board {
            if (size < minSize || maxSize < size) {
                throw IllegalArgumentException("invalid size : $size")
            }
            val stones = (1..size).map {
                (1..size).map {
                    EMPTY
                }
            }
            return create(stones, null)
        }

        val minSize = 3
        val maxSize = 19

        fun create(stones: List<List<Stone>>, lastMove: MoveResult?): Board {
            val points = (0..(stones.size-1)).map { x ->
                (0..(stones[x].size-1)).map { y ->
                    Point(Position(x, y), stones[x][y])
                }
            }
            return Board(points, lastMove)
        }

        private fun validatePoints(points: List<List<Point>>): Boolean {
            val isSquare = points.map { it.size == points.size }.fold(true, Boolean::and)
            val isValidSize = minSize <= points.size && points.size <= maxSize
            val hasCorrectPositions = (0..(points.size-1)).map { x ->
                (0..(points.size-1)).map { y ->
                    points[x][y].position.x == x && points[x][y].position.y == y
                }.fold(true, Boolean::and)
            }.fold(true, Boolean::and)
            return isSquare && isValidSize && hasCorrectPositions
        }

        private fun validateLastMove(lastMove: MoveResult?, points: List<List<Point>>): Boolean {
            if (lastMove == null) {
                return points.map {
                    it.map {
                        it.stone == EMPTY
                    }.fold(true, Boolean::and)
                }.fold(true, Boolean::and)
            } else {
                val hasValidPosition = lastMove.x < points.size && lastMove.y < points.size
                val hasValidStone = lastMove.stone != EMPTY && lastMove.stone == points[lastMove.x][lastMove.y].stone
                val hasValidCapturedPositions = lastMove.capturedPositions.map {
                    it.x < points.size && it.y < points.size && points[it.x][it.y].stone == EMPTY
                }.fold(true, Boolean::and)
                return hasValidPosition && hasValidStone && hasValidCapturedPositions
            }
        }
    }
}
