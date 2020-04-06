package biz.markov.nucleus.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * NucleusController.
 *
 * @author Vasily_Markov
 */
@RestController
class NucleusController {

    @GetMapping("/")
    fun index(): Mono<String> {
        return Mono.just("Welcome")
    }
}