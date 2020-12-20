package com.wonjong.lee.service

import com.wonjong.lee.entity.User
import com.wonjong.lee.models.UserVo
import com.wonjong.lee.entity.Users
import com.wonjong.lee.config.query
import io.ktor.features.*
import org.jetbrains.exposed.sql.SortOrder
import java.time.LocalDateTime

/**
 * @author CaptainWonJong@gmail.com
 */
class UserService {
    suspend fun getAll() = query {
        User.all().orderBy(
            Users.id to SortOrder.DESC
        ).map(UserVo.Companion::of).toList()
    }

    suspend fun getById(id: Int) = query {
        User.findById(id)?.run(UserVo.Companion::of) ?: throw NotFoundException()
    }

    suspend fun new(name: String, phoneNum: String) = query {
        User.new {
            this.name = name
            this.phoneNum = phoneNum
            updatedAt = createdAt
        }
    }

    suspend fun renew(id: Int, vo: UserVo) = query {
        val user = User.findById(id) ?: throw NotFoundException()
        user.apply {
            name = vo.name
            phoneNum = vo.name
            updatedAt = LocalDateTime.now()
        }
    }

    suspend fun delete(id: Int) = query {
        User.findById(id)?.delete() ?: throw NotFoundException()
    }
}