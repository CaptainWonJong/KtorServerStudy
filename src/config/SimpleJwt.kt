package com.wonjong.lee.config

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm

/**
 * @author CaptainWonJong@gmail.com
 */
open class SimpleJwt(secret: String) {
    private val algorithm = Algorithm.HMAC256(secret)
    val verifier: JWTVerifier = JWT.require(algorithm).build()

    fun sign(name: String): String {
        return JWT.create().withClaim("token", name).sign(algorithm)
    }
}
