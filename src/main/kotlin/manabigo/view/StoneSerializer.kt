package manabigo.view

import manabigo.model.Stone
import manabigo.model.Stone.*

class StoneSerializer {
    fun serialize(stone: Stone): String {
        return when(stone) {
            BLACK -> "b"
            WHITE -> "w"
            EMPTY -> "e"
        }
    }

    fun deserialize(str: String): Stone {
        return when(str) {
            "b" -> BLACK
            "w" -> WHITE
            "e" -> EMPTY
            else -> throw IllegalArgumentException("invalid stone : $str")
        }
    }
}
