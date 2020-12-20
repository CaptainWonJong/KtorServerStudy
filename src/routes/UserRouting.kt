package com.wonjong.lee.routes

import com.wonjong.lee.models.UserVo
import com.wonjong.lee.service.UserService
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

/**
 * @author CaptainWonJong@gmail.com
 */
fun Routing.users(service: UserService) {
    route("user") {
        get {
            call.respond(service.getAll())
        }
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("parameter id is null")
            call.respond(service.getById(id))
        }
        post {
            val body = call.receive<UserVo>()
            service.new(
                body.name,
                body.phoneNum
            )
            call.response.status(HttpStatusCode.Created)
        }
        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("parameter id is null")
            val body = call.receive<UserVo>()
            service.renew(id, body)
            call.response.status(HttpStatusCode.NoContent)
        }
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("parameter id is null")
            service.delete(id)
            call.response.status(HttpStatusCode.NoContent)
        }
    }
}