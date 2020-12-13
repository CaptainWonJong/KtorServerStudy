package com.wonjong.lee

import com.wonjong.lee.config.SimpleJwt
import com.wonjong.lee.exception.InvalidCredentialsException
import com.wonjong.lee.model.AccountVo
import com.wonjong.lee.model.ExceptionVo
import com.wonjong.lee.model.SnippetVo
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.slf4j.event.Level
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val simpleJwt = SimpleJwt("leewonjong")
    install(DefaultHeaders)
    install(Authentication) {
        jwt {
            verifier(simpleJwt.verifier)
            authSchemes("WonJong")
            validate {
                UserIdPrincipal(it.payload.getClaim("token").asString())
            }
        }
    }
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        allowCredentials = true
        anyHost()
    }
    install(ContentNegotiation) {
        gson {

        }
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(StatusPages) {
        exception<InvalidCredentialsException> { exception ->
            call.respond(
                HttpStatusCode.Unauthorized, mapOf(
                    "isSuccess" to false,
                    "error" to exception.message
                )
            )
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        post("/login") {
            val post = call.receive<AccountVo>()
            val user = mockUsers.getOrPut(post.id) { AccountVo(post.id, post.password) }
            if (user.password != post.password) throw InvalidCredentialsException(
                ExceptionVo(
                    code = 1001,
                    message = "Invalid credentials"
                )
            )
            call.respond(mapOf("token" to simpleJwt.sign(user.id)))
        }

        route("/snippet") {
            get {
                call.respond(mapOf("snippets" to synchronized(snippets) { snippets.toList() }))
            }
            authenticate {
                post {
                    val post = call.receive<String>()
                    val principal = call.principal<UserIdPrincipal>() ?: error("No principal")
                    snippets += SnippetVo(principal.name, post)
                    call.respond(mapOf("isSuccess" to true))
                }
            }
        }
    }
}

private val mockUsers = Collections.synchronizedMap(
    listOf(AccountVo("testId", "testPassword"))
        .associateBy { it.id }
        .toMutableMap()
)

val snippets = Collections.synchronizedList(
    mutableListOf(
        SnippetVo(user = "test1", text = "hello"),
        SnippetVo(user = "test2", text = "world")
    )
)