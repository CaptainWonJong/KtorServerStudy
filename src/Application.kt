package com.wonjong.lee

import com.wonjong.lee.config.DatabaseInitializer
import com.wonjong.lee.config.SimpleJwt
import com.wonjong.lee.routes.users
import com.wonjong.lee.service.UserService
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
        anyHost()
    }
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            enableComplexMapKeySerialization()
            setDateFormat(DATE_TIME_FORMAT)
        }
    }
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(Routing) {
        users(UserService())
    }
    DatabaseInitializer.init()
}

internal const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"