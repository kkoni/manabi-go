package manabigo.view

import manabigo.model.Board
import manabigo.model.MoveResult
import manabigo.model.Point
import manabigo.model.Position
import manabigo.model.Stone.BLACK
import manabigo.model.Stone.WHITE
import manabigo.model.Stone.EMPTY
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals

class BoardSerializerSpecs: Spek({
    val serializer = BoardSerializer()

    describe("serialize") {
        it("should return a stone table and a last move") {
            val actual = serializer.serialize(Board.create(listOf(
                    listOf(EMPTY, WHITE, EMPTY),
                    listOf(EMPTY, BLACK, WHITE),
                    listOf(EMPTY, EMPTY, EMPTY)
            ), MoveResult(Point(Position(1, 2), WHITE), setOf(Position(0, 2)))))
            val expected = mapOf(
                    "stones" to listOf(
                            listOf("e", "w", "e"),
                            listOf("e", "b", "w"),
                            listOf("e", "e", "e")
                    ),
                    "lastMove" to mapOf(
                            "point" to mapOf("x" to 1, "y" to 2, "s" to "w"),
                            "capturedPositions" to listOf(mapOf("x" to 0, "y" to 2))
                    )
            )
            assertEquals(expected, actual)
        }

        it("should return only a stone table if there is not a last move") {
            val actual = serializer.serialize(Board.getInitialBoard(3))
            val expected = mapOf(
                    "stones" to listOf(
                            listOf("e", "e", "e"),
                            listOf("e", "e", "e"),
                            listOf("e", "e", "e")
                    ))
            assertEquals(expected, actual)
        }
    }
}) {
}
