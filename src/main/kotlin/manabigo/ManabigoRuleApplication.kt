package manabigo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
open class ManabigoRuleApplication {
}

fun main(args: Array<String>) {
    SpringApplication.run(ManabigoRuleApplication::class.java, *args)
}
