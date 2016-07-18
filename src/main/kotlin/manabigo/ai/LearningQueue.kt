package manabigo.ai

import org.springframework.stereotype.Component
import java.util.*

@Component
class LearningQueue {
    private val queue: LinkedList<MovesToLearn> = LinkedList()

    fun enqueue(movesToLearn: MovesToLearn): Unit {
        synchronized(queue) {
            queue.add(movesToLearn)
        }
    }

    fun dequeue(): MovesToLearn? {
        synchronized(queue) {
            return queue.pollFirst()
        }
    }
}
