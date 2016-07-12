package manabigo.model

import manabigo.model.Stone.*
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MoveLogicSpecs : Spek({
        describe("logic") {
        it("should return success with the move change if there is no taken stone") {
            val board = Board.create(listOf(
                    listOf(EMPTY, EMPTY, EMPTY),
                    listOf(EMPTY, EMPTY, EMPTY),
                    listOf(EMPTY, EMPTY, EMPTY)
            ), null)
            assertEquals(
                    MoveResult(Point(Position(1, 1), BLACK), emptySet()),
                    MoveLogic.getMoveResult(Point(Position(1, 1), BLACK), board))
        }

        it("should return a move result with captured positions if some stones are captured") {
            val board = Board.create(listOf(
                    listOf(EMPTY, BLACK, EMPTY),
                    listOf(BLACK, WHITE, EMPTY),
                    listOf(EMPTY, BLACK, EMPTY)
            ), MoveResult(Point(Position(1, 1), WHITE), emptySet()))
            assertEquals(
                    MoveResult(Point(Position(1, 2), BLACK), setOf(Position(1, 1))),
                    MoveLogic.getMoveResult(Point(Position(1, 2), BLACK), board)
            )
        }

        it("should return null if a stone has already existed") {
            val board = Board.create(listOf(
                    listOf(EMPTY, EMPTY, EMPTY),
                    listOf(EMPTY, BLACK, EMPTY),
                    listOf(EMPTY, EMPTY, EMPTY)
            ), MoveResult(Point(Position(1, 1), BLACK), emptySet()))
            assertNull(MoveLogic.getMoveResult(Point(Position(1, 1), WHITE), board))
        }

        it("should return null if the move is self-killing") {
            val board = Board.create(listOf(
                    listOf(EMPTY, BLACK, EMPTY),
                    listOf(BLACK, EMPTY, WHITE),
                    listOf(EMPTY, EMPTY, EMPTY)
            ), MoveResult(Point(Position(0, 1), BLACK), emptySet()))
            assertNull(MoveLogic.getMoveResult(Point(Position(0, 0), WHITE), board))
        }

        it("should return null if the move is played by a same player") {
            val board = Board.create(listOf(
                    listOf(EMPTY, EMPTY, EMPTY),
                    listOf(EMPTY, BLACK, EMPTY),
                    listOf(EMPTY, EMPTY, EMPTY)
            ), MoveResult(Point(Position(1, 1), BLACK), emptySet()))
            assertNull(MoveLogic.getMoveResult(Point(Position(0, 1), BLACK), board))
        }

        it("should return null if it is the first move but is played by white") {
            val board = Board.create(listOf(
                    listOf(EMPTY, EMPTY, EMPTY),
                    listOf(EMPTY, EMPTY, EMPTY),
                    listOf(EMPTY, EMPTY, EMPTY)
            ), null)
            assertNull(MoveLogic.getMoveResult(Point(Position(1, 1), WHITE), board))
        }

        it("should return null if it is an invalid ko move") {
            val board = Board.create(listOf(
                    listOf(EMPTY, BLACK, EMPTY),
                    listOf(BLACK, EMPTY, EMPTY),
                    listOf(EMPTY, EMPTY, EMPTY)
            ), MoveResult(Point(Position(0, 1), BLACK), setOf(Position(0, 0))))
            assertNull(MoveLogic.getMoveResult(Point(Position(0, 0), WHITE), board))
        }
    }

}) {
}
