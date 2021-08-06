package com.example.demo.greeting

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.accept
import org.springframework.web.reactive.function.server.RouterFunctions.route

import reactor.core.publisher.Mono


data class Greeting(
    val message: String
)

@Component
class GreetingHandler {
    fun hello(request: ServerRequest?): Mono<ServerResponse> {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue<Any>(Greeting("Hello, Spring!")))
    }
}


@Configuration(proxyBeanMethods = false)
class GreetingRouter(
    private val greetingHandler: GreetingHandler
) {

    @Bean
    fun getHello() = route(GET("/hello").and(
        accept(MediaType.APPLICATION_JSON)), {
            request: ServerRequest -> greetingHandler.hello(request)
        }
    )

}