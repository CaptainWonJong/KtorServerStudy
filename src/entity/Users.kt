package com.wonjong.lee.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime

/**
 * @author CaptainWonJong@gmail.com
 */
// Table scheme
object Users : IntIdTable() {
    val name = text("name").default("")
    val phoneNum = text("phoneNum").default("")
    val createdAt = datetime("created_at").index().default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}

// Entity
class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var name by Users.name
    var phoneNum by Users.phoneNum
    var createdAt by Users.createdAt
    var updatedAt by Users.updatedAt
}