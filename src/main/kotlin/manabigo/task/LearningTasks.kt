package manabigo.task

import manabigo.ai.LearningQueue
import manabigo.ai.deeplearning.PolicySigmaNetworkLearner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class LearningTasks @Autowired constructor(
        private val learningQueue: LearningQueue,
        private val learner: PolicySigmaNetworkLearner
){
    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    fun learnOneGame(): Unit {
        val movesToLearn = learningQueue.dequeue()
        if (movesToLearn != null) {
            learner.learnMoves(movesToLearn)
        }
    }
}
