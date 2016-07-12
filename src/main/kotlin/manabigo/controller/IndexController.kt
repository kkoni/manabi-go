package manabigo.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestMethod.GET

@Controller
@RequestMapping("/")
class IndexController {
    @RequestMapping(method = arrayOf(GET))
    fun get(): String {
        return "index"
    }
}
