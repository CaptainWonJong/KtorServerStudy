package com.wonjong.lee.models

import com.wonjong.lee.entity.User
import java.time.LocalDateTime

/**
 * @author CaptainWonJong@gmail.com
 */
data class UserVo(
    val id: Int?,
    val name: String,
    val phoneNum: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun of(user: User): UserVo = UserVo(
            id = user.id.value,
            name = user.name,
            phoneNum = user.phoneNum,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }
}