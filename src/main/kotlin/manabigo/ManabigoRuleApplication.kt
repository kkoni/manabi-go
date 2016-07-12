package manabigo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class ManabigoRuleApplication {
}

fun main(args: Array<String>) {
    SpringApplication.run(ManabigoRuleApplication::class.java, *args)
}
